package hanaro.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import hanaro.member.entity.Member;
import hanaro.order.entity.Orders;
import hanaro.order.entity.enums.Status;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	@EntityGraph(attributePaths = "items")
	Optional<Orders> findByOrderId(int orderId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update Orders o set o.status = :to, o.updatedAt = :now " +
		"where o.status = :from and o.createdAt <= :threshold")
	int advanceStatus(@Param("from") Status from,
		@Param("to") Status to,
		@Param("threshold") LocalDateTime threshold,
		@Param("now") LocalDateTime now);

	List<Orders> findAllByMemberOrderByOrderIdDesc(Member member);
}
