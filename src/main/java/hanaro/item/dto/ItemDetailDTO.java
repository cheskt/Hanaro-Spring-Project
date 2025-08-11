package hanaro.item.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailDTO {
	private String itemName;
	private int price;
	private int stock;
	private List<ItemImageResponseDTO> images;
}
