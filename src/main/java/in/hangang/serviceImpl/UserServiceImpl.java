package in.hangang.serviceImpl;

import in.hangang.config.SlackNotiSender;
import in.hangang.domain.*;
import in.hangang.domain.slack.SlackAttachment;
import in.hangang.domain.slack.SlackParameter;
import in.hangang.domain.slack.SlackTarget;
import in.hangang.enums.ErrorMessage;
import in.hangang.enums.Major;
import in.hangang.enums.Point;
import in.hangang.exception.AccessTokenInvalidException;
import in.hangang.exception.RefreshTokenExpireException;
import in.hangang.exception.RefreshTokenInvalidException;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.TimetableMapper;
import in.hangang.mapper.UserMapper;
import in.hangang.response.BaseResponse;
import in.hangang.service.UserService;
import in.hangang.util.Jwt;
import in.hangang.util.S3Util;
import in.hangang.util.SesSender;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


@Transactional
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Resource
    protected UserMapper userMapper;
    @Resource
    private Jwt jwt;
    @Value("${refresh.user.name}")
    private String refreshUserName;
    @Value("${token.user.name}")
    private String accessTokenName;
    @Resource
    private SesSender sesSender;
    @Resource
    private SpringTemplateEngine springTemplateEngine;
    @Resource
    private S3Util s3Util;
    @Resource
    private TimetableMapper timetableMapper;

    @Value("${token.access}")
    private String access_token;

    @Value("${token.refresh}")
    private String refresh_token;

    @Value("${report_slack_url}")
    private String notifyReportUrl;
    @Autowired
    SlackNotiSender slackNotiSender;

    private static final String signOutNickName = "(탈퇴한 회원)";

    public Map<String,String> login(User user) throws Exception{

        User dbUser = userMapper.getPasswordFromPortal(user.getPortal_account());

        // 아이디 중복 검사
        if ( dbUser == null){
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
        }
        // 비밀번호가 틀린 경우
        if ( !BCrypt.checkpw( user.getPassword(), dbUser.getPassword() )){
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }
        // 로그인이 성공한 경우 , access token, refresh token 반환
        else{
            Map<String, String> token = new HashMap<>();
            token.put(access_token, jwt.generateToken(dbUser.getId(), dbUser.getNickname(), access_token) );
            token.put(refresh_token, jwt.generateToken(dbUser.getId(),dbUser.getNickname(),refresh_token));
            return token;
        }
    }


    public BaseResponse signUp(User user) throws Exception {

        //이메일 인증 여부 체크
        ArrayList<AuthNumber> list = userMapper.getAuthTrue(user.getPortal_account(),0);
        if ( list.size() == 0){
            throw new RequestInputException(ErrorMessage.EMAIL_NONE_AUTH_EXCEPTION);
        }

        //전공 null 체크 validation에서 major = [] 로만 보내면 null이 잡히지않는 에러발견
        // major = ["" ] 은 잡힘
        if  (user.getMajor().size() == 0 ){
            throw new RequestInputException(ErrorMessage.MAJOR_INVALID_EXCEPTION);
        }

        // portal account 를 통한 중복가입 여부 확인
        if ( user == null || user.getPortal_account() == null || userMapper.getUserIdFromPortal(user.getPortal_account() ) != null ) {
            throw new RequestInputException(ErrorMessage.EMAIL_ALREADY_AUTHED);
        }

        // 닉네임 null,중복 체크, 혹은 알수 없음은 불가능
        if (user.getNickname() != null ) {
            String nickname = userMapper.getUserByNickName(user.getNickname());
            if ( nickname  != null ) {
                throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
            }
            if ( user.getNickname().equals(signOutNickName)){
                throw new RequestInputException(ErrorMessage.BANNED_NICKNAME);
            }
        }

        // 재가입인 경우
        Long dbId =  userMapper.getUserIdFromPortalForReSignUp(user.getPortal_account());
        if ( dbId != null ){
            this.reSignUp(dbId, user);
            user.setId(dbId);
            sendNoti(user, "회원가입");
            return new BaseResponse("재가입이 완료되었습니다.", HttpStatus.OK);
        }


        // 암호화
        user.setPassword( BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) );
        userMapper.signUp(user); // 회원가입

        //portal 계정으로 들어온 인증 이력을 만료 + soft delete
        this.invalidateAllAuthNumberByPortal(user.getPortal_account());

        //회원가입후 user의 가입된 id를 구함
        Long userId = userMapper.getUserIdFromPortal(user.getPortal_account());
        user.setId(userId);
        // 전공값의 내용이 올바르지 않다면
        for(int i =0; i<user.getMajor().size(); i++){
            boolean result = this.isMajorValid(user.getMajor().get(i));
            if ( !result )
                throw new RequestInputException(ErrorMessage.MAJOR_INVALID_EXCEPTION);
        }
        userMapper.insertMajors(userId,user.getMajor());

        // user salt = timestamp + user_id + BCrypt
        // salt 삽입
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String salt = userId.toString() + calendar.getTime();
        salt = (BCrypt.hashpw(salt , BCrypt.gensalt()));
        userMapper.setSalt(salt,userId);

        //회원가입 포인트 이력 추가
        userMapper.addPointHistory(userId, Point.SIGN_UP.getPoint(), Point.SIGN_UP.getTypeId());
        timetableMapper.createDefaultTimeTable(userId, timetableMapper.getLatestSemesterDateId());
        sendNoti(user, "회원가입");
        return new BaseResponse("회원가입에 성공했습니다", HttpStatus.OK);
    }
    @Override
    public void sendNoti(User user, String event) throws Exception{

        SlackTarget slackTarget = new SlackTarget(notifyReportUrl,"");

        SlackParameter slackParameter = new SlackParameter();
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setTitle("유저");
        slackAttachment.setAuthorName("한강 회원가입");
        slackAttachment.setAuthorIcon("https://static.hangang.in/2021/05/30/49e7013f-458c-4f38-a681-b7ba03be0ca8-1622378903280.PNG");
        String message = String.format("유저  id: %d, 유저 포탈 계정 : %s 인 유저가 %s 하였습니다.\n"
                ,user.getId(), user.getPortal_account(), event);
        slackAttachment.setText(message);
        slackParameter.getSlackAttachments().add(slackAttachment);
        slackNotiSender.send(slackTarget,slackParameter);
    }
    //portal 계정으로 들어온 인증 이력을 만료 + soft delete
    private void invalidateAllAuthNumberByPortal(String portalAccount){
        // 회원가입 완료 시  phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴
        AuthNumber authNumber = new AuthNumber();
        authNumber.setIp(this.getClientIp());
        authNumber.setPortal_account(portalAccount);
        authNumber.setFlag(0); // 회원가입 인증
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 현재시간 빼기 하루
        authNumber.setExpired_at(new Timestamp(calendar.getTimeInMillis()));
        userMapper.expirePastAuthNumber(authNumber);
    }

    private void reSignUp(Long id, User user ){
        user.setPassword( BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) );
        //portal 계정으로 들어온 인증 이력을 만료 + soft delete
        this.invalidateAllAuthNumberByPortal(user.getPortal_account());
        // 전공값의 내용이 올바르지 않다면
        for(int i =0; i<user.getMajor().size(); i++){
            boolean result = this.isMajorValid(user.getMajor().get(i));
            if ( !result )
                throw new RequestInputException(ErrorMessage.MAJOR_INVALID_EXCEPTION);
        }

        userMapper.reSignMajors(id,user.getMajor());

        // user salt = timestamp + user_id + BCrypt
        // salt 삽입
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String salt = id.toString() + calendar.getTime();
        salt = (BCrypt.hashpw(salt , BCrypt.gensalt()));
        user.setSalt(salt);
        user.setId(id);
        userMapper.reSignUp(user);
    }
    public Map<String,Object> refresh()throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String refreshToken = request.getHeader(refreshUserName);
        int result = jwt.isValid(refreshToken,1);
        if (result == 1){ // valid하다면
            Map<String,Object> payloads = jwt.validateFormat(refreshToken,1);
            Long id = Long.valueOf(String.valueOf( payloads.get("id")));
            String nickname = String.valueOf( payloads.get("nickname"));
            Map<String,Object> token = new HashMap<>();
            token.put(access_token, jwt.generateToken(id, nickname, access_token) );
            token.put(refresh_token, jwt.generateToken(id,nickname,refresh_token));
            return token;
        }
        else if (  result == 0 ){
            throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION); // REFRESH 토근에 ACCESS 토근이 들어온 경우
        }
        else{
            throw new RefreshTokenInvalidException(ErrorMessage.UNDEFINED_EXCEPTION); // pass도 expire도 invalid도 아닌경우 발생
        }
    }

    public String sendEmail(AuthNumber authNumber) throws Exception {

        //회원가입 요청이라면 , 가입한 아이디이면 throw
        if (authNumber.getFlag() == 0) {

            Long id = userMapper.getUserIdFromPortal(authNumber.getPortal_account());
            if (id != null) {
                throw new RequestInputException(ErrorMessage.EMAIL_ALREADY_AUTHED);
            }
        }
        //비밀번호 찾기 요청이라면, 가입하지 않은 아이디면 throw
        if (authNumber.getFlag() == 1) {

            Long id = userMapper.getUserIdFromPortal(authNumber.getPortal_account());
            if (id == null) {
                throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
            }
        }

        // 1일 5회 요청제한을 넘겼는지
        String ip = this.getClientIp();
        Calendar calendar = Calendar.getInstance(); // 싱글톤 객체라긔
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year,month,day,0,0 ,0); // 해당 날짜의 00시 00분 00초
        Timestamp start =  new Timestamp(calendar.getTimeInMillis());
        calendar.set(year,month,day,23,59 ,59); // 해당 날짜의 23시 59분 59초
        Timestamp end =  new Timestamp(calendar.getTimeInMillis());
        Integer count = userMapper.authNumberAllSoftDeleteAfterUse(authNumber.getPortal_account(),ip,start, end);
        System.out.println(count);
        if ( count >= 5 ){
            throw new RequestInputException(ErrorMessage.EMAIL_COUNT_EXCEED_EXCEPTION); // 요청한 날의 요청횟수가 5번을 초과한경우
        }

        // 재전송의 경우 phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴
        authNumber.setIp(ip);
        calendar.setTime(new Date()); // 다시 현재시간
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 현재시간 빼기 하루
        authNumber.setExpired_at(new Timestamp(calendar.getTimeInMillis()));
        userMapper.expirePastAuthNumber(authNumber);



        //get random string for secret String
        Random rnd = new Random();
        String secret = "";
        for( int i=0; i<6; i++){
            //secret += String.valueOf((char) ((int) (rnd.nextInt(26)) + 97)); // 6글자의 random string
            secret  += rnd.nextInt(10);// 글자의 random numbers
        }
        Context context = new Context();
        context.setVariable("secret",secret );


        //set auth_number to database
        authNumber.setSecret(secret);

        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 만료기한 10분
        authNumber.setExpired_at(new Timestamp( (calendar.getTime()).getTime()));
        authNumber.setIp(this.getClientIp());

        userMapper.setAuthNumber(authNumber);


        // send mail to portal_account email
        String body = null;
        if ( authNumber.getFlag() == 0) {
            body = springTemplateEngine.process("signUpEmail", context);
            sesSender.sendMail("no-reply@bcsdlab.com", authNumber.getPortal_account(), "한강 서비스 회원가입 인증", body);
        }
        else if ( authNumber.getFlag() == 1) {
            body = springTemplateEngine.process("findPassword", context);
            sesSender.sendMail("no-reply@bcsdlab.com", authNumber.getPortal_account(), "한강서비스 비밀번호 재발급 인증", body);
        }

        return "Email을 발송했습니다.";
    }
   public boolean configEmail(AuthNumber authNumber) throws Exception{


       //회원가입 요청이라면 , 가입한 아이디이면 throw
       if (authNumber.getFlag() == 0) {

           Long id = userMapper.getUserIdFromPortal(authNumber.getPortal_account());
           if (id != null) {
               throw new RequestInputException(ErrorMessage.EMAIL_ALREADY_AUTHED);
           }
       }
       //비밀번호 찾기 요청이라면, 가입하지 않은 아이디면 throw
       if (authNumber.getFlag() == 1) {

           Long id = userMapper.getUserIdFromPortal(authNumber.getPortal_account());
           if (id == null) {
               throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
           }
       }
        //portal account, flag, is_deleted = 0 값으로 select 해옴
        ArrayList<AuthNumber> list = userMapper.getSecret(authNumber);

        //메일로 보낸적 없다면 email인증을 신청하라고 알림
        if (list.size() == 0){
            throw new RequestInputException(ErrorMessage.EMAIL_NONE_AUTH_EXCEPTION);
        }

        //request secret값이 일치하는지? request client의 ip값이 일치하는지?
        AuthNumber dbAuthNumber = null;
        String ip = this.getClientIp();
        for(int i=0;i<list.size();i++){
            if ( authNumber.getSecret().equals(list.get(i).getSecret()) && ip.equals(list.get(i).getIp())){
                dbAuthNumber = list.get(i);
                break;
            }
        }

        // ip가 다른경우도 따로 처리해야함, 현재는 공통으로 invalid 처리
        // portal으로 가져왔으나 secret 값이 다르다면 인증번호를 확인하라는 알림
        if ( dbAuthNumber == null){
            throw new RequestInputException(ErrorMessage.EMAIL_SECRET_INVALID_EXCEPTION);
        }

        //만료시간보다 현재시간이 크다면 만료되었다고 알림
        Timestamp exp = dbAuthNumber.getExpired_at();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        System.out.println(exp );
        System.out.println(now );
        if( exp.getTime() < now.getTime()) {
            userMapper.authNumberSoftDelete(dbAuthNumber.getId()); // 만료시 soft delete
            throw new RequestInputException(ErrorMessage.EMAIL_EXPIRED_AUTH_EXCEPTION);
        }
        // 만료되지않았고 / secret 같고 // portal 같고 / ip 같다면 ==> is_authed = 1
        if ( dbAuthNumber.getSecret().equals(authNumber.getSecret())){
            userMapper.setIs_authed(true,dbAuthNumber.getId(), dbAuthNumber.getFlag());
            return true;
        }
        else{
            return false;
        }
    }

    private String getClientIp(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip =request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    public boolean checkNickname(String nickname) {
        String dbNickname = userMapper.getUserByNickName(nickname);
        if( nickname.equals(signOutNickName)){
            throw new RequestInputException(ErrorMessage.BANNED_NICKNAME);
        }
        else if ( dbNickname != null )
            throw new RequestInputException(ErrorMessage.NICKNAME_DUPLICATED);
        else
            return true;
    }

    public void findPassword(User user) throws  Exception{
        Long id = userMapper.getUserIdFromPortal(user.getPortal_account());
        // 없는 아이디는 아닌지?
        if (id == null) {
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);
        }

        //이메일 인증 여부 체크
        ArrayList<AuthNumber> list = userMapper.getAuthTrue(user.getPortal_account(),1);
        if ( list.size() == 0){
            throw new RequestInputException(ErrorMessage.EMAIL_NONE_AUTH_EXCEPTION);
        }
        // 비밀번호 암호화
        user.setPassword( BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) );

        // 비밀번호 찾기 완료 시  phoneNumber, flag, ip가 같은 이전 이력은 모두 만료 + soft delete 시킴
        AuthNumber authNumber = new AuthNumber();
        authNumber.setIp(this.getClientIp());
        authNumber.setPortal_account(user.getPortal_account());
        authNumber.setFlag(1); // 비밀번호 찾기 인증
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1); // 현재시간 빼기 하루
        authNumber.setExpired_at(new Timestamp(calendar.getTimeInMillis()));
        userMapper.expirePastAuthNumber(authNumber);

        userMapper.findPassword(user);
    }

    // token의 id를 가져와 User를 반환하는 Method
    public User getLoginUser() throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(accessTokenName);
        if ( token == null){
            return null;
        }
        else {
            // user id로 User를 select 하는것은 자유롭게 해도 좋으나, salt값은 조회,수정 하면안된다. 만약 참고할 일이있으면 정수현에게 다렉을 보내도록하자.
            if ( jwt.isValid(token,0) ==0 ) {
                Map<String, Object> payloads = jwt.validateFormat(token, 0);
                Long id = Long.valueOf(String.valueOf(payloads.get("id")));
                return userMapper.getMe(id);
            }
            else{
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
        }
    }



    @Override
    public Map<String, Long> getLectureBankCount(){
        Long id = this.getLoginUserId();
        Map<String,Long> map = new HashMap<>();
        map.put("LectureReview", userMapper.getLectureReviewCount(id));
        map.put("getLectureBankCount", userMapper.getLectureBankCount(id));
        map.put("getLectureBankCommentCount", userMapper.getLectureBankCommentCount(id));
        return map;
    }

    @Override
    public List<PointHistory> getUserPointHistory(){
        Long id = this.getLoginUserId();
        List list = userMapper.getUserPointHistory(id);
        return list;
    }

    private Long getLoginUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(accessTokenName);
        if ( token == null){
            throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }
        else {
            // user id로 User를 select 하는것은 자유롭게 해도 좋으나, salt값은 조회,수정 하면안된다. 만약 참고할 일이있으면 정수현에게 다렉을 보내도록하자.
            if ( jwt.isValid(token,0) ==0 ) {
                Map<String, Object> payloads = jwt.validateFormat(token, 0);
                Long id = Long.valueOf(String.valueOf(payloads.get("id")));
                return id;
            }
            else{
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
        }
    }
    @Override
    public void updateUser(User user) throws  Exception{

        if  (user.getMajor().size() == 0 ){
            throw new RequestInputException(ErrorMessage.MAJOR_INVALID_EXCEPTION);
        }
        User dbUser = this.getLoginUser();

        // 전공값의 내용이 올바르지 않다면
        for(int i =0; i<user.getMajor().size(); i++){
            boolean result = this.isMajorValid(user.getMajor().get(i));
            if ( !result )
                throw new RequestInputException(ErrorMessage.MAJOR_INVALID_EXCEPTION);
        }

        // 현재 유저의 닉네임과 들어온 닉네임이 같은 경우는 체크하지 않는다
        // 다른 경우 체크한다.
        if ( !dbUser.getNickname().equals(user.getNickname())){
            this.checkNickname(user.getNickname());
        }
        //updateUser
        userMapper.updateUser(dbUser.getId() ,user.getNickname(),user.getMajor(), user.getName());

    }

    @Override
    public BaseResponse deleteUser(){
        // soft delete and nickname update to "(탈퇴한 회원)"
        Long id = this.getLoginUserId();
        userMapper.softDeleteUser(id, signOutNickName);
        return new BaseResponse("회원탈퇴 완료", HttpStatus.OK);
    }

    private boolean isMajorValid(String major){
        boolean check = false;
        for( Major m : Major.values()){
            if ( major.equals(( String.valueOf(m)) )){
                check = true;
            }
        }
        if (check)
            return true;
        else
            return false;
    }
    // 유저가 구매한 강의자료들과 대상 강의에 대한 정보
    public List<UserLectureBank> getUserPurchasedLectureBank(){
        Long id = this.getLoginUserId();
        return userMapper.getUserPurchasedLectureBank(id);

    }

    @Override
    public BaseResponse updateUserSort(){
        Long userId = this.getLoginUserId();
        // user salt = timestamp + user_id + BCrypt
        // salt 삽입
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String salt = userId.toString() + calendar.getTime();
        salt = (BCrypt.hashpw(salt , BCrypt.gensalt()));
        userMapper.setSalt(salt,userId);
        return new BaseResponse("모든 기기에서 로그아웃 되었습니다.", HttpStatus.OK);
    }

}
