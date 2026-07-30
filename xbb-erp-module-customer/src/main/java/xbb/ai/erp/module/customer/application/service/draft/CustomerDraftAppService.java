package xbb.ai.erp.module.customer.application.service.draft;

import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftSaveVO;

import java.util.List;

public interface CustomerDraftAppService {
    CustomerDraftSaveVO saveDraft(CustomerDraftSaveDTO dto);

    List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto);

    CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto);
}
