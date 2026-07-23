package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

@Data
public class CustomerListItemVO {
    private Long id;
    private String customerCode;
    private String customerName;
    private String customerShortName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String defaultContactName;
    private String defaultContactMobile;
    private String defaultAddressSummary;
    private String defaultInvoiceTitle;
    private String bizStatus;
    private String refStatus;
    private Long addTime;
    private Long updateTime;
}
