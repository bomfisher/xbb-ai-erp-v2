package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgRepositorySignatureTest {

    @Test
    void should_declare_employee_repository_signatures() throws Exception {
        Method insert = EmployeeRepositoryImpl.class.getMethod("insert", xbb.ai.erp.module.org.domain.model.Employee.class);
        Method update = EmployeeRepositoryImpl.class.getMethod("update", xbb.ai.erp.module.org.domain.model.Employee.class);
        Method list = EmployeeRepositoryImpl.class.getMethod("list", String.class, String.class, Integer.class, Integer.class, Long.class);
        Method replaceDepartmentRelations = EmployeeRepositoryImpl.class.getMethod("replaceDepartmentRelations", String.class, String.class, java.util.List.class);
        Method replaceRoleRelations = EmployeeRepositoryImpl.class.getMethod("replaceRoleRelations", String.class, String.class, java.util.List.class);
        Method findById = EmployeeRepositoryImpl.class.getMethod("findById", String.class, String.class);
        Method countActiveByMainDepartment = EmployeeRepositoryImpl.class.getMethod("countActiveByMainDepartment", String.class, Long.class);
        assertNotNull(insert);
        assertNotNull(update);
        assertNotNull(list);
        assertNotNull(replaceDepartmentRelations);
        assertNotNull(replaceRoleRelations);
        assertNotNull(findById);
        assertNotNull(countActiveByMainDepartment);
    }

    @Test
    void should_declare_department_repository_signatures() throws Exception {
        Method insert = DepartmentRepositoryImpl.class.getMethod("insert", xbb.ai.erp.module.org.domain.model.Department.class);
        Method list = DepartmentRepositoryImpl.class.getMethod("list", String.class, Integer.class);
        Method listByIds = DepartmentRepositoryImpl.class.getMethod("listByIds", String.class, java.util.List.class);
        Method findById = DepartmentRepositoryImpl.class.getMethod("findById", String.class, Long.class);
        assertNotNull(insert);
        assertNotNull(list);
        assertNotNull(listByIds);
        assertNotNull(findById);
    }

    @Test
    void should_declare_role_repository_signatures() throws Exception {
        Method insert = RoleRepositoryImpl.class.getMethod("insert", xbb.ai.erp.module.org.domain.model.Role.class);
        Method list = RoleRepositoryImpl.class.getMethod("list", String.class, String.class, Integer.class);
        Method listByIds = RoleRepositoryImpl.class.getMethod("listByIds", String.class, java.util.List.class);
        Method replacePermissionRelations = RoleRepositoryImpl.class.getMethod("replacePermissionRelations", String.class, Long.class, java.util.List.class);
        Method replacePermissionDataScopes = RoleRepositoryImpl.class.getMethod("replacePermissionDataScopes", String.class, Long.class, java.util.List.class);
        Method listPermissionRelations = RoleRepositoryImpl.class.getMethod("listPermissionRelations", String.class, Long.class);
        Method listPermissionDataScopes = RoleRepositoryImpl.class.getMethod("listPermissionDataScopes", String.class, Long.class);
        Method findById = RoleRepositoryImpl.class.getMethod("findById", String.class, Long.class);
        assertNotNull(insert);
        assertNotNull(list);
        assertNotNull(listByIds);
        assertNotNull(replacePermissionRelations);
        assertNotNull(replacePermissionDataScopes);
        assertNotNull(listPermissionRelations);
        assertNotNull(listPermissionDataScopes);
        assertNotNull(findById);
    }

    @Test
    void should_declare_permission_repository_signatures() throws Exception {
        Method insert = PermissionRepositoryImpl.class.getMethod("insert", xbb.ai.erp.module.org.domain.model.Permission.class);
        Method list = PermissionRepositoryImpl.class.getMethod("list", String.class);
        Method filteredList = PermissionRepositoryImpl.class.getMethod("list", String.class, String.class, Integer.class, Integer.class);
        Method listByIds = PermissionRepositoryImpl.class.getMethod("listByIds", String.class, java.util.List.class);
        Method findById = PermissionRepositoryImpl.class.getMethod("findById", String.class, Long.class);
        assertNotNull(insert);
        assertNotNull(list);
        assertNotNull(filteredList);
        assertNotNull(listByIds);
        assertNotNull(findById);
    }
}
