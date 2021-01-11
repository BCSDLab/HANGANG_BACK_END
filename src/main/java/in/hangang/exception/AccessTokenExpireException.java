package in.hangang.exception;

import in.hangang.enums.ErrorMessage;

public class AccessTokenExpireException extends NonCriticalException {

    public AccessTokenExpireException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public AccessTokenExpireException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
