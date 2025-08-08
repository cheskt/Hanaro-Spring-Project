package hanaro.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.exception.GeneralException;
import hanaro.member.dto.MemberDTO;
import hanaro.member.dto.SignUpDTO;
import hanaro.member.entity.Member;
import hanaro.member.entity.enums.Role;
import hanaro.member.repository.MemberRepository;
import hanaro.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberDTO signUpMember(SignUpDTO requestDTO) {
        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .role(Role.MEMBER) // 기본 역할은 MEMBER로 설정
                .build();
        return toDTO(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(int userId) {
        if (!memberRepository.existsById(userId)) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        memberRepository.deleteById(userId); // @SQLDelete 어노테이션에 의해 deletedAt이 업데이트됩니다.
    }

    private MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .userId(member.getUserId())
                .email(member.getEmail())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
