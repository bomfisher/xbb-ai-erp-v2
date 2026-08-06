package xbb.ai.erp.module.org.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.module.org.admin.dto.DepartmentMoveDTO;
import xbb.ai.erp.module.org.admin.dto.DepartmentSaveDTO;
import xbb.ai.erp.module.org.admin.dto.DepartmentTreeDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeListDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeSaveDTO;
import xbb.ai.erp.module.org.admin.dto.PermissionListDTO;
import xbb.ai.erp.module.org.admin.dto.ResignedEmployeeListDTO;
import xbb.ai.erp.module.org.admin.dto.RoleListDTO;
import xbb.ai.erp.module.org.admin.dto.RolePermissionSaveDTO;
import xbb.ai.erp.module.org.admin.dto.RoleSaveDTO;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgAdminControllerContractTest {

    @Test
    void should_expose_org_controller_methods() throws Exception {
        Method employeeList = EmployeeAdminController.class.getMethod("list", EmployeeListDTO.class);
        Method employeeSave = EmployeeAdminController.class.getMethod("save", EmployeeSaveDTO.class);
        Method employeeDetail = EmployeeAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method employeeEnable = EmployeeAdminController.class.getMethod("enable", IdBaseDTO.class);
        Method employeeDisable = EmployeeAdminController.class.getMethod("disable", IdBaseDTO.class);
        Method employeeResign = EmployeeAdminController.class.getMethod("resign", IdBaseDTO.class);
        Method employeeDelete = EmployeeAdminController.class.getMethod("delete", BatchBaseDTO.class);

        Method resignedEmployeeList = ResignedEmployeeAdminController.class.getMethod("list", ResignedEmployeeListDTO.class);
        Method resignedEmployeeDetail = ResignedEmployeeAdminController.class.getMethod("detail", IdBaseDTO.class);

        Method departmentTree = DepartmentAdminController.class.getMethod("tree", DepartmentTreeDTO.class);
        Method departmentDetail = DepartmentAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method departmentSave = DepartmentAdminController.class.getMethod("save", DepartmentSaveDTO.class);
        Method departmentEnable = DepartmentAdminController.class.getMethod("enable", IdBaseDTO.class);
        Method departmentDisable = DepartmentAdminController.class.getMethod("disable", IdBaseDTO.class);
        Method departmentMove = DepartmentAdminController.class.getMethod("move", DepartmentMoveDTO.class);

        Method roleList = RoleAdminController.class.getMethod("list", RoleListDTO.class);
        Method roleDetail = RoleAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method roleSave = RoleAdminController.class.getMethod("save", RoleSaveDTO.class);
        Method roleEnable = RoleAdminController.class.getMethod("enable", IdBaseDTO.class);
        Method roleDisable = RoleAdminController.class.getMethod("disable", IdBaseDTO.class);
        Method rolePermissionDetail = RoleAdminController.class.getMethod("permissionDetail", IdBaseDTO.class);
        Method roleSavePermission = RoleAdminController.class.getMethod("savePermission", RolePermissionSaveDTO.class);

        Method permissionList = PermissionAdminController.class.getMethod("list", PermissionListDTO.class);
        Method permissionDetail = PermissionAdminController.class.getMethod("detail", IdBaseDTO.class);

        assertNotNull(employeeList);
        assertNotNull(employeeSave);
        assertNotNull(employeeDetail);
        assertNotNull(employeeEnable);
        assertNotNull(employeeDisable);
        assertNotNull(employeeResign);
        assertNotNull(employeeDelete);
        assertNotNull(resignedEmployeeList);
        assertNotNull(resignedEmployeeDetail);
        assertNotNull(departmentTree);
        assertNotNull(departmentDetail);
        assertNotNull(departmentSave);
        assertNotNull(departmentEnable);
        assertNotNull(departmentDisable);
        assertNotNull(departmentMove);
        assertNotNull(roleList);
        assertNotNull(roleDetail);
        assertNotNull(roleSave);
        assertNotNull(roleEnable);
        assertNotNull(roleDisable);
        assertNotNull(rolePermissionDetail);
        assertNotNull(roleSavePermission);
        assertNotNull(permissionList);
        assertNotNull(permissionDetail);
    }
}
