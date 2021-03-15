package in.hangang.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class LectureBankCriteria {
    @ApiParam(required = false, defaultValue = "1")
    private Integer page = 1;
    @ApiParam(required = false, defaultValue = "10")
    private Integer limit = 10;
    @ApiParam(required = false, defaultValue = "id")
    private String order = "id";
    @ApiParam(required = false, defaultValue = "")
    private ArrayList<String> category = new ArrayList<>();
    @ApiParam(required = false, defaultValue = "")
    private String keyword = "";
    @ApiParam(required = false, defaultValue = "")
    private String department = "";

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
