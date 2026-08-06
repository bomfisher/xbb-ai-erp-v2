package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;

@Data
public class SupplierContactItemDTO {
    private Long id;
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String positionName;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private Integer version;
}
