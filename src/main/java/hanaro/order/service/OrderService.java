package hanaro.order.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.cart.entity.Cart;
import hanaro.cart.entity.CartItem;
import hanaro.cart.repository.CartItemRepository;
import hanaro.cart.repository.CartRepository;
import hanaro.exception.GeneralException;
import hanaro.item.entity.Item;
import hanaro.item.repository.ItemRepository;
import hanaro.member.entity.Member;
import hanaro.member.repository.MemberRepository;
import hanaro.order.dto.OrderItemResponseDTO;
import hanaro.order.dto.OrderResponseDTO;
import hanaro.order.entity.OrderItem;
import hanaro.order.entity.Orders;
import hanaro.order.entity.enums.Status;
import hanaro.order.repository.OrderItemRepository;
import hanaro.order.repository.OrdersRepository;
import hanaro.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	private final OrdersRepository ordersRepository;
	private final OrderItemRepository orderItemRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;

	private Member getCurrentMember() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return memberRepository.findByEmail(email)
							   .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
	}

	private OrderResponseDTO toDTO(Orders order) {
		List<OrderItemResponseDTO> items = order.getItems().stream().map(oi -> {
			int line = oi.getOrderPrice() * oi.getQuantity();
			return OrderItemResponseDTO.builder()
									   .orderItemId(oi.getOrderItemId())
									   .itemId(oi.getItem().getItemId())
									   .itemName(oi.getItem().getItemName())
									   .orderPrice(oi.getOrderPrice())
									   .quantity(oi.getQuantity())
									   .lineTotal(line)
									   .build();
		}).toList();

		int total = items.stream().mapToInt(OrderItemResponseDTO::getLineTotal).sum();

		return OrderResponseDTO.builder()
							   .orderId(order.getOrderId())
							   .status(order.getStatus())
							   .totalAmount(total)
							   .items(items)
							   .build();
	}

	@Transactional
	public OrderResponseDTO createOrderFromCart() {
		Member m = getCurrentMember();

		Cart cart = cartRepository.findByMember(m)
								  .orElseThrow(() -> new GeneralException(ErrorStatus.CART_NOT_FOUND));

		List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);
		if (cartItems.isEmpty()) {
			throw new GeneralException(ErrorStatus.CART_IS_EMPTY);
		}

		logger.info("주문한 고객 : {}", m.getEmail());

		Orders order = Orders.builder()
							 .member(m)
							 .status(Status.PAID)
							 .totalAmount(0)
							 .build();

		int total = 0;

		for (CartItem ci : cartItems) {
			Item item = itemRepository.findByIdForUpdate(ci.getItem().getItemId())
									  .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
			int qty = ci.getQuantity();
			if (item.getStock() < qty) {
				throw new GeneralException(ErrorStatus.ITEM_STOCK_NOT_ENOUGH);
			}

			item.setStock(item.getStock() - qty);

			OrderItem oi = OrderItem.builder()
									.order(order)
									.item(item)
									.quantity(qty)
									.orderPrice(item.getPrice())
									.build();
			order.addItem(oi);

			total += oi.getOrderPrice() * qty;
		}

		order.setTotalAmount(total);
		Orders saved = ordersRepository.save(order);

		for (CartItem ci : cartItems) {
			cart.removeItem(ci);
		}
		cartItemRepository.deleteAll(cartItems);

		return toDTO(saved);
	}

	@Transactional(readOnly = true)
	public OrderResponseDTO getOrder(int orderId) {
		Orders order = ordersRepository.findByOrderId(orderId)
									   .orElseThrow(() -> new GeneralException(ErrorStatus.ORDER_NOT_FOUND));

		Member member = getCurrentMember();
		if (order.getMember().getUserId() != member.getUserId() && member.getRole() != hanaro.member.entity.enums.Role.ADMIN) {
			throw new GeneralException(ErrorStatus.MEMBER_NOT_AUTHORITY);
		}

		return toDTO(order);
	}

	@Transactional(readOnly = true)
	public List<OrderResponseDTO> myOrders() {
		Member m = getCurrentMember();
		return ordersRepository.findAllByMemberOrderByOrderIdDesc(m).stream()
							   .map(this::toDTO)
							   .toList();
	}
}

