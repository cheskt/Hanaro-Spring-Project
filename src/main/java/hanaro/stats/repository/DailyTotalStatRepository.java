package hanaro.stats.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import hanaro.stats.entity.DailyTotalStat;

public interface DailyTotalStatRepository extends JpaRepository<DailyTotalStat, Integer> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value =
		"INSERT INTO DailyTotalStat (statDate, orderCount, quantity, amt, createdAt, updatedAt) " +
			"SELECT :statDate, COUNT(DISTINCT oi.orderId), SUM(oi.quantity), " +
			"       SUM(oi.orderPrice * oi.quantity), :now, :now " +
			"FROM OrderItem oi " +
			"JOIN Orders o ON o.orderId = oi.orderId " +
			"WHERE o.createdAt >= :start AND o.createdAt < :end " +
			"  AND o.Status IN ('PAID','READY','TRANSIT','DELIVERED') " +
			"ON DUPLICATE KEY UPDATE " +
			"  orderCount = VALUES(orderCount), " +
			"  quantity   = VALUES(quantity), " +
			"  amt        = VALUES(amt), " +
			"  updatedAt  = VALUES(updatedAt)",
		nativeQuery = true)
	int upsertDailyTotalStat(@Param("statDate") LocalDate statDate,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("now") LocalDateTime now);

	Optional<DailyTotalStat> findByStatDate(LocalDate statDate);
}

