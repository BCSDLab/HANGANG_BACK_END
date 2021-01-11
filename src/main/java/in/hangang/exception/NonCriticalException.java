package in.hangang.exception;

import in.hangang.enums.ErrorMessage;


public class NonCriticalException extends BaseException {

	public NonCriticalException(String className, ErrorMessage errorMessage) {
		super(className, errorMessage);

	}
	public NonCriticalException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
