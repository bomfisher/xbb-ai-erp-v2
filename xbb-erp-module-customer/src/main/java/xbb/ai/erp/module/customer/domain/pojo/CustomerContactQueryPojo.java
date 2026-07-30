package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

import java.util.List;

@Data
public class CustomerContactQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private List<Long> customerIds;
    private String contactName;
    private String mobile;
    private String bizStatus;
    private Integer defaultFlag;
}
