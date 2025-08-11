package hanaro.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
	private int orderItemId;
	private int itemId;
	private String itemName;
	private int orderPrice;
	private int quantity;
	private int lineTotal;
}

