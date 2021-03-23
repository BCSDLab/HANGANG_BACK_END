package in.hangang.domain.criteria;

import java.util.ArrayList;

public class TimeTableCriteria extends Criteria {
    private String keyword;
    private ArrayList<String> classification;
    private Long semesterDateId;
    private String department;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
