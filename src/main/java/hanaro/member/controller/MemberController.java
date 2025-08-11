package hanaro.member.controller;

import hanaro.member.dto.SignInDTO;
import hanaro.member.dto.MemberDTO;
import hanaro.member.dto.SignUpDTO;
import hanaro.member.service.MemberService;
import hanaro.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Tag(name = "유저")
    @Operation(summary = "회원가입", description = "회원가입합니다")
    @PostMapping("/signup")
    public ResponseEntity<MemberDTO> signUpMember(@RequestBody @Valid SignUpDTO requestDTO) {
        return ResponseEntity.ok(memberService.signUpMember(requestDTO));
    }

    @Tag(name = "유저")
    @Operation(summary = "로그인", description = "로그인합니다")
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInDTO requestDTO) {
        return ResponseEntity.ok(memberService.signIn(requestDTO));
    }

    @Tag(name = "유저")
    @Operation(summary = "유저 리스트 조회", description = "유저 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @Tag(name = "유저")
    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable int userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "유저 삭제 성공"));
    }
}
