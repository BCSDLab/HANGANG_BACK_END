package in.hangang.enums;

public enum Report {
    SLANG("욕설/비하",1),
    COPYRIGHT("유출/사칭/저작권 위배",2),
    FORGE("허위/부적절한 정보",3),
    ADVERTISEMENT("광고/도배",4),
    OBSCENE("음란물",5);

    private String name;
    private Integer id;

    Report(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
