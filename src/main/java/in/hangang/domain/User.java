package in.hangang.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private Long id;

    @Email(groups = {ValidationGroups.logIn.class, ValidationGroups.signUp.class, ValidationGroups.findPassword.class} , message = "이메일의 형식이 아닙니다.")
    @NotNull(groups = {ValidationGroups.logIn.class, ValidationGroups.signUp.class,ValidationGroups.findPassword.class} , message = "이메일은 비워둘 수 없습니다.")
    private String portal_account;

    @Size( min=64, max = 64,groups = { ValidationGroups.signUp.class, ValidationGroups.findPassword.class,  ValidationGroups.logIn.class}, message = "비밀번호의 해쉬값은 64글자입니다")
    @NotNull(groups = {ValidationGroups.logIn.class, ValidationGroups.signUp.class, ValidationGroups.findPassword.class} , message = "패스워드는 비워둘 수 없습니다.")
    private String password;
    @Size(min = 1, max=30 , groups = {ValidationGroups.updateUser.class, ValidationGroups.signUp.class}, message = "닉네임은 1글자이상 30글자 이하입니다.")
    @NotNull(groups = {ValidationGroups.signUp.class, ValidationGroups.updateUser.class} , message = "닉네임은 비워둘 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{1,20}$", groups = {ValidationGroups.signUp.class, ValidationGroups.updateUser.class} , message = "닉네임의 특수문자와 초성은 사용불가능합니다")
    private String nickname;

    @Size(max = 2, groups = {ValidationGroups.updateUser.class, ValidationGroups.signUp.class}, message = "전공은 두개이상일 수 없습니다.")
    @NotNull(groups = {ValidationGroups.signUp.class, ValidationGroups.updateUser.class} , message = "전공은 비워둘 수 없습니다.")
    private ArrayList<
            @Length(min=4, max=15, groups = {ValidationGroups.signUp.class,ValidationGroups.updateUser.class}, message = "전공은 4글자 이상 15글자 이하입니다.")
            @NotNull(groups = {ValidationGroups.signUp.class, ValidationGroups.updateUser.class}, message = "전공은 비워둘 수 없습니다") String> major;

    @ApiModelProperty(hidden = true)
    private String salt;
    @ApiModelProperty(hidden = true)
    private String role;
    @ApiModelProperty(hidden = true)
    private List<Authority> authorityList;
    @ApiModelProperty(hidden = true )
    private String profile_image_url;
    @ApiModelProperty(hidden = true)
    private Integer point;
    @ApiModelProperty(hidden = true)
    private Boolean is_deleted ;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;
    @ApiModelProperty(hidden = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    public ArrayList<String> getMajor() {
        return major;
    }

    public void setMajor(ArrayList<String> major) {
        this.major = major;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
