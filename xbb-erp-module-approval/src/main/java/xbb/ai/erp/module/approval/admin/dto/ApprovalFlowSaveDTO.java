package xbb.ai.erp.module.approval.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.domain.model.ApprovalApproverType;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeMode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowSaveDTO extends BaseDTO {
    private Long id;
    private String businessCode;
    private ApprovalScene approvalScene;
    private String flowCode;
    private String flowName;
    private Integer priority = 100;
    private String scopeJson = "{}";
    private List<NodeDTO> nodes = new ArrayList<>();

    @Data
    public static class NodeDTO {
        private Integer nodeNo;
        private String nodeName;
        private ApprovalNodeMode approvalMode;
        private List<ApproverDTO> approvers = new ArrayList<>();
    }

    @Data
    public static class ApproverDTO {
        private ApprovalApproverType approverType;
        private String approverValue;
        private Integer superiorLevel;
    }
}
