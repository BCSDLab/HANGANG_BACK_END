package in.hangang.domain.criteria;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;


public class TimeTableCriteria extends Criteria {
    private String keyword;
    private String criteria;
    private ArrayList<String> classification;
    private Long semesterDateId;
    private ArrayList<String> department;
    private ArrayList<Integer> departmentId;

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<String> getClassification() {
        return classification;
    }

    public void setClassification(ArrayList<String> classification) {
        this.classification = classification;
    }

    public Long getSemesterDateId() {
        return semesterDateId;
    }

    public void setSemesterDateId(Long semesterDateId) {
        this.semesterDateId = semesterDateId;
    }

    public ArrayList<String> getDepartment() {
        return department;
    }

    public void setDepartment(ArrayList<String> department) {
        this.department = department;
    }

    public ArrayList<Integer> getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(ArrayList<Integer> departmentId) {
        this.departmentId = departmentId;
    }
}
