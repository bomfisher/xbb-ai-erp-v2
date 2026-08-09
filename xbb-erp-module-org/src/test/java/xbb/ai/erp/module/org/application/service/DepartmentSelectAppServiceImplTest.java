package xbb.ai.erp.module.org.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.org.admin.dto.DepartmentSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.DepartmentSelectOptionVO;
import xbb.ai.erp.module.org.application.service.impl.DepartmentSelectAppServiceImpl;
import xbb.ai.erp.module.org.domain.enums.EnableStatusEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DepartmentSelectAppServiceImplTest {

    @Test
    void should_search_enabled_department_by_code_or_name() {
        DepartmentRepositoryImpl repository = mock(DepartmentRepositoryImpl.class);
        when(repository.list("corp-1", EnableStatusEnum.ENABLE.getCode())).thenReturn(List.of(
            department(10L, "RD", "研发部", EnableStatusEnum.ENABLE.getCode()),
            department(11L, "FIN", "财务部", EnableStatusEnum.ENABLE.getCode())
        ));
        DepartmentSelectAppServiceImpl service = new DepartmentSelectAppServiceImpl(repository);
        DepartmentSelectQueryDTO dto = new DepartmentSelectQueryDTO();
        dto.setCorpid("corp-1");
        dto.setKeyword("研发");

        List<DepartmentSelectOptionVO> options = service.quickSearch(dto);

        assertEquals(1, options.size());
        assertEquals(10L, options.getFirst().getId());
        assertEquals("RD 研发部", options.getFirst().getLabel());
    }

    @Test
    void should_not_return_disabled_department_when_loading_by_id() {
        DepartmentRepositoryImpl repository = mock(DepartmentRepositoryImpl.class);
        when(repository.findById("corp-1", 10L)).thenReturn(
            department(10L, "RD", "研发部", EnableStatusEnum.DISABLE.getCode()));
        DepartmentSelectAppServiceImpl service = new DepartmentSelectAppServiceImpl(repository);
        DepartmentSelectQueryDTO dto = new DepartmentSelectQueryDTO();
        dto.setCorpid("corp-1");
        dto.setId(10L);

        assertNull(service.getById(dto));
    }

    private Department department(Long id, String code, String name, Integer status) {
        Department department = new Department();
        department.setId(id);
        department.setDepartmentCode(code);
        department.setDepartmentName(name);
        department.setDepartmentStatus(status);
        return department;
    }
}
