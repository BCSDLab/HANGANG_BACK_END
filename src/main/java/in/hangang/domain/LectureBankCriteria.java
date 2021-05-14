package in.hangang.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class LectureBankCriteria {

    @Min(value = 1, groups = ValidationGroups.Search.class, message = "최소값은 1입니다")
    @ApiParam(required = false, defaultValue = "1")
    private Integer page = 1;
    @Min(value = 10,groups = ValidationGroups.Search.class, message = "최소값은 10입니다 ")
    @ApiParam(required = false, defaultValue = "10")
    private Integer limit = 10;
    @Pattern(regexp = "^[a-z]{2,4}$"  ,groups = ValidationGroups.Search.class, message = "영문 소문자 값만 가능합니다, 2~4글자")
    @ApiParam(required = false, defaultValue = "id")
    private String order = "id";
    @ApiParam(required = false, defaultValue = "")
    private ArrayList<
            @Pattern(regexp = "^[가-힣]{4}$"  ,groups = ValidationGroups.Search.class, message = "한글로 4글자만 가능합니다.")
            @NotNull(groups = ValidationGroups.Search.class, message = "카테고리는 빈 값일 수 없습니다.") String> category = new ArrayList<>();


    @ApiParam(required = false, defaultValue = "")
    private @Pattern( regexp = "^[a-zA-Z가-힣0-9]$",groups = ValidationGroups.Search.class , message = "특수문자와 초성은 사용불가능합니다") String keyword;
    @ApiParam(required = false, defaultValue = "")
    private @Pattern(regexp = "^[가-힣]$"  ,groups = ValidationGroups.Search.class, message = "한글로만 가능합니다.") String department;


    @ApiModelProperty(hidden = true)
    private Integer cursor;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit > 50 ? 50 : limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page < 1 ? 1 : page;
    }


    public Integer getCursor() {
        return (page - 1) * limit;
    }

    public String getOrder() {
        return order;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }
}
