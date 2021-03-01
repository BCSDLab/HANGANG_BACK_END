package in.hangang.mapper;


import in.hangang.annotation.Auth;
import in.hangang.domain.AuthNumber;
import in.hangang.domain.User;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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
    Integer authNumberAllSoftDeleteAfterUse(String portal_account, String ip,Timestamp start, Timestamp end);
    void setAuthNumber(AuthNumber authNumber);
    ArrayList<AuthNumber> getSecret(AuthNumber authNumber);
    void setIs_authed(boolean is_authed,Long id,Integer flag);
    ArrayList<AuthNumber> getAuthTrue(String portal_account, Integer flag);
    void authNumberSoftDelete(Long in);
    void findPassword(User user);
    void expirePastAuthNumber(AuthNumber authNumber);
    User getMe(Long id);
    void setProfile(Long id, String url);
}
