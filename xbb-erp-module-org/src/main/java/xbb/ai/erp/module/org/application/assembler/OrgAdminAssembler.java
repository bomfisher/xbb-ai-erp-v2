package xbb.ai.erp.module.org.application.assembler;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.vo.DepartmentTreeItemVO;
import xbb.ai.erp.module.org.admin.vo.EmployeeDetailVO;
import xbb.ai.erp.module.org.admin.vo.EmployeeListItemVO;
import xbb.ai.erp.module.org.admin.vo.PermissionDetailVO;
import xbb.ai.erp.module.org.admin.vo.RoleListItemVO;
import xbb.ai.erp.module.org.application.pojo.EmployeeSavePojo;
import xbb.ai.erp.module.org.application.pojo.RolePermissionSavePojo;
import xbb.ai.erp.module.org.domain.enums.DataScopeTypeEnum;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.enums.PermissionTypeEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.domain.model.EmployeeDepartmentRelation;
import xbb.ai.erp.module.org.domain.model.EmployeeRoleRelation;
import xbb.ai.erp.module.org.domain.model.Permission;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.domain.model.RolePermissionDataScope;
import xbb.ai.erp.module.org.domain.model.RolePermissionRelation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class OrgAdminAssembler {

    private OrgAdminAssembler() {
    }

    public static Employee toEmployee(EmployeeSavePojo dto) {
        Employee employee = new Employee();
        employee.setUserId(dto.getId() == null || dto.getId().isBlank() ? dto.getUserId() : dto.getId());
        employee.setCorpid(dto.getCorpid());
        employee.setAccountId(dto.getAccountId());
        employee.setUserCode(dto.getUserCode());
        employee.setUserName(dto.getUserName());
        employee.setEmail(dto.getEmail());
        employee.setJobNo(dto.getJobNo());
        employee.setMainDepartmentId(dto.getMainDepartmentId());
        employee.setUserStatus(dto.getUserStatus());
        employee.setEmploymentStatus(dto.getEmploymentStatus() == null ? EmploymentStatusEnum.ACTIVE.getCode() : dto.getEmploymentStatus());
        employee.setEntryTime(dto.getEntryTime());
        employee.setResignedTime(dto.getResignedTime());
        employee.setRemark(dto.getRemark());
        return employee;
    }

    public static EmployeeDepartmentRelation toEmployeeDepartmentRelation(String corpid, Long userId, Long departmentId, Integer mainFlag) {
        EmployeeDepartmentRelation relation = new EmployeeDepartmentRelation();
        relation.setCorpid(corpid);
        relation.setUserId(userId);
        relation.setDepartmentId(departmentId);
        relation.setMainFlag(mainFlag);
        return relation;
    }

    public static EmployeeRoleRelation toEmployeeRoleRelation(String corpid, Long userId, Long roleId) {
        EmployeeRoleRelation relation = new EmployeeRoleRelation();
        relation.setCorpid(corpid);
        relation.setUserId(userId);
        relation.setRoleId(roleId);
        return relation;
    }

    public static RolePermissionRelation toRolePermissionRelation(String corpid, Long roleId, Long permissionId) {
        RolePermissionRelation relation = new RolePermissionRelation();
        relation.setCorpid(corpid);
        relation.setRoleId(roleId);
        relation.setPermissionId(permissionId);
        return relation;
    }

    public static RolePermissionDataScope toRolePermissionDataScope(String corpid, Long roleId, Long permissionId, String dataScopeType) {
        RolePermissionDataScope scope = new RolePermissionDataScope();
        scope.setCorpid(corpid);
        scope.setRoleId(roleId);
        scope.setPermissionId(permissionId);
        scope.setDataScopeType(dataScopeType == null ? DataScopeTypeEnum.ALL.getCode() : dataScopeType);
        return scope;
    }

    public static EmployeeSaveResultVO toEmployeeSaveResultVO(String employeeId, List<Long> departmentIdList, List<Long> roleIdList) {
        EmployeeSaveResultVO vo = new EmployeeSaveResultVO();
        vo.setEmployeeId(employeeId);
        vo.setDepartmentIdList(departmentIdList == null ? List.of() : departmentIdList);
        vo.setRoleIdList(roleIdList == null ? List.of() : roleIdList);
        return vo;
    }

    public static RolePermissionSaveResultVO toRolePermissionSaveResultVO(Long roleId, List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> permissionList) {
        RolePermissionSaveResultVO vo = new RolePermissionSaveResultVO();
        vo.setRoleId(roleId);
        vo.setPermissionList(permissionList == null ? List.of() : permissionList);
        return vo;
    }

    public static PermissionListResultVO toPermissionListResultVO(List<Permission> permissionList) {
        PermissionListResultVO vo = new PermissionListResultVO();
        List<PermissionItemVO> itemList = new ArrayList<>();
        if (permissionList != null) {
            for (Permission permission : permissionList) {
                PermissionItemVO item = new PermissionItemVO();
                item.setId(permission.getId());
                item.setParentId(permission.getParentId());
                item.setPermissionCode(permission.getPermissionCode());
                item.setPermissionName(permission.getPermissionName());
                item.setPermissionType(permission.getPermissionType());
                item.setMenuAlias(permission.getMenuAlias());
                item.setActionCode(permission.getActionCode());
                item.setDataScopeFlag(permission.getDataScopeFlag());
                item.setPermissionStatus(permission.getPermissionStatus());
                itemList.add(item);
            }
        }
        vo.setList(itemList);
        return vo;
    }

    public static DepartmentTreeResultVO toDepartmentTreeResultVO(List<Department> departmentList) {
        DepartmentTreeResultVO vo = new DepartmentTreeResultVO();
        List<DepartmentTreeItemVO> flatList = new ArrayList<>();
        Map<Long, DepartmentTreeItemVO> itemMap = new LinkedHashMap<>();
        if (departmentList != null) {
            for (Department department : departmentList) {
                DepartmentTreeItemVO item = new DepartmentTreeItemVO();
                item.setId(department.getId());
                item.setDepartmentCode(department.getDepartmentCode());
                item.setDepartmentName(department.getDepartmentName());
                item.setParentId(department.getParentId());
                item.setDepartmentLevel(department.getDepartmentLevel());
                item.setDepartmentStatus(department.getDepartmentStatus());
                flatList.add(item);
                itemMap.put(item.getId(), item);
            }
        }
        List<DepartmentTreeItemVO> treeList = new ArrayList<>();
        for (DepartmentTreeItemVO item : flatList) {
            if (item.getParentId() == null || item.getParentId() <= 0 || !itemMap.containsKey(item.getParentId())) {
                treeList.add(item);
                continue;
            }
            itemMap.get(item.getParentId()).getChildren().add(item);
        }
        vo.setFlatList(flatList);
        vo.setTreeList(treeList);
        return vo;
    }

    public static ListBaseVO<EmployeeListItemVO> toEmployeeListResultVO(
        List<Employee> employeeList,
        Map<Long, String> departmentNameMap,
        Map<String, List<Long>> roleIdMap,
        Map<String, List<String>> roleNameMap,
        Integer pageNum,
        Integer pageSize
    ) {
        List<EmployeeListItemVO> itemList = new ArrayList<>();
        if (employeeList != null) {
            for (Employee employee : employeeList) {
                itemList.add(toEmployeeListItemVO(employee, departmentNameMap, roleIdMap, roleNameMap));
            }
        }
        return buildListBaseVO(itemList, pageNum, pageSize);
    }

    public static EmployeeListItemVO toEmployeeListItemVO(
        Employee employee,
        Map<Long, String> departmentNameMap,
        Map<String, List<Long>> roleIdMap,
        Map<String, List<String>> roleNameMap
    ) {
        EmployeeListItemVO item = new EmployeeListItemVO();
        item.setId(employee.getUserId());
        item.setUserCode(employee.getUserCode());
        item.setUserName(employee.getUserName());
        item.setEmail(employee.getEmail());
        item.setJobNo(employee.getJobNo());
        item.setMainDepartmentId(employee.getMainDepartmentId());
        item.setMainDepartmentName(departmentNameMap.get(employee.getMainDepartmentId()));
        item.setEmploymentStatus(employee.getEmploymentStatus());
        item.setUserStatus(employee.getUserStatus());
        item.setRoleIdList(roleIdMap.getOrDefault(employee.getUserId(), List.of()));
        item.setRoleNameList(roleNameMap.getOrDefault(employee.getUserId(), List.of()));
        return item;
    }

    public static EmployeeDetailVO toEmployeeDetailVO(
        Employee employee,
        Map<Long, String> departmentNameMap,
        List<Long> departmentIdList,
        List<Role> roleList
    ) {
        EmployeeDetailVO vo = new EmployeeDetailVO();
        List<Long> roleIdList = roleList == null ? List.of() : roleList.stream().map(Role::getId).toList();
        List<String> roleNameList = roleList == null ? List.of() : roleList.stream().map(Role::getRoleName).toList();
        vo.setMainData(toEmployeeListItemVO(
            employee,
            departmentNameMap,
            Map.of(employee.getUserId(), roleIdList),
            Map.of(employee.getUserId(), roleNameList)
        ));
        vo.setDepartmentIdList(departmentIdList == null ? List.of() : departmentIdList);
        vo.setRoleIdList(roleIdList);
        vo.setRoleList(roleList == null ? List.of() : roleList.stream().map(OrgAdminAssembler::toRoleListItemVO).toList());
        return vo;
    }

    public static ListBaseVO<RoleListItemVO> toRoleListResultVO(List<Role> roleList, Integer pageNum, Integer pageSize) {
        List<RoleListItemVO> itemList = new ArrayList<>();
        if (roleList != null) {
            for (Role role : roleList) {
                itemList.add(toRoleListItemVO(role));
            }
        }
        return buildListBaseVO(itemList, pageNum, pageSize);
    }

    public static RoleListItemVO toRoleListItemVO(Role role) {
        RoleListItemVO item = new RoleListItemVO();
        item.setId(role.getId());
        item.setRoleName(role.getRoleName());
        item.setRoleType(role.getRoleType());
        item.setRoleStatus(role.getRoleStatus());
        return item;
    }

    public static RolePermissionDetailResultVO toRolePermissionDetailResultVO(
        Role role,
        List<Permission> permissionList,
        Set<Long> selectedPermissionIdSet,
        Map<Long, String> dataScopeMap
    ) {
        RolePermissionDetailResultVO vo = new RolePermissionDetailResultVO();
        vo.setRoleId(role.getId());
        vo.setRoleName(role.getRoleName());
        vo.setPermissionList(buildPermissionTree(permissionList, selectedPermissionIdSet, dataScopeMap));
        return vo;
    }

    private static List<PermissionDetailVO> buildPermissionTree(
        List<Permission> permissionList,
        Set<Long> selectedPermissionIdSet,
        Map<Long, String> dataScopeMap
    ) {
        Map<Long, PermissionDetailVO> menuMap = new LinkedHashMap<>();
        List<PermissionDetailVO> rootMenuList = new ArrayList<>();
        if (permissionList == null) {
            return rootMenuList;
        }
        for (Permission permission : permissionList) {
            if (!Objects.equals(PermissionTypeEnum.MENU.getCode(), permission.getPermissionType())) {
                continue;
            }
            PermissionDetailVO menuItem = toPermissionDetailVO(permission, selectedPermissionIdSet, dataScopeMap);
            menuMap.put(menuItem.getId(), menuItem);
        }
        for (Permission menuPermission : permissionList) {
            if (!Objects.equals(PermissionTypeEnum.MENU.getCode(), menuPermission.getPermissionType())) {
                continue;
            }
            PermissionDetailVO menuItem = menuMap.get(menuPermission.getId());
            Long parentId = menuPermission.getParentId();
            if (parentId == null || parentId <= 0 || !menuMap.containsKey(parentId)) {
                rootMenuList.add(menuItem);
                continue;
            }
            menuMap.get(parentId).getChildren().add(menuItem);
        }
        for (Permission permission : permissionList) {
            if (!Objects.equals(PermissionTypeEnum.ACTION.getCode(), permission.getPermissionType())) {
                continue;
            }
            PermissionDetailVO actionItem = toPermissionDetailVO(permission, selectedPermissionIdSet, dataScopeMap);
            PermissionDetailVO parentMenu = menuMap.get(permission.getParentId());
            if (parentMenu != null) {
                parentMenu.getChildren().add(actionItem);
                if (actionItem.getSelected() != null && actionItem.getSelected() == 1) {
                    parentMenu.setSelected(1);
                }
            }
        }
        return rootMenuList;
    }

    private static PermissionDetailVO toPermissionDetailVO(
        Permission permission,
        Set<Long> selectedPermissionIdSet,
        Map<Long, String> dataScopeMap
    ) {
        PermissionDetailVO item = new PermissionDetailVO();
        item.setId(permission.getId());
        item.setParentId(permission.getParentId());
        item.setPermissionCode(permission.getPermissionCode());
        item.setPermissionName(permission.getPermissionName());
        item.setPermissionType(permission.getPermissionType());
        item.setMenuAlias(permission.getMenuAlias());
        item.setActionCode(permission.getActionCode());
        item.setDataScopeFlag(permission.getDataScopeFlag());
        item.setPermissionStatus(permission.getPermissionStatus());
        item.setSelected(selectedPermissionIdSet.contains(permission.getId()) ? 1 : 0);
        item.setDataScopeType(dataScopeMap.get(permission.getId()));
        return item;
    }

    private static <T> ListBaseVO<T> buildListBaseVO(List<T> allItemList, Integer pageNum, Integer pageSize) {
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? Math.max(allItemList.size(), 1) : pageSize;
        int pageCount = Math.max((allItemList.size() + safePageSize - 1) / safePageSize, 1);
        int fromIndex = Math.min((safePageNum - 1) * safePageSize, allItemList.size());
        int toIndex = Math.min(fromIndex + safePageSize, allItemList.size());
        ListBaseVO<T> vo = new ListBaseVO<>();
        vo.setList(allItemList.subList(fromIndex, toIndex));
        vo.setPageHelper(new ListBaseVO.PageHelper(safePageNum, pageCount));
        return vo;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class EmployeeSaveResultVO extends BaseVO {
        private String employeeId;
        private List<Long> departmentIdList;
        private List<Long> roleIdList;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class RolePermissionSaveResultVO extends BaseVO {
        private Long roleId;
        private List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> permissionList = List.of();
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class PermissionListResultVO extends BaseVO {
        private List<PermissionItemVO> list = List.of();
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class DepartmentTreeResultVO extends BaseVO {
        private List<DepartmentTreeItemVO> treeList = List.of();
        private List<DepartmentTreeItemVO> flatList = List.of();
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class RolePermissionDetailResultVO extends BaseVO {
        private Long roleId;
        private String roleName;
        private List<PermissionDetailVO> permissionList = List.of();
    }

    @Data
    public static class PermissionItemVO {
        private Long id;
        private Long parentId;
        private String permissionCode;
        private String permissionName;
        private String permissionType;
        private String menuAlias;
        private String actionCode;
        private Integer dataScopeFlag;
        private Integer permissionStatus;
    }
}
