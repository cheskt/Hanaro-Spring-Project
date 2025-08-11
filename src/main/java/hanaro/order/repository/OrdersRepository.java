package hanaro.order.repository;

import java.util.List;
import java.util.Optional;

import hanaro.member.entity.Member;
import hanaro.order.entity.Orders;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	@EntityGraph(attributePaths = "items")
	Optional<Orders> findByOrderId(int orderId);

	List<Orders> findAllByMemberOrderByOrderIdDesc(Member member);
}
