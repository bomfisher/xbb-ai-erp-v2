package xbb.ai.erp.module.approval.domain.repository;

import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowDefinition;

import java.util.List;

public interface ApprovalFlowRepository {

    ApprovalFlowDefinition findById(String corpid, Long id);

    List<ApprovalFlowDefinition> findByCondition(String corpid, String businessCode, ApprovalScene approvalScene);

    int findMaxVersion(String corpid, String businessCode, ApprovalScene approvalScene, String flowCode);

    void save(ApprovalFlowDefinition definition);

    void remove(String corpid, Long id, String userId);
}
