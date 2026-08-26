package xbb.ai.erp.module.approval.admin.vo;

import lombok.Data;

/**
 * 审批中心列表项。
 */
@Data
public class ApprovalInstanceListItemVO {

    private String instanceId;

    private String businessCode;

    private String businessName;

    private String subjectId;

    private String subjectSummary;

    private String submitterName;

    private String status;

    private String currentNodeName;

    private String submittedAt;

    private String completedAt;
}
