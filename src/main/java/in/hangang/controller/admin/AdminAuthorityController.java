package in.hangang.controller.admin;

import in.hangang.annotation.Auth;
import in.hangang.domain.GrantAdmin;
import in.hangang.service.admin.AdminUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin")
public class AdminAuthorityController {

    @Autowired
    @Qualifier("AdminUserServiceImpl")
    private AdminUserService adminUserService;


    @Auth(role = Auth.Role.ROOT)
    @GetMapping("/test")
    @ApiOperation( value = "루트 관리자만 사용가능 apo", notes = " ", authorizations = @Authorization(value = "Bearer +accessToken"))
    public String test(){
        return "test Success";
    }
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.Lecture)
    @ApiOperation( value = "강의평 권한이 있는 관리자 사용가능 api", notes = " ", authorizations = @Authorization(value = "Bearer +accessToken"))
    @GetMapping("/test2")
    public String test2(){
        return "test2 Success";
    }

    @Auth
    @GetMapping("/test3")
    @ApiOperation( value = "일반 유저 사용가능 api", notes = " ", authorizations = @Authorization(value = "Bearer +accessToken"))
    public String test3(){
        return "test3 Success";
    }
    @Auth(role = Auth.Role.MANAGER, authority = Auth.Authority.LectureBank)
    @ApiOperation( value = "강의자료 권한이 있는 관리자 사용가능 api", notes = " ", authorizations = @Authorization(value = "Bearer +accessToken"))
    @GetMapping("/test4")
    public String test4(){
        return "test4 Success";
    }


    @Auth(role = Auth.Role.ROOT)
    @PostMapping("/authority")
    @ApiOperation( value = "관리자의 권한을 추가하는 api", notes = "유저를 권한을 가진 관리자로 만드는 api, 루트관리자만 사용 가능 1-강의자료 2 강의평 3 시간표", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity grantAuthoiry(@RequestBody GrantAdmin grantAdmin) {
        return new ResponseEntity(adminUserService.grantAuthority(grantAdmin), HttpStatus.OK);
    }
    @Auth(role = Auth.Role.ROOT)
    @DeleteMapping("/authority")
    @ApiOperation( value = "관리자의 권한을 제거하는 api", notes = "유저를 권한을 가진 관리자로 만드는 api, 루트관리자만 사용 가능 1-강의자료 2 강의평 3 시간표", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity deleteAuthority(@RequestBody @Valid GrantAdmin grantAdmin) {
        return new ResponseEntity(adminUserService.deleteAuthority(grantAdmin), HttpStatus.OK);
    }

}
