package xbb.ai.erp.module.approval.application.service;

import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceListDTO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalInstanceDetailVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalInstanceListItemVO;

import java.util.List;

/**
 * 审批实例查询用例。
 */
public interface ApprovalInstanceService {

    List<ApprovalInstanceListItemVO> list(ApprovalInstanceListDTO dto);

    ApprovalInstanceDetailVO detail(ApprovalInstanceDetailDTO dto);

    boolean hasActiveInstancesByFlowDefinitionId(String corpid, Long flowDefinitionId);
}
