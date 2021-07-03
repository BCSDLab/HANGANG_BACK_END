package in.hangang.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Report {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    Integer board_type_id;
    @NotNull(message = "신고할 컨텐츠의 id는 비워둘 수 없습니다.")
    Long content_id;
    @Min(value = 1 , message = "1 이상의 값입니다.")
    @Max(value = 5, message = "5 이하의 값입니다.")
    @NotNull(message = "신고 내용은 비워둘 수 없습니다.")
    Long report_id;
    @ApiModelProperty(hidden = true)
    Long user_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBoard_type_id() {
        return board_type_id;
    }

    public void setBoard_type_id(Integer board_type_id) {
        this.board_type_id = board_type_id;
    }

    public Long getContent_id() {
        return content_id;
    }

    public void setContent_id(Long content_id) {
        this.content_id = content_id;
    }

    public Long getReport_id() {
        return report_id;
    }

    public void setReport_id(Long report_id) {
        this.report_id = report_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
