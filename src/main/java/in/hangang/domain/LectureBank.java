package in.hangang.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import in.hangang.annotation.ValidationGroups;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class LectureBank {

    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private Long user_id;

    @NotNull(groups = {ValidationGroups.PostLectureBank.class} , message = "강의번호 값은 필수값입니다.")
    @Min(value = 1, groups = {ValidationGroups.PostLectureBank.class}, message = "최소값은 1입니다")
    private Long lecture_id;

    @NotNull(groups = {ValidationGroups.PostLectureBank.class} ,message = "카테고리는 필수값입니다.")
    private ArrayList<
            @Pattern(regexp = "^[가-힣]{4}$", groups = {ValidationGroups.PostLectureBank.class} , message = "한글로 4글자만 가능합니다.")
            @NotNull(groups = {ValidationGroups.PostLectureBank.class} , message = "카테고리는 0개일 수 없습니다.") String> category;

    @NotNull(groups = {ValidationGroups.PostLectureBank.class} ,message = "제목 비울 수는 없습니다")
    @Length(min=1,  groups = {ValidationGroups.PostLectureBank.class}, message = "제목은 최소 한글자 이상입니다")
    private String title;
    @NotNull(groups = {ValidationGroups.PostLectureBank.class} ,message = "본문을 비울 수는 없습니다")
    @Length(min=1,  groups = {ValidationGroups.PostLectureBank.class}, message = "본문은 최소 한글자 이상입니다")
    private String content;


    @ApiModelProperty(hidden = true)
    private List<LectureBankComment> comments;
    @ApiModelProperty(hidden = true)
    private Integer point_price;

    @ApiModelProperty(hidden = true)
    private String semester_date;

    @NotNull(groups = {ValidationGroups.PostLectureBank.class} ,message = "파일을 업로드하지 않았습니다.")
    private List<@Length(min=10, max=255, groups = {ValidationGroups.PostLectureBank.class}, message = "URL의 길이는 10-255글자입니다")
    @NotNull(message = "파일은 null 일 수 없습니다") String> files;

    @NotNull(groups = {ValidationGroups.PostLectureBank.class} , message = "학기값은 필수값입니다.")
    @Min(value = 1, groups = {ValidationGroups.PostLectureBank.class}, message = "최소값은 1입니다")
    @Max(value = 5, groups = {ValidationGroups.PostLectureBank.class}, message = "최대값은 5입니다")
    private Long semester_id;


    @ApiModelProperty(hidden = true)
    private List<UploadFile> uploadFiles;

    @ApiModelProperty(hidden = true)
    private Long hits;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;

    @ApiModelProperty(hidden = true)
    private Boolean is_deleted = false;

    @ApiModelProperty(hidden = true)
    private Boolean is_hit = false;
    @ApiModelProperty(hidden = true)
    private Boolean is_scrap = false;
    @ApiModelProperty(hidden = true)
    private Boolean is_purchase = false;
    @ApiModelProperty(hidden = true)
    private String thumbnail_ext;
    @ApiModelProperty(hidden = true)
    private String thumbnail;
    @ApiModelProperty(hidden = true)
    private User user;
    @ApiModelProperty(hidden = true)
    private Lecture lecture;

    public Boolean getIs_scrap() {
        return is_scrap;
    }

    public void setIs_scrap(Boolean is_scrap) {
        this.is_scrap = is_scrap;
    }

    public Boolean getIs_purchase() {
        return is_purchase;
    }

    public void setIs_purchase(Boolean is_purchase) {
        this.is_purchase = is_purchase;
    }

    public List<LectureBankComment> getComments() {
        return comments;
    }

    public void setComments(List<LectureBankComment> comments) {
        this.comments = comments;
    }

    public List<UploadFile> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List<UploadFile> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Long getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(Long semester_id) {
        this.semester_id = semester_id;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }



    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getPoint_price() {
        return point_price;
    }

    public Long getHits() {
        return hits;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPoint_price(Integer point_price) {
        this.point_price = point_price;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public String getSemester_date() {
        return semester_date;
    }

    public void setSemester_date(String semester_date) {
        this.semester_date = semester_date;
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

    public String getThumbnail_ext() {
        return thumbnail_ext;
    }

    public void setThumbnail_ext(String thumbnail_ext) {
        this.thumbnail_ext = thumbnail_ext;
    }
}
