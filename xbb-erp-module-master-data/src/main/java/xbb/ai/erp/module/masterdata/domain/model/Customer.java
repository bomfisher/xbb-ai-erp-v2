package xbb.ai.erp.module.masterdata.domain.model;

import lombok.Data;

@Data
public class Customer {
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
