package in.hangang.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class LectureBankComment {
    private Long id;
    private Long user_id;
    private Long lecture_bank_id;
    private String comments;

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
