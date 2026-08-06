package xbb.ai.erp.module.org.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.MemberSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.MemberSelectOptionVO;
import xbb.ai.erp.module.org.application.service.impl.MemberSelectAppServiceImpl;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberSelectAppServiceImplTest {

    @Test
    void should_query_active_enabled_members_and_support_get_by_id() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        when(employeeRepository.list("corp-1", "张", EmploymentStatusEnum.ACTIVE.getCode(), 1, null)).thenReturn(List.of(
            buildEmployee("EMP-1001", "E001", "张三")
        ));
        when(employeeRepository.findByUserId("corp-1", "EMP-1001")).thenReturn(buildEmployee("EMP-1001", "E001", "张三"));

        MemberSelectAppServiceImpl service = new MemberSelectAppServiceImpl(employeeRepository);
        MemberSelectQueryDTO dto = new MemberSelectQueryDTO();
        dto.setCorpid("corp-1");
        dto.setKeyword("张");
        dto.setPageNum(1);
        dto.setPageSize(20);

        List<MemberSelectOptionVO> quickSearch = service.quickSearch(dto);
        ListBaseVO<MemberSelectOptionVO> dialogSearch = service.dialogSearch(dto);
        MemberSelectQueryDTO byIdDTO = new MemberSelectQueryDTO();
        byIdDTO.setCorpid("corp-1");
        byIdDTO.setId("EMP-1001");
        MemberSelectOptionVO byId = service.getById(byIdDTO);

        assertEquals(1, quickSearch.size());
        assertEquals("EMP-1001", quickSearch.get(0).getId());
        assertEquals("E001 张三", quickSearch.get(0).getLabel());
        assertEquals(1, dialogSearch.getList().size());
        assertEquals("张三", byId.getName());
        verify(employeeRepository, times(2)).list("corp-1", "张", EmploymentStatusEnum.ACTIVE.getCode(), 1, null);
        verify(employeeRepository).findByUserId("corp-1", "EMP-1001");
    }

    private Employee buildEmployee(String userId, String userCode, String userName) {
        Employee employee = new Employee();
        employee.setUserId(userId);
        employee.setUserCode(userCode);
        employee.setUserName(userName);
        return employee;
    }
}
