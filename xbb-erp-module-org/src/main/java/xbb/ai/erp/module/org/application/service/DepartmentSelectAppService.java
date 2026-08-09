package xbb.ai.erp.module.org.application.service;

import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.DepartmentSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.DepartmentSelectOptionVO;

import java.util.List;

public interface DepartmentSelectAppService {

    List<DepartmentSelectOptionVO> quickSearch(DepartmentSelectQueryDTO dto);

    ListBaseVO<DepartmentSelectOptionVO> dialogSearch(DepartmentSelectQueryDTO dto);

    DepartmentSelectOptionVO getById(DepartmentSelectQueryDTO dto);
}
