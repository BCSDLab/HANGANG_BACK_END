package in.hangang.domain;

import java.sql.Timestamp;

public class LectureTimeTable {
    private Long id;
    private String semester_date;
    private String code;
    private String name;
    private String classification;
    private String grades;
    private String classNumber;
    private String regular_number;
    private String department;
    private String target;
    private String professor;
    private String is_english;
    private String design_score;
    private String is_elearning;
    private String class_time;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
