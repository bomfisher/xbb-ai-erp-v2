# Customer Save Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将客户模块现有单一 `/save` 改造为 `saveDraft`、`saveAndSubmit`、`draftList`、`loadDraft` 四个接口，支持 Redis 草稿闭环、双严格度校验，以及统一 `ResultVO.success()` 返回。

**Architecture:** 以现有 `CustomerAdminController` 与 `CustomerAdminAppServiceImpl` 为入口，将保存能力拆成“草稿编排 + 正式提交编排 + 校验器 + 草稿缓存网关”四类职责。请求模型采用客户特化统一壳：`main + ext + draftMeta`，避免把客户主数据强行抽象成单据 `lines`；正式提交继续复用现有 Customer 聚合和四类子表仓储。

**Tech Stack:** Java 21、Spring Boot 3.3.2、Spring Web、Spring Data Redis、Maven、JUnit 5、Testcontainers Redis

## Global Constraints

- 项目架构DDD领域驱动设计。
- 对话永远在中文语境下，注释使用中文。
- 业务对象命名遵循：前端入参 `DTO`、接口出参 `VO`、其余中转参数 `Pojo`。
- 所有接口接口DTO作为参数，而不是散列的参数。非脚本接口，入参DTO都需要继承 `BaseDTO`。
- `userId` / 员工Id是字符串id。
- getter setter用Lombok管理。
- 如果接口业务代码没有需要返回的，用 `BaseVO` 返回。
- 所有接口的参数返回，都使用 `ResultVO.success()` 包装返回。
- 客户保存接口当前不向前端暴露后端数据库主键 `id`。
- 新建页“草稿”按钮需要支持最近 10 条草稿，草稿使用后移除，当前阶段存储在 Redis，TTL 为 7 天。
- 本次只重写客户模块保存链路，不做无关模块重构。

---

## File Structure

- Modify: `pom.xml`
  - 将 `xbb-erp-scene-meta`、`xbb-erp-module-common`、`xbb-erp-module-customer` 纳入当前工作区聚合构建，保证后续测试命令可执行。
- Modify: `xbb-erp-module-customer/pom.xml`
  - 引入 `xbb-erp-base-cache` 依赖，为草稿 Redis 网关提供 `RedisTemplate`。
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/vo/BaseVO.java`
  - 统一无业务数据返回对象。
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
  - 删除 `save`，新增 `saveDraft`、`saveAndSubmit`、`draftList`、`loadDraft`。
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`
  - 替换旧 `save` 接口定义，声明四个新方法。
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
  - 保留列表、详情、删除逻辑；接入新保存编排与草稿网关。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftMetaDTO.java`
  - 草稿元数据 DTO，承载 `draftCode` 与 `draftTitle`。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerSaveExtDTO.java`
  - 聚合四类子表 DTO。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftSaveDTO.java`
  - 草稿保存入参。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerSubmitSaveDTO.java`
  - 正式保存入参。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftListDTO.java`
  - 草稿列表入参。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftLoadDTO.java`
  - 草稿加载入参。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftListItemVO.java`
  - 草稿列表摘要返回。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftDetailVO.java`
  - 草稿详情返回。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/pojo/CustomerSaveDraftPojo.java`
  - 草稿内容缓存对象。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/pojo/CustomerSaveContextPojo.java`
  - 保存编排上下文对象。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveProtocolValidator.java`
  - 协议校验器。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveCommonValidator.java`
  - 通用校验器。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveBusinessValidator.java`
  - 业务校验器。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerDraftRepository.java`
  - 草稿缓存仓储接口。
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImpl.java`
  - 基于 `RedisTemplate<String, Object>` 的 Redis 实现。
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java`
  - 增加新 DTO 与草稿对象/VO 之间的装配。
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerDraftRepository.java`
  - 应用层单元测试用内存草稿仓储。
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
  - 更新控制器结构断言。
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`
  - 覆盖草稿保存/列表/加载/清理。
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`
  - 覆盖正式保存与草稿清理行为。
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
  - 覆盖双严格度校验。
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImplTest.java`
  - 基于 Testcontainers Redis 验证 TTL、最近 10 条与删除行为。
- Modify: `docs/superpowers/specs/2026-07-27-customer-save-redesign-design.md`
  - 如实现期发现命名需与代码对齐，回填最终接口名与测试命令。

### Task 1: 修正工作区聚合与公共返回对象

**Files:**
- Modify: `pom.xml`
- Modify: `xbb-erp-module-customer/pom.xml`
- Create: `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/vo/BaseVO.java`
- Test: `mvn -pl xbb-erp-base-common,xbb-erp-module-customer -am -DskipTests compile`

**Interfaces:**
- Consumes: `ResultVO.success(T data)` from `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/vo/ResultVO.java`
- Produces:
  - `public class BaseVO {}`
  - customer 模块新增对 `xbb-erp-base-cache` 的依赖
  - 根 `pom.xml` 包含 `xbb-erp-scene-meta`、`xbb-erp-module-common`、`xbb-erp-module-customer`

- [ ] **Step 1: 写出会失败的编译前置条件检查**

```xml
<!-- pom.xml 预期模块片段 -->
<modules>
    <module>xbb-erp-base-common</module>
    <module>xbb-erp-base-web</module>
    <module>xbb-erp-base-tenant</module>
    <module>xbb-erp-base-persistence</module>
    <module>xbb-erp-base-cache</module>
    <module>xbb-erp-base-idgen</module>
    <module>xbb-erp-base-log</module>
    <module>xbb-erp-base-test</module>
    <module>xbb-erp-codegen</module>
    <module>xbb-erp-scene-meta</module>
    <module>xbb-erp-module-common</module>
    <module>xbb-erp-module-customer</module>
    <module>xbb-erp-module-supplier</module>
    <module>xbb-erp-module-product</module>
    <module>xbb-erp-module-purchase</module>
    <module>xbb-erp-app-admin</module>
    <module>xbb-erp-app-mobile</module>
    <module>xbb-erp-app-job</module>
</modules>
```

- [ ] **Step 2: 运行编译命令确认当前失败**

Run: `mvn -pl xbb-erp-module-customer -am -DskipTests compile`
Expected: FAIL，提示聚合模块或 customer 直接依赖模块未纳入 reactor，或 `BaseVO` 缺失导致后续代码无法编译。

- [ ] **Step 3: 写最小实现使编译前置条件满足**

```xml
<!-- xbb-erp-module-customer/pom.xml 追加依赖 -->
<dependency>
    <groupId>org.bomfish</groupId>
    <artifactId>xbb-erp-base-cache</artifactId>
    <version>${project.version}</version>
</dependency>
```

```java
package xbb.ai.erp.base.common.vo;

import lombok.Data;

@Data
public class BaseVO {
}
```

- [ ] **Step 4: 再次运行编译验证通过**

Run: `mvn -pl xbb-erp-base-common,xbb-erp-module-customer -am -DskipTests compile`
Expected: PASS，customer 模块与公共返回对象均可编译。

- [ ] **Step 5: Commit**

```bash
git add pom.xml xbb-erp-module-customer/pom.xml xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/vo/BaseVO.java
git commit -m "feat: add customer save base response support"
```

### Task 2: 定义新保存接口的 DTO 与 VO 契约

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftMetaDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerSaveExtDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftSaveDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerSubmitSaveDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftListDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto/CustomerDraftLoadDTO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftListItemVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftDetailVO.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`

**Interfaces:**
- Consumes:
  - `CustomerMainDTO`
  - `CustomerContactItemDTO`
  - `CustomerAddressItemDTO`
  - `CustomerBankAccountItemDTO`
  - `CustomerInvoiceProfileItemDTO`
- Produces:
  - `CustomerDraftSaveDTO#getMain(): CustomerMainDTO`
  - `CustomerDraftSaveDTO#getExt(): CustomerSaveExtDTO`
  - `CustomerDraftSaveDTO#getDraftMeta(): CustomerDraftMetaDTO`
  - `CustomerSubmitSaveDTO#getMain(): CustomerMainDTO`
  - `CustomerDraftListDTO extends BaseDTO`
  - `CustomerDraftLoadDTO#getDraftCode(): String`
  - `CustomerDraftListItemVO` with fields `draftCode`, `draftTitle`, `customerCode`, `customerName`, `updatedTime`
  - `CustomerDraftDetailVO` with fields `main`, `ext`, `draftMeta`

- [ ] **Step 1: 先写控制器结构失败用例**

```java
package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerAdminControllerStructureTest {

    @Test
    void should_replace_single_save_with_draft_and_submit_endpoints() throws Exception {
        Method saveDraft = CustomerAdminController.class.getMethod("saveDraft", xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO.class);
        Method saveAndSubmit = CustomerAdminController.class.getMethod("saveAndSubmit", xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO.class);
        Method draftList = CustomerAdminController.class.getMethod("draftList", xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO.class);
        Method loadDraft = CustomerAdminController.class.getMethod("loadDraft", xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO.class);

        assertNotNull(saveDraft);
        assertNotNull(saveAndSubmit);
        assertNotNull(draftList);
        assertNotNull(loadDraft);
        assertThrows(NoSuchMethodException.class,
            () -> CustomerAdminController.class.getMethod("save", xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO.class));
    }
}
```

- [ ] **Step 2: 运行结构测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest test`
Expected: FAIL，提示 `saveDraft`、`saveAndSubmit`、`draftList`、`loadDraft` 方法不存在。

- [ ] **Step 3: 写最小 DTO/VO 契约实现**

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;

@Data
public class CustomerDraftMetaDTO {
    private String draftCode;
    private String draftTitle;
}
```

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerSaveExtDTO {
    private List<CustomerContactItemDTO> contacts = new ArrayList<>();
    private List<CustomerAddressItemDTO> addresses = new ArrayList<>();
    private List<CustomerBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<CustomerInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
```

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDraftSaveDTO extends BaseDTO {
    private CustomerMainDTO main;
    private CustomerSaveExtDTO ext = new CustomerSaveExtDTO();
    private CustomerDraftMetaDTO draftMeta = new CustomerDraftMetaDTO();
}
```

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerSubmitSaveDTO extends BaseDTO {
    private CustomerMainDTO main;
    private CustomerSaveExtDTO ext = new CustomerSaveExtDTO();
    private CustomerDraftMetaDTO draftMeta = new CustomerDraftMetaDTO();
}
```

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDraftListDTO extends BaseDTO {
}
```

```java
package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDraftLoadDTO extends BaseDTO {
    private String draftCode;
}
```

```java
package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDraftListItemVO {
    private String draftCode;
    private String draftTitle;
    private String customerCode;
    private String customerName;
    private LocalDateTime updatedTime;
}
```

```java
package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftMetaDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveExtDTO;

@Data
public class CustomerDraftDetailVO {
    private CustomerMainDTO main;
    private CustomerSaveExtDTO ext;
    private CustomerDraftMetaDTO draftMeta;
}
```

- [ ] **Step 4: 运行结构测试验证 DTO/VO 可被控制器引用**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest test`
Expected: 仍然 FAIL，但失败原因只剩控制器方法未实现；DTO/VO 编译通过。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/dto xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java
git commit -m "feat: define customer draft save contracts"
```

### Task 3: 引入草稿仓储与应用层内存测试替身

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/pojo/CustomerSaveDraftPojo.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerDraftRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerDraftRepository.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`

**Interfaces:**
- Consumes:
  - `CustomerDraftSaveDTO`
  - `CustomerDraftListDTO`
  - `CustomerDraftLoadDTO`
- Produces:
  - `CustomerSaveDraftPojo` fields `corpid: String`, `draftCode: String`, `draftTitle: String`, `main: CustomerMainDTO`, `ext: CustomerSaveExtDTO`, `updatedTime: LocalDateTime`
  - `CustomerDraftRepository#saveDraft(CustomerSaveDraftPojo draft): String`
  - `CustomerDraftRepository#listDrafts(String corpid, int limit): List<CustomerSaveDraftPojo>`
  - `CustomerDraftRepository#loadDraft(String corpid, String draftCode): CustomerSaveDraftPojo`
  - `CustomerDraftRepository#removeDraft(String corpid, String draftCode): void`

- [ ] **Step 1: 先写草稿行为失败测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerDraftRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerDraftServiceTest {

    @Test
    void should_save_list_and_load_customer_draft() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerName("草稿客户");

        CustomerDraftSaveDTO saveDTO = new CustomerDraftSaveDTO();
        saveDTO.setCorpid("corp-001");
        saveDTO.setMain(main);

        service.saveDraft(saveDTO);

        CustomerDraftListDTO listDTO = new CustomerDraftListDTO();
        listDTO.setCorpid("corp-001");
        assertEquals(1, service.draftList(listDTO).size());

        CustomerDraftLoadDTO loadDTO = new CustomerDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(service.draftList(listDTO).get(0).getDraftCode());
        assertEquals("草稿客户", service.loadDraft(loadDTO).getMain().getCustomerName());
    }
}
```

- [ ] **Step 2: 运行草稿行为测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftServiceTest test`
Expected: FAIL，提示 `forTesting(..., InMemoryCustomerDraftRepository)`、`saveDraft`、`draftList`、`loadDraft` 尚不存在。

- [ ] **Step 3: 写最小草稿仓储契约与内存替身**

```java
package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;

import java.util.List;

public interface CustomerDraftRepository {
    String saveDraft(CustomerSaveDraftPojo draft);

    List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit);

    CustomerSaveDraftPojo loadDraft(String corpid, String draftCode);

    void removeDraft(String corpid, String draftCode);
}
```

```java
package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerDraftRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryCustomerDraftRepository implements CustomerDraftRepository {

    private final Map<String, CustomerSaveDraftPojo> store = new LinkedHashMap<>();

    @Override
    public String saveDraft(CustomerSaveDraftPojo draft) {
        String draftCode = draft.getDraftCode() == null || draft.getDraftCode().isBlank()
            ? UUID.randomUUID().toString().replace("-", "")
            : draft.getDraftCode();
        draft.setDraftCode(draftCode);
        draft.setUpdatedTime(LocalDateTime.now());
        store.put(key(draft.getCorpid(), draftCode), draft);
        return draftCode;
    }

    @Override
    public List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit) {
        return store.values().stream()
            .filter(item -> corpid.equals(item.getCorpid()))
            .sorted(Comparator.comparing(CustomerSaveDraftPojo::getUpdatedTime).reversed())
            .limit(limit)
            .toList();
    }

    @Override
    public CustomerSaveDraftPojo loadDraft(String corpid, String draftCode) {
        return store.get(key(corpid, draftCode));
    }

    @Override
    public void removeDraft(String corpid, String draftCode) {
        store.remove(key(corpid, draftCode));
    }

    private String key(String corpid, String draftCode) {
        return corpid + ":" + draftCode;
    }
}
```

- [ ] **Step 4: 运行草稿行为测试验证只剩服务编排未实现**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftServiceTest test`
Expected: FAIL，但失败应收敛到 `CustomerAdminAppServiceImpl` 还未接入草稿仓储与新方法。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/pojo/CustomerSaveDraftPojo.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerDraftRepository.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerDraftRepository.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java
git commit -m "feat: add customer draft repository contract"
```

### Task 4: 重写控制器与应用服务接口为四个新端点

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`

**Interfaces:**
- Consumes:
  - `CustomerDraftSaveDTO`
  - `CustomerSubmitSaveDTO`
  - `CustomerDraftListDTO`
  - `CustomerDraftLoadDTO`
  - `ResultVO.success(T data)`
- Produces:
  - `CustomerAdminController#saveDraft(CustomerDraftSaveDTO dto): ResultVO<BaseVO>`
  - `CustomerAdminController#saveAndSubmit(CustomerSubmitSaveDTO dto): ResultVO<BaseVO>`
  - `CustomerAdminController#draftList(CustomerDraftListDTO dto): ResultVO<List<CustomerDraftListItemVO>>`
  - `CustomerAdminController#loadDraft(CustomerDraftLoadDTO dto): ResultVO<CustomerDraftDetailVO>`
  - `CustomerAdminAppService` 对应四个方法签名

- [ ] **Step 1: 使用已有结构测试作为失败用例**

```java
Method saveDraft = CustomerAdminController.class.getMethod("saveDraft", CustomerDraftSaveDTO.class);
Method saveAndSubmit = CustomerAdminController.class.getMethod("saveAndSubmit", CustomerSubmitSaveDTO.class);
Method draftList = CustomerAdminController.class.getMethod("draftList", CustomerDraftListDTO.class);
Method loadDraft = CustomerAdminController.class.getMethod("loadDraft", CustomerDraftLoadDTO.class);
```

- [ ] **Step 2: 运行结构测试确认当前失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest test`
Expected: FAIL，提示新方法不存在或返回值不匹配。

- [ ] **Step 3: 写最小控制器与服务接口实现**

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
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;

import java.util.List;

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

    @PostMapping("/saveDraft")
    public ResultVO<BaseVO> saveDraft(@RequestBody CustomerDraftSaveDTO dto) {
        return ResultVO.success(customerAdminAppService.saveDraft(dto));
    }

    @PostMapping("/saveAndSubmit")
    public ResultVO<BaseVO> saveAndSubmit(@RequestBody CustomerSubmitSaveDTO dto) {
        return ResultVO.success(customerAdminAppService.saveAndSubmit(dto));
    }

    @PostMapping("/draftList")
    public ResultVO<List<CustomerDraftListItemVO>> draftList(@RequestBody CustomerDraftListDTO dto) {
        return ResultVO.success(customerAdminAppService.draftList(dto));
    }

    @PostMapping("/loadDraft")
    public ResultVO<CustomerDraftDetailVO> loadDraft(@RequestBody CustomerDraftLoadDTO dto) {
        return ResultVO.success(customerAdminAppService.loadDraft(dto));
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

```java
BaseVO saveDraft(CustomerDraftSaveDTO dto);

BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto);

List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto);

CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto);
```

- [ ] **Step 4: 运行结构测试验证通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest test`
Expected: PASS。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java
git commit -m "feat: replace customer save endpoint set"
```

### Task 5: 拆出协议/通用/业务校验器并接入正式保存

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/pojo/CustomerSaveContextPojo.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveProtocolValidator.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveCommonValidator.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveBusinessValidator.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`

**Interfaces:**
- Consumes:
  - `CustomerDraftSaveDTO`
  - `CustomerSubmitSaveDTO`
  - `CustomerSaveExtDTO`
- Produces:
  - `CustomerSaveContextPojo` fields `corpid`, `main`, `ext`, `draftMeta`, `submitMode`
  - `CustomerSaveProtocolValidator#validate(CustomerSaveContextPojo context): void`
  - `CustomerSaveCommonValidator#validateForDraft(CustomerSaveContextPojo context): void`
  - `CustomerSaveCommonValidator#validateForSubmit(CustomerSaveContextPojo context): void`
  - `CustomerSaveBusinessValidator#validateForSubmit(CustomerSaveContextPojo context): void`

- [ ] **Step 1: 先写双严格度失败测试**

```java
package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerDraftRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerSaveValidationTest {

    @Test
    void should_allow_incomplete_payload_for_draft_but_reject_submit() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerName("仅草稿");

        CustomerDraftSaveDTO draftDTO = new CustomerDraftSaveDTO();
        draftDTO.setCorpid("corp-001");
        draftDTO.setMain(main);

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);

        assertDoesNotThrow(() -> service.saveDraft(draftDTO));
        assertThrows(IllegalArgumentException.class, () -> service.saveAndSubmit(submitDTO));
    }
}
```

- [ ] **Step 2: 运行校验测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveValidationTest test`
Expected: FAIL，原因是新方法/校验器未实现，或 `saveAndSubmit` 尚未区分严格度。

- [ ] **Step 3: 写最小校验器实现**

```java
package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;

public class CustomerSaveProtocolValidator {
    public void validate(CustomerSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new IllegalArgumentException("客户主档不能为空");
        }
    }
}
```

```java
package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveExtDTO;

public class CustomerSaveCommonValidator {

    public void validateForDraft(CustomerSaveContextPojo context) {
        validateDefaultUniqueness(context.getExt());
    }

    public void validateForSubmit(CustomerSaveContextPojo context) {
        if (context.getMain().getCustomerCode() == null || context.getMain().getCustomerCode().isBlank()) {
            throw new IllegalArgumentException("客户编码不能为空");
        }
        if (context.getMain().getCustomerName() == null || context.getMain().getCustomerName().isBlank()) {
            throw new IllegalArgumentException("客户名称不能为空");
        }
        validateDefaultUniqueness(context.getExt());
    }

    private void validateDefaultUniqueness(CustomerSaveExtDTO ext) {
        if (ext == null) {
            return;
        }
        long defaultContacts = ext.getContacts().stream().filter(item -> Integer.valueOf(1).equals(item.getDefaultFlag())).count();
        if (defaultContacts > 1) {
            throw new IllegalArgumentException("联系人默认项只能有一个");
        }
    }
}
```

```java
package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

public class CustomerSaveBusinessValidator {

    private final CustomerRepository customerRepository;

    public CustomerSaveBusinessValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateForSubmit(CustomerSaveContextPojo context) {
        // 首版只保留领域扩展点；唯一性规则可在后续任务中按现有仓储查询补全
    }
}
```

- [ ] **Step 4: 运行校验测试验证通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveValidationTest test`
Expected: PASS，证明 `saveDraft` 与 `saveAndSubmit` 严格度不同。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/pojo/CustomerSaveContextPojo.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java
git commit -m "feat: split customer save validators"
```

### Task 6: 接入草稿保存、列表、加载编排

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`

**Interfaces:**
- Consumes:
  - `CustomerDraftRepository`
  - `CustomerSaveProtocolValidator`
  - `CustomerSaveCommonValidator`
- Produces:
  - `CustomerAdminAppServiceImpl#saveDraft(CustomerDraftSaveDTO dto): BaseVO`
  - `CustomerAdminAppServiceImpl#draftList(CustomerDraftListDTO dto): List<CustomerDraftListItemVO>`
  - `CustomerAdminAppServiceImpl#loadDraft(CustomerDraftLoadDTO dto): CustomerDraftDetailVO`
  - `CustomerAdminAssembler#toDraftPojo(CustomerDraftSaveDTO dto): CustomerSaveDraftPojo`
  - `CustomerAdminAssembler#toDraftListItemVO(CustomerSaveDraftPojo pojo): CustomerDraftListItemVO`
  - `CustomerAdminAssembler#toDraftDetailVO(CustomerSaveDraftPojo pojo): CustomerDraftDetailVO`

- [ ] **Step 1: 使用 Task 3 的草稿测试作为失败用例**

```java
service.saveDraft(saveDTO);
assertEquals(1, service.draftList(listDTO).size());
assertEquals("草稿客户", service.loadDraft(loadDTO).getMain().getCustomerName());
```

- [ ] **Step 2: 运行草稿测试确认当前失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftServiceTest test`
Expected: FAIL，提示 `CustomerAdminAppServiceImpl` 未实现草稿编排。

- [ ] **Step 3: 写最小草稿编排实现**

```java
@Override
public BaseVO saveDraft(CustomerDraftSaveDTO dto) {
    CustomerSaveContextPojo context = CustomerAdminAssembler.toDraftContext(dto);
    protocolValidator.validate(context);
    commonValidator.validateForDraft(context);
    CustomerSaveDraftPojo draft = CustomerAdminAssembler.toDraftPojo(dto);
    String draftCode = customerDraftRepository.saveDraft(draft);
    if (dto.getDraftMeta() != null) {
        dto.getDraftMeta().setDraftCode(draftCode);
    }
    return new BaseVO();
}

@Override
public List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto) {
    return customerDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
        .map(CustomerAdminAssembler::toDraftListItemVO)
        .toList();
}

@Override
public CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto) {
    return CustomerAdminAssembler.toDraftDetailVO(
        customerDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftCode())
    );
}
```

- [ ] **Step 4: 运行草稿测试验证通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftServiceTest test`
Expected: PASS。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java
git commit -m "feat: add customer draft application flow"
```

### Task 7: 接入正式保存编排并在成功后删除草稿

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`

**Interfaces:**
- Consumes:
  - `CustomerSubmitSaveDTO`
  - `CustomerDraftRepository#removeDraft(String corpid, String draftCode)`
  - 现有 `CustomerRepository` 与四类子表仓储 `insert/update/removeById/removeBatchByIds/findByCondition`
- Produces:
  - `CustomerAdminAppServiceImpl#saveAndSubmit(CustomerSubmitSaveDTO dto): BaseVO`
  - `CustomerAdminAssembler#toCustomer(CustomerSubmitSaveDTO dto): Customer`
  - `CustomerAdminAssembler#toCustomerContact(String corpid, Long customerId, CustomerContactItemDTO item): CustomerContact`
  - 复用现有 `syncContacts/syncAddresses/syncBankAccounts/syncInvoiceProfiles`

- [ ] **Step 1: 先扩展正式保存失败测试**

```java
@Test
void should_remove_draft_after_submit_success() {
    InMemoryCustomerDraftRepository draftRepository = new InMemoryCustomerDraftRepository();
    CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
        new InMemoryCustomerRepository(),
        new InMemoryCustomerContactRepository(),
        new InMemoryCustomerAddressRepository(),
        new InMemoryCustomerBankAccountRepository(),
        new InMemoryCustomerInvoiceProfileRepository(),
        draftRepository
    );

    CustomerMainDTO main = new CustomerMainDTO();
    main.setCustomerCode("CUST-001");
    main.setCustomerName("杭州客户");
    main.setCustomerCategory("A");
    main.setBizStatus("ENABLED");

    CustomerDraftSaveDTO draftDTO = new CustomerDraftSaveDTO();
    draftDTO.setCorpid("corp-001");
    draftDTO.setMain(main);
    service.saveDraft(draftDTO);

    String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

    CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
    submitDTO.setCorpid("corp-001");
    submitDTO.setMain(main);
    submitDTO.getDraftMeta().setDraftCode(draftCode);

    service.saveAndSubmit(submitDTO);

    assertTrue(draftRepository.listDrafts("corp-001", 10).isEmpty());
}
```

- [ ] **Step 2: 运行正式保存测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveServiceTest test`
Expected: FAIL，提示 `saveAndSubmit` 未实现或未清理草稿。

- [ ] **Step 3: 写最小正式保存编排实现**

```java
@Override
public BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto) {
    CustomerSaveContextPojo context = CustomerAdminAssembler.toSubmitContext(dto);
    protocolValidator.validate(context);
    commonValidator.validateForSubmit(context);
    businessValidator.validateForSubmit(context);

    Customer customer = CustomerAdminAssembler.toCustomer(dto);
    if (customer.getId() == null) {
        customerRepository.insert(customer);
    } else {
        customerRepository.update(customer);
    }

    Long customerId = customer.getId();
    syncContacts(dto.getCorpid(), customerId, dto.getExt().getContacts());
    syncAddresses(dto.getCorpid(), customerId, dto.getExt().getAddresses());
    syncBankAccounts(dto.getCorpid(), customerId, dto.getExt().getBankAccounts());
    syncInvoiceProfiles(dto.getCorpid(), customerId, dto.getExt().getInvoiceProfiles());

    if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
        customerDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
    }
    return new BaseVO();
}
```

- [ ] **Step 4: 运行正式保存测试验证通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveServiceTest test`
Expected: PASS。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java
git commit -m "feat: submit customer save and clear drafts"
```

### Task 8: 实现 Redis 草稿仓储并验证 TTL/最近10条行为

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImpl.java`
- Create: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImplTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImplTest.java`

**Interfaces:**
- Consumes:
  - `RedisTemplate<String, Object>` from `xbb-erp-base-cache/src/main/java/xbb/ai/erp/base/cache/config/RedisAutoConfiguration.java`
  - `CustomerDraftRepository`
- Produces:
  - `CustomerDraftRepositoryImpl(RedisTemplate<String, Object> redisTemplate)`
  - 草稿内容键：`customer:draft:{corpid}:{draftCode}`
  - 草稿索引键：`customer:draft:index:{corpid}`
  - 每次保存刷新 TTL 为 `Duration.ofDays(7)`

- [ ] **Step 1: 先写 Redis 仓储失败测试**

```java
package xbb.ai.erp.module.customer.infrastructure.cache.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import xbb.ai.erp.base.test.container.RedisContainerSupport;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerDraftRepositoryImplTest extends RedisContainerSupport {

    @Test
    void should_keep_only_latest_ten_drafts_and_remove_used_draft() {
        RedisTemplate<String, Object> redisTemplate = TestRedisTemplateFactory.create(redisContainer());
        CustomerDraftRepositoryImpl repository = new CustomerDraftRepositoryImpl(redisTemplate);

        IntStream.rangeClosed(1, 12).forEach(index -> {
            CustomerSaveDraftPojo draft = new CustomerSaveDraftPojo();
            draft.setCorpid("corp-001");
            draft.setDraftCode("draft-" + index);
            draft.setDraftTitle("草稿" + index);
            draft.setUpdatedTime(LocalDateTime.now().plusSeconds(index));
            repository.saveDraft(draft);
        });

        assertEquals(10, repository.listDrafts("corp-001", 10).size());
        repository.removeDraft("corp-001", "draft-12");
        assertNull(repository.loadDraft("corp-001", "draft-12"));
    }
}
```

- [ ] **Step 2: 运行 Redis 仓储测试确认失败**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftRepositoryImplTest test`
Expected: FAIL，提示 Redis 仓储实现或测试工厂不存在。

- [ ] **Step 3: 写最小 Redis 仓储实现**

```java
package xbb.ai.erp.module.customer.infrastructure.cache.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerDraftRepository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomerDraftRepositoryImpl implements CustomerDraftRepository {

    private static final Duration DRAFT_TTL = Duration.ofDays(7);
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String saveDraft(CustomerSaveDraftPojo draft) {
        redisTemplate.opsForValue().set(contentKey(draft.getCorpid(), draft.getDraftCode()), draft, DRAFT_TTL);
        redisTemplate.opsForZSet().add(indexKey(draft.getCorpid()), draft.getDraftCode(), draft.getUpdatedTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        trimIndex(draft.getCorpid());
        return draft.getDraftCode();
    }

    @Override
    public List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit) {
        return Objects.requireNonNull(redisTemplate.opsForZSet().reverseRange(indexKey(corpid), 0, limit - 1)).stream()
            .map(item -> loadDraft(corpid, String.valueOf(item)))
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public CustomerSaveDraftPojo loadDraft(String corpid, String draftCode) {
        return (CustomerSaveDraftPojo) redisTemplate.opsForValue().get(contentKey(corpid, draftCode));
    }

    @Override
    public void removeDraft(String corpid, String draftCode) {
        redisTemplate.delete(contentKey(corpid, draftCode));
        redisTemplate.opsForZSet().remove(indexKey(corpid), draftCode);
    }

    private void trimIndex(String corpid) {
        Long size = redisTemplate.opsForZSet().zCard(indexKey(corpid));
        if (size != null && size > 10) {
            redisTemplate.opsForZSet().removeRange(indexKey(corpid), 0, size - 11);
        }
    }

    private String contentKey(String corpid, String draftCode) {
        return "customer:draft:" + corpid + ":" + draftCode;
    }

    private String indexKey(String corpid) {
        return "customer:draft:index:" + corpid;
    }
}
```

- [ ] **Step 4: 运行 Redis 仓储测试验证通过**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftRepositoryImplTest test`
Expected: PASS。

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImpl.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/cache/repository/CustomerDraftRepositoryImplTest.java
git commit -m "feat: add redis customer draft repository"
```

### Task 9: 回归测试、文档回填与接口文档更新触发点

**Files:**
- Modify: `docs/superpowers/specs/2026-07-27-customer-save-redesign-design.md`
- Modify: `docs/superpowers/plans/2026-07-27-customer-save-redesign.md`
- Test: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest,CustomerDraftServiceTest,CustomerSaveServiceTest,CustomerSaveValidationTest,CustomerDraftRepositoryImplTest test`

**Interfaces:**
- Consumes: 全部前序任务产出
- Produces:
  - 更新后的 spec / plan 中的最终命令与类名
  - 一组可复现的回归测试命令
  - 明确后续执行 `gen-api-md` 的触发条件

- [ ] **Step 1: 写回归测试清单并确认覆盖 spec 要求**

```text
1. 控制器结构：旧 save 删除，新四接口存在
2. 草稿行为：保存、列表、加载、清理
3. 正式保存：主表与四类子表同步
4. 双严格度：draft 可放宽，submit 必完整
5. Redis 行为：最近 10 条、删除、TTL
```

- [ ] **Step 2: 运行完整回归命令确认当前结果**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest,CustomerDraftServiceTest,CustomerSaveServiceTest,CustomerSaveValidationTest,CustomerDraftRepositoryImplTest test`
Expected: PASS。

- [ ] **Step 3: 回填文档中的最终接口名、测试命令与实现偏差**

```markdown
- 若最终实现中 `CustomerDraftRepositoryImplTest` 需要额外测试工厂类，则把实际类名补回 spec。
- 若 `BaseVO` 最终新增字段，更新 spec 第 3.2 与第 4 节。
- 若正式保存增加了唯一性校验仓储查询，把规则补充到 spec 第 5.3 节。
```

- [ ] **Step 4: 运行文档与代码最终一致性检查**

Run: `rg -n "save\(|saveDraft|saveAndSubmit|draftList|loadDraft|BaseVO|draftCode" docs/superpowers/specs/2026-07-27-customer-save-redesign-design.md docs/superpowers/plans/2026-07-27-customer-save-redesign.md xbb-erp-module-customer/src/main/java -g '*.md' -g '*.java'`
Expected: PASS，输出中不再出现旧 `/save` 作为有效接口，文档与代码命名一致。

- [ ] **Step 5: Commit**

```bash
git add docs/superpowers/specs/2026-07-27-customer-save-redesign-design.md docs/superpowers/plans/2026-07-27-customer-save-redesign.md
git commit -m "docs: finalize customer save redesign plan"
```

## Self-Review

- **Spec coverage:**
  - 双接口拆分 → Task 4、Task 7
  - 草稿列表与加载闭环 → Task 3、Task 6、Task 8
  - Redis 7 天 TTL 与最近 10 条 → Task 8
  - 不暴露后端 `id`、保存返回 `BaseVO` → Task 1、Task 4
  - 双严格度校验 → Task 5
  - 正式保存后移除草稿 → Task 7
- **Placeholder scan:** 已避免 `TODO` / `TBD` / “自行处理” 占位；每个任务都给出具体代码骨架和命令。
- **Type consistency:** 全计划统一使用 `CustomerDraftSaveDTO`、`CustomerSubmitSaveDTO`、`CustomerDraftListDTO`、`CustomerDraftLoadDTO`、`CustomerDraftRepository`、`CustomerSaveContextPojo`、`BaseVO` 这些最终名称。
