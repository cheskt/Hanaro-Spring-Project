package hanaro.cart.entity;

import hanaro.member.entity.Member;
import hanaro.util.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Cart")
public class Cart extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false, unique = true)
    private Member member;
}
