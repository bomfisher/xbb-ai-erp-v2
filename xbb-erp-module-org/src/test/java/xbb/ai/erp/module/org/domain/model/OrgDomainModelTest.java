package xbb.ai.erp.module.org.domain.model;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.org.domain.enums.DataScopeTypeEnum;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.enums.EnableStatusEnum;
import xbb.ai.erp.module.org.domain.enums.PermissionTypeEnum;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgDomainModelTest {

    @Test
    void should_expose_required_org_enums() {
        assertNotNull(EmploymentStatusEnum.ACTIVE);
        assertNotNull(EmploymentStatusEnum.RESIGNED);
        assertNotNull(EnableStatusEnum.ENABLE);
        assertNotNull(EnableStatusEnum.DISABLE);
        assertNotNull(PermissionTypeEnum.MENU);
        assertNotNull(PermissionTypeEnum.ACTION);
        assertNotNull(DataScopeTypeEnum.SELF);
        assertNotNull(DataScopeTypeEnum.DEPT);
        assertNotNull(DataScopeTypeEnum.DEPT_AND_CHILD);
        assertNotNull(DataScopeTypeEnum.ALL);
    }

    @Test
    void should_define_employee_department_and_scope_fields() throws Exception {
        assertField(Employee.class, "mainDepartmentId", Long.class);
        assertField(Employee.class, "departmentRelations", List.class);
        assertField(Employee.class, "roleRelations", List.class);

        assertField(Department.class, "parentId", Long.class);
        assertField(Department.class, "ancestorPath", String.class);
        assertField(Department.class, "departmentLevel", Integer.class);

        assertField(RolePermissionDataScope.class, "roleId", Long.class);
        assertField(RolePermissionDataScope.class, "permissionId", Long.class);
        assertField(RolePermissionDataScope.class, "dataScopeType", Integer.class);
    }

    @Test
    void should_expose_org_domain_objects() {
        assertNotNull(new Employee());
        assertNotNull(new Department());
        assertNotNull(new Role());
        assertNotNull(new Permission());
        assertNotNull(new EmployeeDepartmentRelation());
        assertNotNull(new EmployeeRoleRelation());
        assertNotNull(new RolePermissionRelation());
        assertNotNull(new RolePermissionDataScope());
    }

    private void assertField(Class<?> targetClass, String fieldName, Class<?> fieldType) throws Exception {
        Field field = targetClass.getDeclaredField(fieldName);
        assertEquals(fieldType, field.getType());
    }
}
