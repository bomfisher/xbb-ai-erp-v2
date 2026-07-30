package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

import java.util.List;

@Data
public class CustomerBankAccountQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private List<Long> customerIds;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String bizStatus;
    private Integer defaultFlag;
}
