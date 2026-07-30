package xbb.ai.erp.module.customer.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;

public interface CustomerSaveAppService {
    Long save(CustomerSaveDTO dto);

    BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto);
}
