package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

@Component
public class Assignment {

    @Range(groups = {ValidationGroups.createReview.class}, min = 1, max = 4, message = "강의 정보 id는 1~4 사이의 숫자입니다.")
    Long id;
    @ApiModelProperty(hidden = true)
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
