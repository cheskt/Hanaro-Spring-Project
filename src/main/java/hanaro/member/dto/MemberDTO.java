package hanaro.member.dto;

import java.time.LocalDateTime;

import hanaro.member.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	private int userId;
	private String email;
	private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
