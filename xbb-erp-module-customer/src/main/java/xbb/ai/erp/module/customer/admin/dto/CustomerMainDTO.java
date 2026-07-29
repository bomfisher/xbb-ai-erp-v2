package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;

@Data
public class CustomerMainDTO {
    private Long id;
    private String customerCode;
    private String customerName;
    private String customerShortName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String ownerSalesNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private Long defaultBankAccountId;
    private Long defaultInvoiceProfileId;
    private String remark;
    private Integer version;
}
