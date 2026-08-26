package xbb.ai.erp.module.approval.application.service;

import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceActionDTO;

public interface ApprovalInstanceCommandService {
    void approve(ApprovalInstanceActionDTO dto);
    void reject(ApprovalInstanceActionDTO dto);
}
