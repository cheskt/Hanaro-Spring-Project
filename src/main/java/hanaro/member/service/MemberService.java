package hanaro.member.service;

import hanaro.exception.GeneralException;
import hanaro.jwt.JwtUtil;
import hanaro.member.dto.SignInDTO;
import hanaro.member.dto.MemberDTO;
import hanaro.member.dto.SignUpDTO;
import hanaro.member.entity.Member;
import hanaro.member.entity.enums.Role;
import hanaro.member.repository.MemberRepository;
import hanaro.response.code.status.ErrorStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberDTO signUpMember(SignUpDTO requestDTO) {
        if (memberRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.MEMBER_ID_IN_USE);
        }
        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(Role.MEMBER) // 기본 역할은 MEMBER로 설정
                .build();
        return toDTO(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(int userId) {
        if (!memberRepository.existsById(userId)) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
        memberRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public String signIn(@Valid SignInDTO signInDTO) {
        final UserDetails userDetails;
        try {
            userDetails = loadUserByUsername(signInDTO.getEmail());
        } catch (UsernameNotFoundException e) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_MATCH_MEMBERNAME);
        }

        if (!passwordEncoder.matches(signInDTO.getPassword(), userDetails.getPassword())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_MATCH_PASSWORD);
        }

        return jwtUtil.generateToken(userDetails);
    }

    private MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .userId(member.getUserId())
                .email(member.getEmail())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "의 유저를 찾을 수 없습니다"));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }
}

