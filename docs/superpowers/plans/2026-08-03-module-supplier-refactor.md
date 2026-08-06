# Supplier 模块 DDD 全量重构 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 `xbb-erp-module-supplier` 从当前 `Vendor` 命名与薄分层实现，重构为与 `module-customer` 对齐的 `Supplier` 领域模块，并同步升级接口协议、草稿/提交能力与数据库 SQL。

**Architecture:** 以 `module-customer` 为结构母版，先完成 `Vendor* -> Supplier*` 术语统一与接口门面收口，再把应用层拆分为 `query/save/draft/delete` 用例服务，最后统一持久化与数据库 SQL 到 `supplier_*` 最终态。运行时不保留旧 `vendor_*` 兼容层，代码、Mapper、SQL、文档一次性收口。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、JUnit 5、MyBatis-Plus、Redis 7、MySQL 5.6/8.0

## Global Constraints

- 项目架构DDD领域驱动设计
- 对话永远在中文语境下，注释使用中文
- 所有主动捕获的报错、业务的主动抛错，都使用 `BizException`
- 直接对接数据库的对象实体需要添加 `PO` 后缀，并且对象内字段不允许使用布尔值对接，改用 `Integer`
- 枚举类需要 `Enum` 结尾
- 系统内 pojo 尾缀规范：对接前端入参 `DTO`、对接接口出参 `VO`。其余中转参数的对象 `Pojo`
- 所有接口 DTO 作为参数，而不是散列的参数；非脚本接口，入参 DTO 都需要继承 `BaseDTO`
- `userId` 员工 Id 是字符串 id
- getter setter 用 Lombok 管理
- 如果接口业务代码没有需要返回的，用 `BaseVO` 返回
- 所有接口的参数返回，都使用 `ResultVO.success()` 包装返回
- 尽可能避免循环中查询数据库
- 本次不接入 `Flyway`，但必须交付手工执行 SQL
- 本次允许整体升级接口协议，不保留 `Vendor` 兼容层
- 计划执行遵循 DRY、YAGNI、TDD、频繁小步提交

---

## File Structure

### 计划新增文件

- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/SupplierAdminController.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierDraftListDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierDraftLoadDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierDraftSaveDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierSubmitSaveDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierListDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierMainDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierSaveDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierContactItemDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierAddressItemDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierBankAccountItemDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierInvoiceProfileItemDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDetailVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierListItemVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierSaveItemVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDraftDetailVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDraftListItemVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDraftSaveVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/SupplierAdminAppService.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppService.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppServiceImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/save/SupplierSaveAppService.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/save/SupplierSaveAppServiceImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/draft/SupplierDraftAppService.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/draft/SupplierDraftAppServiceImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/delete/SupplierDeleteAppServiceImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/assembler/SupplierAdminAssembler.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSaveContextPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSaveDraftPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSaveExtPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSectionStatePojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/port/SupplierDraftRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/validator/SupplierSaveProtocolValidator.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/validator/SupplierSaveCommonValidator.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/validator/SupplierSaveBusinessValidator.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/Supplier.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierContact.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierAddress.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierBankAccount.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierInvoiceProfile.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/pojo/SupplierContactQueryPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/pojo/SupplierAddressQueryPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/pojo/SupplierBankAccountQueryPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/pojo/SupplierInvoiceProfileQueryPojo.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierContactRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierAddressRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierBankAccountRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierInvoiceProfileRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierContactConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierAddressConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierBankAccountConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierInvoiceProfileConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierContactMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierAddressMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierBankAccountMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierInvoiceProfileMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierContactPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierAddressPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierBankAccountPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierInvoiceProfilePO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierContactRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierAddressRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierBankAccountRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierInvoiceProfileRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierDraftRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/ConditionMapHelper.java`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierContactMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierAddressMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierBankAccountMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierInvoiceProfileMapper.xml`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/admin/SupplierAdminControllerTest.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierSaveServiceTest.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierDraftServiceTest.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierRepository.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierContactRepository.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierAddressRepository.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierBankAccountRepository.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierInvoiceProfileRepository.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierDraftRepository.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/sql/SupplierSqlPlanTest.java`
- `docs/sql/2026-08-03-refactor-supplier-module.sql`

### 计划修改文件

- `docs/api/supplier-supplier.md`
- `docs/base/项目业务module导航.md`
- `docs/sql/2026-07-22-init-supplier-module.sql`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/VendorAdminController.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/VendorListDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/VendorMainDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/VendorSaveDTO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/VendorDetailVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/VendorListItemVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/VendorSaveItemVO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/assembler/VendorAdminAssembler.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/VendorAdminAppService.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/VendorAdminAppServiceImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/Vendor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/VendorContact.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/VendorAddress.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/VendorBankAccount.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/VendorInvoiceProfile.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/VendorRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/VendorContactRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/VendorAddressRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/VendorBankAccountRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/VendorInvoiceProfileRepository.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/VendorConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/VendorContactConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/VendorAddressConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/VendorBankAccountConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/VendorInvoiceProfileConvertor.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/VendorMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/VendorContactMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/VendorAddressMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/VendorBankAccountMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/VendorInvoiceProfileMapper.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorContactPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorAddressPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorBankAccountPO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorInvoiceProfilePO.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/VendorRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/VendorContactRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/VendorAddressRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/VendorBankAccountRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/VendorInvoiceProfileRepositoryImpl.java`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/VendorMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/VendorContactMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/VendorAddressMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/VendorBankAccountMapper.xml`
- `xbb-erp-module-supplier/src/main/resources/mapper/supplier/VendorInvoiceProfileMapper.xml`

### 计划测试文件

- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/admin/SupplierAdminControllerTest.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierSaveServiceTest.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierDraftServiceTest.java`
- `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/sql/SupplierSqlPlanTest.java`

## Task 1: 先建立 Supplier 术语与接口门面骨架

**Files:**
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/SupplierAdminController.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierListDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierMainDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierSaveDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDetailVO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierListItemVO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierSaveItemVO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/SupplierAdminAppService.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/admin/SupplierAdminControllerTest.java`
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/VendorAdminController.java`
- Test: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/admin/SupplierAdminControllerTest.java`

**Interfaces:**
- Consumes: `BaseDTO`
- Consumes: `IdBaseDTO`
- Consumes: `BatchBaseDTO`
- Produces: `SupplierAdminAppService#list(SupplierListDTO dto): ListBaseVO<SupplierListItemVO>`
- Produces: `SupplierAdminAppService#addItem(BaseDTO dto): SaveItemVO<SupplierSaveItemVO>`
- Produces: `SupplierAdminAppService#updateItem(IdBaseDTO dto): SaveItemVO<SupplierSaveItemVO>`
- Produces: `SupplierAdminAppService#detail(IdBaseDTO dto): SupplierDetailVO`
- Produces: `SupplierAdminAppService#delete(BatchBaseDTO dto): void`

- [ ] **Step 1: 先写控制器结构测试，锁定新接口门面和返回包装约束**

```java
package xbb.ai.erp.module.supplier.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import xbb.ai.erp.base.common.vo.ResultVO;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierAdminControllerTest {

    @Test
    void should_use_supplier_route_and_result_wrapper() throws Exception {
        RequestMapping mapping = SupplierAdminController.class.getAnnotation(RequestMapping.class);
        assertEquals("/erp/v1/supplier", mapping.value()[0]);

        Method save = SupplierAdminController.class.getDeclaredMethod(
            "detail",
            Class.forName("xbb.ai.erp.base.common.dto.IdBaseDTO")
        );
        assertTrue(save.getReturnType().equals(ResultVO.class));
    }
}
```

- [ ] **Step 2: 运行结构测试，确认当前红灯**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierAdminControllerTest test`
Expected: FAIL，提示 `SupplierAdminController` 不存在或返回类型不满足要求。

- [ ] **Step 3: 创建 `Supplier` 版 DTO/VO 与应用服务最小签名**

```java
package xbb.ai.erp.module.supplier.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;

public interface SupplierAdminAppService {
    ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto);
    SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto);
    SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto);
    Long save(SupplierSaveDTO dto);
    SupplierDetailVO detail(IdBaseDTO dto);
    void delete(BatchBaseDTO dto);
}
```

- [ ] **Step 4: 创建新控制器，统一使用 `ResultVO.success()` 包装**

```java
@RestController
@RequestMapping("/erp/v1/supplier")
@RequiredArgsConstructor
public class SupplierAdminController {

    private final SupplierAdminAppService supplierAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<SupplierListItemVO>> list(@RequestBody SupplierListDTO dto) {
        return ResultVO.success(supplierAdminAppService.list(dto));
    }

    @PostMapping("/detail")
    public ResultVO<SupplierDetailVO> detail(@RequestBody IdBaseDTO dto) {
        return ResultVO.success(supplierAdminAppService.detail(dto));
    }

    @PostMapping("/delete")
    public ResultVO<Void> delete(@RequestBody BatchBaseDTO dto) {
        supplierAdminAppService.delete(dto);
        return ResultVO.success(null);
    }
}
```

- [ ] **Step 5: 运行结构测试，确认转绿**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierAdminControllerTest test`
Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin \
  xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service \
  xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/admin/SupplierAdminControllerTest.java
git commit -m "feat: scaffold supplier admin api contracts"
```

## Task 2: 拆分 query/save/delete 用例并完成 Supplier 聚合改名

**Files:**
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppService.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/query/SupplierQueryAppServiceImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/save/SupplierSaveAppService.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/save/SupplierSaveAppServiceImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/delete/SupplierDeleteAppServiceImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/assembler/SupplierAdminAssembler.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/Supplier.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierContact.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierAddress.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierBankAccount.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/model/SupplierInvoiceProfile.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierRepository.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierContactRepository.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierAddressRepository.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierBankAccountRepository.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain/repository/SupplierInvoiceProfileRepository.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierSaveServiceTest.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierRepository.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierContactRepository.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierAddressRepository.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierBankAccountRepository.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierInvoiceProfileRepository.java`
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/VendorAdminAppServiceImpl.java`
- Test: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierSaveServiceTest.java`

**Interfaces:**
- Consumes: `SupplierRepository#insert(Supplier supplier): void`
- Consumes: `SupplierRepository#update(Supplier supplier): void`
- Consumes: `SupplierRepository#findByCondition(Map<String, Object> conditionMap): List<Supplier>`
- Consumes: `SupplierContactRepository#findByCondition(Map<String, Object> conditionMap): List<SupplierContact>`
- Produces: `SupplierQueryAppService#list(SupplierListDTO dto): ListBaseVO<SupplierListItemVO>`
- Produces: `SupplierSaveAppService#save(SupplierSaveDTO dto): Long`
- Produces: `SupplierDeleteAppServiceImpl#delete(BatchBaseDTO dto): void`

- [ ] **Step 1: 先写保存测试，锁定主档与子档一并保存的最小行为**

```java
@Test
void should_insert_supplier_and_contact_in_one_save() {
    InMemorySupplierRepository supplierRepository = new InMemorySupplierRepository();
    InMemorySupplierContactRepository contactRepository = new InMemorySupplierContactRepository();
    SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
        supplierRepository,
        contactRepository,
        new InMemorySupplierAddressRepository(),
        new InMemorySupplierBankAccountRepository(),
        new InMemorySupplierInvoiceProfileRepository()
    );

    SupplierMainDTO main = new SupplierMainDTO();
    main.setSupplierCode("SUP-001");
    main.setSupplierName("杭州供应商");
    main.setSupplierCategory("A");
    main.setBizStatus("1");

    SupplierContactItemDTO contact = new SupplierContactItemDTO();
    contact.setContactName("张三");
    contact.setDefaultFlag(1);

    SupplierSaveDTO dto = new SupplierSaveDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setMain(main);
    dto.setContacts(List.of(contact));

    Long supplierId = service.save(dto);

    assertEquals(1, supplierRepository.all().size());
    assertEquals(supplierId, contactRepository.all().get(0).getSupplierId());
}
```

- [ ] **Step 2: 运行保存测试，确认当前红灯**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierSaveServiceTest test`
Expected: FAIL，提示 `Supplier*` 类型或 `forTesting` 工厂不存在。

- [ ] **Step 3: 创建 `Supplier` 领域模型和仓储接口，字段统一改名到 `supplier*`**

```java
@Data
public class Supplier {
    private Long id;
    private String corpid;
    private String supplierCode;
    private String supplierName;
    private String supplierShortName;
    private String supplierCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserId;
    private String ownerPurchaserNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private Long defaultBankAccountId;
    private Long defaultInvoiceProfileId;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
```

- [ ] **Step 4: 将 `SupplierAdminAppServiceImpl` 降级为 facade，委派到 query/save/delete 子服务**

```java
@Service
@RequiredArgsConstructor
public class SupplierAdminAppServiceImpl implements SupplierAdminAppService {

    private final SupplierQueryAppService supplierQueryAppService;
    private final SupplierSaveAppService supplierSaveAppService;
    private final SupplierDeleteAppServiceImpl supplierDeleteAppService;

    @Override
    public ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto) {
        return supplierQueryAppService.list(dto);
    }

    @Override
    public Long save(SupplierSaveDTO dto) {
        return supplierSaveAppService.save(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        supplierDeleteAppService.delete(dto);
    }
}
```

- [ ] **Step 5: 在 `SupplierSaveAppServiceImpl` 中按 customer 风格补主档/子档保存与默认值**

```java
@Override
public Long save(SupplierSaveDTO dto) {
    validateDefaultUniqueness(dto.getContacts(), SupplierContactItemDTO::getDefaultFlag, "联系人默认项只能有一个");
    validateDefaultUniqueness(dto.getAddresses(), SupplierAddressItemDTO::getDefaultFlag, "地址默认项只能有一个");
    validateDefaultUniqueness(dto.getBankAccounts(), SupplierBankAccountItemDTO::getDefaultFlag, "银行账户默认项只能有一个");
    validateDefaultUniqueness(dto.getInvoiceProfiles(), SupplierInvoiceProfileItemDTO::getDefaultFlag, "开票信息默认项只能有一个");

    Supplier supplier = SupplierAdminAssembler.toSupplier(dto);
    applySupplierDefaults(supplier);
    if (supplier.getId() == null) {
        supplierRepository.insert(supplier);
    } else {
        supplierRepository.update(supplier);
    }
    return supplier.getId();
}
```

- [ ] **Step 6: 运行保存测试，确认转绿**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierSaveServiceTest test`
Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application \
  xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/domain \
  xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service
git commit -m "feat: split supplier application services"
```

## Task 3: 补齐 draft/saveAndSubmit 能力与草稿存储端口

**Files:**
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierDraftListDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierDraftLoadDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierDraftSaveDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/dto/SupplierSubmitSaveDTO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDraftDetailVO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDraftListItemVO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/vo/SupplierDraftSaveVO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSaveContextPojo.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSaveDraftPojo.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSaveExtPojo.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/pojo/SupplierSectionStatePojo.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/port/SupplierDraftRepository.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/draft/SupplierDraftAppService.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/draft/SupplierDraftAppServiceImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierDraftRepositoryImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/validator/SupplierSaveProtocolValidator.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/validator/SupplierSaveCommonValidator.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/validator/SupplierSaveBusinessValidator.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierDraftServiceTest.java`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/support/InMemorySupplierDraftRepository.java`
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin/SupplierAdminController.java`
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/SupplierAdminAppService.java`
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/impl/SupplierAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application/service/save/SupplierSaveAppServiceImpl.java`
- Test: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service/SupplierDraftServiceTest.java`

**Interfaces:**
- Consumes: `SupplierDraftRepository#saveDraft(SupplierSaveDraftPojo draft): String`
- Consumes: `SupplierDraftRepository#listDrafts(String corpid, int limit): List<SupplierSaveDraftPojo>`
- Consumes: `SupplierDraftRepository#loadDraft(String corpid, String draftCode): SupplierSaveDraftPojo`
- Produces: `SupplierAdminAppService#saveDraft(SupplierDraftSaveDTO dto): SupplierDraftSaveVO`
- Produces: `SupplierAdminAppService#saveAndSubmit(SupplierSubmitSaveDTO dto): BaseVO`
- Produces: `SupplierAdminAppService#draftList(SupplierDraftListDTO dto): List<SupplierDraftListItemVO>`
- Produces: `SupplierAdminAppService#loadDraft(SupplierDraftLoadDTO dto): SupplierDraftDetailVO`

- [ ] **Step 1: 先写草稿测试，锁定“保存草稿 -> 提交成功后删草稿”行为**

```java
@Test
void should_remove_draft_after_submit_success() {
    InMemorySupplierDraftRepository draftRepository = new InMemorySupplierDraftRepository();
    SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
        new InMemorySupplierRepository(),
        new InMemorySupplierContactRepository(),
        new InMemorySupplierAddressRepository(),
        new InMemorySupplierBankAccountRepository(),
        new InMemorySupplierInvoiceProfileRepository(),
        draftRepository
    );

    SupplierMainDTO main = new SupplierMainDTO();
    main.setSupplierCode("SUP-001");
    main.setSupplierName("杭州供应商");

    SupplierDraftSaveDTO draftDTO = new SupplierDraftSaveDTO();
    draftDTO.setCorpid("corp-001");
    draftDTO.setMain(main);
    draftDTO.getDraftMeta().setDraftTitle("供应商草稿");
    service.saveDraft(draftDTO);

    String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

    SupplierSubmitSaveDTO submitDTO = new SupplierSubmitSaveDTO();
    submitDTO.setCorpid("corp-001");
    submitDTO.setMain(main);
    submitDTO.getDraftMeta().setDraftCode(draftCode);

    service.saveAndSubmit(submitDTO);

    assertTrue(draftRepository.listDrafts("corp-001", 10).isEmpty());
}
```

- [ ] **Step 2: 运行草稿测试，确认红灯**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierDraftServiceTest test`
Expected: FAIL，提示 draft DTO、VO、repository 或 service 不存在。

- [ ] **Step 3: 先抄定 customer 草稿端口结构，再替换成 supplier 语义**

```java
public interface SupplierDraftRepository {
    String saveDraft(SupplierSaveDraftPojo draft);
    List<SupplierSaveDraftPojo> listDrafts(String corpid, int limit);
    SupplierSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
```

- [ ] **Step 4: 在控制器中补齐草稿与提交接口**

```java
@PostMapping("/saveDraft")
public ResultVO<SupplierDraftSaveVO> saveDraft(@RequestBody SupplierDraftSaveDTO dto) {
    return ResultVO.success(supplierAdminAppService.saveDraft(dto));
}

@PostMapping("/saveAndSubmit")
public ResultVO<BaseVO> saveAndSubmit(@RequestBody SupplierSubmitSaveDTO dto) {
    return ResultVO.success(supplierAdminAppService.saveAndSubmit(dto));
}

@PostMapping("/draftList")
public ResultVO<List<SupplierDraftListItemVO>> draftList(@RequestBody SupplierDraftListDTO dto) {
    return ResultVO.success(supplierAdminAppService.draftList(dto));
}
```

- [ ] **Step 5: 在 `SupplierDraftAppServiceImpl` 与 `SupplierSaveAppServiceImpl` 中补草稿与提交删除逻辑**

```java
@Override
public BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto) {
    SupplierSaveContextPojo context = SupplierAdminAssembler.toSubmitContext(dto);
    protocolValidator.validate(context);
    commonValidator.validateForSubmit(context);
    businessValidator.validateForSubmit(context);

    SupplierSaveDTO saveDTO = SupplierAdminAssembler.toSubmitSaveDTO(dto);
    save(saveDTO);

    if (supplierDraftRepository != null && dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
        supplierDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
    }
    return new BaseVO();
}
```

- [ ] **Step 6: 运行草稿测试，确认转绿**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierDraftServiceTest test`
Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/admin \
  xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/application \
  xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/application/service
git commit -m "feat: add supplier draft and submit flow"
```

## Task 4: 统一持久化实现与 Mapper/XML 到 Supplier 最终态

**Files:**
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierConvertor.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierContactConvertor.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierAddressConvertor.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierBankAccountConvertor.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/convertor/SupplierInvoiceProfileConvertor.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierMapper.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierContactMapper.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierAddressMapper.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierBankAccountMapper.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/mapper/SupplierInvoiceProfileMapper.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierPO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierContactPO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierAddressPO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierBankAccountPO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/SupplierInvoiceProfilePO.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierRepositoryImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierContactRepositoryImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierAddressRepositoryImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierBankAccountRepositoryImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/SupplierInvoiceProfileRepositoryImpl.java`
- Create: `xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/repository/ConditionMapHelper.java`
- Create: `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierMapper.xml`
- Create: `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierContactMapper.xml`
- Create: `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierAddressMapper.xml`
- Create: `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierBankAccountMapper.xml`
- Create: `xbb-erp-module-supplier/src/main/resources/mapper/supplier/SupplierInvoiceProfileMapper.xml`
- Modify: `xbb-erp-module-supplier/pom.xml`
- Test: `mvn -pl xbb-erp-module-supplier test`

**Interfaces:**
- Consumes: `SupplierMapper#insert(SupplierPO po): int`
- Consumes: `SupplierMapper#updateById(SupplierPO po): int`
- Consumes: `SupplierMapper#findByCondition(Map<String, Object> conditionMap): List<SupplierPO>`
- Produces: `SupplierRepositoryImpl#findByCondition(Map<String, Object> conditionMap): List<Supplier>`
- Produces: `ConditionMapHelper#normalizePage(Map<String, Object> conditionMap): Map<String, Object>`

- [ ] **Step 1: 先写 SQL/持久化结构测试，锁定最终文件名与主表名**

```java
@Test
void should_reference_supplier_tables_in_sql_plan() throws Exception {
    String sql = Files.readString(Path.of("docs/sql/2026-08-03-refactor-supplier-module.sql"));
    assertTrue(sql.contains("RENAME TABLE `vendor` TO `supplier`"));
    assertTrue(sql.contains("RENAME TABLE `vendor_contact` TO `supplier_contact`"));
}
```

- [ ] **Step 2: 运行 SQL 结构测试，确认红灯**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierSqlPlanTest test`
Expected: FAIL，提示 SQL 文件不存在。

- [ ] **Step 3: 复制 vendor 持久化实现并统一改到 `Supplier*`，字段名同步调整**

```java
@Data
@TableName("supplier")
public class SupplierPO {
    private Long id;
    private String corpid;
    private String supplierCode;
    private String supplierName;
    private String supplierShortName;
    private String supplierCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserId;
    private String ownerPurchaserNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private Long defaultBankAccountId;
    private Long defaultInvoiceProfileId;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
```

- [ ] **Step 4: 在 `ConditionMapHelper` 中统一规整分页、groupBy、orderBy 参数**

```java
public final class ConditionMapHelper {

    private ConditionMapHelper() {
    }

    public static Map<String, Object> normalizePage(Map<String, Object> conditionMap) {
        if (conditionMap.get("offset") == null) {
            conditionMap.put("offset", 0);
        }
        if (conditionMap.get("pageSize") == null) {
            conditionMap.put("pageSize", 20);
        }
        return conditionMap;
    }
}
```

- [ ] **Step 5: 跑模块测试，确认持久化改名后仍可编译通过**

Run: `mvn -pl xbb-erp-module-supplier test`
Expected: PASS

- [ ] **Step 6: 提交**

```bash
git add xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure \
  xbb-erp-module-supplier/src/main/resources/mapper/supplier \
  xbb-erp-module-supplier/pom.xml
git commit -m "feat: migrate supplier persistence layer"
```

## Task 5: 交付数据库 SQL、更新接口文档并清理旧 Vendor 残留

**Files:**
- Create: `docs/sql/2026-08-03-refactor-supplier-module.sql`
- Create: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/sql/SupplierSqlPlanTest.java`
- Modify: `docs/api/supplier-supplier.md`
- Modify: `docs/base/项目业务module导航.md`
- Modify: `docs/sql/2026-07-22-init-supplier-module.sql`
- Test: `xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/sql/SupplierSqlPlanTest.java`

**Interfaces:**
- Produces: `docs/sql/2026-08-03-refactor-supplier-module.sql`
- Produces: `docs/api/supplier-supplier.md`
- Produces: `docs/base/项目业务module导航.md`

- [ ] **Step 1: 先写 SQL 文档测试，锁定重命名和字段重命名语句**

```java
package xbb.ai.erp.module.supplier.sql;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierSqlPlanTest {

    @Test
    void should_include_supplier_rename_sql() throws Exception {
        String sql = Files.readString(Path.of("docs/sql/2026-08-03-refactor-supplier-module.sql"));
        assertTrue(sql.contains("ALTER TABLE `supplier` CHANGE COLUMN `vendor_code` `supplier_code`"));
        assertTrue(sql.contains("ALTER TABLE `supplier_contact` CHANGE COLUMN `vendor_id` `supplier_id`"));
    }
}
```

- [ ] **Step 2: 运行 SQL 文档测试，确认红灯**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierSqlPlanTest test`
Expected: FAIL，提示 SQL 文件不存在或内容不完整。

- [ ] **Step 3: 编写最终 SQL，显式列出表重命名与字段重命名**

```sql
RENAME TABLE `vendor` TO `supplier`;
RENAME TABLE `vendor_contact` TO `supplier_contact`;
RENAME TABLE `vendor_address` TO `supplier_address`;
RENAME TABLE `vendor_bank_account` TO `supplier_bank_account`;
RENAME TABLE `vendor_invoice_profile` TO `supplier_invoice_profile`;

ALTER TABLE `supplier`
  CHANGE COLUMN `vendor_code` `supplier_code` varchar(64) NOT NULL,
  CHANGE COLUMN `vendor_name` `supplier_name` varchar(128) NOT NULL,
  CHANGE COLUMN `vendor_short_name` `supplier_short_name` varchar(128) DEFAULT NULL,
  CHANGE COLUMN `vendor_category` `supplier_category` varchar(32) NOT NULL;

ALTER TABLE `supplier_contact`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL;
```

- [ ] **Step 4: 更新接口文档，所有 DTO/VO、URL 示例、返回包装统一改为 Supplier 协议**

```md
## `/erp/v1/supplier/save`

- 请求方式：`POST`
- 入参：`SupplierSaveDTO`
- 返回：`ResultVO<Long>`
```

- [ ] **Step 5: 扫描全仓旧命名残留并清理**

Run: `rg -n "Vendor|vendor_" xbb-erp-module-supplier docs/api/supplier-supplier.md docs/sql/2026-08-03-refactor-supplier-module.sql`
Expected: 仅允许 SQL 迁移语句里的旧表/字段名残留；业务代码和接口文档中不再出现 `Vendor`。

- [ ] **Step 6: 跑 SQL 测试和模块测试，确认最终交付通过**

Run: `mvn -pl xbb-erp-module-supplier -Dtest=SupplierSqlPlanTest test && mvn -pl xbb-erp-module-supplier test`
Expected: PASS

- [ ] **Step 7: 提交**

```bash
git add docs/sql/2026-08-03-refactor-supplier-module.sql \
  docs/sql/2026-07-22-init-supplier-module.sql \
  docs/api/supplier-supplier.md \
  docs/base/项目业务module导航.md \
  xbb-erp-module-supplier/src/test/java/xbb/ai/erp/module/supplier/sql/SupplierSqlPlanTest.java
git commit -m "feat: finalize supplier protocol and sql delivery"
```

## Self-Review

- Spec coverage：已覆盖术语统一、DDD 分层、query/save/draft/delete 拆分、草稿与提交链路、`ResultVO.success()` 包装、持久化 `Supplier*` 收口、数据库 SQL 与接口文档交付。
- Placeholder scan：无 `TBD`、`TODO`、`implement later` 等占位词；每个任务都含明确文件路径、测试命令与示例代码。
- Type consistency：计划中的主命名统一为 `Supplier*`；数据库最终态统一为 `supplier_*`；draft/save/query/delete 四类服务名称前后一致。
