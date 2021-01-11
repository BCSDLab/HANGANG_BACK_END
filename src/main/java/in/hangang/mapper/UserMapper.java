package in.hangang.mapper;


import in.hangang.domain.AuthNumber;
import in.hangang.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface UserMapper {
    void setMajor(String major,Long user_id);
    void setSalt(String salt,Long user_id);
    void signUp(User user);
    Long getUserIdFromPortal(String portal_account);
    String getUserByNickName(String nickname);
    User getPasswordFromPortal(String portal_account);
    String getSalt(Long id);

    void setAuthNumber(AuthNumber authNumber);
    ArrayList<AuthNumber> getSecret(AuthNumber authNumber);
    void setIs_authed(boolean is_authed,Long id,Integer flag);
    ArrayList<AuthNumber> getAuthTrue(String portal_account, Integer flag);
    void deleteAllAuthNumber(AuthNumber authNumber);
    void findPassword(User user);
}
