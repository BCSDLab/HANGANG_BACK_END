package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;

@Component
public class UserTimetable {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long user_id;
    @NotNull(groups = {ValidationGroups.createUserTimetable.class}, message = "학기 정보를 비워둘 수 없습니다.")
    private Long semester_date_id;
    @NotNull(groups = {ValidationGroups.createUserTimetable.class}, message = "시간표 이름은 비워둘 수 없습니다.")
    @Length(groups = {ValidationGroups.createUserTimetable.class}, min=1, max =20, message = "시간표 이름은 1글자 이상 20글자 이하입니다.")
    private String name;
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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getSemester_date_id() {
        return semester_date_id;
    }

    public void setSemester_date_id(Long semester_date_id) {
        this.semester_date_id = semester_date_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
