package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;

@Data
public class SupplierBankAccountItemDTO {
    private Long id;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String accountUsage;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private Integer version;
}
