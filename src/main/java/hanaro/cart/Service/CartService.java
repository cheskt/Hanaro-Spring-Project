package hanaro.cart.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.cart.DTO.CartItemRequestDTO;
import hanaro.cart.DTO.CartItemResponseDTO;
import hanaro.cart.DTO.CartItemUpdateRequestDTO;
import hanaro.cart.DTO.CartResponseDTO;
import hanaro.cart.entity.Cart;
import hanaro.cart.entity.CartItem;
import hanaro.cart.repository.CartItemRepository;
import hanaro.cart.repository.CartRepository;
import hanaro.exception.GeneralException;
import hanaro.item.entity.Item;
import hanaro.item.repository.ItemRepository;
import hanaro.member.entity.Member;
import hanaro.member.repository.MemberRepository;
import hanaro.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;

	private Member getCurrentMember() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return memberRepository.findByEmail(email)
							   .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
	}

	private Cart getOrCreateCart(Member m) {
		return cartRepository.findByMember(m)
							 .orElseGet(() -> cartRepository.save(Cart.builder().member(m).build()));
	}

	private CartResponseDTO toResponse(Cart cart) {
		List<CartItem> list = cartItemRepository.findAllByCart(cart);
		List<CartItemResponseDTO> items = list.stream().map(ci -> {
			Item it = ci.getItem();
			int price = it.getPrice();
			return CartItemResponseDTO.builder()
									  .cartItemId(ci.getCartItemId())
									  .itemId(it.getItemId())
									  .itemName(it.getItemName())
									  .price(price)
									  .quantity(ci.getQuantity())
									  .lineTotal(price * ci.getQuantity())
									  .build();
		}).toList();

		int totalQty = items.stream().mapToInt(CartItemResponseDTO::getQuantity).sum();
		int totalAmt = items.stream().mapToInt(CartItemResponseDTO::getLineTotal).sum();

		return CartResponseDTO.builder()
							  .cartId(cart.getCartId())
							  .items(items)
							  .totalQuantity(totalQty)
							  .totalAmount(totalAmt)
							  .build();
	}

	@Transactional(readOnly = true)
	public CartResponseDTO getMyCart() {
		Member m = getCurrentMember();
		Cart cart = getOrCreateCart(m);
		return toResponse(cart);
	}

	@Transactional
	public CartResponseDTO addToCart(CartItemRequestDTO req) {
		if (req.getQuantity() <= 0) throw new GeneralException(ErrorStatus._BAD_REQUEST);

		Member m = getCurrentMember();
		Cart cart = getOrCreateCart(m);

		Item item = itemRepository.findById(req.getItemId())
								  .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

		CartItem ci = cartItemRepository.findByCartAndItem(cart, item)
										.map(ex -> {
											int merged = ex.getQuantity() + req.getQuantity();
											if (merged > item.getStock()) throw new GeneralException(ErrorStatus._BAD_REQUEST);
											ex.setQuantity(merged);
											return ex;
										})
										.orElseGet(() -> {
											if (req.getQuantity() > item.getStock()) throw new GeneralException(ErrorStatus._BAD_REQUEST);
											CartItem created = CartItem.builder()
																	   .cart(cart).item(item).quantity(req.getQuantity()).build();
											cart.addItem(created);
											return created;
										});

		cartItemRepository.save(ci);
		return toResponse(cart);
	}

	@Transactional
	public CartResponseDTO updateCartItem(int cartItemId, CartItemUpdateRequestDTO req) {
		Member m = getCurrentMember();
		Cart cart = getOrCreateCart(m);

		CartItem ci = cartItemRepository.findById(cartItemId)
										.orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

		if (ci.getCart().getCartId() != cart.getCartId())
			throw new GeneralException(ErrorStatus.MEMBER_NOT_AUTHORITY);

		if (req.getQuantity() <= 0) {
			cart.removeItem(ci);
			cartItemRepository.delete(ci);
		} else {
			if (req.getQuantity() > ci.getItem().getStock()) throw new GeneralException(ErrorStatus._BAD_REQUEST);
			ci.setQuantity(req.getQuantity());
		}
		return toResponse(cart);
	}

	@Transactional
	public CartResponseDTO removeCartItem(int cartItemId) {
		Member m = getCurrentMember();
		Cart cart = getOrCreateCart(m);

		CartItem ci = cartItemRepository.findById(cartItemId)
										.orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST));

		if (ci.getCart().getCartId() != cart.getCartId())
			throw new GeneralException(ErrorStatus.MEMBER_NOT_AUTHORITY);

		cart.removeItem(ci);
		cartItemRepository.delete(ci);
		return toResponse(cart);
	}

	@Transactional
	public CartResponseDTO clearCart() {
		Member m = getCurrentMember();
		Cart cart = getOrCreateCart(m);
		for (CartItem ci : new ArrayList<>(cart.getItems())) {
			cart.removeItem(ci);
		}
		cartRepository.save(cart);
		return toResponse(cart);
	}
}

