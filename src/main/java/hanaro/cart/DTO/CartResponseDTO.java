package hanaro.cart.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
	private int cartId;
	private int totalQuantity;
	private int totalAmount;
	private List<CartItemResponseDTO> items;
}
