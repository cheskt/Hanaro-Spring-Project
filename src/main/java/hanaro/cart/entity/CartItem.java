package hanaro.cart.entity;

import hanaro.item.entity.Item;
import hanaro.util.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "CartItem",
    uniqueConstraints = @UniqueConstraint(name = "uk_cart_item", columnNames = {"cartId","itemId"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class CartItem extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "itemId", nullable = false)
    private Item item;

    @Column(nullable = false)
    private int quantity;
}
