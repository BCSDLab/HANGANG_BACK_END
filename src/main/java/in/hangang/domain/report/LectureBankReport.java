package in.hangang.domain.report;

import in.hangang.domain.UploadFile;

import java.sql.Timestamp;
import java.util.List;

public class LectureBankReport {

    private Long id;
    private String content_name;
    private String type;
    private Long user_id;
    private Long content_id;
    private Integer point_price;
    private String title;
    private String content;
    private Timestamp created_at;
    private Timestamp updated_at;
    private List<UploadFile> files;
    private Long board_type_id;

    public Long getBoard_type_id() {
        return board_type_id;
    }

    public void setBoard_type_id(Long board_type_id) {
        this.board_type_id = board_type_id;
    }

    public Integer getPoint_price() {
        return point_price;
    }

    public void setPoint_price(Integer point_price) {
        this.point_price = point_price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getContent_id() {
        return content_id;
    }

    public void setContent_id(Long content_id) {
        this.content_id = content_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<UploadFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadFile> files) {
        this.files = files;
    }
}
