package xbb.ai.erp.module.org.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.vo.RoleListItemVO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;

public interface RoleAdminAppService {

    ListBaseVO<RoleListItemVO> list(BaseDTO dto);

    BaseVO detail(IdBaseDTO dto);

    BaseVO save(BaseDTO dto);

    BaseVO enable(IdBaseDTO dto);

    BaseVO disable(IdBaseDTO dto);

    OrgAdminAssembler.RolePermissionDetailResultVO permissionDetail(IdBaseDTO dto);

    BaseVO savePermission(BaseDTO dto);
}
