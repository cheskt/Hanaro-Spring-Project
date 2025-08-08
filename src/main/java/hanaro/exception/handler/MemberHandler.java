package hanaro.exception.handler;

import hanaro.exception.GeneralException;
import hanaro.response.code.BaseErrorCode;

public class MemberHandler extends GeneralException {
	public MemberHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
