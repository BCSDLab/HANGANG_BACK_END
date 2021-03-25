package in.hangang.domain;

import java.util.ArrayList;

public class TimeTableMap {
    private Long id;
    private String tableName;
    private String tableSemesterDate;
    private ArrayList<LectureTimeTable> lectureList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSemesterDate() {
        return tableSemesterDate;
    }

    public void setTableSemesterDate(String tableSemesterDate) {
        this.tableSemesterDate = tableSemesterDate;
    }

    public ArrayList<LectureTimeTable> getLectureList() {
        return lectureList;
    }

    public void setLectureList(ArrayList<LectureTimeTable> lectureList) {
        this.lectureList = lectureList;
    }
}
