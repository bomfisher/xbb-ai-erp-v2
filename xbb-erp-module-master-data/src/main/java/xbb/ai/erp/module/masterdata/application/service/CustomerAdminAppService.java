package xbb.ai.erp.module.masterdata.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerBusinessSelectOptionVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftDetailVO;

import java.util.List;

public interface CustomerAdminAppService {
    ListBaseVO<CustomerListItemVO> list(ListBaseDTO dto);

    SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(CustomerDraftSaveDTO dto);

    BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto);

    List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto);

    CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto);

    List<CustomerBusinessSelectOptionVO> businessSelectQuickSearch(CustomerBusinessSelectQueryDTO dto);

    ListBaseVO<CustomerBusinessSelectOptionVO> businessSelectDialogSearch(CustomerBusinessSelectQueryDTO dto);

    CustomerBusinessSelectOptionVO businessSelectGetById(CustomerBusinessSelectQueryDTO dto);

    Long save(CustomerSaveDTO dto);

    CustomerDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
