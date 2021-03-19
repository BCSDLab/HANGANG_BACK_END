package in.hangang.domain.criteria;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Component;

@Component
public class Criteria {
    @ApiParam(required = false, defaultValue = "1")
    private Integer page = 1;
    @ApiParam(required = false, defaultValue = "10")
    private Integer limit = 10;
    @ApiModelProperty(hidden = true)
    private Integer cursor;

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

    @Override
    public String toString() {
        return "Criteria{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
