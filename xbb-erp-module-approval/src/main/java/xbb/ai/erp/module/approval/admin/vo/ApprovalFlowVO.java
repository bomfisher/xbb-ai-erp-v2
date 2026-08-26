package xbb.ai.erp.module.approval.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowStatus;

import java.util.List;

@Data
public class ApprovalFlowVO {
    private Long id;
    private String businessCode;
    private ApprovalScene approvalScene;
    private String flowCode;
    private String flowName;
    private Integer version;
    private Integer priority;
    private ApprovalFlowStatus status;
    private String scopeJson;
    private List<ApprovalFlowNodeVO> nodes;
}
