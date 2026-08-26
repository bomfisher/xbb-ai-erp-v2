package xbb.ai.erp.module.approval.application.service;

import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowCatalogDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowIdDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowListDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowSaveDTO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowCatalogVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowVO;

import java.util.List;

public interface ApprovalFlowService {

    List<ApprovalFlowCatalogVO> catalog(ApprovalFlowCatalogDTO dto);

    List<ApprovalFlowVO> list(ApprovalFlowListDTO dto);

    ApprovalFlowVO detail(ApprovalFlowDetailDTO dto);

    ApprovalFlowVO saveDraft(ApprovalFlowSaveDTO dto);

    ApprovalFlowVO publish(ApprovalFlowIdDTO dto);

    ApprovalFlowVO createNextVersion(ApprovalFlowIdDTO dto);

    ApprovalFlowVO disable(ApprovalFlowIdDTO dto);

    void remove(ApprovalFlowIdDTO dto);
}
