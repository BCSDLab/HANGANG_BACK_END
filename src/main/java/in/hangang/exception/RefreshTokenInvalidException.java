package in.hangang.exception;

import in.hangang.enums.ErrorMessage;

public class RefreshTokenInvalidException extends CriticalException {
    public RefreshTokenInvalidException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public RefreshTokenInvalidException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
