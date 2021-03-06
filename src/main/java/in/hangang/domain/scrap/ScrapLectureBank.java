package in.hangang.domain.scrap;

import in.hangang.annotation.ValidationGroups;
import in.hangang.domain.Lecture;
import in.hangang.domain.LectureBank;
import in.hangang.domain.User;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ScrapLectureBank {

    private Long scrap_id;
    private Timestamp scraped_at;
    private Long id;
    private String thumbnail;
    private Long user_id;
    private Long lecture_id;
    private ArrayList<String> category;
    private String title;
    private String content;
    private Integer point_price;
    private String semester_date;
    private Long hits;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Boolean is_deleted = false;


    @ApiModelProperty(hidden = true)
    private Boolean is_hit = false;
    @ApiModelProperty(hidden = true)
    private User user;
    @ApiModelProperty(hidden = true)
    private Lecture lecture;




    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

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

    public Boolean getIs_hit() {
        return is_hit;
    }

    public void setIs_hit(Boolean is_hit) {
        this.is_hit = is_hit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
