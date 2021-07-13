package in.hangang.util;


import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.InvalidKeySpecException;
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

    public String generateSignedUrl() throws InvalidKeySpecException, IOException {

        //프로토콜
        SignerUtils.Protocol protocol = SignerUtils.Protocol.http;
        //CloudFront private key file
        File privateKeyFile = new File(keyPath);
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

        //generate, expire 시간, ip주소까지 적용된 url
        /*
        String url2 = CloudFrontUrlSigner.getSignedURLWithCustomPolicy(
                protocol, distributionDomain, privateKeyFile,
                s3ObjectKey, keyPairId, expireDate,
                generateDate, ipRange);
         */
        /*
        기존 레퍼런스가 사용하던 방법
        Date dateLessThan = DateUtils.parseISO8601Date("2021-11-14T22:20:00.000Z");
        Date dateGreaterThan = DateUtils.parseISO8601Date("2021-11-14T22:20:00.000Z");
        String url1 = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                protocol, distributionDomain, privateKeyFile,
                s3ObjectKey, keyPairId, dateLessThan);

        String url2 = CloudFrontUrlSigner.getSignedURLWithCustomPolicy(
                protocol, distributionDomain, privateKeyFile,
                s3ObjectKey, keyPairId, dateLessThan,
                dateGreaterThan, ipRange);

        return url;
        */
    }
}
