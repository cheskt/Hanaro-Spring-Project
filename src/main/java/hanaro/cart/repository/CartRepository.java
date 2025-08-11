package hanaro.cart.repository;

import hanaro.cart.entity.Cart;
import hanaro.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByMember(Member member);
}
