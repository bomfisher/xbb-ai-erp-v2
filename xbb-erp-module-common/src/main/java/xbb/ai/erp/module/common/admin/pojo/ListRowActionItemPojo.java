package xbb.ai.erp.module.common.admin.pojo;

import lombok.Data;

@Data
public class ListRowActionItemPojo {
    private String actionCode;
    private String actionName;
    private Integer sort;
    private String showMode;
    private String confirmType;
}
