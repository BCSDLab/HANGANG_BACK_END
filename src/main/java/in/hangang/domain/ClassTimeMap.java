package in.hangang.domain;

import java.util.ArrayList;

public class ClassTimeMap {
    private String name;
    private String classTime;
    private ArrayList<Integer> classTimeList;

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

    public ArrayList<Integer> getClassTimeList() {
        return classTimeList;
    }

    public void setClassTimeList(ArrayList<Integer> classTimeList) {
        this.classTimeList = classTimeList;
    }
}
