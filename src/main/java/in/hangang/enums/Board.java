package in.hangang.enums;

public enum Board {
    LECTURE_BANK("강의 자료 게시글", 1),
    LECTURE_BANK_COMMENT("강의 자료 댓글", 2),
    REVIEW("강의 후기 게시글", 3);

    private String name;
    private int id;

    Board(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
