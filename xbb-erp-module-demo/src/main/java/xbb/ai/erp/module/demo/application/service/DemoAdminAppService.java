package xbb.ai.erp.module.demo.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.admin.dto.DemoBusinessSelectQueryDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoBusinessSelectOptionVO;

import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;

import java.util.List;

public interface DemoAdminAppService {
    ListBaseVO<DemoListItemVO> list(ListBaseDTO dto);

    SaveItemVO<DemoSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<DemoSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(DemoDraftSaveDTO dto);

    BaseVO saveAndSubmit(DemoSubmitSaveDTO dto);

    List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto);

    DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto);

    Long save(DemoSaveDTO dto);

    DemoDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);

    List<DemoBusinessSelectOptionVO> businessSelectQuickSearch(DemoBusinessSelectQueryDTO dto);

    ListBaseVO<DemoBusinessSelectOptionVO> businessSelectDialogSearch(DemoBusinessSelectQueryDTO dto);

    DemoBusinessSelectOptionVO businessSelectGetById(DemoBusinessSelectQueryDTO dto);
}
