package hanaro.order.entity;

import java.util.ArrayList;
import java.util.List;

import hanaro.member.entity.Member;
import hanaro.order.entity.enums.Status;
import hanaro.util.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Orders")
public class Orders extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private int totalAmount;

    public void addItem(OrderItem oi) {
        this.items.add(oi);
        oi.setOrder(this);
    }
}

