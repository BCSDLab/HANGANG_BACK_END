package in.hangang.domain;

import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;

@Component
public class Review {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long lecture_id;
    @ApiModelProperty(hidden = true)
    private Long user_id;
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "별점 항목은 비워둘 수 없습니다.")
    private float rating;
    private Long likes;
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "과제량 항목은 비워둘 수 없습니다.")
    private Integer assignment_amount;
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "난이도 항목은 비워둘 수 없습니다.")
    private float difficulty;
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "성적 비율 항목은 비워둘 수 없습니다.")
    private float grade_portion;
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "출석 빈도 항목은 비워둘 수 없습니다.")
    private Integer attendance_frequency;
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "시험 횟수 항목은 비워둘 수 없습니다.")
    private Integer test_times;
    @Length(min=10, message = "강의평을 더 성의 있게 작성해주세")
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "별점은 비워둘 수 없습니다.")
    private String comment;
    @Size(min = 1, max = 3, message = "해시태그는 최소 1개 최대 3개까지 선택하실 수 있습니다.")
    @NotNull(groups = ValidationGroups.createReview.class)
    private ArrayList<Hash_tag> hash_tags;
    @Size(min = 1, max = 6, message = "과제 정보는 최소 1개 최대 6개까지 선택하실 수 있습니다.")
    @NotNull(groups = {ValidationGroups.createReview.class}, message = "과제 정보 항목은 비워둘 수 없습니다.")
    private ArrayList<Assignment> assignment;
    @ApiModelProperty(hidden = true)
    private Long return_id;
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

    public Long getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(Long lecture_id) {
        this.lecture_id = lecture_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Integer getAssignment_amount() {
        return assignment_amount;
    }

    public void setAssignment_amount(Integer assignment_amount) {
        this.assignment_amount = assignment_amount;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public float getGrade_portion() {
        return grade_portion;
    }

    public void setGrade_portion(float grade_portion) {
        this.grade_portion = grade_portion;
    }

    public Integer getAttendance_frequency() {
        return attendance_frequency;
    }

    public void setAttendance_frequency(Integer attendance_frequency) {
        this.attendance_frequency = attendance_frequency;
    }

    public ArrayList<Hash_tag> getHash_tags() {
        return hash_tags;
    }

    public void setHash_tags(ArrayList<Hash_tag> hash_tags) {
        this.hash_tags = hash_tags;
    }

    public ArrayList<Assignment> getAssignment() {
        return assignment;
    }

    public void setAssignment(ArrayList<Assignment> assignment) {
        this.assignment = assignment;
    }

    public Long getReturn_id() {
        return return_id;
    }

    public void setReturn_id(Long return_id) {
        this.return_id = return_id;
    }

    public Integer getTest_times() {
        return test_times;
    }

    public void setTest_times(Integer test_times) {
        this.test_times = test_times;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
