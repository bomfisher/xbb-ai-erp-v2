# Org Master Data Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build `xbb-erp-module-org` for employee, department, role, permission dictionary read models, employee relations, and role authorization in the existing DDD style.

**Architecture:** Add one new business module with the same layer shape as the current customer/product modules: `admin` for HTTP entrypoints, `application` for orchestration, `domain` for core rules, and `infrastructure.persistence` for PO/mapper/repository implementations. Keep permission dictionary platform-wide, keep tenant data tenant-scoped, and let employee saves own department and role relation updates.

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、MyBatis-Plus 3.5.7、JUnit 5、Lombok、BaseDTO/ResultVO/BaseVO、BizException

## Global Constraints

- 对话与文档语境保持中文。
- 项目架构遵循 DDD，详细约束见 `docs/base/项目业务module导航.md` 和 `docs/base/项目顶部和底部module导航.md`。
- 模块名固定为 `xbb-erp-module-org`，Java 根包固定为 `xbb.ai.erp.module.org`。
- 权限字典采用平台统一主数据模型，租户角色只引用公共权限点。
- 员工采用 `1` 个主部门 + 多个兼职部门模型。
- 员工保存时直接维护角色挂载。
- 角色菜单级数据权限固定为 `SELF`、`DEPT`、`DEPT_AND_CHILD`、`ALL`。
- 部门不做物理删除，仅允许停用。
- 非脚本接口入参 DTO 统一继承 `BaseDTO`。
- 接口参数与返回统一使用 `ResultVO.success()` 包装；无返回主体时使用 `BaseVO`。
- 直接对接数据库的对象统一使用 `PO` 后缀，状态/是否字段统一使用 `Integer`。
- 所有主动捕获的报错、业务主动抛错统一使用 `BizException`。
- 代码修改在当前分支完成，不创建 worktree。
- 本次计划不包含账号管理、登录认证协议接入、权限快照装载、跨模块 `facade` 输出、字段级权限、审批组织规则、菜单主数据维护。

---

### Task 1: Add org module skeleton and Maven wiring

**Files:**
- Modify: `pom.xml`
- Create: `xbb-erp-module-org/pom.xml`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/dto/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/vo/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/assembler/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/pojo/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/enums/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/repository/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/convertor/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/.gitkeep`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository/.gitkeep`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/ModuleStructureTest.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/admin/OrgAdminControllerStructureTest.java`

**Interfaces:**
- Consumes: parent Maven reactor and the current project package layout
- Produces: a buildable `xbb-erp-module-org` module with a verified directory skeleton and controller naming contract

- [ ] **Step 1: Write the failing module structure test**

```java
package xbb.ai.erp.module.org;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_org_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/admin")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/application/service")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/domain/repository")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository")));
    }
}
```

- [ ] **Step 2: Run the test and confirm it fails**

Run: `mvn -pl xbb-erp-module-org -Dtest=ModuleStructureTest test`
Expected: FAIL because the module does not exist yet.

- [ ] **Step 3: Add the Maven module and create the package skeleton**

`pom.xml` add:

```xml
<module>xbb-erp-module-org</module>
```

`xbb-erp-module-org/pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.bomfish</groupId>
        <artifactId>xbb-erp-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>xbb-erp-module-org</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-base-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-base-persistence</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-base-web</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-base-cache</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-base-idgen</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-module-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Create the `.gitkeep` files listed above.

- [ ] **Step 4: Add the controller contract test**

```java
package xbb.ai.erp.module.org.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgAdminControllerStructureTest {

    @Test
    void should_define_org_endpoints() throws Exception {
        Method employeeList = EmployeeAdminController.class.getMethod("list", BaseDTO.class);
        Method employeeSave = EmployeeAdminController.class.getMethod("save", BaseDTO.class);
        Method employeeDetail = EmployeeAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method employeeResign = EmployeeAdminController.class.getMethod("resign", IdBaseDTO.class);
        Method employeeDelete = EmployeeAdminController.class.getMethod("delete", BatchBaseDTO.class);

        Method departmentTree = DepartmentAdminController.class.getMethod("tree", BaseDTO.class);
        Method departmentSave = DepartmentAdminController.class.getMethod("save", BaseDTO.class);
        Method departmentMove = DepartmentAdminController.class.getMethod("move", BaseDTO.class);

        Method roleList = RoleAdminController.class.getMethod("list", BaseDTO.class);
        Method roleSave = RoleAdminController.class.getMethod("save", BaseDTO.class);
        Method roleSavePermission = RoleAdminController.class.getMethod("savePermission", BaseDTO.class);

        Method permissionList = PermissionAdminController.class.getMethod("list", BaseDTO.class);

        assertNotNull(employeeList);
        assertNotNull(employeeSave);
        assertNotNull(employeeDetail);
        assertNotNull(employeeResign);
        assertNotNull(employeeDelete);
        assertNotNull(departmentTree);
        assertNotNull(departmentSave);
        assertNotNull(departmentMove);
        assertNotNull(roleList);
        assertNotNull(roleSave);
        assertNotNull(roleSavePermission);
        assertNotNull(permissionList);
    }

    @Test
    void should_depend_on_app_service_contracts() throws Exception {
        Field field = EmployeeAdminController.class.getDeclaredField("employeeAdminAppService");
        assertEquals(Class.forName("xbb.ai.erp.module.org.application.service.EmployeeAdminAppService"), field.getType());
    }

    @Test
    void should_wrap_delete_result_with_base_vo() throws Exception {
        Method employeeDelete = EmployeeAdminController.class.getMethod("delete", BatchBaseDTO.class);
        ParameterizedType resultType = (ParameterizedType) employeeDelete.getGenericReturnType();
        assertEquals(BaseVO.class, resultType.getActualTypeArguments()[0]);
    }
}
```

- [ ] **Step 5: Run module tests again and confirm pass**

Run: `mvn -pl xbb-erp-module-org -Dtest=ModuleStructureTest,OrgAdminControllerStructureTest test`
Expected: PASS.

- [ ] **Step 6: Commit this slice**

```bash
git add pom.xml xbb-erp-module-org

git commit -m "feat: initialize org module skeleton"
```

### Task 2: Build org domain model and enums

**Files:**
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/enums/EmploymentStatusEnum.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/enums/EnableStatusEnum.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/enums/PermissionTypeEnum.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/enums/DataScopeTypeEnum.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/Employee.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/Department.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/Role.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/Permission.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/EmployeeDepartmentRelation.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/EmployeeRoleRelation.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/RolePermissionRelation.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain/model/RolePermissionDataScope.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/domain/model/OrgDomainModelTest.java`

**Interfaces:**
- Consumes: `BaseEntity` conventions, the spec’s object and status rules
- Produces: domain objects and enums that later tasks use for PO mapping and validation

- [ ] **Step 1: Write failing model tests**

Test the following facts explicitly:

- `EmploymentStatusEnum` exposes `ACTIVE` and `RESIGNED`
- `EnableStatusEnum` exposes `ENABLE` and `DISABLE`
- `PermissionTypeEnum` exposes `MENU` and `ACTION`
- `DataScopeTypeEnum` exposes `SELF`, `DEPT`, `DEPT_AND_CHILD`, `ALL`
- `Employee` has `mainDepartmentId` and relation collections
- `Department` has `parentId`, `ancestorPath`, `departmentLevel`
- `RolePermissionDataScope` stores `roleId`, `permissionId`, `dataScopeType`

```java
package xbb.ai.erp.module.org.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgDomainModelTest {

    @Test
    void should_expose_org_domain_objects() {
        assertNotNull(EmploymentStatusEnum.ACTIVE);
        assertNotNull(EnableStatusEnum.ENABLE);
        assertNotNull(PermissionTypeEnum.MENU);
        assertNotNull(DataScopeTypeEnum.SELF);
        assertNotNull(new Employee());
        assertNotNull(new Department());
        assertNotNull(new Role());
        assertNotNull(new Permission());
        assertNotNull(new EmployeeDepartmentRelation());
        assertNotNull(new EmployeeRoleRelation());
        assertNotNull(new RolePermissionRelation());
        assertNotNull(new RolePermissionDataScope());
    }
}
```

- [ ] **Step 2: Run the test and confirm it fails**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgDomainModelTest test`
Expected: FAIL because none of the classes exist yet.

- [ ] **Step 3: Implement minimal enums and models**

Create the enums as Lombok-backed Java enums and the models as Lombok-backed POJOs. Keep state fields as `Integer` where the spec requires numeric status/flag storage.

Example enum shape:

```java
package xbb.ai.erp.module.org.domain.enums;

import lombok.Getter;

@Getter
public enum EmploymentStatusEnum {
    ACTIVE(1, "在职"),
    RESIGNED(2, "离职");

    private final Integer code;
    private final String name;

    EmploymentStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
```

Example domain shape:

```java
package xbb.ai.erp.module.org.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Employee {
    private Long id;
    private String corpid;
    private String userCode;
    private String userName;
    private String mobile;
    private String email;
    private Long mainDepartmentId;
    private Integer employmentStatus;
    private Integer enableStatus;
    private List<EmployeeDepartmentRelation> departmentRelations;
    private List<EmployeeRoleRelation> roleRelations;
}
```

- [ ] **Step 4: Re-run model tests and confirm pass**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgDomainModelTest test`
Expected: PASS.

- [ ] **Step 5: Commit this slice**

```bash
git add xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/domain

git commit -m "feat: add org domain model"
```

### Task 3: Add PO, mapper, convertor, and repository layer

**Files:**
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/EmployeePO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/DepartmentPO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/RolePO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/PermissionPO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/EmployeeDepartmentRelationPO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/EmployeeRoleRelationPO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/RolePermissionRelationPO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po/RolePermissionDataScopePO.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/EmployeeMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/DepartmentMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/RoleMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/PermissionMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/EmployeeDepartmentRelationMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/EmployeeRoleRelationMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/RolePermissionRelationMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper/RolePermissionDataScopeMapper.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/convertor/OrgConvertor.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository/EmployeeRepositoryImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository/DepartmentRepositoryImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository/RoleRepositoryImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository/PermissionRepositoryImpl.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/infrastructure/persistence/po/OrgPoStructureTest.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/infrastructure/persistence/repository/OrgRepositorySignatureTest.java`

**Interfaces:**
- Consumes: domain models from Task 2
- Produces: PO/mapper/repository implementations that later application services can call

- [ ] **Step 1: Write failing PO structure tests**

```java
package xbb.ai.erp.module.org.infrastructure.persistence.po;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrgPoStructureTest {

    @Test
    void should_define_employee_po_fields() throws Exception {
        Field corpid = EmployeePO.class.getDeclaredField("corpid");
        Field mainDepartmentId = EmployeePO.class.getDeclaredField("mainDepartmentId");
        Field employmentStatus = EmployeePO.class.getDeclaredField("employmentStatus");

        assertEquals(String.class, corpid.getType());
        assertEquals(Long.class, mainDepartmentId.getType());
        assertEquals(Integer.class, employmentStatus.getType());
    }
}
```

```java
package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgRepositorySignatureTest {

    @Test
    void should_declare_repository_signatures() throws Exception {
        Method insert = EmployeeRepositoryImpl.class.getMethod("insert", xbb.ai.erp.module.org.domain.model.Employee.class);
        Method findById = EmployeeRepositoryImpl.class.getMethod("findById", String.class, Long.class);
        assertNotNull(insert);
        assertNotNull(findById);
    }
}
```

- [ ] **Step 2: Run the tests and confirm they fail**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgPoStructureTest,OrgRepositorySignatureTest test`
Expected: FAIL because the PO and repository classes do not exist yet.

- [ ] **Step 3: Implement PO, mapper, convertor, and repository classes**

Use the same `PO + Mapper + Convertor + RepositoryImpl` pattern already used by `customer` and `product`.

Example repository shape:

```java
package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.domain.repository.EmployeeRepository;
import xbb.ai.erp.module.org.infrastructure.persistence.convertor.OrgConvertor;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.EmployeeMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeePO;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeMapper employeeMapper;

    @Override
    public void insert(Employee employee) {
        EmployeePO po = OrgConvertor.toPO(employee);
        employeeMapper.insert(po);
        employee.setId(po.getId());
    }
}
```

- [ ] **Step 4: Re-run the repository tests and confirm pass**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgPoStructureTest,OrgRepositorySignatureTest test`
Expected: PASS.

- [ ] **Step 5: Commit this slice**

```bash
git add xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/infrastructure

git commit -m "feat: add org persistence layer"
```

### Task 4: Add application services for employee, department, role, and permission reads

**Files:**
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/EmployeeAdminAppService.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/DepartmentAdminAppService.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/RoleAdminAppService.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/PermissionAdminAppService.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/EmployeeAdminAppServiceImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/DepartmentAdminAppServiceImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/RoleAdminAppServiceImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/PermissionAdminAppServiceImpl.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/assembler/OrgAdminAssembler.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/pojo/EmployeeSavePojo.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/pojo/RolePermissionSavePojo.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/application/service/EmployeeAdminAppServiceImplTest.java`

**Interfaces:**
- Consumes: repositories from Task 3
- Produces: transaction-bound application services ready for controller wiring

- [ ] **Step 1: Write failing app service tests**

Test the main orchestration behaviors explicitly:

- employee save calls employee repository, relation repositories, and returns saved ids
- role permission save overwrites existing relations in one call path
- permission list is read-only and comes from the permission repository

- [ ] **Step 2: Run tests and confirm they fail**

Run: `mvn -pl xbb-erp-module-org -Dtest=EmployeeAdminAppServiceImplTest test`
Expected: FAIL because the service classes do not exist yet.

- [ ] **Step 3: Implement the minimal orchestration layer**

Keep the logic thin and focused:

- load and validate tenant-scoped entities
- call repository methods in a single use-case flow
- assemble response DTO/VO values
- use `BizException` for domain violations

- [ ] **Step 4: Re-run the app service tests and confirm pass**

Run: `mvn -pl xbb-erp-module-org -Dtest=EmployeeAdminAppServiceImplTest test`
Expected: PASS.

- [ ] **Step 5: Commit this slice**

```bash
git add xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application

git commit -m "feat: add org application services"
```

### Task 5: Add admin controllers and request/response contracts

**Files:**
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/EmployeeAdminController.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/ResignedEmployeeAdminController.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/DepartmentAdminController.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/RoleAdminController.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/PermissionAdminController.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/dto/*.java`
- Create: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin/vo/*.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/admin/OrgAdminControllerContractTest.java`

**Interfaces:**
- Consumes: application services from Task 4
- Produces: stable HTTP contracts under `/erp/v1/org/**`

- [ ] **Step 1: Write failing controller contract tests**

Verify these methods exist and are wired to the expected types:

- employee list/save/detail/resign/enable/disable/delete
- resigned employee list/detail
- department tree/detail/save/enable/disable/move
- role list/detail/save/enable/disable/permissionDetail/savePermission
- permission list/detail

Example:

```java
package xbb.ai.erp.module.org.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrgAdminControllerContractTest {

    @Test
    void should_expose_org_controller_methods() throws Exception {
        Method save = EmployeeAdminController.class.getMethod("save", BaseDTO.class);
        Method resign = EmployeeAdminController.class.getMethod("resign", IdBaseDTO.class);
        Method tree = DepartmentAdminController.class.getMethod("tree", BaseDTO.class);
        Method savePermission = RoleAdminController.class.getMethod("savePermission", BaseDTO.class);

        assertNotNull(save);
        assertNotNull(resign);
        assertNotNull(tree);
        assertNotNull(savePermission);
    }
}
```

- [ ] **Step 2: Run the controller test and confirm it fails**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgAdminControllerContractTest test`
Expected: FAIL because the controllers do not exist yet.

- [ ] **Step 3: Implement controllers and DTO/VO classes**

Keep the controller layer thin:

- bind DTOs only
- call one application service per endpoint
- wrap every response with `ResultVO.success()`
- use `BaseVO` for endpoints without payload

- [ ] **Step 4: Re-run the controller test and confirm pass**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgAdminControllerContractTest test`
Expected: PASS.

- [ ] **Step 5: Commit this slice**

```bash
git add xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/admin

git commit -m "feat: add org admin controllers"
```

### Task 6: Add service-level validation and repository coverage for the core rules

**Files:**
- Modify: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/EmployeeAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/DepartmentAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/RoleAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl/PermissionAdminAppServiceImpl.java`
- Create: `xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/application/service/OrgBusinessRuleTest.java`

**Interfaces:**
- Consumes: the controllers, services, and repositories from Tasks 3-5
- Produces: validated business behavior for the spec rules around main department, resign flow, department stop rules, and read-only permission dictionary

- [ ] **Step 1: Write failing business rule tests**

Test the specific cases from the spec:

- employee must have exactly one main department
- main department must appear in the employee department relations
- resigned employee cannot be assigned roles or departments
- department cannot be disabled if an active employee uses it as main department
- permission dictionary cannot be saved from the admin side

- [ ] **Step 2: Run the rule tests and confirm they fail**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgBusinessRuleTest test`
Expected: FAIL because rule enforcement is still incomplete.

- [ ] **Step 3: Add the missing validations**

Use `BizException` for every business violation and keep each rule close to the service that owns it.

- [ ] **Step 4: Re-run the rule tests and confirm pass**

Run: `mvn -pl xbb-erp-module-org -Dtest=OrgBusinessRuleTest test`
Expected: PASS.

- [ ] **Step 5: Commit this slice**

```bash
git add xbb-erp-module-org/src/main/java/xbb/ai/erp/module/org/application/service/impl \
        xbb-erp-module-org/src/test/java/xbb/ai/erp/module/org/application/service/OrgBusinessRuleTest.java

git commit -m "fix: enforce org master data rules"
```

## Validation Checklist

- `mvn -pl xbb-erp-module-org -Dtest=ModuleStructureTest,OrgAdminControllerStructureTest,OrgDomainModelTest,OrgPoStructureTest,OrgRepositorySignatureTest,EmployeeAdminAppServiceImplTest,OrgAdminControllerContractTest,OrgBusinessRuleTest test`
- `mvn -pl xbb-erp-module-org test`
- `mvn -pl xbb-erp-module-org -DskipTests package`

## Coverage Map

- Module skeleton and Maven wiring → Task 1
- Domain objects and enums → Task 2
- PO / mapper / convertor / repository layer → Task 3
- Application orchestration and validation → Tasks 4 and 6
- Admin HTTP contracts and DTO/VOs → Task 5
- Spec rules for platform-wide permission dictionary, main department model, data scope, and soft-stop department behavior → Tasks 2, 4, 5, and 6

## Gaps to watch while implementing

- Keep permission dictionary read-only even if the admin controllers already exist.
- Keep all tenant-scoped data keyed by `corpid`.
- Keep `Integer` status fields instead of booleans.
- Keep the controller surface aligned with the existing `customer` and `product` controller style.
- Do not add account/login/facade behavior in this plan.
