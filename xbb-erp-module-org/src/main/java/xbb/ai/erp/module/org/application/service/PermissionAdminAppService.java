package xbb.ai.erp.module.org.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;

public interface PermissionAdminAppService {

    BaseVO list(BaseDTO dto);

    BaseVO detail(IdBaseDTO dto);
}
