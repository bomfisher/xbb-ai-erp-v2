package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;

@Data
public class SupplierMainDTO {
    private Long id;
    private String corpid;
    private String supplierCode;
    private String supplierName;
    private Long defaultContactId;
    private String mobile;
    private String address;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
