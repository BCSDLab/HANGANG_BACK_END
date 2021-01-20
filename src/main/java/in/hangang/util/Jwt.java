package in.hangang.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import in.hangang.enums.ErrorMessage;
import in.hangang.exception.AccessTokenExpireException;
import in.hangang.exception.AccessTokenInvalidException;
import in.hangang.exception.RefreshTokenExpireException;
import in.hangang.exception.RefreshTokenInvalidException;
import in.hangang.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class Jwt {
    @Autowired
    private UserMapper userMapper;

    @Value("${token.access}")
    private String access_token;

    @Value("${token.refresh}")
    private String refresh_token;

    // return generated jwt token method
    public String generateToken(Long id, String nickname , String sub){
        String key = userMapper.getSalt(id);

        Map<String, Object> headers = new HashMap<String, Object>(); // header
        headers.put("typ", "JWT");
        headers.put("alg","HS256");
        Map<String, Object> payloads = new HashMap<String, Object>(); //payload
        payloads.put("nickname", nickname); // name
        payloads.put("id", id );
        payloads.put("sub", sub);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        if( sub.equals(access_token)) {
            calendar.add(Calendar.HOUR_OF_DAY, 24); // access token expire 24h later
            //calendar.add(Calendar.MINUTE, 2); // test용 2분
        }
        else{
            //calendar.add(Calendar.MINUTE, 4); // test용 4분
            calendar.add(Calendar.DAY_OF_YEAR, 14); // refresh token expire 14day later
        }
        Date exp = calendar.getTime();

        String jwt = Jwts.builder().setHeader(headers).setClaims(payloads).setExpiration(exp).signWith(SignatureAlgorithm.HS256, key.getBytes()).compact();
        return jwt;
    }

    // token validation check method
    public int isValid(String token, Integer flag){
        /*
            if (header == null || !token.startsWith("Bearer "))
            return null;
         */
        String authToken = "";
            if ( token == null && flag == 1 ) { // refresh token이 null로 요청된 경우
                throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
            else if ( !token.startsWith("Bearer ") && flag == 0  ) { // access token의 길이가 8보다 작은 경우
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
            else if ( !token.startsWith("Bearer ") && flag == 1){ // refresh toekn의 길이가 8보다 작은 경우
                throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
            else{ // 여기서도 String index bound exception이 발생할 수 있었다.
                authToken = token.substring(7); // "Bearer " 제거
        }
        Map<String,Object> payloads = this.validateFormat(authToken,flag);
        String key = userMapper.getSalt(Long.valueOf(String.valueOf( payloads.get("id"))));
        String sub = String.valueOf(payloads.get("sub"));

        try {
            Claims claims = Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(authToken).getBody();
            Date exp = claims.get("exp", Date.class);
            Date now = new Date();

            // refresh token이 access로 들어간 경우 pass되는 경우 방지
            // exp.get time 할필요 없음. test후 지울 예정
            if ( sub.equals(access_token)) {
                return 0; // access, true
            }
            else
                return 1; // refresh true

        }catch (ExpiredJwtException e1){
            if ( sub.equals(access_token) && flag == 0)
                throw new AccessTokenExpireException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_EXPIRE_EXCEPTION); // access expire
            else if ( sub.equals(refresh_token) && flag == 1)
                throw new RefreshTokenExpireException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_EXPIRE_EXCEPTION); // refresh expire
            else if ( sub.equals(access_token) && flag == 1)
                throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION); // access in refresh
            else
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION); // refresh in access
        }
        catch(Throwable e2){
            if ( sub.equals(access_token))
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            else {
                throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
        }
    }
    // Token의 Payload를 decode해서 반환
    // flag 0 은 access , 1은 refresh
    public Map<String, Object> validateFormat(String token,Integer flag) {

        if (token == null && flag == 0) {
            throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }else if ( token == null && flag == 1){
            throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }


        String[] strings = token.split("\\.");
        if (strings.length != 3 && flag == 0) {
            throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }
        else if ( strings.length != 3 && flag == 1){
            throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }
        Map<String, Object> map = null;
        try {
            map = new ObjectMapper().readValue(new String(Base64.decodeBase64(strings[1])), Map.class);
            if (map.get("sub") == null || map.get("id") == null || map.get("exp") == null || map.get("nickname") == null) {
                if ( flag == 0){
                    throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
                }
                else{
                    throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
                }
            }

        } catch (Exception e) {
            if ( flag == 0) {
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }else{
                throw new RefreshTokenInvalidException(ErrorMessage.REFRESH_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
        }
        return map;
    }
}
