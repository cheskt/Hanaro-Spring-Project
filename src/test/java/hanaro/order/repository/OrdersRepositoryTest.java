package hanaro.order.repository;

import hanaro.member.entity.Member;
import hanaro.member.repository.MemberRepository;
import hanaro.order.entity.Orders;
import hanaro.order.entity.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrdersRepositoryTest {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    void saveAndFindOrder() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .build();
        memberRepository.save(member);

        Orders order = Orders.builder()
                .member(member)
                .status(Status.PAID)
                .totalAmount(10000)
                .build();

        // when
        Orders savedOrder = ordersRepository.save(order);
        Orders foundOrder = ordersRepository.findById(savedOrder.getOrderId()).orElse(null);

        // then
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getMember().getEmail()).isEqualTo("test@example.com");
        assertThat(foundOrder.getStatus()).isEqualTo(Status.PAID);
    }
}
