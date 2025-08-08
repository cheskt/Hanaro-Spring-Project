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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "유저 등록", description = "회원가입합니다")
    @PostMapping("/signup")
    public ResponseEntity<MemberDTO> signUpMember(@RequestBody @Valid SignUpDTO requestDTO) {
        return ResponseEntity.ok(memberService.signUpMember(requestDTO));
    }
    
    @Operation(summary = "유저 리스트 조회", description = "유저 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable int userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null, "유저 삭제 성공"));
    }
}
