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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "유저")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입합니다")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberDTO>> signUpMember(@RequestBody @Valid SignUpDTO requestDTO) {
        MemberDTO result = memberService.signUpMember(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(result, "회원가입 성공"));
    }

    @Operation(summary = "로그인", description = "로그인합니다")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<String>> signIn(@RequestBody @Valid SignInDTO requestDTO) {
        String token = memberService.signIn(requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(token, "로그인 성공"));
    }

    @Operation(summary = "유저 리스트 조회", description = "유저 리스트를 조회합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getAllMembers() {
        List<MemberDTO> list = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.onSuccess(list, "유저 리스트 조회 성공"));
    }

    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable int userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "유저 삭제 성공"));
    }
}
