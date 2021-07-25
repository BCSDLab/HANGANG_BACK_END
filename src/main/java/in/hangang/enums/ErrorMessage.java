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
	INVALID_ACCESS_EXCEPTION(18, "정보에 접근할 권한이 없습니다.", HttpStatus.BAD_REQUEST),
	INVALID_USER_EXCEPTION(19, "회원 정보가 존재하지 않습니다", HttpStatus.BAD_REQUEST),
	INVALID_SEMESTER_DATE_EXCEPTION(20, "잘못된 수강 학기입니다.", HttpStatus.BAD_REQUEST),
	PROHIBITED_ATTEMPT(21, "수강 후기는 한 과목에 하나만 작성 가능합니다.", HttpStatus.BAD_REQUEST),
	EMAIL_COUNT_EXCEED_EXCEPTION(22, "이메일 인증은 하루에 5회만 가능합니다.", HttpStatus.BAD_REQUEST),
	TIME_LIST_CONFLICT(23, "기존 강의 시간과 중복됩니다.", HttpStatus.BAD_REQUEST),
	TIME_TABLE_LIMIT_SEMESTER(24, "시간표는 학기당 최대 5개만 생성 가능합니다.", HttpStatus.BAD_REQUEST),
	NOT_MATCH_SEMESTER_DATE(25, "시간표의 학기정보와 과목의 학기 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
	NICKNAME_DUPLICATED(26, "중복된 닉네임입니다.", HttpStatus.BAD_REQUEST),
	BANNED_NICKNAME(27, "금지된 닉네임입니다.", HttpStatus.BAD_REQUEST),
	MEMO_ALREADY_EXISTS(28, "메모가 이미 등록되어 있습니다.", HttpStatus.BAD_REQUEST),
	SCRAP_ALREADY_EXISTS(29, "이미 스크랩한 게시글 입니다.", HttpStatus.BAD_REQUEST),
	CONTENT_NOT_EXISTS(30, "해당 게시글 혹은 자료가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
	TIME_TABLE_LIMIT_TOTAL(31, "시간표는 총 50개만 생성 가능합니다.", HttpStatus.BAD_REQUEST),
	NOT_ENOUGH_POINT(32, "point가 부족합니다", HttpStatus.BAD_REQUEST),
	ALREADY_REPORTED(33, "이미 신고한 기록이 있습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_SCRAP_LECTURE(34, "이미 찜한 강의입니다.", HttpStatus.BAD_REQUEST),
	FORBIDDEN_EXCEPTION(35, "작업을 수행할 수 있는 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
	ROOT_AUTHORITY_CAN_NOT_DELETE_EXCEPTION(36,"루트 어드민의 권한은 건들 수 없습니다", HttpStatus.FORBIDDEN),
	USER_IS_NOT_MANAGER(37,"해당 유저는 뺏을 권한이 없습니다.", HttpStatus.BAD_REQUEST),
	COMMENT_NOT_EXIST(38, "존재하지 않는 강의자료 댓글입니다.", HttpStatus.BAD_REQUEST),
	REVIEW_NOT_EXIST(39,"존재하지 않는 강의평입니다.",HttpStatus.BAD_REQUEST),
	LECTURE_CRITERIA_LIMIT_DEPARTMENT(40, "학부 정보는 최대 2개까지만 선택 가능합니다", HttpStatus.BAD_REQUEST),
	REPORTED_CONTENT(41, "신고된 강의자료에는 댓글을 달 수 없습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_PURCHASED(42, "이미 구입한 강의자료 입니다.", HttpStatus.BAD_REQUEST),
	PURCHASE_EXCEPTION(43, "자신의 강의자료는 구매할 수 없습니다.", HttpStatus.BAD_REQUEST),
	DIDNT_PURCHASED(44, "구입하지 않은 강의자료에 접근하실 수 없습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_DELETED_SCRAP(45, "이미 삭제된 스크랩입니다.", HttpStatus.BAD_REQUEST),
	SCRAP_DOES_NOT_EXIST(46, "존재하지 않는 스크랩입니다.", HttpStatus.BAD_REQUEST),
	REPORT_NOT_EXIST(47, "해당 신고내역이 존재하지 않습니다." , HttpStatus.BAD_REQUEST),
	REPORT_TYPE_EXCEPTION(48, "신고 대상의 영역이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
	REPORT_CONTENT_EXCEPTION(49, "신고 대상이 이미 삭제되었거나 존재하지 않습니다", HttpStatus.BAD_REQUEST),
	TIME_TABLE_CRUSHED(50,"시간표 내용이 겹칩니다.", HttpStatus.BAD_REQUEST),
	CATEGORY_INVALID(51,"카테고리값이 잘못 입력되었습니다.", HttpStatus.BAD_REQUEST),
	URL_NOT_UNIQUE(52,"url값이 유니크하지 못합니다.", HttpStatus.BAD_REQUEST),
	KEYWORD_INVALID(53,"키워드는 최신순과 좋아요순만 가능합니다.", HttpStatus.BAD_REQUEST),
	URL_INVALID(54,"URL의 값이 INVALID합니다.",HttpStatus.BAD_REQUEST),
	DELETED_FILE(55,"이미 삭제된 파일입니다", HttpStatus.BAD_REQUEST),
	DELETE_MAIN_TIMETABLE(56, "메인 시간표는 삭제가 불가능합니다.", HttpStatus.BAD_REQUEST),
	TOO_FAST_REQUEST(57, "요청이 중복되었습니다.", HttpStatus.BAD_REQUEST);

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
