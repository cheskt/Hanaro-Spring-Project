package hanaro.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
	@NotBlank(message = "상품 이름은 필수입니다.")
	private String itemName;
	@PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
	private int price;
	@PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
	private int stock;
}
