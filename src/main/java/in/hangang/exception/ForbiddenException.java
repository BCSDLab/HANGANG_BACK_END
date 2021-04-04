package in.hangang.exception;

import in.hangang.enums.ErrorMessage;

public class ForbiddenException extends NonCriticalException{
    public ForbiddenException(String className, ErrorMessage errorMessage) {
        super(className, errorMessage);

    }

    public ForbiddenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
