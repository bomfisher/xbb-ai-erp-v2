package xbb.ai.erp.module.approval.application.service;

import xbb.ai.erp.module.approval.domain.model.ApprovalNodeApprover;

import java.util.List;

/**
 * 在提交时解析并冻结实际审批人。
 */
public interface ApprovalApproverResolver {

    List<String> resolve(String corpid, String submitterId, ApprovalNodeApprover rule);
}
