package hanaro.response.code.status;

import hanaro.response.code.BaseCode;
import hanaro.response.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

	SUCCESS(HttpStatus.OK, "COMMON200", "성공입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public ReasonDTO getReason() {
		return ReasonDTO.builder()
						.message(message)
						.code(code)
						.isSuccess(true)
						.build();
	}

	@Override
	public ReasonDTO getReasonHttpStatus() {
		return ReasonDTO.builder()
						.message(message)
						.code(code)
						.isSuccess(true)
						.httpStatus(httpStatus)
						.build();
	}
}
