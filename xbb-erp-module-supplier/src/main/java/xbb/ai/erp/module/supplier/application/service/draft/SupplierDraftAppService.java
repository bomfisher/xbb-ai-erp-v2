package xbb.ai.erp.module.supplier.application.service.draft;

import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftSaveVO;

import java.util.List;

public interface SupplierDraftAppService {
    SupplierDraftSaveVO saveDraft(SupplierDraftSaveDTO dto);

    List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto);

    SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto);
}
