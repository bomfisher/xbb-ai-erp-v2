package xbb.ai.erp.module.approval.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeMode;

import java.util.List;

@Data
public class ApprovalFlowNodeVO {
    private Integer nodeNo;
    private String nodeName;
    private ApprovalNodeMode approvalMode;
    private List<ApprovalFlowApproverVO> approvers;
}
