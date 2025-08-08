package hanaro.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hanaro.member.dto.MemberDTO;
import hanaro.member.dto.SignUpDTO;
import hanaro.member.service.MemberService;
import hanaro.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberDTO> signUpMember(@RequestBody @Valid SignUpDTO requestDTO) {
        return ResponseEntity.ok(memberService.signUpMember(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable int userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "멤버 삭제 성공"));
    }
}
