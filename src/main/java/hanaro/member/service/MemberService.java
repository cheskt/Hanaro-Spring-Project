package hanaro.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.member.dto.MemberDTO;
import hanaro.member.dto.MemberRegisterRequestDTO;
import hanaro.member.entity.Member;
import hanaro.member.entity.enums.Role;
import hanaro.member.repository.MemberRepository;
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

    // 회원가입 -> 나중에 암호화 추가해야됨
    @Transactional
    public MemberDTO registerMember(MemberRegisterRequestDTO requestDTO) {
        Member member = Member.builder()
                              .email(requestDTO.getEmail())
                              .password(requestDTO.getPassword())
                              .role(Role.MEMBER)
                              .build();
        return toDTO(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(int userId) {
        memberRepository.deleteById(userId);
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
