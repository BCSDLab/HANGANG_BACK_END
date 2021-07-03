package in.hangang.enums;

public enum ContentType {
    REVIEW("강의후기 게시판", 1),
    LECTURE_BANK("강의자료 게시판", 2),
    LECTURE_BANK_COMMENT("강의자료 게시판 댓글", 3);

    private String name;
    private Integer typeId;

    ContentType(String name, Integer typeId) {
        this.name = name;
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
