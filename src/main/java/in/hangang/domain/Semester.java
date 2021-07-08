package in.hangang.domain;

import java.sql.Timestamp;

public class Semester {
    private Long id;
    private String semester;
    private Timestamp start_time;
    private Long is_regular;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Long getIs_regular() {
        return is_regular;
    }

    public void setIs_regular(Long is_regular) {
        this.is_regular = is_regular;
    }
}
