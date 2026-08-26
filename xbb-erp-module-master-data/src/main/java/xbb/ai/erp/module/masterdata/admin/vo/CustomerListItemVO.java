package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;

@Data
public class CustomerListItemVO {
    private Long id;
    private String customerCode;
    private String customerName;
    private String address;
    private String enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
