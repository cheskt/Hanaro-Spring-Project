package hanaro.stats.entity;

import java.time.LocalDate;

import hanaro.util.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "DailyItemStat",
	uniqueConstraints = @UniqueConstraint(name = "uk_daily_item_stat", columnNames = {"statDate","itemId"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyItemStat extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int statId;

	@Column(nullable = false)
	private LocalDate statDate;

	@Column(nullable = false)
	private int itemId;

	@Column(nullable = false)
	private int orderCount;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private int amt;
}

