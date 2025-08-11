package hanaro.cart.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hanaro.cart.DTO.CartItemRequestDTO;
import hanaro.cart.DTO.CartItemUpdateRequestDTO;
import hanaro.cart.DTO.CartResponseDTO;
import hanaro.cart.Service.CartService;
import hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "장바구니")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

	private final CartService cartService;

	@Operation(summary = "장바구니 조회", description = "내 장바구니를 조회합니다")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@GetMapping("/mycart")
	public ResponseEntity<ApiResponse<CartResponseDTO>> getMyCart() {
		return ResponseEntity.ok(ApiResponse.onSuccess(cartService.getMyCart(), "장바구니 조회 성공"));
	}

	@Operation(summary = "장바구니 담기", description = "장바구니에 상품을 담습니다")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(@RequestBody CartItemRequestDTO req) {
		return ResponseEntity.ok(ApiResponse.onSuccess(cartService.addToCart(req), "장바구니 담기 성공"));
	}

	@Operation(summary = "장바구니 수량 변경", description = "장바구니 상품 수량을 변경합니다")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@PutMapping("/items/{cartItemId}/update")
	public ResponseEntity<ApiResponse<CartResponseDTO>> updateCartItem(@PathVariable int cartItemId,
		@RequestBody CartItemUpdateRequestDTO req) {
		return ResponseEntity.ok(ApiResponse.onSuccess(cartService.updateCartItem(cartItemId, req), "장바구니 상품 수량 변경 성공"));
	}

	@Operation(summary = "장바구니 상품 삭제", description = "장바구니의 상품을 삭제합니다")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@DeleteMapping("/items/{cartItemId}/delete")
	public ResponseEntity<ApiResponse<CartResponseDTO>> removeCartItem(@PathVariable int cartItemId) {
		return ResponseEntity.ok(ApiResponse.onSuccess(cartService.removeCartItem(cartItemId), "장바구니 상품 삭제 성공"));
	}

	@Operation(summary = "장바구니 비우기", description = "장바구니를 삭제합니다")
	@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<CartResponseDTO>> clearCart() {
		return ResponseEntity.ok(ApiResponse.onSuccess(cartService.clearCart(), "장바구니 비우기 성공"));
	}
}

