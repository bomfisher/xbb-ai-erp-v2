package xbb.ai.erp.module.masterdata.domain.model;

import lombok.Data;

@Data
public class CustomerContact {
    private Long id;
    private String corpid;
    private Long customerId;
    private String name;
    private String mobile;
    private Integer defaultFlag;
    private String creatorId;
    private String modifyId;
}
