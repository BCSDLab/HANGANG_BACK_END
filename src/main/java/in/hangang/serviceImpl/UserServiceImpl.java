package in.hangang.serviceImpl;

import in.hangang.domain.AuthNumber;
import in.hangang.domain.User;
import in.hangang.enums.ErrorMessage;
import in.hangang.enums.Major;
import in.hangang.exception.RefreshTokenExpireException;
import in.hangang.exception.RefreshTokenInvalidException;
import in.hangang.exception.RequestInputException;
import in.hangang.mapper.UserMapper;
import in.hangang.service.UserService;
import in.hangang.util.Jwt;
import in.hangang.util.SesSender;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
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


    public Map<String,String> login(User user) throws Exception{

        User dbUser = userMapper.getPasswordFromPortal(user.getPortal_account());

        // 아이디 중복 검사
        if ( dbUser == null){
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }
        // 비밀번호가 틀린 경우
        if ( !BCrypt.checkpw( user.getPassword(), dbUser.getPassword() )){
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }
        // 로그인이 성공한 경우 , access token, refresh token 반환
        else{
            Map<String, String> token = new HashMap<>();
            token.put("access_token", jwt.generateToken(dbUser.getId(), dbUser.getNickname(), "access_token") );
            token.put("refresh_token", jwt.generateToken(dbUser.getId(),dbUser.getNickname(),"refresh_token"));
            return token;
        }
    }

    private void setMajor(String major, Long user_id) throws Exception{

        if (user_id == null){
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }

        boolean check = false;
        for( Major majors : Major.values()){
            if ( major.equals(( String.valueOf(majors)) )){
                check = true;
            }
        }

        if (check)
            userMapper.setMajor(major,user_id);
        else
            throw new RequestInputException(ErrorMessage.MAJOR_INVALID_EXCEPTION);
    }

    public void signUp(User user) throws Exception {

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
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }

        // 닉네임 null,중복 체크
        if (user.getNickname() != null) {
            if (userMapper.getUserByNickName(user.getNickname() ) != null ) {
                throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
            }
        }

        // 암호화
        user.setPassword( BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) );
        userMapper.signUp(user); // 회원가입


        //회원가입후 user의 가입된 id를 구함
        Long user_id = userMapper.getUserIdFromPortal(user.getPortal_account());

        // n개의 전공을 삽입
        for ( int i=0; i< user.getMajor().size(); i++){
            setMajor(user.getMajor().get(i), user_id);
        }

        // user salt = timestamp + user_id + BCrypt
        // salt 삽입
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String salt = user_id.toString() + calendar.getTime();
        salt = (BCrypt.hashpw(salt , BCrypt.gensalt()));
        userMapper.setSalt(salt,user_id);
        // 인증 테이블에 해당 portal 계정 모두 삭제
        AuthNumber authNumber = new AuthNumber();
        authNumber.setPortal_account(user.getPortal_account());
        authNumber.setFlag(0);
        userMapper.deleteAllAuthNumber(authNumber);
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
            token.put("access_token", jwt.generateToken(id, nickname, "access_token") );
            token.put("refresh_token", jwt.generateToken(id,nickname,"refresh_token"));
            return token;
        }
        else if (result == -2 ){ // refresh expire
            throw new RefreshTokenExpireException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_EXPIRE_EXCEPTION);
        }
        else if ( result == -1 || result == 0){
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

        // 포탈계정 기준 재요청이라면 이전 내용 전부 삭제
        userMapper.deleteAllAuthNumber(authNumber);


        //get random string for secret String
        Random rnd = new Random();
        String secret = "";
        for( int i=0; i<20; i++){
            secret += String.valueOf((char) ((int) (rnd.nextInt(26)) + 97));
        }
        Context context = new Context();
        context.setVariable("secret",secret );


        //set auth_number to database
        authNumber.setSecret(secret);

        Calendar calendar = Calendar.getInstance();
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
        //portal account, flag 값으로 select 해옴
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
        // ip가 다른경우도 처리해야함, 현재는 공통으로 처리 수정 필요

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
        if (userMapper.getUserByNickName(nickname) == null)
            return true;
        else
            return false;
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

        // 인증 테이블에 해당 portal 계정 모두 삭제
        AuthNumber authNumber = new AuthNumber();
        authNumber.setPortal_account(user.getPortal_account());
        authNumber.setFlag(1);
        userMapper.deleteAllAuthNumber(authNumber);

        userMapper.findPassword(user);
    }

    // token의 id를 가져와 User를 반환하는 Method
    public Long getUserIdByToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(accessTokenName);
        // jwt token의 valid 체크를 해야하는가?
        // interceptor에서 이미 진행하므로 생략해도 괜찮을 것 같다.
        // user id로 User를 select 하는것은 자유롭게 해도 좋으나, salt값은 조회,수정 하면안된다. 만약 참고할 일이있으면 정수현에게 다렉을 보내도록하자.
        Map<String,Object> payloads = jwt.validateFormat(token,0);
        Long id = Long.valueOf(String.valueOf( payloads.get("id")));
        return id;
    }
}
