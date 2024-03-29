package in.hangang.domain.criteria;


import java.util.ArrayList;

public class LectureCriteria extends Criteria{
    private String keyword;
    private ArrayList<String> classification;
    private ArrayList<Long> hashtag;
    private ArrayList<String> department;
    private ArrayList<Integer> departmentId;
    private String sort;

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

    public ArrayList<Long> getHashtag() {
        return hashtag;
    }

    public void setHashtag(ArrayList<Long> hashtag) {
        this.hashtag = hashtag;
    }

    public ArrayList<String> getDepartment() {
        return department;
    }

    public void setDepartment(ArrayList<String> department) {
        this.department = department;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public ArrayList<Integer> getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(ArrayList<Integer> departmentId) {
        this.departmentId = departmentId;
    }
}
