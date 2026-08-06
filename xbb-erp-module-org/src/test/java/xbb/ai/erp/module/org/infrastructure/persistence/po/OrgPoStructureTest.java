package xbb.ai.erp.module.org.infrastructure.persistence.po;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrgPoStructureTest {

    @Test
    void should_define_employee_po_fields() throws Exception {
        Field id = EmployeePO.class.getDeclaredField("id");
        Field corpid = EmployeePO.class.getDeclaredField("corpid");
        Field mainDepartmentId = EmployeePO.class.getDeclaredField("mainDepartmentId");
        Field employmentStatus = EmployeePO.class.getDeclaredField("employmentStatus");

        assertEquals(String.class, id.getType());
        assertEquals(String.class, corpid.getType());
        assertEquals(Long.class, mainDepartmentId.getType());
        assertEquals(Integer.class, employmentStatus.getType());
    }

    @Test
    void should_define_department_po_fields() throws Exception {
        Field id = DepartmentPO.class.getDeclaredField("id");
        Field parentId = DepartmentPO.class.getDeclaredField("parentId");
        Field departmentLevel = DepartmentPO.class.getDeclaredField("departmentLevel");

        assertEquals(Long.class, id.getType());
        assertEquals(Long.class, parentId.getType());
        assertEquals(Integer.class, departmentLevel.getType());
    }

    @Test
    void should_define_role_po_fields() throws Exception {
        Field id = RolePO.class.getDeclaredField("id");
        Field enableStatus = RolePO.class.getDeclaredField("enableStatus");

        assertEquals(Long.class, id.getType());
        assertEquals(Integer.class, enableStatus.getType());
    }

    @Test
    void should_define_permission_po_fields() throws Exception {
        Field id = PermissionPO.class.getDeclaredField("id");
        Field permissionType = PermissionPO.class.getDeclaredField("permissionType");
        Field parentId = PermissionPO.class.getDeclaredField("parentId");

        assertEquals(Long.class, id.getType());
        assertEquals(Integer.class, permissionType.getType());
        assertEquals(Long.class, parentId.getType());
    }
}
