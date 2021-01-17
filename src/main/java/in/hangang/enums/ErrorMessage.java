package in.hangang.enums;

public enum ErrorMessage {
	EXCEPTION_FOR_TEST(-1,"TEST를 위한 에러 메시지입니다."),
	UNDEFINED_EXCEPTION(0,"정의되지 않은 에러입니다."),
	NULL_POINTER_EXCEPTION(1,"NULL 여부를 확인해주세요"),
	JSON_PARSE_EXCEPTION(2,"JSON Parse 과정에 문제가 있습니다. 데이터를 확인해주세요"),
	AOP_XSS_SETTER_NO_EXSISTS_EXCEPTION(3,"해당 필드에 SETTER가 존재하지 않습니다."),
	AOP_XSS_FIELD_NO_EXSISTS_EXCEPTION(4,"해당 필드에 FIELD가 존재하지 않습니다."),
	ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION(5,"ACCESS TOKEN이 VALID하지 않습니다."),
	REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION(6,"REFRESH TOKEN이 VALID하지 않습니다."),
	REQUEST_INVALID_EXCEPTION(7,"입력 값이 올바르지 않습니다."),
	ACCESS_JWT_NULL_POINTER_EXCEPTION(8,"ACCESS JWT TOKEN이 NULL입니다"),
	REFRESH_JWT_NULL_POINTER_EXCEPTION(9,"REFRESH JWT TOKEN이 NULL입니다"),
	ACCESS_JWT_FORMAT_EXCEPTION(10,"ACCESS JWT TOKEN FORMAT이 잘못되었습니다"),
	REFRESH_JWT_FORMAT_EXCEPTION(11,"REFRESH JWT TOKEN FORMAT이 잘못되었습니다"),
	ACCESS_JWT_PAYLOAD_EXCEPTION(12,"ACCESS JWT TOKEN값의 PAYLOAD가 잘못되었습니다."),
	REFRESH_JWT_PAYLOAD_EXCEPTION(13,"REFRESH JWT TOKEN값의 PAYLOAD가 잘못되었습니다."),
	ACCESS_FORBIDDEN_AUTH_EXPIRE_EXCEPTION(14,"ACCESS TOKEN이 EXPIRE 되었습니다."),
	REFRESH_FORBIDDEN_AUTH_EXPIRE_EXCEPTION(15,"REFRESH TOKEN이 EXPIRE 되었습니다."),
	MAJOR_INVALID_EXCEPTION(16, "학과정보가 잘못 입력되었습니다."),
	EMAIL_EXPIRED_AUTH_EXCEPTION(17, "이메일 인증번호가 만료되었습니다."),
	EMAIL_NONE_AUTH_EXCEPTION(18, "이메일 인증을 해주세요."),
	EMAIL_SECRET_INVALID_EXCEPTION(19, "이메일 인증 번호를 확인해주세요."),
	EMAIL_ALREADY_AUTHED(20, "이메일이 이미 인증된 포탈 계정입니다."),
	NO_USER_EXCEPTION(21, "가입되지 않은 계정입니다."),
	REVIEW_ALREADY_LIKED(22, "이미 추천한 게시글입니다."),
	/*
	 * DB_CONSTRAIN INVALID
	 */
	DB_CONSTRAINT_INVALID(1080,"데이터베이스의 고유 제약 조건을 위반하였습니다."),
	
	/*
	 * @VALID INVALID
	 */
	VALID_ANNOTATION_INVALID(1100,"@Validation 에러가 발생하였습니다."),
	PAGENATION_INVALID(1120,"pagenation의 범위를 확인해주세요. 0 < page , 0 < per_count"),
	

	;
	
	
	Integer code;
	String errorMessage;
	ErrorMessage(int code, String errorMessage) {
		this.code = code;
		this.errorMessage = errorMessage;
	}
	
	
	public Integer getCode() {
		return code;
	}
	public String getErrorMessage() {
		return errorMessage;
	}

	
}
