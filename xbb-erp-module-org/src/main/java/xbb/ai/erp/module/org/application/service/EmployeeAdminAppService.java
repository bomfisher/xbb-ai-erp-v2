package xbb.ai.erp.module.org.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.EmployeeBatchDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeIdDTO;
import xbb.ai.erp.module.org.admin.vo.EmployeeListItemVO;

public interface EmployeeAdminAppService {

    ListBaseVO<EmployeeListItemVO> list(BaseDTO dto);

    BaseVO save(BaseDTO dto);

    BaseVO detail(EmployeeIdDTO dto);

    BaseVO enable(EmployeeIdDTO dto);

    BaseVO disable(EmployeeIdDTO dto);

    BaseVO resign(EmployeeIdDTO dto);

    BaseVO delete(EmployeeBatchDTO dto);
}
