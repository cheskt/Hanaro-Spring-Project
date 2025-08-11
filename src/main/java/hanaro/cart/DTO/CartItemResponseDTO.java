package hanaro.cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
	private int cartItemId;
	private int itemId;
	private String itemName;
	private int price;
	private int quantity;
	private int lineTotal;
}
