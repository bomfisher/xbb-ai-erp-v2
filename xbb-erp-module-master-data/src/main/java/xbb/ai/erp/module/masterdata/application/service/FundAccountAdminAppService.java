package xbb.ai.erp.module.masterdata.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftDetailVO;

import java.util.List;

public interface FundAccountAdminAppService {
    ListBaseVO<FundAccountListItemVO> list(ListBaseDTO dto);

    SaveItemVO<FundAccountSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<FundAccountSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(FundAccountDraftSaveDTO dto);

    BaseVO saveAndSubmit(FundAccountSubmitSaveDTO dto);

    List<FundAccountDraftListItemVO> draftList(FundAccountDraftListDTO dto);

    FundAccountDraftDetailVO loadDraft(FundAccountDraftLoadDTO dto);

    List<FundAccountBusinessSelectOptionVO> businessSelectQuickSearch(FundAccountBusinessSelectQueryDTO dto);

    ListBaseVO<FundAccountBusinessSelectOptionVO> businessSelectDialogSearch(FundAccountBusinessSelectQueryDTO dto);

    FundAccountBusinessSelectOptionVO businessSelectGetById(FundAccountBusinessSelectQueryDTO dto);

    Long save(FundAccountSaveDTO dto);

    FundAccountDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
