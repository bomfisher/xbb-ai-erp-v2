package xbb.ai.erp.module.org.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.org.admin.dto.PermissionListDTO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
import xbb.ai.erp.module.org.application.service.PermissionAdminAppService;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.PermissionRepositoryImpl;

@Service
public class PermissionAdminAppServiceImpl implements PermissionAdminAppService {

    private final PermissionRepositoryImpl permissionRepository;

    public PermissionAdminAppServiceImpl(PermissionRepositoryImpl permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public BaseVO list(BaseDTO dto) {
        if (dto instanceof PermissionListDTO listDTO) {
            return OrgAdminAssembler.toPermissionListResultVO(permissionRepository.list(
                listDTO.getKeyword(),
                listDTO.getPermissionType(),
                listDTO.getPermissionStatus()
            ));
        }
        return OrgAdminAssembler.toPermissionListResultVO(permissionRepository.list());
    }

    @Override
    public BaseVO detail(IdBaseDTO dto) {
        return new BaseVO();
    }

    public static PermissionAdminAppServiceImpl forTesting(PermissionRepositoryImpl permissionRepository) {
        return new PermissionAdminAppServiceImpl(permissionRepository);
    }
}
