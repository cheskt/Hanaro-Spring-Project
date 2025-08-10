package hanaro.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageResponseDTO {
    private int imageId;
    private String imageUrl;
    private int itemId;
}
