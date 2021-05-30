package in.hangang.service;


import in.hangang.domain.*;
import in.hangang.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String,String> login(User user)throws Exception;
    Map<String,Object> refresh()throws Exception;
    BaseResponse signUp(User user) throws Exception;
    String sendEmail(AuthNumber authNumber) throws Exception;
    boolean configEmail(AuthNumber authNumber) throws Exception;
    boolean checkNickname(String nickname);
    void findPassword(User user)throws  Exception;
    User getLoginUser() throws Exception;
    Map<String, Long> getLectureBankCount();
    List<PointHistory> getUserPointHistory();
    void updateUser(User user) throws  Exception;
    BaseResponse deleteUser();
    List<UserLectureBank> getUserPurchasedLectureBank();
    BaseResponse updateUserSort();
    void sendNoti(User user,String event) throws Exception;
}
