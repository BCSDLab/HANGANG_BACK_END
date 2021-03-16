package in.hangang.domain;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

public class TimeTable {
    @ApiModelProperty(hidden = true)
    private Long id;
    private Long user_timetable_id;
    private Long lecture_id;
    @ApiModelProperty(hidden = true)
    private boolean is_deleted;
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

    public Long getUser_timetable_id() {
        return user_timetable_id;
    }

    public void setUser_timetable_id(Long user_timetable_id) {
        this.user_timetable_id = user_timetable_id;
    }

    public Long getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(Long lecture_id) {
        this.lecture_id = lecture_id;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
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
