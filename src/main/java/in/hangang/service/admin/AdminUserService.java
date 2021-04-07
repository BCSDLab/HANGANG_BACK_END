package in.hangang.service.admin;


import in.hangang.domain.GrantAdmin;
import in.hangang.response.BaseResponse;
import in.hangang.service.UserService;

public interface AdminUserService extends UserService {

    BaseResponse grantAuthority(GrantAdmin grantAdmin);
    BaseResponse deleteAuthority(GrantAdmin grantAdmin);
}
