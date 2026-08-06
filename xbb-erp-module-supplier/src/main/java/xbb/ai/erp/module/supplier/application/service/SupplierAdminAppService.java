package xbb.ai.erp.module.supplier.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftSaveVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;

import java.util.List;

public interface SupplierAdminAppService {
    ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto);

    SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto);

    default SupplierDraftSaveVO saveDraft(SupplierDraftSaveDTO dto) {
        throw new UnsupportedOperationException();
    }

    default BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto) {
        throw new UnsupportedOperationException();
    }

    default List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto) {
        throw new UnsupportedOperationException();
    }

    default SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto) {
        throw new UnsupportedOperationException();
    }

    List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto);

    ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto);

    SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto);

    Long save(SupplierSaveDTO dto);

    SupplierDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
