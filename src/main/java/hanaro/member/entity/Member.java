package hanaro.member.entity;

import org.hibernate.annotations.SQLDelete;
import hanaro.member.entity.enums.Role;
import hanaro.util.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
@Getter
public class Member extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Email
	@Column(length = 30, unique = true)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;
}
