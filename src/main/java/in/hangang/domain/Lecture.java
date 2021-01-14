package in.hangang.domain;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;

@Component
public class Lecture {
    private Long id;
    private String semester_data;
    //private ArrayList<String> hash_tag_name;
    private ArrayList<Hash_tag> top3_hash_tag;
    private String name;
    private String department;
    private String professor;
    private String classification;
    private Float total_rating;
    private Boolean is_deleted;
    private Timestamp created_at;
    private Timestamp updated_at;


    public ArrayList<Hash_tag> getTop3_hash_tag() {
        return top3_hash_tag;
    }

    public void setTop3_hash_tag(ArrayList<Hash_tag> top3_hash_tag) {
        this.top3_hash_tag = top3_hash_tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemester_data() {
        return semester_data;
    }

    public void setSemester_data(String semester_data) {
        this.semester_data = semester_data;
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
