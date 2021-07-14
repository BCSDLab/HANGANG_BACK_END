package in.hangang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class CloudFrontConfig {

    @Value("${cloudfront.key.path}")
    String keyPath;

    @Bean
    public File privateKeyFile(){
        return new File(keyPath);
    }
}
