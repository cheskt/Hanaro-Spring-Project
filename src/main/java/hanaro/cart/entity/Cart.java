package hanaro.cart.entity;

import java.util.ArrayList;
import java.util.List;

import hanaro.member.entity.Member;
import hanaro.util.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @OneToOne
    @JoinColumn(name = "memberId", nullable = false, unique = true)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem ci) {
        items.add(ci);
        ci.setCart(this);
    }
    public void removeItem(CartItem ci) {
        items.remove(ci);
        ci.setCart(null);
    }
}
