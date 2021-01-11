package in.hangang.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lecture {
    private Long id;
    private String semester_date;
    private String name;
    private String department;
    private String professor;
    private String classification;
    private String total_rating;
    private Boolean is_deleted;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Long getId() {
        return id;
    }

    public String getSemester_date() {
        return semester_date;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getClassification() {
        return classification;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
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

    public void setSemester_date(String semester_date) {
        this.semester_date = semester_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
