package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;

@Data
public class SupplierListItemVO {
    private Long id;
    private String supplierCode;
    private String supplierName;
    private String mobile;
    private String address;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
