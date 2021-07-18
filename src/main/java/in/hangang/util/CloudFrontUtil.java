package in.hangang.util;

import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.services.s3.internal.ServiceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@Component
public class CloudFrontUtil {
    //CloudFront 도메인
    @Value("${cloudfront.distributionDomain}")
    String distributionDomain;

    @Value("${cloudfront.keypairid}")
    String keyPairId;

    @Value("${cloudfront.key.path}")
    String keyPath;

    @Resource
    File privateKeyFile;

    public String generateSignedUrl() throws InvalidKeySpecException, IOException {

        //프로토콜
        SignerUtils.Protocol protocol = SignerUtils.Protocol.http;
        //접근 가능 ip 주소
        String ipRange = "0.0.0.0/0";
        //S3 파일명
        String s3ObjectKey = "reference.jpg";

        //생성 시간
        Date generateDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(generateDate);
        //expire 시간 설정
        calendar.add(Calendar.HOUR, 1);
        Date expireDate = calendar.getTime();

        //expire 시간만 적용된 url
        String url = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                protocol, distributionDomain, privateKeyFile,
                s3ObjectKey, keyPairId, expireDate);

        return url;

        /*** generate, expire 시간, ip주소까지 적용된 url
        String url2 = CloudFrontUrlSigner.getSignedURLWithCustomPolicy(
                protocol, distributionDomain, privateKeyFile,
                s3ObjectKey, keyPairId, expireDate,
                generateDate, ipRange);
         ***/
    }


    public void generateSignedCookie(HttpServletResponse response) throws ParseException, InvalidKeySpecException, IOException {

        String s3ObjectKey = "reference.jpg";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //expire 시간 설정
        calendar.add(Calendar.HOUR, 1);
        Date expireDate = calendar.getTime();

        CloudFrontCookieSigner.CookiesForCannedPolicy cookies = CloudFrontCookieSigner.getCookiesForCannedPolicy(
                SignerUtils.Protocol.http,
                distributionDomain,
                privateKeyFile,
                s3ObjectKey,
                keyPairId,
                expireDate
        );
        Cookie keyPairId = new Cookie(cookies.getKeyPairId().getKey(), cookies.getKeyPairId().getValue());
        Cookie expire = new Cookie(cookies.getExpires().getKey(), cookies.getExpires().getValue());
        Cookie signature = new Cookie(cookies.getSignature().getKey(), cookies.getSignature().getValue());

        response.addCookie(keyPairId);
        response.addCookie(expire);
        response.addCookie(signature);
    }

}
