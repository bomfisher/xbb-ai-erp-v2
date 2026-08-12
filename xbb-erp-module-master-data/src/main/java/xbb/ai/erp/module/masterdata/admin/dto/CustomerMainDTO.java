package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;

@Data
public class CustomerMainDTO {
    private Long id;
    private String corpid;
    private String customerCode;
    private String customerName;
    private Long defaultContactId;
    private String address;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
