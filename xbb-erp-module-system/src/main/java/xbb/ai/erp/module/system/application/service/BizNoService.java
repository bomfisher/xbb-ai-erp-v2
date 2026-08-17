package xbb.ai.erp.module.system.application.service;

import xbb.ai.erp.module.system.admin.dto.BizNoNextDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BizNoNextVO;
import xbb.ai.erp.module.system.admin.vo.BizNoRuleVO;

public interface BizNoService {

    BizNoRuleVO getRule(String corpid, String businessCode);

    void saveRule(BizNoRuleSaveDTO dto);

    BizNoNextVO next(BizNoNextDTO dto);
}
