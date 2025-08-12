package hanaro.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hanaro.order.dto.OrderResponseDTO;
import hanaro.order.service.OrderService;
import hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "주문")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

	private final OrderService orderService;

	@Operation(summary = "주문 생성")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@PostMapping()
	public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder() {
		return ResponseEntity.ok(ApiResponse.onSuccess(orderService.createOrderFromCart(), "주문 생성 성공"));
	}

	@Operation(summary = "주문 상세")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrder(@PathVariable @Positive(message = "주문 ID는 양수여야 합니다.") int orderId) {
		return ResponseEntity.ok(ApiResponse.onSuccess(orderService.getOrder(orderId), "주문 상세 조회 성공"));
	}

	@Operation(summary = "내 주문 목록")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@GetMapping("/list")
	public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> myOrders() {
		return ResponseEntity.ok(ApiResponse.onSuccess(orderService.myOrders(), "주문 목록 조회 성공"));
	}
}

