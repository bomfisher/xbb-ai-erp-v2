package xbb.ai.erp.module.org.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;

public interface DepartmentAdminAppService {

    OrgAdminAssembler.DepartmentTreeResultVO tree(BaseDTO dto);

    BaseVO detail(IdBaseDTO dto);

    BaseVO save(BaseDTO dto);

    BaseVO enable(IdBaseDTO dto);

    BaseVO disable(IdBaseDTO dto);

    BaseVO move(BaseDTO dto);
}
