package in.hangang.domain;

import java.util.ArrayList;

public class ClassTimeMap {
    private Long id;
    private String name;
    private String classTime;
    private String classNumber;
    private String professor;
    private ArrayList<Long> selectedTableId;
    private String target;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public ArrayList<Long> getSelectedTableId() {
        return selectedTableId;
    }

    public void setSelectedTableId(ArrayList<Long> selectedTableId) {
        this.selectedTableId = selectedTableId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
