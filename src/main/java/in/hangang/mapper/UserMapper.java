package in.hangang.mapper;


import in.hangang.annotation.Auth;
import in.hangang.domain.AuthNumber;
import in.hangang.domain.PointHistory;
import in.hangang.domain.User;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface UserMapper {
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
    Long getLectureBankCount(Long id);
    Long getLectureReviewCount(Long id);
    Long getLectureBankCommentCount(Long id);
    void addPointHistory(Long user_id, Integer variance, Integer pointTypeId);
    List<PointHistory> getUserPointHistory(Long id);
    void updateUser(Long id, String nickname, List<String> major);
    void insertMajors(Long id, List<String> major);
}
