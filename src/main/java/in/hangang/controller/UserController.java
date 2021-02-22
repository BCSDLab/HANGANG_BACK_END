package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.annotation.Xss;
import in.hangang.domain.AuthNumber;
import in.hangang.domain.User;
import in.hangang.mapper.UserMapper;
import in.hangang.response.BaseResponse;
import in.hangang.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    // 로그인
    @Xss
    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    @ApiOperation(value ="로그인" , notes = "로그인을 하기위한 api입니다.")
    public ResponseEntity login(@RequestBody @Validated(ValidationGroups.logIn.class) User user) throws Exception{
        return new ResponseEntity(userService.login(user), HttpStatus.OK);
    }


    //회원가입
    @Xss
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ApiOperation(value ="회원가입" , notes = "회원가입 하기위한 api입니다. 이메일인증이 선행되어야 합니다.")
    public ResponseEntity signUp(@RequestBody @Validated(ValidationGroups.signUp.class) User user) throws Exception{
        userService.signUp(user);
        return new ResponseEntity( new BaseResponse("회원가입에 성공했습니다.", HttpStatus.OK),HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "유저 인증 여부 확인",notes = "유저 인증 여부 확인을 위한 test api입니다.", authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/auth-test" , method =  RequestMethod.GET)
    public ResponseEntity test() throws Exception{
        return new ResponseEntity( new BaseResponse("테스트 성공했습니다.", HttpStatus.OK),HttpStatus.OK);
    }


    @ApiOperation( value = "access 토큰 갱신" ,notes = "refresh token을 이용하여 access token을 갱신하는 api 입니다.", authorizations = @Authorization(value = "Bearer +refreshToken"))
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity refresh()throws Exception{
        return new ResponseEntity(userService.refresh(), HttpStatus.OK);
    }

    // send email to portal_account
    @Xss
    @RequestMapping( value = "/email", method = RequestMethod.POST)
    @ApiOperation(value ="이메일 전송" , notes = "이메일 전송을 위한 api 입니다. flag 0은 회원가입전용, 1은 비밀번호 찾기 전용 입니다.")
    public ResponseEntity sendEmail( @Validated(ValidationGroups.sendEmail.class) @RequestBody AuthNumber authNumber) throws Exception{
        userService.sendEmail(authNumber);
        return new ResponseEntity(  new BaseResponse("메일발송에 성공했습니다.", HttpStatus.OK), HttpStatus.OK);

    }
    //config email by secret random String and client ip address
    @Xss
    @RequestMapping( value ="/email/config", method = RequestMethod.POST)
    @ApiOperation(value ="이메일 인증" , notes = "이메일 인증번호 인증을 위한 api 입니다. flag 0은 회원가입전용, 1은 비밀번호 찾기 전용 입니다. \n 이메일 전송이 선행되어야합니다.")
    public ResponseEntity configEmail(@Validated(ValidationGroups.configEmail.class)  @RequestBody AuthNumber authNumber )throws Exception{
        boolean result = userService.configEmail(authNumber);
        if (result)
            return new ResponseEntity( new BaseResponse("메일인증에 성공했습니다.", HttpStatus.OK),HttpStatus.OK);
        else
            return new ResponseEntity( new BaseResponse("메일발송에 실패했습니다.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @Xss
    @RequestMapping( value = "/nickname-check", method = RequestMethod.POST)
    @ApiOperation(value ="닉네임 중복체크" , notes = "닉네임 중복 체크를 위한 api 입니다.")
    public ResponseEntity checkNickname(@RequestParam @NotNull String nickname ){
        if ( userService.checkNickname(nickname) )
            return new ResponseEntity(new BaseResponse("사용 가능한 닉네임입니다.", HttpStatus.OK) ,HttpStatus.OK);
        else
            return new ResponseEntity(new BaseResponse("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @Xss
    @RequestMapping( value ="/password-find" , method = RequestMethod.POST)
    @ApiOperation(value ="비밀번호 재설정" , notes = "비밀번호 재 설정을 위한 api입니다. 이메일 인증이 선행되어야 합니다.")
    public ResponseEntity passwordFind(@RequestBody User user)throws  Exception{
        userService.findPassword(user);
        return new ResponseEntity( new BaseResponse("비밀번호 재설정에 성공했습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @Xss
    @RequestMapping( value ="/me" , method = RequestMethod.GET)
    @ApiOperation(value ="유저의 현재정보" , notes = "유재의 현재 정보를 반환한다, 로그인 하지 않은 경우 null을 return한다." ,authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity getMe() throws Exception{
        return new ResponseEntity( userService.getLoginUser(), HttpStatus.OK);
    }
}
