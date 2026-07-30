package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAddressQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private List<Long> customerIds;
    private String addressType;
    private String receiverName;
    private String receiverMobile;
    private String bizStatus;
    private Integer defaultFlag;
}
