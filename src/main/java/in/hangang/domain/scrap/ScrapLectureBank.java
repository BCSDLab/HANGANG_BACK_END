package in.hangang.domain.scrap;

import in.hangang.domain.LectureBank;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ScrapLectureBank {

    private Long scrap_id;
    private Timestamp scraped_at;

    private Long id;
    private Long user_id;
    private Long lecture_id;
    private ArrayList<String> category;
    private String title;
    private String content;
    private Integer point_price;
    private String semester_date;
    private Integer available;
    private Long hits;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Boolean is_deleted = false;


    public Long getScrap_id() {
        return scrap_id;
    }

    public void setScrap_id(Long scrap_id) {
        this.scrap_id = scrap_id;
    }

    public Timestamp getScraped_at() {
        return scraped_at;
    }

    public void setScraped_at(Timestamp scraped_at) {
        this.scraped_at = scraped_at;
    }


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

    public Long getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(Long lecture_id) {
        this.lecture_id = lecture_id;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
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

    public Integer getPoint_price() {
        return point_price;
    }

    public void setPoint_price(Integer point_price) {
        this.point_price = point_price;
    }

    public String getSemester_date() {
        return semester_date;
    }

    public void setSemester_date(String semester_date) {
        this.semester_date = semester_date;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
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

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

}
