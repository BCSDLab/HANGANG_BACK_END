package in.hangang.enums;
import org.springframework.http.HttpStatus;

public enum ErrorMessage {
	UNDEFINED_EXCEPTION(0,"정의되지 않은 에러입니다.",HttpStatus.INTERNAL_SERVER_ERROR),
	NULL_POINTER_EXCEPTION(1,"NULL 여부를 확인해주세요",HttpStatus.BAD_REQUEST),
	JSON_PARSE_EXCEPTION(2,"JSON Parse 과정에 문제가 있습니다. 데이터를 확인해주세요",HttpStatus.BAD_REQUEST),
	AOP_XSS_SETTER_NO_EXSISTS_EXCEPTION(3,"해당 필드에 SETTER가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	AOP_XSS_FIELD_NO_EXSISTS_EXCEPTION(4,"해당 필드에 FIELD가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION(5,"ACCESS TOKEN이 VALID하지 않습니다.",HttpStatus.UNAUTHORIZED),
	REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION(6,"REFRESH TOKEN이 VALID하지 않습니다.",HttpStatus.UNAUTHORIZED),
	REQUEST_INVALID_EXCEPTION(7,"입력 값이 올바르지 않습니다.",HttpStatus.BAD_REQUEST),
	ACCESS_FORBIDDEN_AUTH_EXPIRE_EXCEPTION(8,"ACCESS TOKEN이 EXPIRE 되었습니다.",HttpStatus.UNAUTHORIZED),
	REFRESH_FORBIDDEN_AUTH_EXPIRE_EXCEPTION(9,"REFRESH TOKEN이 EXPIRE 되었습니다.",HttpStatus.UNAUTHORIZED),
	MAJOR_INVALID_EXCEPTION(10, "학과정보가 잘못 입력되었습니다.",HttpStatus.BAD_REQUEST),
	EMAIL_EXPIRED_AUTH_EXCEPTION(11, "이메일 인증번호가 만료되었습니다.",HttpStatus.BAD_REQUEST),
	EMAIL_NONE_AUTH_EXCEPTION(12, "이메일 인증을 해주세요.",HttpStatus.BAD_REQUEST),
	EMAIL_SECRET_INVALID_EXCEPTION(13, "이메일 인증 번호를 확인해주세요.",HttpStatus.BAD_REQUEST),
	EMAIL_ALREADY_AUTHED(14, "이메일이 이미 인증된 포탈 계정입니다.",HttpStatus.BAD_REQUEST),
	NO_USER_EXCEPTION(15, "가입되지 않은 계정입니다.",HttpStatus.BAD_REQUEST),
	VALIDATION_FAIL_EXCEPTION(16, "입력값의 조건이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
	INVALID_RECOMMENDATION(17, "추천은 1회만 가능합니다.", HttpStatus.BAD_REQUEST),
	INVALID_ACCESS_EXCEPTION(18, "존재하지 않는 게시글이거나 잘못된 접근입니다.", HttpStatus.BAD_REQUEST),
	INVALID_USER_EXCEPTION(19, "회원 정보가 존재하지 않습니다", HttpStatus.BAD_REQUEST),
	INVALID_SEMESTER_DATE_EXCEPTION(20, "잘못된 수강 학기입니다.", HttpStatus.BAD_REQUEST),
	PROHIBITED_ATTEMPT(21, "수강 후기는 한 과목에 하나만 작성 가능합니다.", HttpStatus.BAD_REQUEST);



	Integer code;
	String errorMessage;
	HttpStatus httpStatus;
	ErrorMessage(int code, String errorMessage, HttpStatus httpStatus) {
		this.code = code;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
	
	
	public Integer getCode() {
		return code;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public HttpStatus getHttpStatus() {return httpStatus;}
	
}
