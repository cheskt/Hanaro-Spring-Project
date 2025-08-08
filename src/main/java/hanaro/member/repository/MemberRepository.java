package hanaro.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hanaro.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}