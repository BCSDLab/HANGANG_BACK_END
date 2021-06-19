package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Component
public class Memo {
    @ApiModelProperty(hidden = true)
    private Long id;
    @NotNull(groups = {ValidationGroups.createMemo.class}, message = "timetable_id 값을 비워둘 수 없습니다.")
    private Long timetable_component;
    @NotNull(groups = {ValidationGroups.createMemo.class}, message = "메모는 비워둘 수 없습니다.")
    @Length(groups = {ValidationGroups.createMemo.class}, min = 1, max = 500, message = "메모는 1자 이상 500자 이내로 입력해야 합니다.")
    private String memo;
    @ApiModelProperty(hidden = true)
    private Boolean is_deleted;
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

    public Long getTimetable_component() {
        return timetable_component;
    }

    public void setTimetable_component(Long timetable_component) {
        this.timetable_component = timetable_component;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
