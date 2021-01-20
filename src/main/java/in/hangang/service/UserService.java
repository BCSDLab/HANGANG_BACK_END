package in.hangang.service;


import in.hangang.domain.AuthNumber;
import in.hangang.domain.User;
import java.util.Map;

public interface UserService {
    Map<String,String> login(User user)throws Exception;
    Map<String,Object> refresh()throws Exception;
    void signUp(User user) throws Exception;
    String sendEmail(AuthNumber authNumber) throws Exception;
    boolean configEmail(AuthNumber authNumber) throws Exception;
    boolean checkNickname(String nickname);
    void findPassword(User user)throws  Exception;
    Long getUserIdByToken() throws Exception;
}
