package in.hangang.domain;

import javax.validation.constraints.*;

public class GrantAdmin {
    @Email(message = "email 형식이 아닙니다.")
    @NotNull(message = "portal 계정은 필수입니다.")
    private String portalAccount;
    @NotNull(message = "flag는 필수 값입니다.")
    @Min(value = 1 , message = "1부터 3까지의 값을 넣을 수 있습니다.")
    @Max(value = 3 , message = "1부터 3까지의 값을 넣을 수 있습니다.")
    private Integer flag;

    public String getPortalAccount() {
        return portalAccount;
    }

    public void setPortalAccount(String portalAccount) {
        this.portalAccount = portalAccount;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
