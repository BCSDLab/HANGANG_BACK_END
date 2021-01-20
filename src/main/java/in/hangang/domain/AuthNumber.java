package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;


import javax.validation.constraints.*;
import java.sql.Timestamp;

@Component
public class AuthNumber {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private String ip;

    @Email( groups = { ValidationGroups.configEmail.class,  ValidationGroups.sendEmail.class}, message = "포탈계정은 이메일 형식입니다.")
    @NotNull( groups = { ValidationGroups.configEmail.class,  ValidationGroups.sendEmail.class}, message = "포탈계정은 null일 수 없습니다.")
    private String portal_account;
    // size min은 이상, max는 초과이다.
    @Size( min=20, max = 21,groups = ValidationGroups.configEmail.class, message = "인증 키 값은 20글자입니다.")
    @NotNull( groups = ValidationGroups.configEmail.class, message = "인증 키 값은 null일 수 없습니다.")
    private String secret;
    @NotNull( groups = { ValidationGroups.configEmail.class,  ValidationGroups.sendEmail.class}, message = "flag 값은 필수입니다.")
    @Min( value = 0,groups = { ValidationGroups.configEmail.class,  ValidationGroups.sendEmail.class}, message = "이메일 인증의 flag는 0 보다 작을 수 없습니다.")
    @Max(value = 1,groups = { ValidationGroups.configEmail.class,  ValidationGroups.sendEmail.class}, message = "이메일 인증의 flag는 1 보다 클 수 없습니다.")
    private Integer flag;

    @ApiModelProperty(hidden = true)
    private boolean is_authed;
    @ApiModelProperty(hidden = true)
    private Timestamp expired_at;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortal_account() {
        return portal_account;
    }

    public void setPortal_account(String portal_account) {
        this.portal_account = portal_account;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isIs_authed() {
        return is_authed;
    }

    public void setIs_authed(boolean is_authed) {
        this.is_authed = is_authed;
    }

    public Timestamp getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(Timestamp expired_at) {
        this.expired_at = expired_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
