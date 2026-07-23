# Customer Module Initialization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 初始化 `xbb-erp-module-customer` 模块，覆盖 `customer`、`customer_contact`、`customer_address` 三张表的最小 DDD + MyBatis-Plus 后端骨架与建表 SQL。

**Architecture:** 新模块仅包含 `domain` 与 `infrastructure.persistence` 两层。领域层按表独立建模，每张表各自拥有 `Domain + QueryPojo + Repository`；持久化层按 `PO + Mapper + Convertor + RepositoryImpl` 落地，`PO` 继承 `BaseEntity`，所有查询/修改/删除强制带 `corpid`。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、MyBatis-Plus 3.5.7、JUnit 5、Testcontainers（仅在需要容器型验证时使用）

## Global Constraints

- 对话与文档语境保持中文。
- 项目架构遵循 DDD，详细约束见 `docs/base/11-进销存系统工程结构与分层架构设计文档.md`。
- 模块名固定为 `xbb-erp-module-customer`，Java 根包固定为 `xbb.ai.erp.module.customer`。
- 仅实现最小范围：`customer`、`customer_contact`、`customer_address` 三张表。
- 不创建 `worktree`，直接在当前分支实现。
- 直接对接数据库的对象统一使用 `PO` 后缀。
- 直接对接数据库的状态/是否字段统一使用 `Integer`，不要使用布尔值。
- POJO 后缀规范：前端入参 `DTO`、接口出参 `VO`、其余中转对象 `Pojo`。
- 所有 `userId` / 员工 ID 类型统一使用 `String`。
- getter / setter 使用 Lombok。
- 本次不实现 `controller`、`app service`、`facade`、默认项切换、主表默认项回写、附件、审计、幂等、引用摘要、状态流转。
- 逻辑删除仅修改 `del`，不做物理删除。
- 所有查询、修改、删除都必须显式带 `corpid`。
- SQL 输出位置固定为 `docs/sql/2026-07-22-init-customer-module.sql`。

---

### Task 1: 建立模块骨架并接入父工程

**Files:**
- Create: `xbb-erp-module-customer/pom.xml`
- Modify: `pom.xml:12`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/.gitkeep`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/.gitkeep`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/.gitkeep`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/.gitkeep`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/.gitkeep`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/.gitkeep`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/.gitkeep`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/ModuleStructureTest.java`

**Interfaces:**
- Consumes: `org.bomfish:xbb-erp-parent`、`org.bomfish:xbb-erp-base-persistence`、`org.bomfish:xbb-erp-base-common`
- Produces: `xbb-erp-module-customer` 模块可被 Maven Reactor 识别；最小目录骨架可供后续任务写入类文件

- [ ] **Step 1: 写一个失败的模块结构测试**

```java
package xbb.ai.erp.module.customer;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_customer_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/domain/pojo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/domain/repository")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository")));
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=ModuleStructureTest test`
Expected: FAIL，提示模块不存在或测试目标模块无法解析。

- [ ] **Step 3: 修改父工程并创建模块 `pom.xml`**

在 `pom.xml` 的 `<modules>` 中加入：

```xml
<module>xbb-erp-module-customer</module>
```

创建 `xbb-erp-module-customer/pom.xml`：

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

    <artifactId>xbb-erp-module-customer</artifactId>

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
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

- [ ] **Step 4: 创建最小包结构**

创建这些目录与占位文件：

```text
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/.gitkeep
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/.gitkeep
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/.gitkeep
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/.gitkeep
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/.gitkeep
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/.gitkeep
xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/.gitkeep
```

- [ ] **Step 5: 重新运行结构测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=ModuleStructureTest test`
Expected: PASS

- [ ] **Step 6: 提交当前任务**

```bash
git add pom.xml xbb-erp-module-customer/pom.xml xbb-erp-module-customer/src

git commit -m "feat: initialize customer module skeleton"
```

### Task 2: 对齐基础持久化自动填充字段

**Files:**
- Modify: `xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/handler/AuditMetaObjectHandler.java`
- Modify: `xbb-erp-base-persistence/src/test/java/xbb/ai/erp/base/persistence/entity/BaseEntityTest.java`

**Interfaces:**
- Consumes: `xbb.ai.erp.base.persistence.entity.BaseEntity`
- Produces: `AuditMetaObjectHandler` 对 `addTime`、`updateTime`、`del` 的自动填充能力，供三张 PO 直接复用

- [ ] **Step 1: 写一个失败的自动填充测试**

将 `BaseEntityTest` 改为：

```java
package xbb.ai.erp.base.persistence.entity;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.persistence.handler.AuditMetaObjectHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BaseEntityTest {

    @Test
    void should_fill_add_time_update_time_and_del() {
        BaseEntity entity = new BaseEntity();
        AuditMetaObjectHandler handler = new AuditMetaObjectHandler();
        MetaObject metaObject = SystemMetaObject.forObject(entity);

        handler.insertFill(metaObject);

        assertNotNull(entity.getAddTime());
        assertNotNull(entity.getUpdateTime());
        assertEquals(0, entity.getDel());
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-base-persistence -Dtest=BaseEntityTest test`
Expected: FAIL，字段名不匹配导致 `addTime` / `del` 未填充。

- [ ] **Step 3: 以最小修改修正自动填充实现**

将 `AuditMetaObjectHandler` 改为：

```java
package xbb.ai.erp.base.persistence.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        long now = System.currentTimeMillis();
        strictInsertFill(metaObject, "addTime", Long.class, now);
        strictInsertFill(metaObject, "updateTime", Long.class, now);
        strictInsertFill(metaObject, "del", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateTime", Long.class, System.currentTimeMillis());
    }
}
```

- [ ] **Step 4: 重新运行测试并确认通过**

Run: `mvn -pl xbb-erp-base-persistence -Dtest=BaseEntityTest test`
Expected: PASS

- [ ] **Step 5: 提交当前任务**

```bash
git add xbb-erp-base-persistence/src/main/java/xbb/ai/erp/base/persistence/handler/AuditMetaObjectHandler.java \
        xbb-erp-base-persistence/src/test/java/xbb/ai/erp/base/persistence/entity/BaseEntityTest.java

git commit -m "fix: align persistence audit fields with base entity"
```

### Task 3: 建立领域对象与查询对象

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/Customer.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/CustomerContact.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/CustomerAddress.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/CustomerQueryPojo.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/CustomerContactQueryPojo.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/CustomerAddressQueryPojo.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/model/CustomerModelTest.java`

**Interfaces:**
- Consumes: `BaseEntity` 字段约定、设计文档中的三张表字段定义
- Produces: 领域对象与查询对象，供仓储接口、转换器、仓储实现复用

- [ ] **Step 1: 写一个失败的领域模型测试**

```java
package xbb.ai.erp.module.customer.domain.model;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerContactQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerModelTest {

    @Test
    void should_hold_customer_fields() {
        Customer customer = new Customer();
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");

        assertEquals("corp-001", customer.getCorpid());
        assertEquals("CUST-001", customer.getCustomerCode());
        assertEquals("杭州客户", customer.getCustomerName());
    }

    @Test
    void should_hold_contact_and_address_query_fields() {
        CustomerContactQueryPojo contactQuery = new CustomerContactQueryPojo();
        contactQuery.setCorpid("corp-001");
        contactQuery.setDefaultFlag(1);

        CustomerAddressQueryPojo addressQuery = new CustomerAddressQueryPojo();
        addressQuery.setCorpid("corp-001");
        addressQuery.setAddressType("DELIVERY");

        CustomerQueryPojo customerQuery = new CustomerQueryPojo();
        customerQuery.setCorpid("corp-001");
        customerQuery.setCustomerCode("CUST-001");

        assertEquals(1, contactQuery.getDefaultFlag());
        assertEquals("DELIVERY", addressQuery.getAddressType());
        assertEquals("CUST-001", customerQuery.getCustomerCode());
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerModelTest test`
Expected: FAIL，类不存在。

- [ ] **Step 3: 创建三个领域对象**

`Customer.java`：

```java
package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String corpid;
    private String customerCode;
    private String customerName;
    private String customerShortName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String ownerSalesNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
```

`CustomerContact.java`：

```java
package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class CustomerContact {
    private Long id;
    private String corpid;
    private Long customerId;
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String positionName;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
```

`CustomerAddress.java`：

```java
package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class CustomerAddress {
    private Long id;
    private String corpid;
    private Long customerId;
    private String addressType;
    private String receiverName;
    private String receiverMobile;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String detailAddress;
    private String postalCode;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
```

- [ ] **Step 4: 创建三个查询对象**

`CustomerQueryPojo.java`：

```java
package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerQueryPojo {
    private String corpid;
    private Long id;
    private String customerCode;
    private String customerName;
    private String customerCategory;
    private String bizStatus;
    private String refStatus;
    private String ownerSalesId;
}
```

`CustomerContactQueryPojo.java`：

```java
package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerContactQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private String contactName;
    private String mobile;
    private String bizStatus;
    private Integer defaultFlag;
}
```

`CustomerAddressQueryPojo.java`：

```java
package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerAddressQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private String addressType;
    private String receiverName;
    private String receiverMobile;
    private String bizStatus;
    private Integer defaultFlag;
}
```

- [ ] **Step 5: 重新运行测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerModelTest test`
Expected: PASS

- [ ] **Step 6: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/model/CustomerModelTest.java

git commit -m "feat: add customer domain models and query pojos"
```

### Task 4: 定义三张表的仓储接口

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepository.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerContactRepository.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerAddressRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepositorySignatureTest.java`

**Interfaces:**
- Consumes: `Customer`、`CustomerContact`、`CustomerAddress`、三个 `QueryPojo`
- Produces: 统一的仓储接口签名，供后续 `RepositoryImpl` 实现

- [ ] **Step 1: 写一个失败的仓储接口签名测试**

```java
package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerRepositorySignatureTest {

    @Test
    void should_declare_customer_repository_methods() throws Exception {
        Method save = CustomerRepository.class.getMethod("save", xbb.ai.erp.module.customer.domain.model.Customer.class);
        Method findById = CustomerRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerRepository.class.getMethod("findByCondition", xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo.class);

        assertNotNull(save);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerRepositorySignatureTest test`
Expected: FAIL，接口不存在。

- [ ] **Step 3: 创建三个仓储接口**

`CustomerRepository.java`：

```java
package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;

import java.util.List;

public interface CustomerRepository {
    void save(Customer customer);
    void saveBatch(List<Customer> customers);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(Customer customer);
    Customer findById(String corpid, Long id);
    List<Customer> findByCondition(CustomerQueryPojo queryPojo);
}
```

`CustomerContactRepository.java` 与 `CustomerAddressRepository.java` 按同一模式，分别替换类型为 `CustomerContact` / `CustomerAddress` 与对应 `QueryPojo`。

- [ ] **Step 4: 重新运行测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerRepositorySignatureTest test`
Expected: PASS

- [ ] **Step 5: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepositorySignatureTest.java

git commit -m "feat: define customer repository contracts"
```

### Task 5: 实现三张表的 PO 与 Mapper

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerPO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerContactPO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerAddressPO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/CustomerMapper.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/CustomerContactMapper.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/CustomerAddressMapper.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerPOTest.java`

**Interfaces:**
- Consumes: `BaseEntity`、MyBatis-Plus `BaseMapper`
- Produces: 三张表的持久化载体与单表 Mapper 能力

- [ ] **Step 1: 写一个失败的 PO 测试**

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerPOTest {

    @Test
    void should_extend_base_entity_and_hold_customer_fields() {
        CustomerPO customerPO = new CustomerPO();
        customerPO.setCorpid("corp-001");
        customerPO.setCustomerCode("CUST-001");
        customerPO.setDel(0);

        assertTrue(customerPO instanceof BaseEntity);
        assertEquals("corp-001", customerPO.getCorpid());
        assertEquals("CUST-001", customerPO.getCustomerCode());
        assertEquals(0, customerPO.getDel());
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerPOTest test`
Expected: FAIL，类不存在。

- [ ] **Step 3: 创建三个 PO 类**

`CustomerPO.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class CustomerPO extends BaseEntity {
    private String corpid;
    private String customerCode;
    private String customerName;
    private String customerShortName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String ownerSalesNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
}
```

`CustomerContactPO.java` 与 `CustomerAddressPO.java` 按表字段对应创建，均继承 `BaseEntity`，均使用 `@TableName`。

- [ ] **Step 4: 创建三个 Mapper 接口**

`CustomerMapper.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

public interface CustomerMapper extends BaseMapper<CustomerPO> {
}
```

其余两个 Mapper 同理替换为 `CustomerContactPO`、`CustomerAddressPO`。

- [ ] **Step 5: 重新运行测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerPOTest test`
Expected: PASS

- [ ] **Step 6: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerPOTest.java

git commit -m "feat: add customer persistence objects and mappers"
```

### Task 6: 实现 Convertor 与 RepositoryImpl

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerConvertor.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerContactConvertor.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerAddressConvertor.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerContactRepositoryImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerAddressRepositoryImpl.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerConvertorTest.java`

**Interfaces:**
- Consumes: 三个领域对象、三个 `PO`、三个 `Mapper`、三个仓储接口
- Produces: `Domain <-> PO` 转换，以及按 `corpid` 约束的基础 CRUD 仓储实现

- [ ] **Step 1: 写一个失败的转换器测试**

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerConvertorTest {

    @Test
    void should_convert_between_customer_and_po() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");

        CustomerPO customerPO = CustomerConvertor.toPO(customer);
        Customer converted = CustomerConvertor.toDomain(customerPO);

        assertEquals(1L, customerPO.getId());
        assertEquals("corp-001", customerPO.getCorpid());
        assertEquals("CUST-001", converted.getCustomerCode());
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerConvertorTest test`
Expected: FAIL，转换器不存在。

- [ ] **Step 3: 创建三个转换器**

`CustomerConvertor.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

public final class CustomerConvertor {

    private CustomerConvertor() {
    }

    public static CustomerPO toPO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerPO po = new CustomerPO();
        po.setId(customer.getId());
        po.setCorpid(customer.getCorpid());
        po.setCustomerCode(customer.getCustomerCode());
        po.setCustomerName(customer.getCustomerName());
        po.setCustomerShortName(customer.getCustomerShortName());
        po.setCustomerCategory(customer.getCustomerCategory());
        po.setRegionCode(customer.getRegionCode());
        po.setOwnerSalesId(customer.getOwnerSalesId());
        po.setOwnerSalesNameSnapshot(customer.getOwnerSalesNameSnapshot());
        po.setBizStatus(customer.getBizStatus());
        po.setRefStatus(customer.getRefStatus());
        po.setDefaultContactId(customer.getDefaultContactId());
        po.setDefaultAddressId(customer.getDefaultAddressId());
        po.setRemark(customer.getRemark());
        po.setCreatorId(customer.getCreatorId());
        po.setModifyId(customer.getModifyId());
        po.setVersion(customer.getVersion());
        po.setDel(customer.getDel());
        po.setAddTime(customer.getAddTime());
        po.setUpdateTime(customer.getUpdateTime());
        return po;
    }

    public static Customer toDomain(CustomerPO po) {
        if (po == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(po.getId());
        customer.setCorpid(po.getCorpid());
        customer.setCustomerCode(po.getCustomerCode());
        customer.setCustomerName(po.getCustomerName());
        customer.setCustomerShortName(po.getCustomerShortName());
        customer.setCustomerCategory(po.getCustomerCategory());
        customer.setRegionCode(po.getRegionCode());
        customer.setOwnerSalesId(po.getOwnerSalesId());
        customer.setOwnerSalesNameSnapshot(po.getOwnerSalesNameSnapshot());
        customer.setBizStatus(po.getBizStatus());
        customer.setRefStatus(po.getRefStatus());
        customer.setDefaultContactId(po.getDefaultContactId());
        customer.setDefaultAddressId(po.getDefaultAddressId());
        customer.setRemark(po.getRemark());
        customer.setCreatorId(po.getCreatorId());
        customer.setModifyId(po.getModifyId());
        customer.setVersion(po.getVersion());
        customer.setDel(po.getDel());
        customer.setAddTime(po.getAddTime());
        customer.setUpdateTime(po.getUpdateTime());
        return customer;
    }
}
```

`CustomerContactConvertor.java`、`CustomerAddressConvertor.java` 采用相同模式逐字段映射。

- [ ] **Step 4: 创建 `CustomerRepositoryImpl` 样板并复制到其余两表**

`CustomerRepositoryImpl.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public void save(Customer customer) {
        customerMapper.insert(CustomerConvertor.toPO(customer));
    }

    @Override
    public void saveBatch(List<Customer> customers) {
        for (Customer customer : customers) {
            customerMapper.insert(CustomerConvertor.toPO(customer));
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerMapper.update(null,
            new LambdaUpdateWrapper<CustomerPO>()
                .eq(CustomerPO::getCorpid, corpid)
                .eq(CustomerPO::getId, id)
                .eq(CustomerPO::getDel, 0)
                .set(CustomerPO::getDel, 1));
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerMapper.update(null,
            new LambdaUpdateWrapper<CustomerPO>()
                .eq(CustomerPO::getCorpid, corpid)
                .in(CustomerPO::getId, ids)
                .eq(CustomerPO::getDel, 0)
                .set(CustomerPO::getDel, 1));
    }

    @Override
    public void update(Customer customer) {
        CustomerPO po = CustomerConvertor.toPO(customer);
        customerMapper.update(po,
            new LambdaUpdateWrapper<CustomerPO>()
                .eq(CustomerPO::getCorpid, customer.getCorpid())
                .eq(CustomerPO::getId, customer.getId())
                .eq(CustomerPO::getDel, 0));
    }

    @Override
    public Customer findById(String corpid, Long id) {
        CustomerPO po = customerMapper.selectOne(
            new LambdaQueryWrapper<CustomerPO>()
                .eq(CustomerPO::getCorpid, corpid)
                .eq(CustomerPO::getId, id)
                .eq(CustomerPO::getDel, 0));
        return CustomerConvertor.toDomain(po);
    }

    @Override
    public List<Customer> findByCondition(CustomerQueryPojo queryPojo) {
        return customerMapper.selectList(
                new LambdaQueryWrapper<CustomerPO>()
                    .eq(CustomerPO::getCorpid, queryPojo.getCorpid())
                    .eq(queryPojo.getId() != null, CustomerPO::getId, queryPojo.getId())
                    .eq(queryPojo.getCustomerCode() != null, CustomerPO::getCustomerCode, queryPojo.getCustomerCode())
                    .like(queryPojo.getCustomerName() != null, CustomerPO::getCustomerName, queryPojo.getCustomerName())
                    .eq(queryPojo.getCustomerCategory() != null, CustomerPO::getCustomerCategory, queryPojo.getCustomerCategory())
                    .eq(queryPojo.getBizStatus() != null, CustomerPO::getBizStatus, queryPojo.getBizStatus())
                    .eq(queryPojo.getRefStatus() != null, CustomerPO::getRefStatus, queryPojo.getRefStatus())
                    .eq(queryPojo.getOwnerSalesId() != null, CustomerPO::getOwnerSalesId, queryPojo.getOwnerSalesId())
                    .eq(CustomerPO::getDel, 0))
            .stream()
            .map(CustomerConvertor::toDomain)
            .toList();
    }
}
```

`CustomerContactRepositoryImpl.java` 与 `CustomerAddressRepositoryImpl.java` 采用同样模式，过滤条件改为各自 `QueryPojo` 字段。

- [ ] **Step 5: 重新运行转换器测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerConvertorTest test`
Expected: PASS

- [ ] **Step 6: 运行模块编译校验**

Run: `mvn -pl xbb-erp-module-customer test`
Expected: PASS 或仅出现与本任务无关的既有失败

- [ ] **Step 7: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerConvertorTest.java

git commit -m "feat: implement customer persistence repositories"
```

### Task 7: 生成三张表建表 SQL

**Files:**
- Create: `docs/sql/2026-07-22-init-customer-module.sql`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/sql/CustomerSqlPlanTest.java`

**Interfaces:**
- Consumes: 设计文档中的字段、索引约束
- Produces: 三张表可落库的 SQL 脚本，覆盖主键、公共字段、业务字段、索引

- [ ] **Step 1: 写一个失败的 SQL 存在性测试**

```java
package xbb.ai.erp.module.customer.sql;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSqlPlanTest {

    @Test
    void should_generate_customer_sql_script() {
        assertTrue(Files.exists(Path.of("../docs/sql/2026-07-22-init-customer-module.sql")));
    }
}
```

- [ ] **Step 2: 运行测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSqlPlanTest test`
Expected: FAIL，SQL 文件不存在。

- [ ] **Step 3: 编写建表 SQL**

在 `docs/sql/2026-07-22-init-customer-module.sql` 中写入：

```sql
CREATE TABLE `customer` (
  `id` BIGINT NOT NULL,
  `corpid` VARCHAR(50) NOT NULL,
  `customer_code` VARCHAR(64) NOT NULL,
  `customer_name` VARCHAR(128) NOT NULL,
  `customer_short_name` VARCHAR(128) DEFAULT NULL,
  `customer_category` VARCHAR(32) NOT NULL,
  `region_code` VARCHAR(32) DEFAULT NULL,
  `owner_sales_id` VARCHAR(50) DEFAULT NULL,
  `owner_sales_name_snapshot` VARCHAR(50) DEFAULT NULL,
  `biz_status` VARCHAR(32) NOT NULL,
  `ref_status` VARCHAR(32) NOT NULL,
  `default_contact_id` BIGINT DEFAULT NULL,
  `default_address_id` BIGINT DEFAULT NULL,
  `remark` VARCHAR(500) DEFAULT NULL,
  `del` TINYINT NOT NULL DEFAULT 0,
  `add_time` BIGINT NOT NULL,
  `update_time` BIGINT NOT NULL,
  `creator_id` VARCHAR(50) NOT NULL,
  `modify_id` VARCHAR(50) NOT NULL,
  `version` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_corpid_customer_code` (`corpid`, `customer_code`),
  KEY `idx_corpid_biz_status_update_time` (`corpid`, `biz_status`, `update_time`),
  KEY `idx_corpid_ref_status_update_time` (`corpid`, `ref_status`, `update_time`),
  KEY `idx_corpid_owner_sales` (`corpid`, `owner_sales_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `customer_contact` (
  `id` BIGINT NOT NULL,
  `corpid` VARCHAR(50) NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `contact_name` VARCHAR(64) NOT NULL,
  `mobile` VARCHAR(32) DEFAULT NULL,
  `phone` VARCHAR(32) DEFAULT NULL,
  `email` VARCHAR(128) DEFAULT NULL,
  `position_name` VARCHAR(64) DEFAULT NULL,
  `default_flag` TINYINT NOT NULL DEFAULT 0,
  `biz_status` VARCHAR(32) NOT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  `del` TINYINT NOT NULL DEFAULT 0,
  `add_time` BIGINT NOT NULL,
  `update_time` BIGINT NOT NULL,
  `creator_id` VARCHAR(50) NOT NULL,
  `modify_id` VARCHAR(50) NOT NULL,
  `version` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_corpid_customer_id` (`corpid`, `customer_id`),
  KEY `idx_corpid_customer_default_flag` (`corpid`, `customer_id`, `default_flag`),
  KEY `idx_corpid_mobile` (`corpid`, `mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `customer_address` (
  `id` BIGINT NOT NULL,
  `corpid` VARCHAR(50) NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `address_type` VARCHAR(32) NOT NULL,
  `receiver_name` VARCHAR(64) DEFAULT NULL,
  `receiver_mobile` VARCHAR(32) DEFAULT NULL,
  `province_code` VARCHAR(32) DEFAULT NULL,
  `city_code` VARCHAR(32) DEFAULT NULL,
  `district_code` VARCHAR(32) DEFAULT NULL,
  `detail_address` VARCHAR(255) NOT NULL,
  `postal_code` VARCHAR(16) DEFAULT NULL,
  `default_flag` TINYINT NOT NULL DEFAULT 0,
  `biz_status` VARCHAR(32) NOT NULL,
  `del` TINYINT NOT NULL DEFAULT 0,
  `add_time` BIGINT NOT NULL,
  `update_time` BIGINT NOT NULL,
  `creator_id` VARCHAR(50) NOT NULL,
  `modify_id` VARCHAR(50) NOT NULL,
  `version` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_corpid_customer_id` (`corpid`, `customer_id`),
  KEY `idx_corpid_customer_default_flag` (`corpid`, `customer_id`, `default_flag`),
  KEY `idx_corpid_address_type` (`corpid`, `address_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

- [ ] **Step 4: 重新运行测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSqlPlanTest test`
Expected: PASS

- [ ] **Step 5: 提交当前任务**

```bash
git add docs/sql/2026-07-22-init-customer-module.sql \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/sql/CustomerSqlPlanTest.java

git commit -m "feat: add customer module schema sql"
```

### Task 8: 端到端校验模块最小可用性

**Files:**
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/ModuleStructureTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/model/CustomerModelTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepositorySignatureTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerPOTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerConvertorTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/sql/CustomerSqlPlanTest.java`

**Interfaces:**
- Consumes: 前 7 个任务的全部产物
- Produces: 模块最小可用的验证结果，确认范围闭环且没有多余结构

- [ ] **Step 1: 写一个总体验证测试补充范围约束**

在 `ModuleStructureTest` 中补充：

```java
@Test
void should_not_create_app_or_controller_packages() {
    assertFalse(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/app")));
    assertFalse(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/controller")));
    assertFalse(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/facade")));
}
```

- [ ] **Step 2: 运行模块定向测试集**

Run: `mvn -pl xbb-erp-module-customer -Dtest=ModuleStructureTest,CustomerModelTest,CustomerRepositorySignatureTest,CustomerPOTest,CustomerConvertorTest,CustomerSqlPlanTest test`
Expected: 全部 PASS

- [ ] **Step 3: 运行模块全量测试**

Run: `mvn -pl xbb-erp-module-customer test`
Expected: PASS

- [ ] **Step 4: 运行基础持久化回归测试**

Run: `mvn -pl xbb-erp-base-persistence -Dtest=BaseEntityTest test`
Expected: PASS

- [ ] **Step 5: 运行多模块联编**

Run: `mvn -pl xbb-erp-base-persistence,xbb-erp-module-customer test`
Expected: PASS

- [ ] **Step 6: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/test xbb-erp-base-persistence/src/test

git commit -m "test: verify customer module initialization"
```

## Self-Review

- Spec coverage：计划覆盖了模块接入、基础持久化对齐、领域模型、查询对象、仓储接口、PO、Mapper、Convertor、RepositoryImpl、建表 SQL、最终验证，和规格文档一致。
- Placeholder scan：文内没有 `TBD`、`TODO`、`implement later`、`similar to` 等占位表达。
- Type consistency：`PO` 统一继承 `BaseEntity`，仓储接口统一使用 `String corpid, Long id`，查询对象统一以 `Pojo` 结尾，三张表类型命名一致。

Plan complete and saved to `docs/superpowers/plans/2026-07-22-customer-module-init-plan.md`. Two execution options:

**1. Subagent-Driven (recommended)** - 我按任务逐个派独立子代理执行，并在任务间做 review

**2. Inline Execution** - 我在当前会话里按这个计划直接实现，并分阶段给你检查点

Which approach?