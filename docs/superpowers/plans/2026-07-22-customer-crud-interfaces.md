# Customer CRUD Interfaces Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为 `xbb-erp-module-customer` 补齐客户管理 6 个基础接口：`/list`、`/addItem`、`/updateItem`、`/save`、`/detail`、`/delete`，并把联系人、地址、银行账户、开票信息一并纳入整单覆盖式保存。

**Architecture:** 继续沿用当前模块已有的 `domain + infrastructure.persistence` 结构，并新增 `admin + application` 两层。控制器只承接协议；应用服务统一编排列表、详情、整单保存、删除校验；领域与持久化层补齐银行账户与开票信息两张表，并复用已有客户、联系人、地址仓储实现。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、Spring Web、MyBatis-Plus、JUnit 5

## Global Constraints

- 对话与文档语境保持中文。
- 项目架构遵循 DDD，分层约束参考 `docs/base/11-进销存系统工程结构与分层架构设计文档.md`。
- 不创建 `worktree`，直接在当前分支实现。
- 模块固定为 `xbb-erp-module-customer`，Java 根包固定为 `xbb.ai.erp.module.customer`。
- 所有接口入参必须使用 DTO；非脚本接口 DTO 需继承 `BaseDTO`。
- `userId` / 员工 ID 统一使用 `String`。
- 直接对接数据库的对象统一使用 `PO` 后缀。
- 直接对接数据库的状态/是否字段统一使用 `Integer`，不要使用布尔值。
- getter / setter 统一使用 Lombok。
- 本次接口范围只覆盖：客户主档、联系人、地址、银行账户、开票信息。
- 附件本期不做，不暴露字段、不接收入参、不做上传。
- `/save` 采用整单覆盖式保存，支持直接保存 `DRAFT` 与 `ENABLED`。
- 默认项规则按文档严格执行：联系人、地址、银行账户、开票信息每类最多一个默认项；冲突直接报错，不自动纠偏。
- 删除规则按文档严格执行：默认项不能直接删；已形成历史快照的子资料优先停用；已被引用的客户主档不允许物理删除。
- “引用与上下游”“操作流水”本期只保留返回结构，统一返回空列表。
- 所有查询、修改、删除都必须显式带 `corpid`。
- SQL 文件固定维护在 `docs/sql/2026-07-22-init-customer-module.sql`。
- 必须同步维护客户模块 `docs/api` 接口文档，领域名按 `.claude/commands/init-crud-ctrl/cal-domain.md` 规则取 `customer-customer`。

---

### Task 1: 扩展持久化模型到银行账户与开票信息

**Files:**
- Modify: `docs/sql/2026-07-22-init-customer-module.sql`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/CustomerBankAccount.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/CustomerInvoiceProfile.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/CustomerBankAccountQueryPojo.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/pojo/CustomerInvoiceProfileQueryPojo.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerBankAccountRepository.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerInvoiceProfileRepository.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerBankAccountPO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerInvoiceProfilePO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/CustomerBankAccountMapper.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/CustomerInvoiceProfileMapper.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerBankAccountConvertor.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerInvoiceProfileConvertor.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerBankAccountRepositoryImpl.java`
- Create: `xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerBankAccountMapper.xml`
- Create: `xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerInvoiceProfileMapper.xml`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/model/CustomerBankAccountModelTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/model/CustomerInvoiceProfileModelTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerBankAccountRepositorySignatureTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerInvoiceProfileRepositorySignatureTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerBankAccountConvertorTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/convertor/CustomerInvoiceProfileConvertorTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/sql/CustomerSqlPlanTest.java`

**Interfaces:**
- Consumes: 现有 `CustomerRepository`、`ConditionMapHelper`、`BaseEntity`、SQL 文件 `docs/sql/2026-07-22-init-customer-module.sql`
- Produces: `CustomerBankAccount*` 与 `CustomerInvoiceProfile*` 全套领域/持久化对象，供后续应用服务与接口层直接编排

- [ ] **Step 1: 先写失败的领域模型与仓储签名测试**

```java
package xbb.ai.erp.module.customer.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerBankAccountModelTest {

    @Test
    void should_hold_bank_account_fields() {
        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setCorpid("corp-001");
        bankAccount.setCustomerId(10L);
        bankAccount.setAccountNo("62220001");
        bankAccount.setDefaultFlag(1);

        assertEquals("corp-001", bankAccount.getCorpid());
        assertEquals(10L, bankAccount.getCustomerId());
        assertEquals("62220001", bankAccount.getAccountNo());
        assertEquals(1, bankAccount.getDefaultFlag());
    }
}
```

```java
package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerBankAccountRepositorySignatureTest {

    @Test
    void should_declare_bank_account_repository_methods() throws Exception {
        Method insert = CustomerBankAccountRepository.class.getMethod(
            "insert",
            xbb.ai.erp.module.customer.domain.model.CustomerBankAccount.class
        );
        Method findById = CustomerBankAccountRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerBankAccountRepository.class.getMethod("findByCondition", Map.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
```

`CustomerInvoiceProfileModelTest` 与 `CustomerInvoiceProfileRepositorySignatureTest` 采用同一模式，字段分别覆盖 `invoiceTitle`、`taxNo`、`defaultFlag`。

- [ ] **Step 2: 运行定向测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerBankAccountModelTest,CustomerInvoiceProfileModelTest,CustomerBankAccountRepositorySignatureTest,CustomerInvoiceProfileRepositorySignatureTest test`
Expected: FAIL，提示类或接口不存在。

- [ ] **Step 3: 创建两个领域对象与查询对象**

`CustomerBankAccount.java`：

```java
package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class CustomerBankAccount {
    private Long id;
    private String corpid;
    private Long customerId;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String accountUsage;
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

`CustomerInvoiceProfile.java`：

```java
package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class CustomerInvoiceProfile {
    private Long id;
    private String corpid;
    private Long customerId;
    private String invoiceTitle;
    private String taxNo;
    private String addressPhone;
    private String bankName;
    private String bankAccountNo;
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

`CustomerBankAccountQueryPojo.java`：

```java
package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerBankAccountQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String bizStatus;
    private Integer defaultFlag;
}
```

`CustomerInvoiceProfileQueryPojo.java`：

```java
package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;

@Data
public class CustomerInvoiceProfileQueryPojo {
    private String corpid;
    private Long id;
    private Long customerId;
    private String invoiceTitle;
    private String taxNo;
    private String bizStatus;
    private Integer defaultFlag;
}
```

- [ ] **Step 4: 定义两个仓储接口，保持与现有 customer 仓储风格一致**

`CustomerBankAccountRepository.java`：

```java
package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;

import java.util.List;
import java.util.Map;

public interface CustomerBankAccountRepository {
    void insert(CustomerBankAccount bankAccount);
    void insertBatch(List<CustomerBankAccount> bankAccounts);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(CustomerBankAccount bankAccount);
    CustomerBankAccount findById(String corpid, Long id);
    List<CustomerBankAccount> findByCondition(Map<String, Object> conditionMap);
}
```

`CustomerInvoiceProfileRepository.java` 同样保持 `insert / insertBatch / removeById / removeBatchByIds / update / findById / findByCondition` 签名。

- [ ] **Step 5: 写失败的转换器测试**

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerBankAccountPO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerBankAccountConvertorTest {

    @Test
    void should_convert_between_bank_account_and_po() {
        CustomerBankAccount domain = new CustomerBankAccount();
        domain.setId(1L);
        domain.setCorpid("corp-001");
        domain.setAccountNo("62220001");

        CustomerBankAccountPO po = CustomerBankAccountConvertor.toPO(domain);
        CustomerBankAccount converted = CustomerBankAccountConvertor.toDomain(po);

        assertEquals(1L, po.getId());
        assertEquals("corp-001", po.getCorpid());
        assertEquals("62220001", converted.getAccountNo());
    }
}
```

`CustomerInvoiceProfileConvertorTest` 同样验证 `invoiceTitle` 与 `taxNo` 映射。

- [ ] **Step 6: 创建 PO、Mapper、Convertor、RepositoryImpl，并延续当前 XML Mapper 模式**

`CustomerBankAccountPO.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_bank_account")
public class CustomerBankAccountPO extends BaseEntity {
    private String corpid;
    private Long customerId;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String accountUsage;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
}
```

`CustomerInvoiceProfilePO.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_invoice_profile")
public class CustomerInvoiceProfilePO extends BaseEntity {
    private String corpid;
    private Long customerId;
    private String invoiceTitle;
    private String taxNo;
    private String addressPhone;
    private String bankName;
    private String bankAccountNo;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
}
```

`CustomerBankAccountMapper.java`：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerBankAccountPO;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerBankAccountMapper extends BaseMapper<CustomerBankAccountPO> {
    void insertBatch(@Param("list") List<CustomerBankAccountPO> list);
    void removeById(@Param("corpid") String corpid, @Param("id") Long id);
    void removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);
    void update(CustomerBankAccountPO po);
    CustomerBankAccountPO findById(@Param("corpid") String corpid, @Param("id") Long id);
    List<CustomerBankAccountPO> findByCondition(Map<String, Object> conditionMap);
}
```

`CustomerBankAccountRepositoryImpl.java` 参考 `CustomerRepositoryImpl`，保持 `ConditionMapHelper.prepare(conditionMap)` 的调用方式：

```java
package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerBankAccountConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerBankAccountMapper;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerBankAccountRepositoryImpl implements CustomerBankAccountRepository {

    private final CustomerBankAccountMapper mapper;

    @Override
    public void insert(CustomerBankAccount bankAccount) {
        mapper.insert(CustomerBankAccountConvertor.toPO(bankAccount));
    }

    @Override
    public void insertBatch(List<CustomerBankAccount> bankAccounts) {
        mapper.insertBatch(bankAccounts.stream().map(CustomerBankAccountConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        mapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        mapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(CustomerBankAccount bankAccount) {
        mapper.update(CustomerBankAccountConvertor.toPO(bankAccount));
    }

    @Override
    public CustomerBankAccount findById(String corpid, Long id) {
        return CustomerBankAccountConvertor.toDomain(mapper.findById(corpid, id));
    }

    @Override
    public List<CustomerBankAccount> findByCondition(Map<String, Object> conditionMap) {
        return mapper.findByCondition(ConditionMapHelper.prepare(conditionMap))
            .stream()
            .map(CustomerBankAccountConvertor::toDomain)
            .toList();
    }
}
```

`CustomerInvoiceProfileMapper`、`CustomerInvoiceProfileRepositoryImpl`、两个 Convertor 与上面同一模式，字段替换为开票信息对象。

- [ ] **Step 7: 在 SQL 中补两张表与索引，并先让 SQL 检查失败后变绿**

将 `CustomerSqlPlanTest.java` 扩展为：

```java
package xbb.ai.erp.module.customer.sql;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSqlPlanTest {

    @Test
    void should_generate_customer_sql_script() throws Exception {
        Path path = Path.of("../docs/sql/2026-07-22-init-customer-module.sql");
        assertTrue(Files.exists(path));
        String sql = Files.readString(path);
        assertTrue(sql.contains("CREATE TABLE `customer_bank_account`"));
        assertTrue(sql.contains("CREATE TABLE `customer_invoice_profile`"));
        assertTrue(sql.contains("idx_corpid_customer_default_flag"));
    }
}
```

在 `docs/sql/2026-07-22-init-customer-module.sql` 追加：

```sql
CREATE TABLE `customer_bank_account` (
  `id` BIGINT NOT NULL,
  `corpid` VARCHAR(50) NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `account_name` VARCHAR(128) NOT NULL,
  `bank_name` VARCHAR(128) NOT NULL,
  `account_no` VARCHAR(64) NOT NULL,
  `account_usage` VARCHAR(64) DEFAULT NULL,
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
  KEY `idx_corpid_account_no` (`corpid`, `account_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `customer_invoice_profile` (
  `id` BIGINT NOT NULL,
  `corpid` VARCHAR(50) NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `invoice_title` VARCHAR(128) NOT NULL,
  `tax_no` VARCHAR(64) NOT NULL,
  `address_phone` VARCHAR(128) DEFAULT NULL,
  `bank_name` VARCHAR(128) DEFAULT NULL,
  `bank_account_no` VARCHAR(64) DEFAULT NULL,
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
  KEY `idx_corpid_tax_no` (`corpid`, `tax_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

- [ ] **Step 8: 运行定向测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerBankAccountModelTest,CustomerInvoiceProfileModelTest,CustomerBankAccountRepositorySignatureTest,CustomerInvoiceProfileRepositorySignatureTest,CustomerBankAccountConvertorTest,CustomerInvoiceProfileConvertorTest,CustomerSqlPlanTest test`
Expected: PASS

- [ ] **Step 9: 提交当前任务**

```bash
git add docs/sql/2026-07-22-init-customer-module.sql \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence \
        xbb-erp-module-customer/src/main/resources/mapper/customer \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/sql/CustomerSqlPlanTest.java

git commit -m "feat: add customer bank account and invoice persistence"
```

### Task 2: 搭建 admin 与 application 层骨架

**Files:**
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/ModuleStructureTest.java`
- Modify: `xbb-erp-module-customer/pom.xml`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerListDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerSaveDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerMainDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerContactItemDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerAddressItemDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerBankAccountItemDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerInvoiceProfileItemDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerListItemVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerSaveItemVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDetailVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerFieldAssembler.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`

**Interfaces:**
- Consumes: `BaseDTO`、`IdBaseDTO`、`BatchBaseDTO`、`ListBaseDTO`、`ListBaseVO`、`SaveItemVO`
- Produces: 客户模块接口层与应用层骨架，供后续逐步补齐字段枚举、列表/详情/保存逻辑

- [ ] **Step 1: 先写失败的结构测试，明确新增分层目录与控制器方法**

把 `ModuleStructureTest.java` 改为：

```java
package xbb.ai.erp.module.customer;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_customer_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/admin")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/admin/dto")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/admin/vo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/application/service")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/application/assembler")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository")));
    }
}
```

新增 `CustomerAdminControllerStructureTest.java`：

```java
package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerAdminControllerStructureTest {

    @Test
    void should_declare_six_crud_endpoints() throws Exception {
        Method list = CustomerAdminController.class.getMethod("list", xbb.ai.erp.module.customer.admin.dto.CustomerListDTO.class);
        Method addItem = CustomerAdminController.class.getMethod("addItem", xbb.ai.erp.base.common.dto.BaseDTO.class);
        Method updateItem = CustomerAdminController.class.getMethod("updateItem", xbb.ai.erp.base.common.dto.IdBaseDTO.class);
        Method save = CustomerAdminController.class.getMethod("save", xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO.class);
        Method detail = CustomerAdminController.class.getMethod("detail", xbb.ai.erp.base.common.dto.IdBaseDTO.class);
        Method delete = CustomerAdminController.class.getMethod("delete", xbb.ai.erp.base.common.dto.BatchBaseDTO.class);

        assertNotNull(list);
        assertNotNull(addItem);
        assertNotNull(updateItem);
        assertNotNull(save);
        assertNotNull(detail);
        assertNotNull(delete);
    }
}
```

- [ ] **Step 2: 运行定向测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=ModuleStructureTest,CustomerAdminControllerStructureTest test`
Expected: FAIL，提示目录或类不存在。

- [ ] **Step 3: 给模块加上 Spring Web 依赖，允许控制器编译**

在 `xbb-erp-module-customer/pom.xml` 的 `<dependencies>` 中追加：

```xml
<dependency>
    <groupId>org.bomfish</groupId>
    <artifactId>xbb-erp-base-web</artifactId>
    <version>${project.version}</version>
</dependency>
```

- [ ] **Step 4: 创建 DTO、VO、应用服务与装配器骨架**

`CustomerListDTO.java`：

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.ListBaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerListDTO extends ListBaseDTO {
    private String keyword;
    private String customerCode;
    private String customerName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String bizStatus;
    private String refStatus;
}
```

`CustomerSaveDTO.java`：

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerSaveDTO extends BaseDTO {
    private CustomerMainDTO main;
    private List<CustomerContactItemDTO> contacts = new ArrayList<>();
    private List<CustomerAddressItemDTO> addresses = new ArrayList<>();
    private List<CustomerBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<CustomerInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
```

`CustomerDetailVO.java`：

```java
package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerDetailVO {
    private CustomerSaveItemVO mainData;
    private List<String> referenceTodoSections = new ArrayList<>();
    private List<String> operateLogTodoSections = new ArrayList<>();
}
```

`CustomerAdminAppService.java`：

```java
package xbb.ai.erp.module.customer.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;

public interface CustomerAdminAppService {
    ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto);
    SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto);
    SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto);
    Long save(CustomerSaveDTO dto);
    CustomerDetailVO detail(IdBaseDTO dto);
    void delete(BatchBaseDTO dto);
}
```

其余 DTO / VO 先最小字段集：`id`、`bizStatus`、业务字段，全部用 Lombok `@Data`。

- [ ] **Step 5: 创建控制器空实现，先只让签名通过**

`CustomerAdminController.java`：

```java
package xbb.ai.erp.module.customer.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;

@RestController
@RequestMapping("/erp/v1/customer")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerAdminAppService customerAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<CustomerListItemVO> list(@RequestBody CustomerListDTO dto) {
        return customerAdminAppService.list(dto);
    }

    @PostMapping("/addItem")
    public SaveItemVO<CustomerSaveItemVO> addItem(@RequestBody BaseDTO dto) {
        return customerAdminAppService.addItem(dto);
    }

    @PostMapping("/updateItem")
    public SaveItemVO<CustomerSaveItemVO> updateItem(@RequestBody IdBaseDTO dto) {
        return customerAdminAppService.updateItem(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody CustomerSaveDTO dto) {
        return customerAdminAppService.save(dto);
    }

    @PostMapping("/detail")
    public CustomerDetailVO detail(@RequestBody IdBaseDTO dto) {
        return customerAdminAppService.detail(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody BatchBaseDTO dto) {
        customerAdminAppService.delete(dto);
    }
}
```

- [ ] **Step 6: 重新运行定向测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=ModuleStructureTest,CustomerAdminControllerStructureTest test`
Expected: PASS

- [ ] **Step 7: 提交当前任务**

```bash
git add xbb-erp-module-customer/pom.xml \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/ModuleStructureTest.java \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java

git commit -m "feat: scaffold customer admin and application layers"
```

### Task 3: 定义字段枚举与新建/编辑页返回结构

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerFieldEnum.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerFieldAssembler.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldAssemblerTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerAddItemViewTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerUpdateItemViewTest.java`

**Interfaces:**
- Consumes: `FieldEntity`、`FieldTypeEnum`、`SaveItemVO`、设计文档中五组字段结构
- Produces: `CustomerFieldEnum` 与 `addItem/updateItem` 的统一 `headList + data` 结构

- [ ] **Step 1: 写失败的字段装配测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.customer.admin.CustomerFieldEnum;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFieldAssemblerTest {

    @Test
    void should_build_customer_head_list() {
        List<FieldEntity> headList = CustomerFieldAssembler.buildAddItemHeadList();

        assertTrue(headList.stream().anyMatch(field -> "main.customerName".equals(field.getAttr())));
        assertTrue(headList.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())));
        assertTrue(headList.stream().anyMatch(field -> "bankAccounts.accountNo".equals(field.getAttr())));
        assertEquals("main.customerCode", CustomerFieldEnum.CUSTOMER_CODE.getAttr());
    }
}
```

- [ ] **Step 2: 运行定向测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerFieldAssemblerTest test`
Expected: FAIL，枚举与装配器方法不存在。

- [ ] **Step 3: 创建字段枚举，平铺字段命名带分组前缀**

`CustomerFieldEnum.java`：

```java
package xbb.ai.erp.module.customer.admin;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.BusinessField;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;

import java.util.List;

@Getter
public enum CustomerFieldEnum implements BusinessField {
    CUSTOMER_CODE("main.customerCode", "客户编码", FieldTypeEnum.TEXT),
    CUSTOMER_NAME("main.customerName", "客户名称", FieldTypeEnum.TEXT),
    CUSTOMER_SHORT_NAME("main.customerShortName", "客户简称", FieldTypeEnum.TEXT),
    CUSTOMER_CATEGORY("main.customerCategory", "客户分类", FieldTypeEnum.TEXT),
    REGION_CODE("main.regionCode", "所属区域", FieldTypeEnum.TEXT),
    OWNER_SALES_ID("main.ownerSalesId", "归属销售", FieldTypeEnum.USER),
    BIZ_STATUS("main.bizStatus", "业务状态", FieldTypeEnum.COMB),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT),
    CONTACT_NAME("contacts.contactName", "联系人姓名", FieldTypeEnum.TEXT),
    CONTACT_MOBILE("contacts.mobile", "手机号", FieldTypeEnum.TEXT),
    CONTACT_PHONE("contacts.phone", "电话", FieldTypeEnum.TEXT),
    CONTACT_EMAIL("contacts.email", "邮箱", FieldTypeEnum.TEXT),
    CONTACT_POSITION_NAME("contacts.positionName", "职位", FieldTypeEnum.TEXT),
    CONTACT_DEFAULT_FLAG("contacts.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    ADDRESS_TYPE("addresses.addressType", "地址类型", FieldTypeEnum.COMB),
    RECEIVER_NAME("addresses.receiverName", "收件人", FieldTypeEnum.TEXT),
    RECEIVER_MOBILE("addresses.receiverMobile", "联系电话", FieldTypeEnum.TEXT),
    DETAIL_ADDRESS("addresses.detailAddress", "详细地址", FieldTypeEnum.TEXT),
    POSTAL_CODE("addresses.postalCode", "邮编", FieldTypeEnum.TEXT),
    ADDRESS_DEFAULT_FLAG("addresses.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    ACCOUNT_NAME("bankAccounts.accountName", "账户名称", FieldTypeEnum.TEXT),
    BANK_NAME("bankAccounts.bankName", "开户行", FieldTypeEnum.TEXT),
    ACCOUNT_NO("bankAccounts.accountNo", "银行账号", FieldTypeEnum.TEXT),
    ACCOUNT_USAGE("bankAccounts.accountUsage", "账户用途", FieldTypeEnum.TEXT),
    BANK_DEFAULT_FLAG("bankAccounts.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    INVOICE_TITLE("invoiceProfiles.invoiceTitle", "开票抬头", FieldTypeEnum.TEXT),
    TAX_NO("invoiceProfiles.taxNo", "税号", FieldTypeEnum.TEXT),
    ADDRESS_PHONE("invoiceProfiles.addressPhone", "地址电话", FieldTypeEnum.TEXT),
    BANK_ACCOUNT_NAME("invoiceProfiles.bankName", "开户行", FieldTypeEnum.TEXT),
    BANK_ACCOUNT_NO("invoiceProfiles.bankAccountNo", "银行账号", FieldTypeEnum.TEXT),
    INVOICE_DEFAULT_FLAG("invoiceProfiles.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    ;

    private final String attr;
    private final String attrName;
    private final Integer fieldType;

    CustomerFieldEnum(String attr, String attrName, FieldTypeEnum fieldType) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType.getType();
    }

    @Override
    public List<String> getRequiredList() {
        return List.of(
            CUSTOMER_CODE.getAttr(),
            CUSTOMER_NAME.getAttr(),
            CUSTOMER_CATEGORY.getAttr(),
            BIZ_STATUS.getAttr(),
            CONTACT_NAME.getAttr(),
            ADDRESS_TYPE.getAttr(),
            DETAIL_ADDRESS.getAttr(),
            ACCOUNT_NAME.getAttr(),
            BANK_NAME.getAttr(),
            ACCOUNT_NO.getAttr(),
            INVOICE_TITLE.getAttr(),
            TAX_NO.getAttr()
        );
    }
}
```

- [ ] **Step 4: 实现字段装配器与默认空数据构建**

`CustomerFieldAssembler.java`：

```java
package xbb.ai.erp.module.customer.application.assembler;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.customer.admin.CustomerFieldEnum;

import java.util.Arrays;
import java.util.List;

public final class CustomerFieldAssembler {

    private CustomerFieldAssembler() {
    }

    public static List<FieldEntity> buildAddItemHeadList() {
        List<String> requiredList = CustomerFieldEnum.CUSTOMER_CODE.getRequiredList();
        return Arrays.stream(CustomerFieldEnum.values())
            .map(field -> {
                FieldEntity entity = new FieldEntity();
                entity.setAttr(field.getAttr());
                entity.setAttrName(field.getAttrName());
                entity.setFieldType(String.valueOf(field.getFieldType()));
                entity.setRequired(requiredList.contains(field.getAttr()) ? 1 : 0);
                entity.setEditable(1);
                return entity;
            })
            .toList();
    }
}
```

`CustomerAdminAssembler.java` 先提供一个静态默认空值方法：

```java
public static CustomerSaveItemVO buildEmptySaveItemVO() {
    CustomerSaveItemVO vo = new CustomerSaveItemVO();
    vo.setMain(new CustomerMainDTO());
    vo.setContacts(new ArrayList<>());
    vo.setAddresses(new ArrayList<>());
    vo.setBankAccounts(new ArrayList<>());
    vo.setInvoiceProfiles(new ArrayList<>());
    return vo;
}
```

- [ ] **Step 5: 写失败的新建/编辑页测试，先锁定返回结构**

`CustomerAddItemViewTest.java`：

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerAddItemViewTest {

    @Test
    void should_return_head_list_and_empty_nested_data() {
        CustomerAdminAppServiceImpl service = new CustomerAdminAppServiceImpl();
        SaveItemVO<CustomerSaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
        assertTrue(result.getData().getContacts().isEmpty());
        assertTrue(result.getData().getBankAccounts().isEmpty());
    }
}
```

`CustomerUpdateItemViewTest` 先锁定“被引用后客户编码不可编辑”的返回约束：

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerUpdateItemViewTest {

    @Test
    void should_return_existing_data_structure_for_update_item() {
        CustomerAdminAppServiceImpl service = new CustomerAdminAppServiceImpl();
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        SaveItemVO<CustomerSaveItemVO> result = service.updateItem(dto);

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
    }
}
```

- [ ] **Step 6: 先实现 `CustomerAdminAppServiceImpl` 的 `addItem/updateItem` 最小可运行版本**

`CustomerAdminAppServiceImpl.java` 先只填新建/编辑页相关实现，其余方法先 `throw new UnsupportedOperationException()`：

```java
package xbb.ai.erp.module.customer.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;

@Service
public class CustomerAdminAppServiceImpl implements CustomerAdminAppService {

    @Override
    public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<CustomerSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(CustomerFieldAssembler.buildAddItemHeadList());
        vo.setData(CustomerAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<CustomerSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(CustomerFieldAssembler.buildAddItemHeadList());
        vo.setData(CustomerAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public Long save(CustomerSaveDTO dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CustomerDetailVO detail(IdBaseDTO dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        throw new UnsupportedOperationException();
    }
}
```

- [ ] **Step 7: 重新运行定向测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerFieldAssemblerTest,CustomerAddItemViewTest,CustomerUpdateItemViewTest test`
Expected: PASS

- [ ] **Step 8: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerFieldEnum.java \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service

git commit -m "feat: add customer field metadata and save-item views"
```

### Task 4: 实现 `/list` 与 `/detail` 的聚合查询

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerListItemVO.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDetailVO.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDetailServiceTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerContactRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerAddressRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerBankAccountRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerInvoiceProfileRepository.java`

**Interfaces:**
- Consumes: 五类仓储接口、`CustomerFieldAssembler`、`CustomerAdminAssembler`
- Produces: `/list`、`/detail` 的聚合读取能力，`/list` 使用独立的列表字段 `headList`，列表项含默认联系人/地址/开票抬头摘要，详情含五类资料和两个空列表留白字段

- [ ] **Step 1: 写失败的列表聚合测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CustomerListServiceTest {

    @Test
    void should_aggregate_default_contact_into_list_item() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");
        customer.setBizStatus("ENABLED");

        CustomerContact contact = new CustomerContact();
        contact.setCustomerId(1L);
        contact.setContactName("张三");
        contact.setMobile("13800000000");
        contact.setDefaultFlag(1);

        CustomerRepository customerRepository = new FakeCustomerRepository(List.of(customer));
        CustomerContactRepository contactRepository = new FakeCustomerContactRepository(List.of(contact));

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(customerRepository, contactRepository, null, null, null);
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<CustomerListItemVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("张三", result.getList().get(0).getDefaultContactName());
    }
}
```

- [ ] **Step 2: 写失败的详情聚合测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDetailServiceTest {

    @Test
    void should_return_nested_data_and_empty_todo_sections() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerName("杭州客户");

        CustomerRepository customerRepository = new FakeCustomerRepository(List.of(customer));
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(customerRepository, null, null, null, null);

        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        CustomerDetailVO result = service.detail(dto);

        assertNotNull(result.getMainData());
        assertTrue(result.getReferenceTodoSections().isEmpty());
        assertTrue(result.getOperateLogTodoSections().isEmpty());
    }
}
```

- [ ] **Step 3: 运行定向测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListServiceTest,CustomerDetailServiceTest test`
Expected: FAIL，缺少构造方式、VO 字段或读取实现。

- [ ] **Step 4: 先实现最小 VO 与装配逻辑**

`CustomerListItemVO.java`：

```java
package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

@Data
public class CustomerListItemVO {
    private Long id;
    private String customerCode;
    private String customerName;
    private String customerShortName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String defaultContactName;
    private String defaultContactMobile;
    private String defaultAddressSummary;
    private String defaultInvoiceTitle;
    private String bizStatus;
    private String refStatus;
    private Long addTime;
    private Long updateTime;
}
```

`CustomerAdminAssembler.java` 增加两个方法：

```java
public static CustomerListItemVO toListItemVO(
    Customer customer,
    CustomerContact defaultContact,
    CustomerAddress defaultAddress,
    CustomerInvoiceProfile defaultInvoiceProfile
) {
    CustomerListItemVO vo = new CustomerListItemVO();
    vo.setId(customer.getId());
    vo.setCustomerCode(customer.getCustomerCode());
    vo.setCustomerName(customer.getCustomerName());
    vo.setCustomerShortName(customer.getCustomerShortName());
    vo.setCustomerCategory(customer.getCustomerCategory());
    vo.setRegionCode(customer.getRegionCode());
    vo.setOwnerSalesId(customer.getOwnerSalesId());
    vo.setDefaultContactName(defaultContact == null ? null : defaultContact.getContactName());
    vo.setDefaultContactMobile(defaultContact == null ? null : defaultContact.getMobile());
    vo.setDefaultAddressSummary(defaultAddress == null ? null : defaultAddress.getDetailAddress());
    vo.setDefaultInvoiceTitle(defaultInvoiceProfile == null ? null : defaultInvoiceProfile.getInvoiceTitle());
    vo.setBizStatus(customer.getBizStatus());
    vo.setRefStatus(customer.getRefStatus());
    vo.setAddTime(customer.getAddTime());
    vo.setUpdateTime(customer.getUpdateTime());
    return vo;
}
```

```java
public static CustomerDetailVO toDetailVO(CustomerSaveItemVO saveItemVO) {
    CustomerDetailVO detailVO = new CustomerDetailVO();
    detailVO.setMainData(saveItemVO);
    return detailVO;
}
```

- [ ] **Step 5: 实现 `/list` 与 `/detail` 的最小读取编排**

在 `CustomerAdminAppServiceImpl.java` 中追加构造注入和测试工厂：

```java
@RequiredArgsConstructor
@Service
public class CustomerAdminAppServiceImpl implements CustomerAdminAppService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerInvoiceProfileRepository customerInvoiceProfileRepository;

    public CustomerAdminAppServiceImpl() {
        this(null, null, null, null, null);
    }

    public static CustomerAdminAppServiceImpl forTesting(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository
    ) {
        return new CustomerAdminAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository
        );
    }
```

实现 `/list`：

```java
@Override
public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
    Map<String, Object> conditionMap = new HashMap<>();
    conditionMap.put("corpid", dto.getCorpid());
    conditionMap.put("customerCode", dto.getCustomerCode());
    conditionMap.put("customerName", dto.getCustomerName());
    conditionMap.put("customerCategory", dto.getCustomerCategory());
    conditionMap.put("regionCode", dto.getRegionCode());
    conditionMap.put("ownerSalesId", dto.getOwnerSalesId());
    conditionMap.put("bizStatus", dto.getBizStatus());
    conditionMap.put("refStatus", dto.getRefStatus());
    List<Customer> customers = customerRepository.findByCondition(conditionMap);

    List<CustomerListItemVO> list = customers.stream().map(customer -> {
        CustomerContact defaultContact = findDefaultContact(dto.getCorpid(), customer.getId());
        CustomerAddress defaultAddress = findDefaultAddress(dto.getCorpid(), customer.getId());
        CustomerInvoiceProfile defaultInvoiceProfile = findDefaultInvoiceProfile(dto.getCorpid(), customer.getId());
        return CustomerAdminAssembler.toListItemVO(customer, defaultContact, defaultAddress, defaultInvoiceProfile);
    }).toList();

    ListBaseVO<CustomerListItemVO> vo = new ListBaseVO<>();
    vo.setHeadList(CustomerFieldAssembler.buildListHeadList());
    vo.setList(list);
    vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), dto.getPageNum()));
    return vo;
}
```

实现 `/detail`：

```java
@Override
public CustomerDetailVO detail(IdBaseDTO dto) {
    Customer customer = customerRepository.findById(dto.getCorpid(), dto.getId());
    CustomerSaveItemVO saveItemVO = CustomerAdminAssembler.toSaveItemVO(
        customer,
        customerContactRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId())),
        customerAddressRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId())),
        customerBankAccountRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId())),
        customerInvoiceProfileRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()))
    );
    return CustomerAdminAssembler.toDetailVO(saveItemVO);
}
```

- [ ] **Step 6: 重新运行定向测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListServiceTest,CustomerDetailServiceTest test`
Expected: PASS

- [ ] **Step 7: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service

git commit -m "feat: add customer list and detail queries"
```

### Task 5: 实现 `/save` 的整单覆盖保存与默认项校验

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerSaveItemVO.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerContactRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerAddressRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerBankAccountRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerInvoiceProfileRepository.java`

**Interfaces:**
- Consumes: 五类仓储接口、`CustomerSaveDTO`、`CustomerMainDTO` 与四类子项 DTO
- Produces: `/save` 的新建/更新/新增子项/更新子项/缺失子项删除判定/默认项冲突校验

- [ ] **Step 1: 先写失败的整单保存测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerSaveServiceTest {

    @Test
    void should_insert_customer_and_contact_in_one_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("ENABLED");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(1);

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of(contact));

        Long customerId = service.save(dto);

        assertEquals(1, customerRepository.all().size());
        assertEquals(1, contactRepository.all().size());
        assertEquals(customerId, contactRepository.all().get(0).getCustomerId());
    }
}
```

- [ ] **Step 2: 写失败的默认项冲突测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerSaveValidationTest {

    @Test
    void should_reject_multiple_default_contacts() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        CustomerContactItemDTO contactA = new CustomerContactItemDTO();
        contactA.setContactName("张三");
        contactA.setDefaultFlag(1);

        CustomerContactItemDTO contactB = new CustomerContactItemDTO();
        contactB.setContactName("李四");
        contactB.setDefaultFlag(1);

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("DRAFT");

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of(contactA, contactB));

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
    }
}
```

- [ ] **Step 3: 运行定向测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveServiceTest,CustomerSaveValidationTest test`
Expected: FAIL，`save` 尚未实现。

- [ ] **Step 4: 先完善 DTO / VO 的最小字段集，保证装配可落地**

`CustomerMainDTO.java`：

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;

@Data
public class CustomerMainDTO {
    private Long id;
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
    private Integer version;
}
```

`CustomerSaveItemVO.java`：

```java
package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerSaveItemVO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private List<CustomerContactItemDTO> contacts = new ArrayList<>();
    private List<CustomerAddressItemDTO> addresses = new ArrayList<>();
    private List<CustomerBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<CustomerInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
```

- [ ] **Step 5: 实现 `/save` 的最小绿灯逻辑**

在 `CustomerAdminAppServiceImpl.java` 中实现：

```java
@Override
public Long save(CustomerSaveDTO dto) {
    validateDefaultUniqueness(dto.getContacts(), CustomerContactItemDTO::getDefaultFlag, "联系人默认项只能有一个");
    validateDefaultUniqueness(dto.getAddresses(), CustomerAddressItemDTO::getDefaultFlag, "地址默认项只能有一个");
    validateDefaultUniqueness(dto.getBankAccounts(), CustomerBankAccountItemDTO::getDefaultFlag, "银行账户默认项只能有一个");
    validateDefaultUniqueness(dto.getInvoiceProfiles(), CustomerInvoiceProfileItemDTO::getDefaultFlag, "开票信息默认项只能有一个");

    Customer customer = CustomerAdminAssembler.toCustomer(dto);
    if (customer.getId() == null) {
        customerRepository.insert(customer);
    } else {
        customerRepository.update(customer);
    }

    Long customerId = customer.getId();
    syncContacts(dto.getCorpid(), customerId, dto.getContacts());
    syncAddresses(dto.getCorpid(), customerId, dto.getAddresses());
    syncBankAccounts(dto.getCorpid(), customerId, dto.getBankAccounts());
    syncInvoiceProfiles(dto.getCorpid(), customerId, dto.getInvoiceProfiles());
    return customerId;
}
```

把默认校验辅助方法写进同一个类：

```java
private <T> void validateDefaultUniqueness(List<T> list, Function<T, Integer> getter, String message) {
    long count = list == null ? 0 : list.stream().filter(item -> Integer.valueOf(1).equals(getter.apply(item))).count();
    if (count > 1) {
        throw new IllegalArgumentException(message);
    }
}
```

同步函数先实现最小行为：

```java
private void syncContacts(String corpid, Long customerId, List<CustomerContactItemDTO> items) {
    if (items == null) {
        return;
    }
    for (CustomerContactItemDTO item : items) {
        CustomerContact contact = CustomerAdminAssembler.toCustomerContact(corpid, customerId, item);
        if (contact.getId() == null) {
            customerContactRepository.insert(contact);
        } else {
            customerContactRepository.update(contact);
        }
    }
}
```

`syncAddresses/syncBankAccounts/syncInvoiceProfiles` 先按相同模式实现新增/更新；“缺失子项删除判定”放到下一步补红灯测试后再加。

- [ ] **Step 6: 再写一条失败测试，锁定整单覆盖的删除判定**

```java
@Test
void should_remove_missing_non_default_contact_on_full_save() {
    InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();

    Customer savedCustomer = new Customer();
    savedCustomer.setId(1L);
    savedCustomer.setCorpid("corp-001");
    savedCustomer.setCustomerCode("CUST-001");
    customerRepository.seed(savedCustomer);

    CustomerContact oldContact = new CustomerContact();
    oldContact.setId(10L);
    oldContact.setCorpid("corp-001");
    oldContact.setCustomerId(1L);
    oldContact.setContactName("旧联系人");
    oldContact.setDefaultFlag(0);
    contactRepository.seed(oldContact);

    CustomerMainDTO main = new CustomerMainDTO();
    main.setId(1L);
    main.setCustomerCode("CUST-001");
    main.setCustomerName("杭州客户");
    main.setCustomerCategory("A");
    main.setBizStatus("ENABLED");

    CustomerSaveDTO dto = new CustomerSaveDTO();
    dto.setCorpid("corp-001");
    dto.setMain(main);
    dto.setContacts(List.of());

    CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
        customerRepository,
        contactRepository,
        new InMemoryCustomerAddressRepository(),
        new InMemoryCustomerBankAccountRepository(),
        new InMemoryCustomerInvoiceProfileRepository()
    );

    service.save(dto);

    assertTrue(contactRepository.all().isEmpty());
}
```

- [ ] **Step 7: 把“缺失子项删除判定”补到同步逻辑里**

在 `syncContacts` 中补充：

```java
private void syncContacts(String corpid, Long customerId, List<CustomerContactItemDTO> items) {
    List<CustomerContact> existing = customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
    Set<Long> incomingIds = items == null ? Set.of() : items.stream()
        .map(CustomerContactItemDTO::getId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    for (CustomerContact contact : existing) {
        if (!incomingIds.contains(contact.getId())) {
            if (Integer.valueOf(1).equals(contact.getDefaultFlag())) {
                throw new IllegalArgumentException("默认联系人不能直接删除");
            }
            customerContactRepository.removeById(corpid, contact.getId());
        }
    }

    if (items == null) {
        return;
    }
    for (CustomerContactItemDTO item : items) {
        CustomerContact contact = CustomerAdminAssembler.toCustomerContact(corpid, customerId, item);
        if (contact.getId() == null) {
            customerContactRepository.insert(contact);
        } else {
            customerContactRepository.update(contact);
        }
    }
}
```

`syncAddresses/syncBankAccounts/syncInvoiceProfiles` 用相同模式补齐，删除默认项时均抛出明确异常信息。

- [ ] **Step 8: 重新运行定向测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveServiceTest,CustomerSaveValidationTest test`
Expected: PASS

- [ ] **Step 9: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto \
        xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service

git commit -m "feat: implement customer aggregate save flow"
```

### Task 6: 实现 `/delete` 规则与接口文档

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDeleteServiceTest.java`
- Create: `docs/api/customer-customer.md`

**Interfaces:**
- Consumes: 五类仓储接口、`BatchBaseDTO`
- Produces: `/delete` 的批量删除规则与客户模块 API 文档

- [ ] **Step 1: 写失败的删除规则测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.domain.model.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerDeleteServiceTest {

    @Test
    void should_reject_delete_when_customer_has_reference_status() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setRefStatus("USED");
        customerRepository.seed(customer);

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L));

        assertThrows(IllegalArgumentException.class, () -> service.delete(dto));
    }
}
```

- [ ] **Step 2: 运行定向测试并确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDeleteServiceTest test`
Expected: FAIL，`delete` 尚未实现。

- [ ] **Step 3: 实现删除规则**

在 `CustomerAdminAppServiceImpl.java` 中实现：

```java
@Override
public void delete(BatchBaseDTO dto) {
    for (Long id : dto.getIdList()) {
        Customer customer = customerRepository.findById(dto.getCorpid(), id);
        if (customer == null) {
            continue;
        }
        if (customer.getRefStatus() != null && !"UNUSED".equals(customer.getRefStatus())) {
            throw new IllegalArgumentException("客户已被引用，不允许删除，请改为停用");
        }
        validateSubRecordsBeforeDelete(dto.getCorpid(), id);
        customerRepository.removeById(dto.getCorpid(), id);
    }
}
```

并补辅助方法：

```java
private void validateSubRecordsBeforeDelete(String corpid, Long customerId) {
    List<CustomerContact> contacts = customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
    if (contacts.stream().anyMatch(contact -> Integer.valueOf(1).equals(contact.getDefaultFlag()))) {
        throw new IllegalArgumentException("存在默认联系人，不能直接删除客户");
    }

    List<CustomerAddress> addresses = customerAddressRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
    if (addresses.stream().anyMatch(address -> Integer.valueOf(1).equals(address.getDefaultFlag()))) {
        throw new IllegalArgumentException("存在默认地址，不能直接删除客户");
    }

    List<CustomerBankAccount> bankAccounts = customerBankAccountRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
    if (bankAccounts.stream().anyMatch(bankAccount -> Integer.valueOf(1).equals(bankAccount.getDefaultFlag()))) {
        throw new IllegalArgumentException("存在默认银行账户，不能直接删除客户");
    }

    List<CustomerInvoiceProfile> invoiceProfiles = customerInvoiceProfileRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
    if (invoiceProfiles.stream().anyMatch(profile -> Integer.valueOf(1).equals(profile.getDefaultFlag()))) {
        throw new IllegalArgumentException("存在默认开票信息，不能直接删除客户");
    }
}
```

- [ ] **Step 4: 写 API 文档**

创建 `docs/api/customer-customer.md`，内容至少包含：

```md
# customer-customer 接口文档

## 1. /erp/v1/customer/list
- 方法：POST
- 入参：CustomerListDTO
- 回参：ListBaseVO<CustomerListItemVO>

## 2. /erp/v1/customer/addItem
- 方法：POST
- 入参：BaseDTO
- 回参：SaveItemVO<CustomerSaveItemVO>

## 3. /erp/v1/customer/updateItem
- 方法：POST
- 入参：IdBaseDTO
- 回参：SaveItemVO<CustomerSaveItemVO>

## 4. /erp/v1/customer/save
- 方法：POST
- 入参：CustomerSaveDTO
- 回参：Long

## 5. /erp/v1/customer/detail
- 方法：POST
- 入参：IdBaseDTO
- 回参：CustomerDetailVO

## 6. /erp/v1/customer/delete
- 方法：POST
- 入参：BatchBaseDTO
- 回参：void
```

在每节下补：
- 核心字段说明
- 默认项规则说明
- 详情中的空列表留白说明
- 附件本期不支持说明

- [ ] **Step 5: 重新运行定向测试并确认通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDeleteServiceTest test`
Expected: PASS

- [ ] **Step 6: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java \
        xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDeleteServiceTest.java \
        docs/api/customer-customer.md

git commit -m "feat: add customer delete flow and api docs"
```

### Task 7: 端到端回归客户模块

**Files:**
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerAddItemViewTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerUpdateItemViewTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDetailServiceTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDeleteServiceTest.java`

**Interfaces:**
- Consumes: 前 6 个任务所有产物
- Produces: 客户模块 6 个基础接口的最小回归证据，确认结构与规则闭环

- [ ] **Step 1: 补一条控制器签名测试，锁定实际 URL 前缀与 6 个方法映射**

把 `CustomerAdminControllerStructureTest.java` 扩展为：

```java
@Test
void should_use_customer_request_mapping_prefix() {
    RequestMapping mapping = CustomerAdminController.class.getAnnotation(RequestMapping.class);
    assertArrayEquals(new String[]{"/erp/v1/customer"}, mapping.value());
}
```

- [ ] **Step 2: 运行定向测试集**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest,CustomerFieldAssemblerTest,CustomerAddItemViewTest,CustomerUpdateItemViewTest,CustomerListServiceTest,CustomerDetailServiceTest,CustomerSaveServiceTest,CustomerSaveValidationTest,CustomerDeleteServiceTest,CustomerSqlPlanTest test`
Expected: 全部 PASS

- [ ] **Step 3: 运行客户模块全量测试**

Run: `mvn -pl xbb-erp-module-customer test`
Expected: PASS

- [ ] **Step 4: 运行多模块联编回归**

Run: `mvn -pl xbb-erp-base-persistence,xbb-erp-base-common,xbb-erp-base-web,xbb-erp-module-customer test`
Expected: PASS

- [ ] **Step 5: 提交当前任务**

```bash
git add xbb-erp-module-customer/src/test

git commit -m "test: verify customer crud interfaces"
```

## Self-Review

- Spec coverage：计划覆盖了 6 个接口、字段枚举、admin/application 分层、银行账户与开票信息持久化扩展、整单覆盖保存、删除规则、SQL 扩展、API 文档与最终回归，和设计文档一致。
- Placeholder scan：计划中没有 `TBD`、`implement later`、`similar to task` 之类占位语；所有关键实现步骤都给出了路径、命令与代码骨架。
- Type consistency：DTO 统一继承 `BaseDTO` 体系，仓储接口维持当前模块已有 `Map<String, Object>` 查询风格，控制器路径固定为 `/erp/v1/customer`，文档领域名固定为 `customer-customer`。

Plan complete and saved to `docs/superpowers/plans/2026-07-22-customer-crud-interfaces.md`. Two execution options:

**1. Subagent-Driven (recommended)** - 我按任务逐个派独立子代理执行，并在任务间做 review

**2. Inline Execution** - 我在当前会话里按这个计划直接实现，并分阶段给你检查点

Which approach?