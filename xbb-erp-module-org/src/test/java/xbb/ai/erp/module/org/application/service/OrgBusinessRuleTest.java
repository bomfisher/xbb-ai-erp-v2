package xbb.ai.erp.module.org.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.org.application.pojo.EmployeeSavePojo;
import xbb.ai.erp.module.org.application.pojo.RolePermissionSavePojo;
import xbb.ai.erp.module.org.application.service.impl.DepartmentAdminAppServiceImpl;
import xbb.ai.erp.module.org.application.service.impl.EmployeeAdminAppServiceImpl;
import xbb.ai.erp.module.org.application.service.impl.PermissionAdminAppServiceImpl;
import xbb.ai.erp.module.org.application.service.impl.RoleAdminAppServiceImpl;
import xbb.ai.erp.module.org.domain.enums.DataScopeTypeEnum;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.domain.model.EmployeeRoleRelation;
import xbb.ai.erp.module.org.domain.model.Permission;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.PermissionRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.RoleRepositoryImpl;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgBusinessRuleTest {

    @Test
    void should_require_exactly_one_main_department() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("主部门不能为空", exception.getMessage());
    }

    @Test
    void should_keep_main_department_in_employee_relations() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L, 11L))).thenReturn(List.of(
            buildDepartment(10L, "总部"),
            buildDepartment(11L, "研发部")
        ));
        when(roleRepository.listByIds("corp-1", List.of())).thenReturn(List.of());

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setMainDepartmentId(10L);
        dto.setDepartmentIdList(List.of(11L));

        service.save(dto);
    }

    @Test
    void should_reject_department_and_role_assignment_for_resigned_employee() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L, 11L))).thenReturn(List.of(
            buildDepartment(10L, "总部"),
            buildDepartment(11L, "研发部")
        ));
        when(roleRepository.listByIds("corp-1", List.of(101L))).thenReturn(List.of());

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setMainDepartmentId(10L);
        dto.setDepartmentIdList(List.of(11L));
        dto.setRoleIdList(List.of(101L));
        dto.setEmploymentStatus(EmploymentStatusEnum.RESIGNED.getCode());

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("离职员工不能分配部门和角色", exception.getMessage());
    }

    @Test
    void should_reject_disabling_department_used_by_active_main_employee() {
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        when(departmentRepository.findById("corp-1", 10L)).thenReturn(buildDepartment(10L, "总部"));
        when(employeeRepository.countActiveByMainDepartment("corp-1", 10L)).thenReturn(1L);

        DepartmentAdminAppServiceImpl service = DepartmentAdminAppServiceImpl.forTesting(
            departmentRepository,
            employeeRepository
        );

        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-1");
        dto.setId(10L);

        BizException exception = assertThrows(BizException.class, () -> service.disable(dto));
        assertEquals("部门下存在在职主部门员工，不能停用", exception.getMessage());
    }

    @Test
    void should_keep_permission_dictionary_read_only() {
        try {
            Method save = PermissionAdminAppServiceImpl.class.getMethod("save", xbb.ai.erp.base.common.dto.BaseDTO.class);
            fail("permission save should not exist: " + save.getName());
        } catch (NoSuchMethodException ignored) {
        }
    }

    @Test
    void should_reject_data_scope_for_unselected_menu() {
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);
        PermissionRepositoryImpl permissionRepository = mock(PermissionRepositoryImpl.class);
        when(roleRepository.findById("corp-1", 9L)).thenReturn(buildRole(9L, "管理员"));
        when(permissionRepository.list()).thenReturn(List.of(
            buildPermission(200L, 0L, "MENU", "销售业务", 0),
            buildPermission(201L, 200L, "MENU", "销售报价", 1)
        ));

        RoleAdminAppServiceImpl service = RoleAdminAppServiceImpl.forTesting(
            roleRepository,
            permissionRepository
        );

        RolePermissionSavePojo dto = new RolePermissionSavePojo();
        dto.setCorpid("corp-1");
        dto.setRoleId(9L);
        RolePermissionSavePojo.MenuPermissionSaveItemPojo item = new RolePermissionSavePojo.MenuPermissionSaveItemPojo();
        item.setMenuPermissionId(201L);
        item.setSelected(0);
        item.setDataScopeType(DataScopeTypeEnum.ALL.getCode());
        dto.setPermissionList(List.of(item));

        BizException exception = assertThrows(BizException.class, () -> service.savePermission(dto));
        assertEquals("未授权菜单不能设置数据权限", exception.getMessage());
    }

    @Test
    void should_reject_assigning_disabled_role() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(roleRepository.listByIds("corp-1", List.of(101L))).thenReturn(List.of(
            buildRole(101L, "超级管理员", "SYSTEM", 0)
        ));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setMainDepartmentId(10L);
        dto.setRoleIdList(List.of(101L));

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("角色已停用，不能分配", exception.getMessage());
    }

    @Test
    void should_allow_keeping_existing_disabled_role() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(roleRepository.listByIds("corp-1", List.of(101L))).thenReturn(List.of(
            buildRole(101L, "历史停用角色", "CUSTOM", 0)
        ));
        when(employeeRepository.findByUserId("corp-1", "EMP-1001")).thenReturn(buildEmployee(1001L, "EMP-1001"));
        when(employeeRepository.listRoleRelationsByUserId("corp-1", "EMP-1001")).thenReturn(List.of(
            buildEmployeeRoleRelation(1001L, 101L)
        ));
        when(roleRepository.listByType("corp-1", "SYSTEM")).thenReturn(List.of());

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setUserId("EMP-2000");
        dto.setId("EMP-1001");
        dto.setMainDepartmentId(10L);
        dto.setRoleIdList(List.of(101L));

        service.save(dto);
    }

    @Test
    void should_reject_removing_own_system_role() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(roleRepository.listByIds("corp-1", List.of())).thenReturn(List.of());
        when(employeeRepository.findByUserId("corp-1", "EMP-1001")).thenReturn(buildEmployee(1001L, "EMP-1001"));
        when(roleRepository.listByType("corp-1", "SYSTEM")).thenReturn(List.of(buildRole(101L, "超级管理员", "SYSTEM", 1)));
        when(employeeRepository.listRoleRelationsByUserId("corp-1", "EMP-1001")).thenReturn(List.of(
            buildEmployeeRoleRelation(1001L, 101L)
        ));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setUserId("EMP-1001");
        dto.setId("EMP-1001");
        dto.setMainDepartmentId(10L);
        dto.setRoleIdList(List.of());

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("不能删除自己的超级管理员角色", exception.getMessage());
    }

    @Test
    void should_reject_removing_last_system_role_in_company() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(roleRepository.listByIds("corp-1", List.of())).thenReturn(List.of());
        when(employeeRepository.findByUserId("corp-1", "EMP-1002")).thenReturn(buildEmployee(1002L, "EMP-1002"));
        when(roleRepository.listByType("corp-1", "SYSTEM")).thenReturn(List.of(buildRole(101L, "超级管理员", "SYSTEM", 1)));
        when(employeeRepository.listRoleRelationsByUserId("corp-1", "EMP-1002")).thenReturn(List.of(
            buildEmployeeRoleRelation(1002L, 101L)
        ));
        when(employeeRepository.listByRoleIds("corp-1", List.of(101L))).thenReturn(List.of(
            buildEmployee(1002L, "EMP-1002")
        ));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setUserId("EMP-2000");
        dto.setId("EMP-1002");
        dto.setMainDepartmentId(10L);
        dto.setRoleIdList(List.of());

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("一个公司至少需要保留一个超级管理员", exception.getMessage());
    }

    @Test
    void should_allow_removing_system_role_when_other_system_admin_exists() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(roleRepository.listByIds("corp-1", List.of())).thenReturn(List.of());
        when(employeeRepository.findByUserId("corp-1", "EMP-1002")).thenReturn(buildEmployee(1002L, "EMP-1002"));
        when(roleRepository.listByType("corp-1", "SYSTEM")).thenReturn(List.of(buildRole(101L, "超级管理员", "SYSTEM", 1)));
        when(employeeRepository.listRoleRelationsByUserId("corp-1", "EMP-1002")).thenReturn(List.of(
            buildEmployeeRoleRelation(1002L, 101L)
        ));
        when(employeeRepository.listByRoleIds("corp-1", List.of(101L))).thenReturn(List.of(
            buildEmployee(1002L, "EMP-1002"),
            buildEmployee(1003L, "EMP-1003")
        ));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setUserId("EMP-2000");
        dto.setId("EMP-1002");
        dto.setMainDepartmentId(10L);
        dto.setRoleIdList(List.of());

        service.save(dto);
    }

    private Department buildDepartment(Long id, String name) {
        Department department = new Department();
        department.setId(id);
        department.setCorpid("corp-1");
        department.setDepartmentName(name);
        department.setDepartmentStatus(1);
        return department;
    }

    private Role buildRole(Long id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setCorpid("corp-1");
        role.setRoleName(name);
        return role;
    }

    private Role buildRole(Long id, String name, String roleType, Integer roleStatus) {
        Role role = buildRole(id, name);
        role.setRoleType(roleType);
        role.setRoleStatus(roleStatus);
        return role;
    }

    private Permission buildPermission(Long id, Long parentId, String permissionType, String name, Integer dataScopeFlag) {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setParentId(parentId);
        permission.setPermissionType(permissionType);
        permission.setPermissionName(name);
        permission.setDataScopeFlag(dataScopeFlag);
        return permission;
    }

    private Employee buildEmployee(Long id, String userId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setCorpid("corp-1");
        employee.setUserId(userId);
        employee.setMainDepartmentId(10L);
        return employee;
    }

    private EmployeeRoleRelation buildEmployeeRoleRelation(Long userId, Long roleId) {
        EmployeeRoleRelation relation = new EmployeeRoleRelation();
        relation.setCorpid("corp-1");
        relation.setUserId(userId);
        relation.setRoleId(roleId);
        return relation;
    }
}
