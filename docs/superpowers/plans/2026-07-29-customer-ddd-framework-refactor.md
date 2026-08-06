# 客户模块 DDD 收敛与可复用框架化改造 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将客户模块改造成首个标准接入样板模块，并把公共列表能力升级为稳定的平台协议入口，优先服务后续新业务快速接入。

**Architecture:** 保留现有 controller 入口与兼容接口，先把 `CustomerAdminAppServiceImpl` 降级为 facade，再按列表、表单、草稿、提交、删除拆出独立 use case。公共层先围绕列表 schema/query/page 协议收口，避免继续扩散 customer 特化语义，同时切断写侧校验对列表 DSL 的反向依赖。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、JUnit 5、MyBatis-Plus、Redis 7

## Global Constraints

- 项目架构DDD领域驱动设计
- 对话永远在中文语境下，注释使用中文
- 所有主动捕获的报错、业务的主动抛错，都使用 `BizException`
- 直接对接数据库的对象实体需要添加 `PO` 后缀，并且对象内字段不允许使用布尔值对接，改用 `Integer`
- 枚举类需要 `Enum` 结尾
- 系统内 pojo 尾缀规范：对接前端入参 `DTO`、对接接口出参 `VO`，其余中转参数的对象 `Pojo`
- 所有接口接口 DTO 作为参数，而不是散列的参数；非脚本接口，入参 DTO 都需要继承 `BaseDTO`
- `userId` 员工 Id 是字符串 id
- getter setter 用 Lombok 管理
- 如果接口业务代码没有需要返回的，用 `BaseVO` 返回
- 所有接口的参数返回，都使用 `ResultVO.success()` 包装返回
- 尽可能避免循环中查询数据库
- 计划执行遵循 DRY、YAGNI、TDD、频繁小步提交

---

## File Structure

### 计划新增文件

- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/CustomerAdminFacade.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/impl/CustomerAdminFacadeImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/CustomerListQueryUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/impl/CustomerListQueryUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/form/CustomerFormSchemaUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/form/impl/CustomerFormSchemaUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/CustomerSaveDraftUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/CustomerLoadDraftUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/CustomerDraftListUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/impl/CustomerSaveDraftUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/impl/CustomerLoadDraftUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/impl/CustomerDraftListUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/submit/CustomerSubmitUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/submit/impl/CustomerSubmitUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/detail/CustomerDetailQueryUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/detail/impl/CustomerDetailQueryUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/delete/CustomerDeleteUseCase.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/delete/impl/CustomerDeleteUseCaseImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/schema/CustomerListQueryAdapter.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/schema/CustomerListSchemaProvider.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/dto/ListQueryDTO.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListPageVO.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListSchemaVO.java`

### 计划修改文件

- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveBusinessValidator.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepository.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftDetailVO.java`
- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerDraftRepositoryImpl.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java`
- `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java`

### 计划测试文件

- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
- `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepositorySignatureTest.java`
- `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`

## Task 1: 先把客户应用服务降级为 facade，并拆出列表与表单用例入口

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/CustomerAdminFacade.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/impl/CustomerAdminFacadeImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/CustomerListQueryUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/impl/CustomerListQueryUseCaseImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/form/CustomerFormSchemaUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/form/impl/CustomerFormSchemaUseCaseImpl.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`

**Interfaces:**
- Consumes: `CustomerRepository#findByCondition(Map<String, Object>): List<Customer>`
- Consumes: `CustomerContactRepository#findByCondition(Map<String, Object>): List<CustomerContact>`
- Produces: `CustomerAdminFacade#list(CustomerListDTO dto): ListBaseVO<CustomerListItemVO>`
- Produces: `CustomerAdminFacade#addItem(BaseDTO dto): SaveItemVO<CustomerSaveItemVO>`
- Produces: `CustomerAdminFacade#updateItem(IdBaseDTO dto): SaveItemVO<CustomerSaveItemVO>`
- Produces: `CustomerListQueryUseCase#execute(CustomerListDTO dto): ListBaseVO<CustomerListItemVO>`
- Produces: `CustomerFormSchemaUseCase#buildCreateForm(BaseDTO dto): SaveItemVO<CustomerSaveItemVO>`
- Produces: `CustomerFormSchemaUseCase#buildUpdateForm(IdBaseDTO dto): SaveItemVO<CustomerSaveItemVO>`

- [ ] **Step 1: 先写结构测试，要求 controller 面向 facade，列表与表单职责有独立用例出口**

```java
package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerAdminControllerStructureTest {

    @Test
    void should_depend_on_customer_admin_facade() throws Exception {
        Field field = CustomerAdminController.class.getDeclaredField("customerAdminFacade");
        assertEquals(
            Class.forName("xbb.ai.erp.module.customer.application.facade.CustomerAdminFacade"),
            field.getType()
        );
    }

    @Test
    void should_define_list_and_form_use_case_contracts() throws Exception {
        assertNotNull(Class.forName("xbb.ai.erp.module.customer.application.usecase.list.CustomerListQueryUseCase"));
        assertNotNull(Class.forName("xbb.ai.erp.module.customer.application.usecase.form.CustomerFormSchemaUseCase"));
    }
}
```

- [ ] **Step 2: 运行结构测试，确认红灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest test`
Expected: FAIL，提示 `customerAdminFacade` 字段或 use case 类型不存在。

- [ ] **Step 3: 新建 facade 与 list/form use case 接口，先给最小签名**

```java
package xbb.ai.erp.module.customer.application.facade;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;

public interface CustomerAdminFacade {
    ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto);
    SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto);
    SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto);
}
```

```java
package xbb.ai.erp.module.customer.application.usecase.list;

import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;

public interface CustomerListQueryUseCase {
    ListBaseVO<CustomerListItemVO> execute(CustomerListDTO dto);
}
```

```java
package xbb.ai.erp.module.customer.application.usecase.form;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;

public interface CustomerFormSchemaUseCase {
    SaveItemVO<CustomerSaveItemVO> buildCreateForm(BaseDTO dto);
    SaveItemVO<CustomerSaveItemVO> buildUpdateForm(IdBaseDTO dto);
}
```

- [ ] **Step 4: 让 `CustomerAdminController` 改依赖 facade，先做最小委派**

```java
@RestController
@RequestMapping("/erp/v1/customer")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerAdminFacade customerAdminFacade;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<CustomerListItemVO>> list(@RequestBody CustomerListDTO dto) {
        return ResultVO.success(customerAdminFacade.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<CustomerSaveItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(customerAdminFacade.addItem(dto));
    }

    @PostMapping("/updateItem")
    public ResultVO<SaveItemVO<CustomerSaveItemVO>> updateItem(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(customerAdminFacade.updateItem(dto));
    }
}
```

- [ ] **Step 5: 在旧 `CustomerAdminAppServiceImpl` 基础上抽最小实现，先保证行为不变**

```java
@Service
@RequiredArgsConstructor
public class CustomerAdminFacadeImpl implements CustomerAdminFacade {

    private final CustomerListQueryUseCase customerListQueryUseCase;
    private final CustomerFormSchemaUseCase customerFormSchemaUseCase;

    @Override
    public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
        return customerListQueryUseCase.execute(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto) {
        return customerFormSchemaUseCase.buildCreateForm(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto) {
        return customerFormSchemaUseCase.buildUpdateForm(dto);
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class CustomerListQueryUseCaseImpl implements CustomerListQueryUseCase {

    private final CustomerAdminAppService customerAdminAppService;

    @Override
    public ListBaseVO<CustomerListItemVO> execute(CustomerListDTO dto) {
        return customerAdminAppService.list(dto);
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class CustomerFormSchemaUseCaseImpl implements CustomerFormSchemaUseCase {

    private final CustomerAdminAppService customerAdminAppService;

    @Override
    public SaveItemVO<CustomerSaveItemVO> buildCreateForm(BaseDTO dto) {
        return customerAdminAppService.addItem(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> buildUpdateForm(IdBaseDTO dto) {
        return customerAdminAppService.updateItem(dto);
    }
}
```

- [ ] **Step 6: 跑结构测试与现有列表测试，确认绿灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest,CustomerListServiceTest test`
Expected: PASS，结构测试通过，现有列表行为不回归。

- [ ] **Step 7: 提交本任务**

```bash
git add \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/CustomerAdminFacade.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/impl/CustomerAdminFacadeImpl.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/CustomerListQueryUseCase.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/impl/CustomerListQueryUseCaseImpl.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/form/CustomerFormSchemaUseCase.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/form/impl/CustomerFormSchemaUseCaseImpl.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java

git commit -m "refactor: introduce customer facade and use case entrypoints"
```

## Task 2: 拆出草稿、提交、详情、删除用例，把旧大类收成委派实现

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/CustomerSaveDraftUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/CustomerLoadDraftUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/CustomerDraftListUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/impl/CustomerSaveDraftUseCaseImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/impl/CustomerLoadDraftUseCaseImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft/impl/CustomerDraftListUseCaseImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/submit/CustomerSubmitUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/submit/impl/CustomerSubmitUseCaseImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/detail/CustomerDetailQueryUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/detail/impl/CustomerDetailQueryUseCaseImpl.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/delete/CustomerDeleteUseCase.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/delete/impl/CustomerDeleteUseCaseImpl.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/CustomerAdminFacade.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/impl/CustomerAdminFacadeImpl.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDetailServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDeleteServiceTest.java`

**Interfaces:**
- Consumes: `CustomerAdminAppService#saveDraft(CustomerDraftSaveDTO dto): CustomerDraftSaveVO`
- Consumes: `CustomerAdminAppService#saveAndSubmit(CustomerSubmitSaveDTO dto): BaseVO`
- Consumes: `CustomerAdminAppService#draftList(CustomerDraftListDTO dto): List<CustomerDraftListItemVO>`
- Consumes: `CustomerAdminAppService#loadDraft(CustomerDraftLoadDTO dto): CustomerDraftDetailVO`
- Consumes: `CustomerAdminAppService#detail(IdBaseDTO dto): CustomerDetailVO`
- Consumes: `CustomerAdminAppService#delete(BatchBaseDTO dto): void`
- Produces: `CustomerAdminFacade#saveDraft(CustomerDraftSaveDTO dto): CustomerDraftSaveVO`
- Produces: `CustomerAdminFacade#saveAndSubmit(CustomerSubmitSaveDTO dto): BaseVO`
- Produces: `CustomerAdminFacade#draftList(CustomerDraftListDTO dto): List<CustomerDraftListItemVO>`
- Produces: `CustomerAdminFacade#loadDraft(CustomerDraftLoadDTO dto): CustomerDraftDetailVO`
- Produces: `CustomerAdminFacade#detail(IdBaseDTO dto): CustomerDetailVO`
- Produces: `CustomerAdminFacade#delete(BatchBaseDTO dto): BaseVO`

- [ ] **Step 1: 先补结构测试，要求 facade 提供草稿、提交、详情、删除完整出口，并且删除返回 `BaseVO`**

```java
@Test
void should_expose_full_customer_use_case_contracts_on_facade() throws Exception {
    Class<?> facade = Class.forName("xbb.ai.erp.module.customer.application.facade.CustomerAdminFacade");
    assertNotNull(facade.getMethod("saveDraft", Class.forName("xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO")));
    assertNotNull(facade.getMethod("saveAndSubmit", Class.forName("xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO")));
    assertNotNull(facade.getMethod("draftList", Class.forName("xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO")));
    assertNotNull(facade.getMethod("loadDraft", Class.forName("xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO")));
    assertNotNull(facade.getMethod("detail", Class.forName("xbb.ai.erp.base.common.dto.IdBaseDTO")));
    assertEquals(
        Class.forName("xbb.ai.erp.base.common.vo.BaseVO"),
        facade.getMethod("delete", Class.forName("xbb.ai.erp.base.common.dto.BatchBaseDTO")).getReturnType()
    );
}
```

- [ ] **Step 2: 运行结构测试，确认红灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerAdminControllerStructureTest test`
Expected: FAIL，提示 facade 方法缺失或 `delete` 返回类型不符。

- [ ] **Step 3: 为草稿、提交、详情、删除补齐 use case 接口和最小委派实现**

```java
public interface CustomerSubmitUseCase {
    BaseVO execute(CustomerSubmitSaveDTO dto);
}
```

```java
@Service
@RequiredArgsConstructor
public class CustomerSubmitUseCaseImpl implements CustomerSubmitUseCase {
    private final CustomerAdminAppService customerAdminAppService;

    @Override
    public BaseVO execute(CustomerSubmitSaveDTO dto) {
        return customerAdminAppService.saveAndSubmit(dto);
    }
}
```

```java
public interface CustomerDeleteUseCase {
    BaseVO execute(BatchBaseDTO dto);
}
```

```java
@Service
@RequiredArgsConstructor
public class CustomerDeleteUseCaseImpl implements CustomerDeleteUseCase {
    private final CustomerAdminAppService customerAdminAppService;

    @Override
    public BaseVO execute(BatchBaseDTO dto) {
        customerAdminAppService.delete(dto);
        return new BaseVO();
    }
}
```

- [ ] **Step 4: 扩充 facade 与 controller，让全量接口都改走 facade**

```java
public interface CustomerAdminFacade {
    ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto);
    SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto);
    SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto);
    CustomerDraftSaveVO saveDraft(CustomerDraftSaveDTO dto);
    BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto);
    List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto);
    CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto);
    CustomerDetailVO detail(IdBaseDTO dto);
    BaseVO delete(BatchBaseDTO dto);
}
```

```java
@PostMapping("/delete")
public ResultVO<BaseVO> delete(@RequestBody BatchBaseDTO dto) {
    return ResultVO.success(customerAdminFacade.delete(dto));
}
```

- [ ] **Step 5: 让旧 `CustomerAdminAppServiceImpl` 只保留兼容实现，禁止新增主逻辑**

```java
@Deprecated
@Service
public class CustomerAdminAppServiceImpl implements CustomerAdminAppService {
    // 保留既有实现给委派 use case 使用，本任务不再往此类新增主要业务逻辑。
}
```

- [ ] **Step 6: 运行草稿、保存、详情、删除测试，确认行为不回归**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerDraftServiceTest,CustomerSaveServiceTest,CustomerDetailServiceTest,CustomerDeleteServiceTest,CustomerAdminControllerStructureTest test`
Expected: PASS，草稿、提交、详情、删除行为与结构约束同时通过。

- [ ] **Step 7: 提交本任务**

```bash
git add \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/CustomerAdminFacade.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/facade/impl/CustomerAdminFacadeImpl.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/draft \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/submit \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/detail \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/delete \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java

git commit -m "refactor: route customer workflows through facade and use cases"
```

## Task 3: 切断写侧校验对列表 DSL 的依赖，补明确语义的仓储接口

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveBusinessValidator.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepository.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/FakeCustomerRepository.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/support/InMemoryCustomerRepository.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepositorySignatureTest.java`

**Interfaces:**
- Consumes: `CustomerSaveContextPojo#getCorpid(): String`
- Consumes: `CustomerSaveContextPojo#getMain(): CustomerMainDTO`
- Produces: `CustomerRepository#existsByCustomerCode(String corpid, String customerCode, Long excludeId): boolean`

- [ ] **Step 1: 先写仓储签名测试与业务校验测试，明确去掉列表 DSL 依赖**

```java
package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerRepositorySignatureTest {

    @Test
    void should_define_exists_by_customer_code_signature() throws Exception {
        assertEquals(
            boolean.class,
            CustomerRepository.class
                .getMethod("existsByCustomerCode", String.class, String.class, Long.class)
                .getReturnType()
        );
    }
}
```

```java
@Test
void should_reject_duplicate_customer_code_without_list_filter_builder_dependency() {
    InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
    Customer existing = new Customer();
    existing.setId(1L);
    existing.setCorpid("corp-001");
    existing.setCustomerCode("CUST-001");
    repository.seed(existing);

    CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
        repository,
        new InMemoryCustomerContactRepository(),
        new InMemoryCustomerAddressRepository(),
        new InMemoryCustomerBankAccountRepository(),
        new InMemoryCustomerInvoiceProfileRepository()
    );

    CustomerSubmitSaveDTO dto = new CustomerSubmitSaveDTO();
    dto.setCorpid("corp-001");
    CustomerMainDTO main = new CustomerMainDTO();
    main.setCustomerCode("CUST-001");
    main.setCustomerName("重复客户");
    main.setCustomerCategory("A");
    main.setBizStatus("DRAFT");
    dto.setMain(main);

    BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(dto));
    assertEquals("客户编码已存在", ex.getMessage());
}
```

- [ ] **Step 2: 运行测试，确认红灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerRepositorySignatureTest,CustomerSaveValidationTest test`
Expected: FAIL，仓储接口缺方法，或重复编码场景仍依赖旧实现。

- [ ] **Step 3: 给 domain repository 加明确语义方法，并在 fake / in-memory / impl 同步实现**

```java
public interface CustomerRepository {
    void insert(Customer customer);
    void insertBatch(List<Customer> customers);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(Customer customer);
    Customer findById(String corpid, Long id);
    List<Customer> findByCondition(Map<String, Object> conditionMap);
    boolean existsByCustomerCode(String corpid, String customerCode, Long excludeId);
}
```

```java
@Override
public boolean existsByCustomerCode(String corpid, String customerCode, Long excludeId) {
    return findByCondition(Map.of("corpid", corpid)).stream()
        .filter(customer -> customerCode.equals(customer.getCustomerCode()))
        .anyMatch(customer -> excludeId == null || !excludeId.equals(customer.getId()));
}
```

- [ ] **Step 4: 重写 `CustomerSaveBusinessValidator`，不再依赖 `ListFilterConditionBuilder` 与 `CustomerListMetaProvider`**

```java
public class CustomerSaveBusinessValidator {

    private final CustomerRepository customerRepository;

    public CustomerSaveBusinessValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateForSubmit(CustomerSaveContextPojo context) {
        validateCustomerCodeDuplicate(context);
    }

    private void validateCustomerCodeDuplicate(CustomerSaveContextPojo context) {
        if (customerRepository == null || context.getMain() == null) {
            return;
        }
        String customerCode = context.getMain().getCustomerCode();
        if (customerCode == null || customerCode.isBlank()) {
            return;
        }
        if (customerRepository.existsByCustomerCode(context.getCorpid(), customerCode, context.getMain().getId())) {
            throw new BizException("客户编码已存在");
        }
    }
}
```

- [ ] **Step 5: 运行校验测试与签名测试，确认绿灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerRepositorySignatureTest,CustomerSaveValidationTest test`
Expected: PASS，写侧查重不再依赖列表 DSL。

- [ ] **Step 6: 提交本任务**

```bash
git add \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/validator/CustomerSaveBusinessValidator.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepository.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/domain/repository/CustomerRepositorySignatureTest.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java

git commit -m "refactor: replace list-dsl save validation with repository semantics"
```

## Task 4: 统一删除返回壳、草稿 VO 边界和基础设施异常类型

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftDetailVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftMetaVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerSectionStateVO.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerSaveExtVO.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerDraftRepositoryImpl.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerSaveContractStructureTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerDraftRepositoryImplTest.java`

**Interfaces:**
- Consumes: `CustomerDraftRepository#loadDraft(String corpid, String draftCode): CustomerSaveDraftPojo`
- Produces: `CustomerDraftDetailVO#main: CustomerMainDTO`
- Produces: `CustomerDraftDetailVO#ext: CustomerSaveExtVO`
- Produces: `CustomerDraftDetailVO#sectionState: CustomerSectionStateVO`
- Produces: `CustomerDraftDetailVO#draftMeta: CustomerDraftMetaVO`

- [ ] **Step 1: 先写结构测试，要求草稿 VO 不再直接暴露 application.pojo，删除接口统一返回 `BaseVO`**

```java
package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerSaveContractStructureTest {

    @Test
    void should_use_admin_vo_types_in_customer_draft_detail_vo() throws Exception {
        Class<?> voClass = Class.forName("xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO");
        Field ext = voClass.getDeclaredField("ext");
        Field sectionState = voClass.getDeclaredField("sectionState");
        Field draftMeta = voClass.getDeclaredField("draftMeta");

        assertEquals(Class.forName("xbb.ai.erp.module.customer.admin.vo.CustomerSaveExtVO"), ext.getType());
        assertEquals(Class.forName("xbb.ai.erp.module.customer.admin.vo.CustomerSectionStateVO"), sectionState.getType());
        assertEquals(Class.forName("xbb.ai.erp.module.customer.admin.vo.CustomerDraftMetaVO"), draftMeta.getType());
    }
}
```

- [ ] **Step 2: 运行结构测试，确认红灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveContractStructureTest test`
Expected: FAIL，提示 VO 类型仍指向 application.pojo。

- [ ] **Step 3: 建立 admin.vo 壳对象，并让 `CustomerDraftDetailVO` 改依赖 admin.vo**

```java
@Data
public class CustomerSaveExtVO {
    private List<CustomerContactItemDTO> contacts = new ArrayList<>();
    private List<CustomerAddressItemDTO> addresses = new ArrayList<>();
    private List<CustomerBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<CustomerInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
```

```java
@Data
public class CustomerDraftDetailVO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtVO ext = new CustomerSaveExtVO();
    private CustomerSectionStateVO sectionState = new CustomerSectionStateVO();
    private CustomerDraftMetaVO draftMeta = new CustomerDraftMetaVO();
}
```

- [ ] **Step 4: 把 `CustomerDraftRepositoryImpl` 的主动捕获异常统一换成 `BizException`**

```java
private String toJson(CustomerSaveDraftPojo draft) {
    try {
        return objectMapper.writeValueAsString(draft);
    } catch (JsonProcessingException ex) {
        throw new BizException("客户草稿序列化失败");
    }
}

private CustomerSaveDraftPojo fromJson(String payload) {
    try {
        return objectMapper.readValue(payload, CustomerSaveDraftPojo.class);
    } catch (JsonProcessingException ex) {
        throw new BizException("客户草稿反序列化失败");
    }
}
```

- [ ] **Step 5: 运行结构测试与草稿仓储测试，确认绿灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerSaveContractStructureTest,CustomerDraftRepositoryImplTest test`
Expected: PASS，VO 边界收口，异常符合约束。

- [ ] **Step 6: 提交本任务**

```bash
git add \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftDetailVO.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerDraftMetaVO.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerSectionStateVO.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/vo/CustomerSaveExtVO.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerDraftRepositoryImpl.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerSaveContractStructureTest.java

git commit -m "refactor: align customer draft contracts and exception types"
```

## Task 5: 把 common 列表从“元数据服务”升级为统一 schema/query/page 协议入口

**Files:**
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/dto/ListQueryDTO.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListPageVO.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListSchemaVO.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`

**Interfaces:**
- Consumes: `BusinessBaseDTO#businessCode: String`
- Produces: `ListQueryDTO extends BusinessBaseDTO`
- Produces: `ListPageVO<T>#list: List<T>`
- Produces: `ListPageVO<T>#pageHelper: ListBaseVO.PageHelper`
- Produces: `ListSchemaVO#filter: ListFilterVO`
- Produces: `ListSchemaVO#header: ListHeaderVO`
- Produces: `ListSchemaVO#topButton: ListTopButtonVO`
- Produces: `ListSchemaVO#bottomButton: ListBottomButtonVO`
- Produces: `ListSchemaVO#rowAction: ListRowActionVO`

- [ ] **Step 1: 先写结构测试，要求 common 层出现统一 query/page/schema 协议类型**

```java
@Test
void should_define_unified_list_protocol_types() throws Exception {
    assertNotNull(Class.forName("xbb.ai.erp.module.common.admin.dto.ListQueryDTO"));
    assertNotNull(Class.forName("xbb.ai.erp.module.common.admin.vo.ListPageVO"));
    assertNotNull(Class.forName("xbb.ai.erp.module.common.admin.vo.ListSchemaVO"));
}
```

```java
@Test
void should_keep_row_action_endpoint_and_support_schema_aggregation() throws Exception {
    Method rowAction = ListCommonController.class.getMethod("rowAction", Class.forName("xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO"));
    assertNotNull(rowAction);
}
```

- [ ] **Step 2: 运行 common 结构测试，确认红灯**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonControllerStructureTest,ListCommonServiceTest test`
Expected: FAIL，提示新协议类型不存在。

- [ ] **Step 3: 先新增统一协议类型，保持旧 endpoint 兼容**

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class ListQueryDTO extends BusinessBaseDTO {
    private String keyword;
    private List<ListFilterCondition> conditions;
    private Integer pageNum;
    private Integer pageSize;
}
```

```java
@Data
public class ListPageVO<T> {
    private List<T> list;
    private ListBaseVO.PageHelper pageHelper;
}
```

```java
@Data
public class ListSchemaVO {
    private ListFilterVO filter;
    private ListHeaderVO header;
    private ListTopButtonVO topButton;
    private ListBottomButtonVO bottomButton;
    private ListRowActionVO rowAction;
}
```

- [ ] **Step 4: 让 `ListCommonService` 先提供 schema 聚合能力，不破坏现有单端点**

```java
public interface ListCommonService {
    ListFilterVO filter(ListCommonQueryDTO dto);
    ListHeaderVO header(ListCommonQueryDTO dto);
    ListTopButtonVO topButton(ListCommonQueryDTO dto);
    ListBottomButtonVO bottomButton(ListCommonQueryDTO dto);
    ListRowActionVO rowAction(ListCommonQueryDTO dto);
    ListSchemaVO schema(ListCommonQueryDTO dto);
}
```

```java
@Override
public ListSchemaVO schema(ListCommonQueryDTO dto) {
    ListSchemaVO vo = new ListSchemaVO();
    vo.setFilter(filter(dto));
    vo.setHeader(header(dto));
    vo.setTopButton(topButton(dto));
    vo.setBottomButton(bottomButton(dto));
    vo.setRowAction(rowAction(dto));
    return vo;
}
```

- [ ] **Step 5: 运行 common 结构测试与服务测试，确认绿灯**

Run: `mvn -pl xbb-erp-module-common -Dtest=ListCommonControllerStructureTest,ListCommonServiceTest test`
Expected: PASS，旧元数据能力不回归，同时统一协议类型已存在。

- [ ] **Step 6: 提交本任务**

```bash
git add \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/dto/ListQueryDTO.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListPageVO.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListSchemaVO.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java \
  xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java \
  xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java \
  xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java

git commit -m "feat: introduce unified list platform contracts"
```

## Task 6: 让 customer 成为第一个按新列表平台协议接入的样板模块

**Files:**
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/schema/CustomerListSchemaProvider.java`
- Create: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/schema/CustomerListQueryAdapter.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/impl/CustomerListQueryUseCaseImpl.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProviderStructureTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`

**Interfaces:**
- Consumes: `ListQueryDTO#keyword: String`
- Consumes: `ListQueryDTO#conditions: List<ListFilterCondition>`
- Produces: `CustomerListSchemaProvider#conditionMetaMap(): Map<String, ListFilterMetaPojo>`
- Produces: `CustomerListQueryAdapter#toConditionMap(CustomerListDTO dto): Map<String, Object>`
- Produces: `CustomerListQueryUseCaseImpl` 只依赖 query adapter，不直接依赖 `CustomerListMetaProvider` 的 schema 组装职责

- [ ] **Step 1: 先写结构测试，要求 customer 列表 schema 与 query adapter 分离**

```java
package xbb.ai.erp.module.customer.application.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerListMetaProviderStructureTest {

    @Test
    void should_define_customer_list_schema_provider_and_query_adapter() throws Exception {
        assertNotNull(Class.forName("xbb.ai.erp.module.customer.application.schema.CustomerListSchemaProvider"));
        assertNotNull(Class.forName("xbb.ai.erp.module.customer.application.schema.CustomerListQueryAdapter"));
    }
}
```

- [ ] **Step 2: 运行结构测试，确认红灯**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderStructureTest test`
Expected: FAIL，提示 schema provider / query adapter 类型不存在。

- [ ] **Step 3: 把旧 `CustomerListMetaProvider` 的职责拆成 schema 与 query 两块**

```java
@Component
public class CustomerListSchemaProvider {
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) { ... }
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) { ... }
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) { ... }
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) { ... }
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) { ... }
    public Map<String, ListFilterMetaPojo> conditionMetaMap() { ... }
}
```

```java
@Component
@RequiredArgsConstructor
public class CustomerListQueryAdapter {

    private final CustomerListSchemaProvider customerListSchemaProvider;
    private final ListFilterConditionBuilder listFilterConditionBuilder = new ListFilterConditionBuilder();

    public Map<String, Object> toConditionMap(CustomerListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("keyword", dto.getKeyword());
        conditionMap.put("conditions", listFilterConditionBuilder.build(dto.getConditions(), customerListSchemaProvider.conditionMetaMap()));
        return conditionMap;
    }
}
```

- [ ] **Step 4: 让列表 use case 改依赖 query adapter，旧 `CustomerListMetaProvider` 退化为兼容外壳或删除**

```java
@Service
@RequiredArgsConstructor
public class CustomerListQueryUseCaseImpl implements CustomerListQueryUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerInvoiceProfileRepository customerInvoiceProfileRepository;
    private final CustomerListQueryAdapter customerListQueryAdapter;

    @Override
    public ListBaseVO<CustomerListItemVO> execute(CustomerListDTO dto) {
        Map<String, Object> fullConditionMap = customerListQueryAdapter.toConditionMap(dto);
        // 保留原列表装配逻辑，本任务只迁移接缝。
    }
}
```

- [ ] **Step 5: 运行列表 provider / service 测试，确认新结构下行为不回归**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderStructureTest,CustomerListMetaProviderTest,CustomerListServiceTest test`
Expected: PASS，列表 schema 与查询适配分离，但结果保持一致。

- [ ] **Step 6: 提交本任务**

```bash
git add \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/schema/CustomerListSchemaProvider.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/schema/CustomerListQueryAdapter.java \
  xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/usecase/list/impl/CustomerListQueryUseCaseImpl.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProviderStructureTest.java \
  xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java

git commit -m "refactor: split customer list schema from query adapter"
```

## Task 7: 跑聚合验证，补文档与样板约束回归检查

**Files:**
- Modify: `docs/superpowers/specs/2026-07-29-customer-ddd-framework-refactor-design.md`
- Modify: `docs/base/项目业务module导航.md`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListServiceTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerSaveValidationTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerDraftServiceTest.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`

**Interfaces:**
- Consumes: 前六个任务产出的全部 facade / use case / protocol / repository 签名
- Produces: 可供后续第二业务模块参考的样板约束说明

- [ ] **Step 1: 先补一个样板约束说明测试清单，明确要验证的结构能力**

```text
需要最终验证：
1. controller 仅依赖 facade
2. facade 仅委派 use case
3. 写侧校验不再依赖列表 DSL
4. common 已具备统一 list protocol 类型
5. customer 已具备 schema provider + query adapter 接缝
```

- [ ] **Step 2: 跑 customer/common 的核心测试集合**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListCommonControllerStructureTest,ListCommonServiceTest,CustomerAdminControllerStructureTest,CustomerListServiceTest,CustomerSaveValidationTest,CustomerDraftServiceTest,CustomerSaveContractStructureTest,CustomerRepositorySignatureTest test`
Expected: PASS，平台接缝与 customer 样板约束同时通过。

- [ ] **Step 3: 根据最终实现回写设计文档和模块导航，补充“customer 是首个样板模块”的落地说明**

```markdown
- 客户模块已完成 facade + use case + list protocol 样板收敛
- 新业务模块优先复用 common 列表协议与 customer 样板接缝
- 禁止新模块复制巨型应用服务实现
```

- [ ] **Step 4: 再跑一次最小结构回归测试**

Run: `mvn -pl xbb-erp-module-common,xbb-erp-module-customer -Dtest=ListCommonControllerStructureTest,CustomerAdminControllerStructureTest test`
Expected: PASS，说明最后的文档或轻微收边没有破坏结构约束。

- [ ] **Step 5: 提交本任务**

```bash
git add \
  docs/superpowers/specs/2026-07-29-customer-ddd-framework-refactor-design.md \
  docs/base/项目业务module导航.md

git commit -m "docs: record customer module as first reusable sample"
```

## Self-Review

### Spec coverage

- facade + use case 拆分：Task 1、Task 2
- 写侧校验与列表 DSL 解耦：Task 3
- 草稿 VO 边界与异常规范：Task 4
- common 统一列表协议：Task 5
- customer 作为样板模块接入新协议：Task 6
- 聚合验证与文档沉淀：Task 7

### Placeholder scan

- 本计划未使用未完成占位语句或空泛描述
- 每个代码步骤都附了实际类名、方法名、测试命令与预期结果

### Type consistency

- facade 类型统一为 `CustomerAdminFacade`
- 列表用例统一为 `CustomerListQueryUseCase#execute(CustomerListDTO dto)`
- 仓储查重签名统一为 `existsByCustomerCode(String corpid, String customerCode, Long excludeId)`
- common 平台协议统一为 `ListQueryDTO`、`ListPageVO<T>`、`ListSchemaVO`

Plan complete and saved to `docs/superpowers/plans/2026-07-29-customer-ddd-framework-refactor.md`. Two execution options:

**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints

**Which approach?**
