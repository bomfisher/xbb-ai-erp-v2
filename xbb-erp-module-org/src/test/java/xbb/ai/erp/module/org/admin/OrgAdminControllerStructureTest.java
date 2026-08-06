package xbb.ai.erp.module.org.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgAdminControllerStructureTest {

    @Test
    void should_define_org_endpoints() throws Exception {
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

    @Test
    void should_wrap_query_result_with_expected_generic_vo() throws Exception {
        Method employeeList = EmployeeAdminController.class.getMethod("list", EmployeeListDTO.class);
        ParameterizedType employeeResultType = (ParameterizedType) employeeList.getGenericReturnType();
        assertEquals(xbb.ai.erp.base.common.vo.ListBaseVO.class, rawTypeOf(employeeResultType.getActualTypeArguments()[0]));

        Method resignedEmployeeList = ResignedEmployeeAdminController.class.getMethod("list", ResignedEmployeeListDTO.class);
        ParameterizedType resignedEmployeeResultType = (ParameterizedType) resignedEmployeeList.getGenericReturnType();
        assertEquals(xbb.ai.erp.base.common.vo.ListBaseVO.class, rawTypeOf(resignedEmployeeResultType.getActualTypeArguments()[0]));

        Method departmentTree = DepartmentAdminController.class.getMethod("tree", DepartmentTreeDTO.class);
        ParameterizedType departmentTreeResultType = (ParameterizedType) departmentTree.getGenericReturnType();
        assertEquals(xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler.DepartmentTreeResultVO.class, departmentTreeResultType.getActualTypeArguments()[0]);

        Method roleList = RoleAdminController.class.getMethod("list", RoleListDTO.class);
        ParameterizedType roleListResultType = (ParameterizedType) roleList.getGenericReturnType();
        assertEquals(xbb.ai.erp.base.common.vo.ListBaseVO.class, rawTypeOf(roleListResultType.getActualTypeArguments()[0]));

        Method rolePermissionDetail = RoleAdminController.class.getMethod("permissionDetail", IdBaseDTO.class);
        ParameterizedType rolePermissionDetailResultType = (ParameterizedType) rolePermissionDetail.getGenericReturnType();
        assertEquals(xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler.RolePermissionDetailResultVO.class, rolePermissionDetailResultType.getActualTypeArguments()[0]);
    }

    @Test
    void should_wrap_delete_result_with_base_vo() throws Exception {
        Method employeeDelete = EmployeeAdminController.class.getMethod("delete", BatchBaseDTO.class);
        ParameterizedType resultType = (ParameterizedType) employeeDelete.getGenericReturnType();
        assertEquals(BaseVO.class, resultType.getActualTypeArguments()[0]);
    }

    private Class<?> rawTypeOf(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getRawType();
        }
        return (Class<?>) type;
    }
}
