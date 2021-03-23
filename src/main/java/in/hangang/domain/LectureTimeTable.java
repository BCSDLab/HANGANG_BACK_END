package in.hangang.domain;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;

public class LectureTimeTable {
    @ApiModelProperty(hidden = true)
    private Long id;
    @ApiModelProperty(hidden = true)
    private boolean is_custom;
    private Long user_timetable_id;
    @ApiModelProperty(hidden = true)
    private String semester_date;
    @ApiModelProperty(hidden = true)
    private String code;
    private String name;
    @ApiModelProperty(hidden = true)
    private String classification;
    @ApiModelProperty(hidden = true)
    private String grades;
    @ApiModelProperty(hidden = true)
    private String classNumber;
    @ApiModelProperty(hidden = true)
    private String regular_number;
    @ApiModelProperty(hidden = true)
    private String department;
    @ApiModelProperty(hidden = true)
    private String target;
    private String professor;
    @ApiModelProperty(hidden = true)
    private String is_english;
    @ApiModelProperty(hidden = true)
    private String design_score;
    @ApiModelProperty(hidden = true)
    private String is_elearning;
    private String class_time;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;
    private float rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIs_custom() {
        return is_custom;
    }

    public void setIs_custom(boolean is_custom) {
        this.is_custom = is_custom;
    }

    public Long getUser_timetable_id() {
        return user_timetable_id;
    }

    public void setUser_timetable_id(Long user_timetable_id) {
        this.user_timetable_id = user_timetable_id;
    }

    public String getSemester_date() {
        return semester_date;
    }

    public void setSemester_date(String semester_date) {
        this.semester_date = semester_date;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getRegular_number() {
        return regular_number;
    }

    public void setRegular_number(String regular_number) {
        this.regular_number = regular_number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getIs_english() {
        return is_english;
    }

    public void setIs_english(String is_english) {
        this.is_english = is_english;
    }

    public String getDesign_score() {
        return design_score;
    }

    public void setDesign_score(String design_score) {
        this.design_score = design_score;
    }

    public String getIs_elearning() {
        return is_elearning;
    }

    public void setIs_elearning(String is_elearning) {
        this.is_elearning = is_elearning;
    }

    public String getClass_time() {
        return class_time;
    }

    public void setClass_time(String class_time) {
        this.class_time = class_time;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
