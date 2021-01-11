package in.hangang.controller;

import in.hangang.annotation.Auth;
import in.hangang.annotation.ValidationGroups;
import in.hangang.annotation.Xss;
import in.hangang.domain.AuthNumber;
import in.hangang.domain.User;
import in.hangang.mapper.UserMapper;
import in.hangang.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
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
    public ResponseEntity login(@RequestBody @Validated(ValidationGroups.logIn.class) User user) throws Exception{
        return new ResponseEntity(userService.login(user), HttpStatus.OK);
    }


    //회원가입
    @Xss
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody @Validated(ValidationGroups.signUp.class) User user) throws Exception{
        userService.signUp(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auth
    @ApiOperation( value = "" , authorizations = @Authorization(value = "Bearer +accessToken"))
    @RequestMapping(value = "/auth-test" , method =  RequestMethod.GET)
    public ResponseEntity test(){
        return new ResponseEntity("test success",HttpStatus.OK);
    }


    @ApiOperation( value = "" , authorizations = @Authorization(value = "Bearer +refreshToken"))
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity refresh()throws Exception{
        return new ResponseEntity(userService.refresh(), HttpStatus.OK);
    }

    // send email to portal_account
    @Xss
    @RequestMapping( value = "/email", method = RequestMethod.POST)
    public ResponseEntity sendEmail( @Validated(ValidationGroups.sendEmail.class) @RequestBody AuthNumber authNumber) throws Exception{
        return new ResponseEntity(userService.sendEmail(authNumber), HttpStatus.OK);

    }
    //config email by secret random String and client ip address
    @Xss
    @RequestMapping( value ="/email/config", method = RequestMethod.POST)
    public ResponseEntity configEmail(@Validated(ValidationGroups.configEmail.class)  @RequestBody AuthNumber authNumber)throws Exception{
        boolean result = userService.configEmail(authNumber);
        if (result)
            return new ResponseEntity("email인증이 확인되었습니다",HttpStatus.OK);
        else
            return new ResponseEntity("email인증이 확인되지 않았습니다.", HttpStatus.BAD_REQUEST);
    }

    @Xss
    @RequestMapping( value = "/nickname-check", method = RequestMethod.POST)
    public ResponseEntity checkNickname(@RequestParam @NotNull String nickname ){
        if ( userService.checkNickname(nickname) )
            return new ResponseEntity("사용 가능한 닉네임입니다." ,HttpStatus.OK);
        else
            return new ResponseEntity("중복된 닉네임입니다.",HttpStatus.BAD_REQUEST);
    }

    /**HAVE TO FIX**/
    @Xss
    @RequestMapping( value ="/password-find" , method = RequestMethod.POST)
    public ResponseEntity passwordFind(@RequestBody User user)throws  Exception{
        userService.findPassword(user);
        return new ResponseEntity( HttpStatus.OK);
    }
}
