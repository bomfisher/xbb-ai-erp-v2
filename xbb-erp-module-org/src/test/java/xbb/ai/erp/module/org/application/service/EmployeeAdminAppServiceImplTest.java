package xbb.ai.erp.module.org.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.org.admin.dto.DepartmentTreeDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeListDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeIdDTO;
import xbb.ai.erp.module.org.admin.dto.RoleListDTO;
import xbb.ai.erp.module.org.admin.vo.EmployeeDetailVO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
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
import xbb.ai.erp.module.org.domain.model.EmployeeDepartmentRelation;
import xbb.ai.erp.module.org.domain.model.EmployeeRoleRelation;
import xbb.ai.erp.module.org.domain.model.Permission;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.domain.model.RolePermissionDataScope;
import xbb.ai.erp.module.org.domain.model.RolePermissionRelation;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.PermissionRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.RoleRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeAdminAppServiceImplTest {

    @Test
    void should_save_employee_and_sync_relations() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L, 11L))).thenReturn(List.of(
            buildDepartment(10L, "总部"),
            buildDepartment(11L, "研发部")
        ));
        when(roleRepository.listByIds("corp-1", List.of(101L, 102L))).thenReturn(List.of(
            buildRole(101L, "管理员"),
            buildRole(102L, "审计员")
        ));
        doAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setId(1001L);
            return null;
        }).when(employeeRepository).insert(any(Employee.class));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setCorpid("corp-1");
        dto.setUserId("EMP-1001");
        dto.setUserCode("E001");
        dto.setUserName("张三");
        dto.setEmail("zhangsan@example.com");
        dto.setMainDepartmentId(10L);
        dto.setDepartmentIdList(List.of(10L, 11L));
        dto.setRoleIdList(List.of(101L, 102L));

        OrgAdminAssembler.EmployeeSaveResultVO result = (OrgAdminAssembler.EmployeeSaveResultVO) service.save(dto);

        assertEquals("EMP-1001", result.getEmployeeId());
        assertEquals(List.of(10L, 11L), result.getDepartmentIdList());
        assertEquals(List.of(101L, 102L), result.getRoleIdList());
        verify(employeeRepository).insert(any(Employee.class));

        ArgumentCaptor<List<EmployeeDepartmentRelation>> departmentCaptor = ArgumentCaptor.forClass(List.class);
        verify(employeeRepository).replaceDepartmentRelations(org.mockito.ArgumentMatchers.eq("corp-1"), org.mockito.ArgumentMatchers.eq("EMP-1001"), departmentCaptor.capture());
        assertEquals(2, departmentCaptor.getValue().size());
        assertEquals(Integer.valueOf(1), departmentCaptor.getValue().get(0).getMainFlag());
        assertEquals(Integer.valueOf(0), departmentCaptor.getValue().get(1).getMainFlag());

        ArgumentCaptor<List<EmployeeRoleRelation>> roleCaptor = ArgumentCaptor.forClass(List.class);
        verify(employeeRepository).replaceRoleRelations(org.mockito.ArgumentMatchers.eq("corp-1"), org.mockito.ArgumentMatchers.eq("EMP-1001"), roleCaptor.capture());
        assertEquals(List.of(101L, 102L), roleCaptor.getValue().stream().map(EmployeeRoleRelation::getRoleId).toList());
    }

    @Test
    void should_require_existing_employee_before_update() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(roleRepository.listByIds("corp-1", List.of())).thenReturn(List.of());
        when(employeeRepository.findByUserId("corp-1", "EMP-1001")).thenReturn(null);

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeSavePojo dto = new EmployeeSavePojo();
        dto.setId("EMP-1001");
        dto.setCorpid("corp-1");
        dto.setMainDepartmentId(10L);

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("员工不存在", exception.getMessage());
    }

    @Test
    void should_overwrite_role_permission_relations_in_one_call_path() {
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);
        PermissionRepositoryImpl permissionRepository = mock(PermissionRepositoryImpl.class);

        when(roleRepository.findById("corp-1", 9L)).thenReturn(buildRole(9L, "运营主管"));
        when(permissionRepository.list()).thenReturn(List.of(
            buildPermission(200L, 0L, "MENU", "销售业务", 0),
            buildPermission(201L, 200L, "MENU", "销售报价", 1),
            buildPermission(202L, 201L, "ACTION", "新建", 0)
        ));

        RoleAdminAppServiceImpl service = RoleAdminAppServiceImpl.forTesting(
            roleRepository,
            permissionRepository
        );

        RolePermissionSavePojo dto = new RolePermissionSavePojo();
        dto.setCorpid("corp-1");
        dto.setRoleId(9L);
        RolePermissionSavePojo.MenuPermissionSaveItemPojo topMenuItem = new RolePermissionSavePojo.MenuPermissionSaveItemPojo();
        topMenuItem.setMenuPermissionId(200L);
        topMenuItem.setSelected(1);
        topMenuItem.setActionPermissionIdList(List.of());
        RolePermissionSavePojo.MenuPermissionSaveItemPojo permissionItem = new RolePermissionSavePojo.MenuPermissionSaveItemPojo();
        permissionItem.setMenuPermissionId(201L);
        permissionItem.setSelected(1);
        permissionItem.setDataScopeType(DataScopeTypeEnum.ALL.getCode());
        permissionItem.setActionPermissionIdList(List.of(202L));
        dto.setPermissionList(List.of(topMenuItem, permissionItem));

        OrgAdminAssembler.RolePermissionSaveResultVO result = (OrgAdminAssembler.RolePermissionSaveResultVO) service.savePermission(dto);

        assertEquals(9L, result.getRoleId());
        assertEquals(2, result.getPermissionList().size());
        assertEquals(200L, result.getPermissionList().get(0).getMenuPermissionId());
        assertEquals(List.of(), result.getPermissionList().get(0).getActionPermissionIdList());
        assertEquals(201L, result.getPermissionList().get(1).getMenuPermissionId());
        assertEquals(List.of(202L), result.getPermissionList().get(1).getActionPermissionIdList());

        ArgumentCaptor<List<RolePermissionRelation>> relationCaptor = ArgumentCaptor.forClass(List.class);
        verify(roleRepository).replacePermissionRelations(org.mockito.ArgumentMatchers.eq("corp-1"), org.mockito.ArgumentMatchers.eq(9L), relationCaptor.capture());
        assertEquals(List.of(200L, 201L, 202L), relationCaptor.getValue().stream().map(RolePermissionRelation::getPermissionId).toList());

        ArgumentCaptor<List<RolePermissionDataScope>> scopeCaptor = ArgumentCaptor.forClass(List.class);
        verify(roleRepository).replacePermissionDataScopes(org.mockito.ArgumentMatchers.eq("corp-1"), org.mockito.ArgumentMatchers.eq(9L), scopeCaptor.capture());
        assertEquals(1, scopeCaptor.getValue().size());
        assertEquals(List.of(DataScopeTypeEnum.ALL.getCode()), scopeCaptor.getValue().stream().map(RolePermissionDataScope::getDataScopeType).toList());
        assertEquals(List.of(201L), scopeCaptor.getValue().stream().map(RolePermissionDataScope::getPermissionId).toList());
    }

    @Test
    void should_list_employees_for_department_structure_page() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        Employee employee = buildEmployee("EMP-1001", "E001", "张三", 10L, EmploymentStatusEnum.ACTIVE.getCode(), 1);
        employee.setId(1001L);
        when(employeeRepository.list("corp-1", "张", EmploymentStatusEnum.ACTIVE.getCode(), 1, 10L)).thenReturn(List.of(employee));
        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(
            buildDepartment(10L, "总部")
        ));
        when(employeeRepository.listRoleRelationsByUserIds("corp-1", List.of(1001L))).thenReturn(List.of(
            buildEmployeeRoleRelation(1001L, 101L),
            buildEmployeeRoleRelation(1001L, 102L)
        ));
        when(roleRepository.listByIds("corp-1", List.of(101L, 102L))).thenReturn(List.of(
            buildRole(101L, "超级管理员", "SYSTEM", 1),
            buildRole(102L, "采购员", "CUSTOM", 1)
        ));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeListDTO dto = new EmployeeListDTO();
        dto.setCorpid("corp-1");
        dto.setDepartmentId(10L);
        dto.setKeyword("张");
        dto.setEmploymentStatus(EmploymentStatusEnum.ACTIVE.getCode());
        dto.setUserStatus(1);
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<xbb.ai.erp.module.org.admin.vo.EmployeeListItemVO> result = service.list(dto);

        assertEquals(1, result.getList().size());
        assertEquals("张三", result.getList().get(0).getUserName());
        assertEquals("总部", result.getList().get(0).getMainDepartmentName());
        assertEquals(List.of(101L, 102L), result.getList().get(0).getRoleIdList());
        assertEquals(List.of("超级管理员", "采购员"), result.getList().get(0).getRoleNameList());
        verify(employeeRepository).list("corp-1", "张", EmploymentStatusEnum.ACTIVE.getCode(), 1, 10L);
    }

    @Test
    void should_return_employee_detail_with_roles() {
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);

        Employee employee = buildEmployee("EMP-1001", "E001", "张三", 10L, EmploymentStatusEnum.ACTIVE.getCode(), 1);
        employee.setId(1001L);
        when(employeeRepository.findByUserId("corp-1", "EMP-1001")).thenReturn(employee);
        when(departmentRepository.listByIds("corp-1", List.of(10L))).thenReturn(List.of(buildDepartment(10L, "总部")));
        when(employeeRepository.listRoleRelationsByUserId("corp-1", "EMP-1001")).thenReturn(List.of(
            buildEmployeeRoleRelation(1001L, 101L),
            buildEmployeeRoleRelation(1001L, 102L)
        ));
        when(employeeRepository.listDepartmentRelationsByUserId("corp-1", "EMP-1001")).thenReturn(List.of(
            buildEmployeeDepartmentRelation(1001L, 10L, 1),
            buildEmployeeDepartmentRelation(1001L, 11L, 0)
        ));
        when(roleRepository.listByIds("corp-1", List.of(101L, 102L))).thenReturn(List.of(
            buildRole(101L, "超级管理员", "SYSTEM", 1),
            buildRole(102L, "采购员", "CUSTOM", 1)
        ));

        EmployeeAdminAppServiceImpl service = EmployeeAdminAppServiceImpl.forTesting(
            employeeRepository,
            departmentRepository,
            roleRepository
        );

        EmployeeIdDTO dto = new EmployeeIdDTO();
        dto.setCorpid("corp-1");
        dto.setId("EMP-1001");

        EmployeeDetailVO result = (EmployeeDetailVO) service.detail(dto);

        assertEquals("EMP-1001", result.getMainData().getId());
        assertEquals(List.of(10L, 11L), result.getDepartmentIdList());
        assertEquals(List.of(101L, 102L), result.getRoleIdList());
        assertEquals(2, result.getRoleList().size());
        assertEquals("超级管理员", result.getRoleList().get(0).getRoleName());
    }

    @Test
    void should_build_department_tree_for_structure_page() {
        DepartmentRepositoryImpl departmentRepository = mock(DepartmentRepositoryImpl.class);
        EmployeeRepositoryImpl employeeRepository = mock(EmployeeRepositoryImpl.class);
        when(departmentRepository.list("corp-1", 1)).thenReturn(List.of(
            buildDepartment(10L, "总部", 0L, 1),
            buildDepartment(11L, "研发部", 10L, 2)
        ));

        DepartmentAdminAppServiceImpl service = DepartmentAdminAppServiceImpl.forTesting(
            departmentRepository,
            employeeRepository
        );

        DepartmentTreeDTO dto = new DepartmentTreeDTO();
        dto.setCorpid("corp-1");
        dto.setDepartmentStatus(1);

        OrgAdminAssembler.DepartmentTreeResultVO result = service.tree(dto);

        assertEquals(2, result.getFlatList().size());
        assertEquals(1, result.getTreeList().size());
        assertEquals(1, result.getTreeList().get(0).getChildren().size());
        assertEquals("研发部", result.getTreeList().get(0).getChildren().get(0).getDepartmentName());
    }

    @Test
    void should_list_roles_for_role_permission_page() {
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);
        PermissionRepositoryImpl permissionRepository = mock(PermissionRepositoryImpl.class);
        when(roleRepository.list("corp-1", "管理员", 1)).thenReturn(List.of(
            buildRole(9L, "管理员")
        ));

        RoleAdminAppServiceImpl service = RoleAdminAppServiceImpl.forTesting(
            roleRepository,
            permissionRepository
        );

        RoleListDTO dto = new RoleListDTO();
        dto.setCorpid("corp-1");
        dto.setKeyword("管理员");
        dto.setRoleStatus(1);
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<xbb.ai.erp.module.org.admin.vo.RoleListItemVO> result = service.list(dto);

        assertEquals(1, result.getList().size());
        assertEquals("管理员", result.getList().get(0).getRoleName());
        verify(roleRepository).list("corp-1", "管理员", 1);
    }

    @Test
    void should_enable_and_disable_role() {
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);
        PermissionRepositoryImpl permissionRepository = mock(PermissionRepositoryImpl.class);
        when(roleRepository.findById("corp-1", 9L)).thenReturn(buildRole(9L, "管理员", "SYSTEM", 1));

        RoleAdminAppServiceImpl service = RoleAdminAppServiceImpl.forTesting(
            roleRepository,
            permissionRepository
        );

        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-1");
        dto.setId(9L);

        service.disable(dto);
        service.enable(dto);

        verify(roleRepository).updateRoleStatus("corp-1", 9L, 0);
        verify(roleRepository).updateRoleStatus("corp-1", 9L, 1);
    }

    @Test
    void should_return_permission_detail_for_selected_role() {
        RoleRepositoryImpl roleRepository = mock(RoleRepositoryImpl.class);
        PermissionRepositoryImpl permissionRepository = mock(PermissionRepositoryImpl.class);
        when(roleRepository.findById("corp-1", 9L)).thenReturn(buildRole(9L, "管理员"));
        when(roleRepository.listPermissionRelations("corp-1", 9L)).thenReturn(List.of(
            buildRolePermissionRelation(9L, 200L),
            buildRolePermissionRelation(9L, 201L),
            buildRolePermissionRelation(9L, 202L)
        ));
        when(roleRepository.listPermissionDataScopes("corp-1", 9L)).thenReturn(List.of(
            buildRolePermissionDataScope(9L, 201L, DataScopeTypeEnum.ALL.getCode())
        ));
        when(permissionRepository.list()).thenReturn(List.of(
            buildPermission(200L, 0L, "MENU", "销售业务", 0),
            buildPermission(201L, 200L, "MENU", "销售报价", 1),
            buildPermission(202L, 201L, "ACTION", "新建", 0)
        ));

        RoleAdminAppServiceImpl service = RoleAdminAppServiceImpl.forTesting(
            roleRepository,
            permissionRepository
        );

        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-1");
        dto.setId(9L);

        OrgAdminAssembler.RolePermissionDetailResultVO result = service.permissionDetail(dto);

        assertEquals(9L, result.getRoleId());
        assertEquals(1, result.getPermissionList().size());
        assertEquals("销售业务", result.getPermissionList().get(0).getPermissionName());
        assertEquals(Integer.valueOf(1), result.getPermissionList().get(0).getSelected());
        assertEquals(1, result.getPermissionList().get(0).getChildren().size());
        assertEquals("销售报价", result.getPermissionList().get(0).getChildren().get(0).getPermissionName());
        assertEquals(Integer.valueOf(1), result.getPermissionList().get(0).getChildren().get(0).getSelected());
        assertEquals(DataScopeTypeEnum.ALL.getCode(), result.getPermissionList().get(0).getChildren().get(0).getDataScopeType());
        assertEquals(1, result.getPermissionList().get(0).getChildren().get(0).getChildren().size());
        assertEquals(Integer.valueOf(1), result.getPermissionList().get(0).getChildren().get(0).getChildren().get(0).getSelected());
    }

    @Test
    void should_list_permissions_read_only_from_repository_source() {
        PermissionRepositoryImpl permissionRepository = mock(PermissionRepositoryImpl.class);
        when(permissionRepository.list()).thenReturn(List.of(
            buildPermission(301L, 0L, "MENU", "员工查看", 1),
            buildPermission(302L, 301L, "ACTION", "员工编辑", 0)
        ));

        PermissionAdminAppServiceImpl service = PermissionAdminAppServiceImpl.forTesting(permissionRepository);

        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-1");

        OrgAdminAssembler.PermissionListResultVO result = (OrgAdminAssembler.PermissionListResultVO) service.list(dto);

        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertEquals("员工查看", result.getList().get(0).getPermissionName());
        verify(permissionRepository).list();
        verify(permissionRepository, never()).insert(any(Permission.class));
    }

    private Department buildDepartment(Long id, String name) {
        Department department = new Department();
        department.setId(id);
        department.setCorpid("corp-1");
        department.setDepartmentName(name);
        return department;
    }

    private Department buildDepartment(Long id, String name, Long parentId, Integer level) {
        Department department = buildDepartment(id, name);
        department.setParentId(parentId);
        department.setDepartmentLevel(level);
        return department;
    }

    private Employee buildEmployee(String userId, String userCode, String userName, Long mainDepartmentId, String employmentStatus, Integer userStatus) {
        Employee employee = new Employee();
        employee.setId(1001L);
        employee.setUserId(userId);
        employee.setCorpid("corp-1");
        employee.setUserCode(userCode);
        employee.setUserName(userName);
        employee.setMainDepartmentId(mainDepartmentId);
        employee.setEmploymentStatus(employmentStatus);
        employee.setUserStatus(userStatus);
        return employee;
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

    private RolePermissionRelation buildRolePermissionRelation(Long roleId, Long permissionId) {
        RolePermissionRelation relation = new RolePermissionRelation();
        relation.setCorpid("corp-1");
        relation.setRoleId(roleId);
        relation.setPermissionId(permissionId);
        return relation;
    }

    private RolePermissionDataScope buildRolePermissionDataScope(Long roleId, Long permissionId, String dataScopeType) {
        RolePermissionDataScope scope = new RolePermissionDataScope();
        scope.setCorpid("corp-1");
        scope.setRoleId(roleId);
        scope.setPermissionId(permissionId);
        scope.setDataScopeType(dataScopeType);
        return scope;
    }

    private EmployeeRoleRelation buildEmployeeRoleRelation(Long userId, Long roleId) {
        EmployeeRoleRelation relation = new EmployeeRoleRelation();
        relation.setCorpid("corp-1");
        relation.setUserId(userId);
        relation.setRoleId(roleId);
        return relation;
    }

    private EmployeeDepartmentRelation buildEmployeeDepartmentRelation(Long userId, Long departmentId, Integer mainFlag) {
        EmployeeDepartmentRelation relation = new EmployeeDepartmentRelation();
        relation.setCorpid("corp-1");
        relation.setUserId(userId);
        relation.setDepartmentId(departmentId);
        relation.setMainFlag(mainFlag);
        return relation;
    }
}
