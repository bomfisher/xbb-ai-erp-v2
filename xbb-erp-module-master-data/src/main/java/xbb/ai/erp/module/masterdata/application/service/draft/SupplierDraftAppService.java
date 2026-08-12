package xbb.ai.erp.module.masterdata.application.service.draft;

import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface SupplierDraftAppService {
    DraftSaveVO saveDraft(SupplierDraftSaveDTO dto);

    List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto);

    SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto);
}
