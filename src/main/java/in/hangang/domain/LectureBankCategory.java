package in.hangang.domain;

public class LectureBankCategory {
    private Long id;
    private Long lecture_bank_id;
    private String category;

    public Long getId() {
        return id;
    }

    public Long getLecture_bank_id() {
        return lecture_bank_id;
    }

    public String getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLecture_bank_id(Long lecture_bank_id) {
        this.lecture_bank_id = lecture_bank_id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
