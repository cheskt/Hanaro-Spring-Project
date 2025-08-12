package hanaro.cart.repository;

import hanaro.cart.entity.Cart;
import hanaro.member.entity.Member;
import hanaro.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("장바구니 저장 및 조회 테스트")
    void saveAndFindCart() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .build();
        memberRepository.save(member);

        Cart cart = Cart.builder()
                .member(member)
                .build();

        // when
        Cart savedCart = cartRepository.save(cart);
        Cart foundCart = cartRepository.findById(savedCart.getCartId()).orElse(null);

        // then
        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getMember().getEmail()).isEqualTo("test@example.com");
    }
}
