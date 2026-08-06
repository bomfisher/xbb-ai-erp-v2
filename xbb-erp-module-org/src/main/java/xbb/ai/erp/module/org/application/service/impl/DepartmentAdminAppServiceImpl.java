package xbb.ai.erp.module.org.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.org.admin.dto.DepartmentTreeDTO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
import xbb.ai.erp.module.org.application.service.DepartmentAdminAppService;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;

@Service
public class DepartmentAdminAppServiceImpl implements DepartmentAdminAppService {

    private final DepartmentRepositoryImpl departmentRepository;
    private final EmployeeRepositoryImpl employeeRepository;

    public DepartmentAdminAppServiceImpl(
        DepartmentRepositoryImpl departmentRepository,
        EmployeeRepositoryImpl employeeRepository
    ) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public OrgAdminAssembler.DepartmentTreeResultVO tree(BaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (!(dto instanceof DepartmentTreeDTO treeDTO)) {
            throw new BizException("部门树查询参数错误");
        }
        return OrgAdminAssembler.toDepartmentTreeResultVO(departmentRepository.list(dto.getCorpid(), treeDTO.getDepartmentStatus()));
    }

    @Override
    public BaseVO detail(IdBaseDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO save(BaseDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO enable(IdBaseDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO disable(IdBaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (dto.getId() == null) {
            throw new BizException("部门id不能为空");
        }
        if (departmentRepository.findById(dto.getCorpid(), dto.getId()) == null) {
            throw new BizException("部门不存在");
        }
        if (employeeRepository.countActiveByMainDepartment(dto.getCorpid(), dto.getId()) > 0) {
            throw new BizException("部门下存在在职主部门员工，不能停用");
        }
        return new BaseVO();
    }

    @Override
    public BaseVO move(BaseDTO dto) {
        return new BaseVO();
    }

    public static DepartmentAdminAppServiceImpl forTesting(
        DepartmentRepositoryImpl departmentRepository,
        EmployeeRepositoryImpl employeeRepository
    ) {
        return new DepartmentAdminAppServiceImpl(departmentRepository, employeeRepository);
    }
}
