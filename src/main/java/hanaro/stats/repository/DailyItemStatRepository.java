package hanaro.stats.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import hanaro.stats.entity.DailyItemStat;

public interface DailyItemStatRepository extends JpaRepository<DailyItemStat, Integer> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value =
		"INSERT INTO DailyItemStat (statDate, itemId, orderCount, quantity, amt, createdAt, updatedAt) " +
			"SELECT :statDate, oi.itemId, COUNT(DISTINCT oi.orderId), SUM(oi.quantity), " +
			"       SUM(oi.orderPrice * oi.quantity), :now, :now " +
			"FROM OrderItem oi " +
			"JOIN Orders o ON o.orderId = oi.orderId " +
			"WHERE o.createdAt >= :start AND o.createdAt < :end " +
			"  AND o.Status IN ('PAID','READY','TRANSIT','DELIVERED') " +
			"GROUP BY oi.itemId " +
			"ON DUPLICATE KEY UPDATE " +
			"  orderCount = VALUES(orderCount), " +
			"  quantity   = VALUES(quantity), " +
			"  amt        = VALUES(amt), " +
			"  updatedAt  = VALUES(updatedAt)",
		nativeQuery = true)
	int upsertDailyItemStat(@Param("statDate") LocalDate statDate,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("now") LocalDateTime now);

	List<DailyItemStat> findAllByStatDate(LocalDate statDate);
}

