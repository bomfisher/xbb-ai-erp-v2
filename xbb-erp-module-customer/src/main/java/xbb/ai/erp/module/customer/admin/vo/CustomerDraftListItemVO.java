package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

@Data
public class CustomerDraftListItemVO {
    private String draftCode;
    private String draftTitle;
    private String customerName;
    private String customerCode;
    private Long updatedTime;
}
