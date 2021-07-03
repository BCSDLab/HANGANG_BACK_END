package in.hangang.mapper;


import in.hangang.domain.*;
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
    Long getLectureBankCount(Long id);
    Long getLectureReviewCount(Long id);
    Long getLectureBankCommentCount(Long id);
    void addPointHistory(Long user_id, Integer variance, Integer pointTypeId);
    void addPoint(Long user_id, Integer variance);
    List<PointHistory> getUserPointHistory(Long id);
    void updateUser(Long id, String nickname, List<String> major, String name);
    void insertMajors(Long id, List<String> major);
    Long getUserIdFromPortalForReSignUp(String portal_account);
    void reSignUp(User user);
    void softDeleteUser(Long id, String nickname);
    void reSignMajors(Long id, List<String> major);
    List<UserLectureBank> getUserPurchasedLectureBank(Long id);
    String getRole(Long id);
    List<Authority> getAuthority(Long id);
    void grantAuthority(Long id, Integer flag);
    void deleteAuthority(Long id, Integer flag);

}
