package xbb.ai.erp.base.common.pojo;

import lombok.Data;

@Data
public class ListButtonItemPojo {
    private String buttonCode;
    private String buttonName;
    private Integer sort;
    private String actionCode;

    public ListButtonItemPojo(String buttonCode, String buttonName, int sort, String actionCode) {
        this.buttonCode = buttonCode;
        this.buttonName = buttonName;
        this.sort = sort;
        this.actionCode = actionCode;
    }

    public ListButtonItemPojo() {
    }
}
