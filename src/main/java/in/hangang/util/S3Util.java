package in.hangang.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class S3Util {
    @Resource
    private AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${s3.custom-domain}")
    private String customDomain;

    public String uploadObject(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();

        int index = fileName.lastIndexOf(".");
        String fileExt = fileName.substring(index+1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(new Date());

        UUID uid = UUID.randomUUID();

        String savedName = uid.toString() + "-" + System.currentTimeMillis() + "." + fileExt;

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());

        amazonS3.putObject(new PutObjectRequest(bucket + "/"+ date,
                savedName, multipartFile.getInputStream(), omd)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return "https://" + customDomain +"/" + date + "/" + savedName;
    }

    public void deleteObject(String path,String savedName,boolean isHard) throws AmazonServiceException {

        if(isHard){
            amazonS3.deleteObject(new DeleteObjectRequest(bucket+ "/" + path,savedName));
        }else{
            String key = path+"/"+savedName;
            AccessControlList acl = amazonS3.getObjectAcl(bucket,key);
            acl.getGrantsAsList().clear();
            acl.grantPermission(new CanonicalGrantee(acl.getOwner().getId()), Permission.FullControl);
            amazonS3.setObjectAcl(bucket, key, acl);
        }

    }

    public org.springframework.core.io.Resource getObject(String path, String savedName) throws IOException {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket+"/" + path,savedName));
        org.springframework.core.io.Resource resource = new InputStreamResource(s3Object.getObjectContent());
        return resource;
    }

    public URL getPrivateObjectURL(String objectKey, String filename) throws IOException{
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;    //1 hour
        expiration.setTime(expTimeMillis);


        // input example https://static.hangang.in/2021/05/15/fdd7fe34-f9a4-4d78-9624-36538c1d3fe6-1621061078655.PNG
        String key = objectKey.substring(26);
        // output example 2021/05/15/fdd7fe34-f9a4-4d78-9624-36538c1d3fe6-1621061078655.PNG
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        // file이름을 설정한다. 아래처럼 하지 않는 경우 file 이름이 매우 랜덤해진다\
        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        responseHeaders.setContentDisposition("attachment; filename =\"" +URLEncoder.encode(filename, "UTF-8") + "\"");
        generatePresignedUrlRequest.setResponseHeaders(responseHeaders);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url;
    }

    public String privateUpload(MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();

        int index = fileName.lastIndexOf(".");
        String fileExt = fileName.substring(index+1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(new Date());

        UUID uid = UUID.randomUUID();

        String savedName = uid.toString() + "-" + System.currentTimeMillis() + "." + fileExt;

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());

        amazonS3.putObject(new PutObjectRequest(bucket + "/"+ date,
                savedName, multipartFile.getInputStream(), omd)
                .withCannedAcl(CannedAccessControlList.Private));

        return "https://" + customDomain +"/" + date + "/" + savedName;
    }

    public void deleteObjectbyKey(String objectKey){
        amazonS3.deleteObject(bucket, objectKey);

    }

}
