package in.hangang.domain;

import org.springframework.stereotype.Component;

@Component
public class Hash_tag {
    Long id;
    String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
