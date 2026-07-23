package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerQueryPojo {
    private String corpid;
    private Long id;
    private String customerCode;
    private String customerName;
    private String customerCategory;
    private String bizStatus;
    private String refStatus;
    private String ownerSalesId;
}
