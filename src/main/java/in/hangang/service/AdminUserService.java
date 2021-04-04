package in.hangang.service;


import in.hangang.domain.GrantAdmin;
import in.hangang.response.BaseResponse;

public interface AdminUserService extends UserService {

    BaseResponse grantAuthority(GrantAdmin grantAdmin);
    BaseResponse deleteAuthority(GrantAdmin grantAdmin);
}
