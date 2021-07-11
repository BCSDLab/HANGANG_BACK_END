package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class LectureBankComment {

    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long lecture_bank_id;
    @ApiModelProperty(hidden = true)
    private Long user_id;
    @ApiModelProperty(hidden = true)
    private String nickname;
    @NotNull(groups = {ValidationGroups.PostLectureBankComment.class} ,message = "본문을 비울 수는 없습니다")
    @Length(min=1,  groups = {ValidationGroups.PostLectureBankComment.class}, message = "본문은 최소 한글자 이상입니다")
    private String comments;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getLecture_bank_id() {
        return lecture_bank_id;
    }

    public void setLecture_bank_id(Long lecture_bank_id) {
        this.lecture_bank_id = lecture_bank_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
