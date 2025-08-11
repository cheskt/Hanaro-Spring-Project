package hanaro.order.dto;

import java.util.List;

import hanaro.order.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
	private int orderId;
	private Status status;
	private int totalAmount;
	private List<OrderItemResponseDTO> items;
}
