package in.hangang.enums;

import org.springframework.http.HttpStatus;

public enum Point {
    SIGN_UP("유저 회원가입", 300,1),
    LECTURE_BANK("강의자료 구매",100, 2),
    LECTURE_REVIEW("강의평 작성", 30,3),
    LECTURE_UPLOAD("강의자료 업로드", 70,4);


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
