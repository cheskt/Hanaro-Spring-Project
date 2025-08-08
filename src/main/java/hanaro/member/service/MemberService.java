package hanaro.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hanaro.member.dto.MemberDTO;
import hanaro.member.entity.Member;
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
                .deletedAt(member.getDeletedAt())
                .build();
    }
}
