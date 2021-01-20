package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
public class HashTag {

    @Range(groups = {ValidationGroups.createReview.class}, min = 1, max = 9, message = "해시태그의 id는 1~9의 숫자입니다.")
    Long id;

    @ApiModelProperty(hidden = true)
    String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
