package hanaro.cart.DTO;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequestDTO {
	@Positive(message = "수량은 양수여야 합니다.")
	private int quantity;
}
