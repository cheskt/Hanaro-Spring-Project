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
import hanaro.member.dto.MemberRegisterRequestDTO;
import hanaro.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDTO> registerMember(@RequestBody MemberRegisterRequestDTO requestDTO) {
        return ResponseEntity.ok(memberService.registerMember(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteMember(@PathVariable int userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok().build();
    }
}
