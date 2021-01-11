package in.hangang.exception;

import in.hangang.enums.ErrorMessage;

public class RefreshTokenExpireException extends NonCriticalException {
    public RefreshTokenExpireException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public RefreshTokenExpireException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
