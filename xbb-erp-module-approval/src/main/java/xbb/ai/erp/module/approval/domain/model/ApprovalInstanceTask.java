package xbb.ai.erp.module.approval.domain.model;

import lombok.Data;

@Data
public class ApprovalInstanceTask {
    private Long id;
    private String instanceId;
    private Integer nodeNo;
    private String assigneeId;
    private String status;
    private String comment;
    private Long handledAt;
}
