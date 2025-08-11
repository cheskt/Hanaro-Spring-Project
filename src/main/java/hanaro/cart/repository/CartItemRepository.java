package hanaro.cart.repository;

import hanaro.cart.entity.Cart;
import hanaro.cart.entity.CartItem;
import hanaro.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndItem(Cart cart, Item item);
    List<CartItem> findAllByCart(Cart cart);
}
