package in.hangang.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class Lecture {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long user_id;
    @ApiModelProperty(hidden = true)
    private Boolean is_scraped;
    private ArrayList<String> semester_data;
    private ArrayList<HashTag> top3_hash_tag;
    private String code;
    private String name;
    private String department;
    private String professor;
    private String classification;
    private Float total_rating;
    private Timestamp last_reviewed_at;
    private Long review_count;
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

    public ArrayList<String> getSemester_data() {
        return semester_data;
    }

    public void setSemester_data(ArrayList<String> semester_data) {
        this.semester_data = semester_data;
    }

    public ArrayList<HashTag> getTop3_hash_tag() {
        return top3_hash_tag;
    }

    public void setTop3_hash_tag(ArrayList<HashTag> top3_hash_tag) {
        this.top3_hash_tag = top3_hash_tag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Float getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(Float total_rating) {
        this.total_rating = total_rating;
    }

    public Timestamp getLast_reviewed_at() {
        return last_reviewed_at;
    }

    public Long getReview_count() {
        return review_count;
    }

    public void setReview_count(Long review_count) {
        this.review_count = review_count;
    }

    public void setLast_reviewed_at(Timestamp last_reviewed_at) {
        this.last_reviewed_at = last_reviewed_at;
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

    public Boolean getIs_scraped() {
        return is_scraped;
    }

    public void setIs_scraped(Boolean is_scraped) {
        this.is_scraped = is_scraped;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
