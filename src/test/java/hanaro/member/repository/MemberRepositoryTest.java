package hanaro.member.repository;

import hanaro.member.entity.Member;
import hanaro.member.entity.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 및 조회 테스트")
    void saveAndFindMember() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.MEMBER)
                .build();

        // when
        Member savedMember = memberRepository.save(member);
        Member foundMember = memberRepository.findById(savedMember.getUserId()).orElse(null);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
    }
}
