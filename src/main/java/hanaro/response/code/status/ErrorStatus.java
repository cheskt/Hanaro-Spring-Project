package hanaro.response.code.status;

import hanaro.response.code.BaseErrorCode;
import hanaro.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	// 일반적인 응답
	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러"),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","권한이 없습니다."),
	// Token 응답

	MEMBER_ID_IN_USE(HttpStatus.NOT_FOUND, "MEMBER4000", "사용중인 유저아이디 입니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4002", "해당 유저가 없습니다"),
	MEMBER_NOT_AUTHORITY(HttpStatus.FORBIDDEN, "MEMBER4003", "권한이 없습니다"),
	MEMBER_NOT_MATCH_MEMBERNAME(HttpStatus.NOT_FOUND, "MEMBER4004", "유저 아이디가 잘못됐습니다."),
	MEMBER_NOT_MATCH_PASSWORD(HttpStatus.NOT_FOUND, "MEMBER4005", "유저 비밀번호가 잘못됐습니다."),

	// 상품 관련 에러
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM4000", "해당 상품이 없습니다."),

	// 파일 업로드 관련 에러
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE5000", "파일 업로드에 실패했습니다."),
	FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE4000", "파일 크기가 제한을 초과했습니다."),
	TOTAL_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE4001", "총 파일 크기가 제한을 초과했습니다."),
	;

	private final HttpStatus httpStatusCode;
	private final String code;
	private final String message;

	@Override
	public ErrorReasonDTO getReason() {
		return ErrorReasonDTO.builder()
							 .message(message)
							 .code(code)
							 .isSuccess(false)
							 .build();
	}

	@Override
		public ErrorReasonDTO getReasonHttpStatus() {
		return ErrorReasonDTO.builder()
							 .message(message)
							 .code(code)
							 .isSuccess(false)
							 .httpStatusCode(httpStatusCode)
							 .build();
	}

}
