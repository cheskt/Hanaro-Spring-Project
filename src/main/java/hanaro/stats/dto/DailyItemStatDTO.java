package hanaro.stats.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyItemStatDTO {
	private int statId;
	private LocalDate statDate;
	private int itemId;
	private int orderCount;
	private int quantity;
	private int amt;
}
