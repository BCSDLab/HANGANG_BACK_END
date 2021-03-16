package in.hangang.enums;

import org.springframework.http.HttpStatus;

public enum Point {
    SIGN_UP("유저 회원가입", 20,1),
    LECTURE_PURCHASE("강의자료 업로드", 2),
    LECTURE_REVIEW("강의평 작성", 20,3),
    LECTURE_UPLOAD("강의자료 업로드", 50,4);


    private String name;
    private Integer point;
    private Integer typeId;
    Point(String name, Integer point, Integer typeId) {
        this.name = name;
        this.point = point;
        this.typeId = typeId;
    }

    Point(String name, Integer typeId) {
        this.name = name;
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
