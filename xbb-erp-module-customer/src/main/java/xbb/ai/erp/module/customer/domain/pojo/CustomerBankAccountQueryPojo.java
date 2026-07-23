package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerBankAccountQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String bizStatus;
    private Integer defaultFlag;
}
