package xbb.ai.erp.module.masterdata.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftDetailVO;

import java.util.List;

public interface SupplierAdminAppService {
    ListBaseVO<SupplierListItemVO> list(ListBaseDTO dto);

    SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(SupplierDraftSaveDTO dto);

    BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto);

    List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto);

    SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto);

    Long save(SupplierSaveDTO dto);

    SupplierDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
