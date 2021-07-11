package in.hangang.serviceImpl.admin;

import in.hangang.annotation.Auth;
import in.hangang.domain.Authority;
import in.hangang.domain.GrantAdmin;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.RequestInputException;
import in.hangang.response.BaseResponse;
import in.hangang.service.admin.AdminUserService;
import in.hangang.serviceImpl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class AdminUserServiceImpl extends UserServiceImpl implements AdminUserService {




    public BaseResponse grantAuthority(GrantAdmin grantAdmin){
        Long id = userMapper.getUserIdFromPortal(grantAdmin.getPortalAccount());

        //없는 유저에 경우 Error
        if ( id == null)
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);

        String role = userMapper.getRole(id);

        // 대상이 루트 유저인 경우 불가능
        if ( role != null && role.equals(Auth.Role.ROOT.toString()))
            throw new RequestInputException(ErrorMessage.ROOT_AUTHORITY_CAN_NOT_DELETE_EXCEPTION);

        //부여하고자 하는 권한
        String target = this.getTarget(grantAdmin.getFlag());

        // 유저의 권한 리스트를 찾아옴
        List<Authority> list = userMapper.getAuthority(id);
        boolean check = false;
        for (int i=0; i<list.size(); i++){
            if ( list.get(i).getAuthority().equals(target)){
                check = true;
            }
        }
        // 이미 있는 권한이라면
        if (check){
            return new BaseResponse("해당 유저는 이미 권한을 가지고 있습니다", HttpStatus.OK);
        }

        //권한 부여
        userMapper.grantAuthority(id, grantAdmin.getFlag());
        return new BaseResponse("권한을 부여했습니다", HttpStatus.OK);
    }
    public BaseResponse deleteAuthority(GrantAdmin grantAdmin){
        Long id = userMapper.getUserIdFromPortal(grantAdmin.getPortalAccount());

        //없는 유저에 경우 Error
        if ( id == null)
            throw new RequestInputException(ErrorMessage.NO_USER_EXCEPTION);

        String role = userMapper.getRole(id);

        // 대상이 일반 유저인 경우 불가능
        if ( role == null)
            throw new RequestInputException(ErrorMessage.USER_IS_NOT_MANAGER);
        // 대상이 루트 유저인 경우 불가능
        if ( role.equals(Auth.Role.ROOT.toString()))
            throw new RequestInputException(ErrorMessage.ROOT_AUTHORITY_CAN_NOT_DELETE_EXCEPTION);

        //부여하고자 하는 권한
        String target = this.getTarget(grantAdmin.getFlag());

        // 유저의 권한 리스트를 찾아옴
        List<Authority> list = userMapper.getAuthority(id);
        boolean check = false;
        for (int i=0; i<list.size(); i++){
            if ( list.get(i).getAuthority().equals(target)){
                check = true;
            }
        }
        // 없는 권한이라면
        if (!check){
            return new BaseResponse("해당 유저는 이미 권한을 가지고 있지 않습니다.", HttpStatus.OK);
        }

        //권한 제거
        userMapper.deleteAuthority(id, grantAdmin.getFlag());
        return new BaseResponse("권한을 제거하였습니다.", HttpStatus.OK);
    }

    private String getTarget(Integer flag){
        switch (flag){
            case 1:
                return Auth.Authority.LectureBank.toString();
            case 2:
                return Auth.Authority.Lecture.toString();
            case 3:
                return Auth.Authority.TimeTable.toString();
            default:
                // spring validation에서 체크가 되기 때문에 발생하지 않는 에러이다.
                throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }
    }
}
