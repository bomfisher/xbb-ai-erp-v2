package xbb.ai.erp.module.org.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.DepartmentSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.DepartmentSelectOptionVO;
import xbb.ai.erp.module.org.application.service.DepartmentSelectAppService;
import xbb.ai.erp.module.org.domain.enums.EnableStatusEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;

import java.util.List;

@Service
public class DepartmentSelectAppServiceImpl implements DepartmentSelectAppService {

    private final DepartmentRepositoryImpl departmentRepository;

    public DepartmentSelectAppServiceImpl(DepartmentRepositoryImpl departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentSelectOptionVO> quickSearch(DepartmentSelectQueryDTO dto) {
        return listEnabledDepartments(dto).stream().map(this::toOption).toList();
    }

    @Override
    public ListBaseVO<DepartmentSelectOptionVO> dialogSearch(DepartmentSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<DepartmentSelectOptionVO> all = listEnabledDepartments(dto).stream().map(this::toOption).toList();
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<DepartmentSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    @Override
    public DepartmentSelectOptionVO getById(DepartmentSelectQueryDTO dto) {
        validateCorpid(dto);
        if (dto.getId() == null) {
            throw new BizException("id不能为空");
        }
        Department department = departmentRepository.findById(dto.getCorpid(), dto.getId());
        if (department == null || !EnableStatusEnum.ENABLE.getCode().equals(department.getDepartmentStatus())) {
            return null;
        }
        return toOption(department);
    }

    private List<Department> listEnabledDepartments(DepartmentSelectQueryDTO dto) {
        validateCorpid(dto);
        String keyword = dto.getKeyword();
        return departmentRepository.list(dto.getCorpid(), EnableStatusEnum.ENABLE.getCode()).stream()
            .filter(department -> matchesKeyword(department, keyword))
            .toList();
    }

    private void validateCorpid(DepartmentSelectQueryDTO dto) {
        if (dto == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
    }

    private boolean matchesKeyword(Department department, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String normalizedKeyword = keyword.trim().toLowerCase();
        return containsIgnoreCase(department.getDepartmentCode(), normalizedKeyword)
            || containsIgnoreCase(department.getDepartmentName(), normalizedKeyword);
    }

    private boolean containsIgnoreCase(String source, String normalizedKeyword) {
        return source != null && source.toLowerCase().contains(normalizedKeyword);
    }

    private DepartmentSelectOptionVO toOption(Department department) {
        DepartmentSelectOptionVO option = new DepartmentSelectOptionVO();
        option.setId(department.getId());
        option.setCode(department.getDepartmentCode());
        option.setName(department.getDepartmentName());
        option.setLabel(buildLabel(department));
        return option;
    }

    private String buildLabel(Department department) {
        if (department.getDepartmentCode() == null || department.getDepartmentCode().isBlank()) {
            return department.getDepartmentName();
        }
        if (department.getDepartmentName() == null || department.getDepartmentName().isBlank()) {
            return department.getDepartmentCode();
        }
        return department.getDepartmentCode() + " " + department.getDepartmentName();
    }
}
