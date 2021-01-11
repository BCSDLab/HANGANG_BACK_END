package in.hangang.exception;

import in.hangang.enums.ErrorMessage;

public class AccessTokenInvalidException extends CriticalException {

    public AccessTokenInvalidException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public AccessTokenInvalidException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
