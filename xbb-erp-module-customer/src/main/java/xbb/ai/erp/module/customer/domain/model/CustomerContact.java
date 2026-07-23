package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class CustomerContact {
    private Long id;
    private String corpid;
    private Long customerId;
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String positionName;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
