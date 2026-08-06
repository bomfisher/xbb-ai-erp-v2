package xbb.ai.erp.module.org.infrastructure.persistence.convertor;

import xbb.ai.erp.module.org.domain.enums.DataScopeTypeEnum;
import xbb.ai.erp.module.org.domain.enums.EnableStatusEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.domain.model.EmployeeDepartmentRelation;
import xbb.ai.erp.module.org.domain.model.EmployeeRoleRelation;
import xbb.ai.erp.module.org.domain.model.Permission;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.domain.model.RolePermissionDataScope;
import xbb.ai.erp.module.org.domain.model.RolePermissionRelation;
import xbb.ai.erp.module.org.infrastructure.persistence.po.DepartmentPO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeeDepartmentRelationPO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeePO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeeRoleRelationPO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.PermissionPO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.RolePO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.RolePermissionDataScopePO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.RolePermissionRelationPO;

public final class OrgConvertor {

    private OrgConvertor() {
    }

    public static EmployeePO toPO(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeePO po = new EmployeePO();
        po.setId(employee.getId());
        po.setUserId(employee.getUserId());
        po.setCorpid(employee.getCorpid());
        po.setAccountId(employee.getAccountId());
        po.setUserCode(employee.getUserCode());
        po.setUserName(employee.getUserName());
        po.setEmail(employee.getEmail());
        po.setJobNo(employee.getJobNo());
        po.setMainDepartmentId(employee.getMainDepartmentId());
        po.setUserStatus(employee.getUserStatus());
        po.setEmploymentStatus(employee.getEmploymentStatus());
        po.setEntryTime(employee.getEntryTime());
        po.setResignedTime(employee.getResignedTime());
        po.setRemark(employee.getRemark());
        return po;
    }

    public static Employee toDomain(EmployeePO po) {
        if (po == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(po.getId());
        employee.setUserId(po.getUserId());
        employee.setCorpid(po.getCorpid());
        employee.setAccountId(po.getAccountId());
        employee.setUserCode(po.getUserCode());
        employee.setUserName(po.getUserName());
        employee.setEmail(po.getEmail());
        employee.setJobNo(po.getJobNo());
        employee.setMainDepartmentId(po.getMainDepartmentId());
        employee.setUserStatus(po.getUserStatus());
        employee.setEmploymentStatus(po.getEmploymentStatus());
        employee.setEntryTime(po.getEntryTime());
        employee.setResignedTime(po.getResignedTime());
        employee.setRemark(po.getRemark());
        return employee;
    }

    public static EmployeeDepartmentRelationPO toPO(EmployeeDepartmentRelation relation) {
        if (relation == null) {
            return null;
        }
        EmployeeDepartmentRelationPO po = new EmployeeDepartmentRelationPO();
        po.setId(relation.getId());
        po.setCorpid(relation.getCorpid());
        po.setUserId(relation.getUserId());
        po.setDepartmentId(relation.getDepartmentId());
        po.setMainFlag(relation.getMainFlag());
        po.setRelStatus(relation.getRelStatus() == null ? EnableStatusEnum.ENABLE.getCode() : relation.getRelStatus());
        po.setDel(0);
        return po;
    }

    public static EmployeeDepartmentRelation toDomain(EmployeeDepartmentRelationPO po) {
        if (po == null) {
            return null;
        }
        EmployeeDepartmentRelation relation = new EmployeeDepartmentRelation();
        relation.setId(po.getId());
        relation.setCorpid(po.getCorpid());
        relation.setUserId(po.getUserId());
        relation.setDepartmentId(po.getDepartmentId());
        relation.setMainFlag(po.getMainFlag());
        relation.setRelStatus(po.getRelStatus());
        return relation;
    }

    public static EmployeeRoleRelationPO toPO(EmployeeRoleRelation relation) {
        if (relation == null) {
            return null;
        }
        EmployeeRoleRelationPO po = new EmployeeRoleRelationPO();
        po.setId(relation.getId());
        po.setCorpid(relation.getCorpid());
        po.setUserId(relation.getUserId());
        po.setRoleId(relation.getRoleId());
        po.setRelStatus(relation.getRelStatus() == null ? EnableStatusEnum.ENABLE.getCode() : relation.getRelStatus());
        po.setDel(0);
        return po;
    }

    public static EmployeeRoleRelation toDomain(EmployeeRoleRelationPO po) {
        if (po == null) {
            return null;
        }
        EmployeeRoleRelation relation = new EmployeeRoleRelation();
        relation.setId(po.getId());
        relation.setCorpid(po.getCorpid());
        relation.setUserId(po.getUserId());
        relation.setRoleId(po.getRoleId());
        relation.setRelStatus(po.getRelStatus());
        return relation;
    }

    public static RolePermissionRelationPO toPO(RolePermissionRelation relation) {
        if (relation == null) {
            return null;
        }
        RolePermissionRelationPO po = new RolePermissionRelationPO();
        po.setId(relation.getId());
        po.setCorpid(relation.getCorpid());
        po.setRoleId(relation.getRoleId());
        po.setPermissionId(relation.getPermissionId());
        po.setRelStatus(relation.getRelStatus() == null ? EnableStatusEnum.ENABLE.getCode() : relation.getRelStatus());
        po.setDel(0);
        return po;
    }

    public static RolePermissionRelation toDomain(RolePermissionRelationPO po) {
        if (po == null) {
            return null;
        }
        RolePermissionRelation relation = new RolePermissionRelation();
        relation.setId(po.getId());
        relation.setCorpid(po.getCorpid());
        relation.setRoleId(po.getRoleId());
        relation.setPermissionId(po.getPermissionId());
        relation.setRelStatus(po.getRelStatus());
        return relation;
    }

    public static RolePermissionDataScopePO toPO(RolePermissionDataScope scope) {
        if (scope == null) {
            return null;
        }
        RolePermissionDataScopePO po = new RolePermissionDataScopePO();
        po.setId(scope.getId());
        po.setCorpid(scope.getCorpid());
        po.setRoleId(scope.getRoleId());
        po.setPermissionId(scope.getPermissionId());
        po.setDataScopeType(scope.getDataScopeType() == null ? DataScopeTypeEnum.ALL.getCode() : scope.getDataScopeType());
        po.setRelStatus(scope.getRelStatus() == null ? EnableStatusEnum.ENABLE.getCode() : scope.getRelStatus());
        po.setDel(0);
        return po;
    }

    public static RolePermissionDataScope toDomain(RolePermissionDataScopePO po) {
        if (po == null) {
            return null;
        }
        RolePermissionDataScope scope = new RolePermissionDataScope();
        scope.setId(po.getId());
        scope.setCorpid(po.getCorpid());
        scope.setRoleId(po.getRoleId());
        scope.setPermissionId(po.getPermissionId());
        scope.setDataScopeType(po.getDataScopeType());
        scope.setRelStatus(po.getRelStatus());
        return scope;
    }

    public static DepartmentPO toPO(Department department) {
        if (department == null) {
            return null;
        }
        DepartmentPO po = new DepartmentPO();
        po.setId(department.getId());
        po.setCorpid(department.getCorpid());
        po.setDepartmentCode(department.getDepartmentCode());
        po.setDepartmentName(department.getDepartmentName());
        po.setParentId(department.getParentId());
        po.setAncestorPath(department.getAncestorPath());
        po.setDepartmentLevel(department.getDepartmentLevel());
        po.setLeaderUserId(department.getLeaderUserId());
        po.setSortNo(department.getSortNo());
        po.setDepartmentStatus(department.getDepartmentStatus());
        po.setRemark(department.getRemark());
        return po;
    }

    public static Department toDomain(DepartmentPO po) {
        if (po == null) {
            return null;
        }
        Department department = new Department();
        department.setId(po.getId());
        department.setCorpid(po.getCorpid());
        department.setDepartmentCode(po.getDepartmentCode());
        department.setDepartmentName(po.getDepartmentName());
        department.setParentId(po.getParentId());
        department.setAncestorPath(po.getAncestorPath());
        department.setDepartmentLevel(po.getDepartmentLevel());
        department.setLeaderUserId(po.getLeaderUserId());
        department.setSortNo(po.getSortNo());
        department.setDepartmentStatus(po.getDepartmentStatus());
        department.setRemark(po.getRemark());
        return department;
    }

    public static RolePO toPO(Role role) {
        if (role == null) {
            return null;
        }
        RolePO po = new RolePO();
        po.setId(role.getId());
        po.setCorpid(role.getCorpid());
        po.setRoleName(role.getRoleName());
        po.setRoleType(role.getRoleType());
        po.setRoleStatus(role.getRoleStatus());
        po.setRemark(role.getRemark());
        return po;
    }

    public static Role toDomain(RolePO po) {
        if (po == null) {
            return null;
        }
        Role role = new Role();
        role.setId(po.getId());
        role.setCorpid(po.getCorpid());
        role.setRoleName(po.getRoleName());
        role.setRoleType(po.getRoleType());
        role.setRoleStatus(po.getRoleStatus());
        role.setRemark(po.getRemark());
        return role;
    }

    public static PermissionPO toPO(Permission permission) {
        if (permission == null) {
            return null;
        }
        PermissionPO po = new PermissionPO();
        po.setId(permission.getId());
        po.setParentId(permission.getParentId());
        po.setPermissionCode(permission.getPermissionCode());
        po.setPermissionName(permission.getPermissionName());
        po.setPermissionType(permission.getPermissionType());
        po.setMenuAlias(permission.getMenuAlias());
        po.setActionCode(permission.getActionCode());
        po.setDataScopeFlag(permission.getDataScopeFlag());
        po.setPermissionStatus(permission.getPermissionStatus());
        po.setRemark(permission.getRemark());
        return po;
    }

    public static Permission toDomain(PermissionPO po) {
        if (po == null) {
            return null;
        }
        Permission permission = new Permission();
        permission.setId(po.getId());
        permission.setParentId(po.getParentId());
        permission.setPermissionCode(po.getPermissionCode());
        permission.setPermissionName(po.getPermissionName());
        permission.setPermissionType(po.getPermissionType());
        permission.setMenuAlias(po.getMenuAlias());
        permission.setActionCode(po.getActionCode());
        permission.setDataScopeFlag(po.getDataScopeFlag());
        permission.setPermissionStatus(po.getPermissionStatus());
        permission.setRemark(po.getRemark());
        return permission;
    }
}
