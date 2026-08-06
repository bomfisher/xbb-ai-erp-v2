package xbb.ai.erp.module.org.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.MemberSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.MemberSelectOptionVO;
import xbb.ai.erp.module.org.application.service.MemberSelectAppService;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;

import java.util.List;

@Service
public class MemberSelectAppServiceImpl implements MemberSelectAppService {

    private final EmployeeRepositoryImpl employeeRepository;

    public MemberSelectAppServiceImpl(EmployeeRepositoryImpl employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<MemberSelectOptionVO> quickSearch(MemberSelectQueryDTO dto) {
        return listActiveEmployees(dto).stream().map(this::toOption).toList();
    }

    @Override
    public ListBaseVO<MemberSelectOptionVO> dialogSearch(MemberSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<MemberSelectOptionVO> all = listActiveEmployees(dto).stream().map(this::toOption).toList();
        int fromIndex = Math.min((pageNum - 1) * pageSize, all.size());
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        ListBaseVO<MemberSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(all.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, Math.max((all.size() + pageSize - 1) / pageSize, 1)));
        return vo;
    }

    @Override
    public MemberSelectOptionVO getById(MemberSelectQueryDTO dto) {
        validateCorpid(dto);
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new BizException("id不能为空");
        }
        Employee employee = employeeRepository.findByUserId(dto.getCorpid(), dto.getId());
        if (employee == null) {
            return null;
        }
        return toOption(employee);
    }

    private List<Employee> listActiveEmployees(MemberSelectQueryDTO dto) {
        validateCorpid(dto);
        return employeeRepository.list(dto.getCorpid(), dto.getKeyword(), EmploymentStatusEnum.ACTIVE.getCode(), 1, null);
    }

    private void validateCorpid(MemberSelectQueryDTO dto) {
        if (dto == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
    }

    private MemberSelectOptionVO toOption(Employee employee) {
        MemberSelectOptionVO option = new MemberSelectOptionVO();
        option.setId(employee.getUserId());
        option.setCode(employee.getUserCode());
        option.setName(employee.getUserName());
        option.setLabel(buildLabel(employee));
        return option;
    }

    private String buildLabel(Employee employee) {
        if (employee.getUserCode() == null || employee.getUserCode().isBlank()) {
            return employee.getUserName();
        }
        if (employee.getUserName() == null || employee.getUserName().isBlank()) {
            return employee.getUserCode();
        }
        return employee.getUserCode() + " " + employee.getUserName();
    }
}
