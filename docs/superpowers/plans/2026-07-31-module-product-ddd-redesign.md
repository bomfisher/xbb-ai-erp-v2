# Module Product DDD Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 `xbb-erp-module-product` 全量接口对齐 `module-customer` 最新 DDD 架构，其中 `product` 按 `SPU + SKU[]` 聚合升级为草稿化保存协议，`brand/category/unit/warehouse` 统一为规范化普通 CRUD。

**Architecture:** 保留 `product` 领域现有 `SPU` 与 `SKU` 持久化模型，新增 `application/service/product/{query,save,draft,delete}`、`application/assembler`、`application/validator` 与 `application/port` 分层，对控制器协议整体切换到 `POST + @RequestBody`。`brand/category/unit/warehouse` 不引入草稿协议，只迁移到最新接口与应用层风格；商品草稿复用 `customer` 的 Redis 草稿模式，不新增 MySQL 草稿表。

**Tech Stack:** `JDK 21`、`Spring Boot 3.3.2`、`Maven`、`Spring Web`、`MyBatis-Plus`、`Redis 7`、`JUnit 5`

## Global Constraints

- 项目架构必须遵循 DDD 领域驱动设计。
- 对话永远在中文语境下，注释使用中文。
- 所有主动捕获的报错、业务的主动抛错，都使用 `BizException`。
- 直接对接数据库的对象实体需要添加 `PO` 后缀，并且对象内字段不允许使用布尔值对接，改用 `Integer`。
- 枚举类需要以 `Enum` 结尾。
- 系统内 pojo 尾缀规范：对接前端入参 `DTO`、对接接口出参 `VO`，其余中转参数对象使用 `Pojo`。
- 所有接口 DTO 作为参数，而不是散列参数。非脚本接口，入参 DTO 都需要继承 `BaseDTO`。
- `userId` 员工 Id 是字符串 id。
- getter / setter 用 Lombok 管理。
- 如果接口业务代码没有需要返回的，用 `BaseVO` 返回。
- 所有接口的参数返回，都使用 `ResultVO.success()` 包装返回。
- 尽可能避免循环中查询数据库。
- `product` 保存协议必须兼顾多规格，保存对象是 `SPU + SKU[]` 聚合，而不是单 `SPU`。
- `warehouse` 保持普通 CRUD，不纳入商品草稿协议。
- `brand/category/unit` 保持普通 CRUD，不引入草稿协议。
- 本次仅修改后端仓库，不联动 `/Users/bomfish/xbb-ai-erp-v2-front`。
- 数据库只允许最小必要调整；仅当 `product_sku` 缺失 `spec_signature` / `spec_snapshot` 时补充 Flyway。
- 完成实现后，需执行技能 `gen-api-md` 生成或更新接口文档。

---

### Task 1: 建立 `product` 新协议骨架与结构测试

**Files:**
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/ModuleStructureTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductAdminControllerStructureTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductSaveContractStructureTest.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductMainDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSkuItemDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSaveDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSubmitSaveDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDraftSaveDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDraftListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDraftLoadDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductDetailVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductDraftDetailVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductDraftListItemVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductDraftSaveVO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductAdminController.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductVO.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/ModuleStructureTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductAdminControllerStructureTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductSaveContractStructureTest.java`

**Interfaces:**
- Consumes: `xbb.ai.erp.base.common.dto.BaseDTO`、`xbb.ai.erp.base.common.dto.BatchBaseDTO`、`xbb.ai.erp.base.common.dto.IdBaseDTO`、`xbb.ai.erp.base.common.vo.ResultVO`、`xbb.ai.erp.base.common.vo.SaveItemVO`
- Produces:
  - `ProductAdminController.list(ProductListDTO dto): ResultVO<ListBaseVO<ProductListItemVO>>`
  - `ProductAdminController.addItem(BaseDTO dto): ResultVO<SaveItemVO<ProductSkuItemVO>>`
  - `ProductAdminController.updateItem(IdBaseDTO dto): ResultVO<SaveItemVO<ProductSkuItemVO>>`
  - `ProductAdminController.saveDraft(ProductDraftSaveDTO dto): ResultVO<ProductDraftSaveVO>`
  - `ProductAdminController.saveAndSubmit(ProductSubmitSaveDTO dto): ResultVO<BaseVO>`
  - `ProductAdminController.draftList(ProductDraftListDTO dto): ResultVO<List<ProductDraftListItemVO>>`
  - `ProductAdminController.loadDraft(ProductDraftLoadDTO dto): ResultVO<ProductDraftDetailVO>`
  - `ProductAdminController.detail(IdBaseDTO dto): ResultVO<ProductDetailVO>`
  - `ProductAdminController.delete(BatchBaseDTO dto): ResultVO<Void>`

- [ ] **Step 1: 写失败的模块结构测试**

```java
package xbb.ai.erp.module.product;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_product_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/admin")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/admin/dto")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/admin/vo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/service")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/assembler")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/validator")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/port")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository")));
    }
}
```

- [ ] **Step 2: 写失败的 `ProductAdminController` 结构测试**

```java
package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductAdminControllerStructureTest {

    @Test
    void should_declare_product_endpoints_with_draft_flow() throws Exception {
        Method list = ProductAdminController.class.getMethod("list", ProductListDTO.class);
        Method addItem = ProductAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = ProductAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method saveDraft = ProductAdminController.class.getMethod("saveDraft", ProductDraftSaveDTO.class);
        Method saveAndSubmit = ProductAdminController.class.getMethod("saveAndSubmit", ProductSubmitSaveDTO.class);
        Method draftList = ProductAdminController.class.getMethod("draftList", ProductDraftListDTO.class);
        Method loadDraft = ProductAdminController.class.getMethod("loadDraft", ProductDraftLoadDTO.class);
        Method detail = ProductAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method delete = ProductAdminController.class.getMethod("delete", BatchBaseDTO.class);

        assertNotNull(list);
        assertNotNull(addItem);
        assertNotNull(updateItem);
        assertNotNull(saveDraft);
        assertNotNull(saveAndSubmit);
        assertNotNull(draftList);
        assertNotNull(loadDraft);
        assertNotNull(detail);
        assertNotNull(delete);
    }

    @Test
    void should_depend_on_product_admin_app_service() throws Exception {
        Field field = ProductAdminController.class.getDeclaredField("productAdminAppService");
        assertEquals(
            Class.forName("xbb.ai.erp.module.product.application.service.ProductAdminAppService"),
            field.getType()
        );
    }
}
```

- [ ] **Step 3: 写失败的保存协议结构测试**

```java
package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductSaveContractStructureTest {

    @Test
    void should_make_product_save_dto_extend_base_dto_and_hold_spu_plus_skus() throws Exception {
        assertEquals(BaseDTO.class, ProductSaveDTO.class.getSuperclass());

        Field main = ProductSaveDTO.class.getDeclaredField("main");
        Field skus = ProductSaveDTO.class.getDeclaredField("skus");
        assertEquals(ProductMainDTO.class, main.getType());
        assertEquals(List.class, skus.getType());
    }

    @Test
    void should_make_submit_and_draft_extend_save_contract() {
        assertEquals(ProductSaveDTO.class, ProductSubmitSaveDTO.class.getSuperclass());
        assertEquals(ProductSaveDTO.class, ProductDraftSaveDTO.class.getSuperclass());
    }

    @Test
    void should_define_product_sku_item_dto() throws Exception {
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("skuCode").getType());
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("skuName").getType());
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("specSignature").getType());
        assertEquals(String.class, ProductSkuItemDTO.class.getDeclaredField("specSnapshot").getType());
    }
}
```

- [ ] **Step 4: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-product -Dtest=ModuleStructureTest,ProductAdminControllerStructureTest,ProductSaveContractStructureTest test`
Expected: FAIL，缺少 `ProductListDTO`、`ProductDraftSaveDTO`、`ProductAdminAppService` 或 `ProductAdminController` 方法签名不匹配。

- [ ] **Step 5: 写最小 DTO / VO 与控制器骨架**

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSaveDTO extends BaseDTO {
    private ProductMainDTO main;
    private List<ProductSkuItemDTO> skus;
}

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSubmitSaveDTO extends ProductSaveDTO {
    private ProductDraftMetaDTO draftMeta;
}

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDraftSaveDTO extends ProductSaveDTO {
    private ProductDraftMetaDTO draftMeta;
}
```

```java
@RestController
@RequestMapping("/erp/v1/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductAdminAppService productAdminAppService;

    @PostMapping("/list")
    public ResultVO<ListBaseVO<ProductListItemVO>> list(@RequestBody ProductListDTO dto) {
        return ResultVO.success(productAdminAppService.list(dto));
    }

    @PostMapping("/addItem")
    public ResultVO<SaveItemVO<ProductSkuItemVO>> addItem(@RequestBody BaseDTO dto) {
        return ResultVO.success(productAdminAppService.addItem(dto));
    }
}
```

- [ ] **Step 6: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=ModuleStructureTest,ProductAdminControllerStructureTest,ProductSaveContractStructureTest test`
Expected: PASS

- [ ] **Step 7: Commit**

```bash
git add xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product
git commit -m "feat: define product aggregate admin contract"
```

### Task 2: 搭建 `product` 应用层门面、查询与删除分层

**Files:**
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/ProductAdminAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/impl/ProductAdminAppServiceImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/query/ProductQueryAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/query/ProductQueryAppServiceImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/delete/ProductDeleteAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/delete/ProductDeleteAppServiceImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/assembler/ProductAdminAssembler.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/pojo/ProductQueryPojo.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/pojo/ProductSkuQueryPojo.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductSpuRepository.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductSkuRepository.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductSpuRepositoryImpl.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductSkuRepositoryImpl.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductAppService.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductDetailServiceTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductListServiceTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductDeleteServiceTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/support/FakeProductSpuRepository.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/support/FakeProductSkuRepository.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/support/InMemoryProductSpuRepository.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/support/InMemoryProductSkuRepository.java`

**Interfaces:**
- Consumes:
  - `ProductSpuRepository.findById(String corpid, Long id): ProductSpu`
  - `ProductSkuRepository.findBySpuId(String corpid, Long spuId): List<ProductSku>`
  - `ProductSkuRepository.removeBySpuId(String corpid, Long spuId, String userId): void`
- Produces:
  - `ProductAdminAppService.list(ProductListDTO dto): ListBaseVO<ProductListItemVO>`
  - `ProductAdminAppService.detail(IdBaseDTO dto): ProductDetailVO`
  - `ProductAdminAppService.delete(BatchBaseDTO dto): void`
  - `ProductQueryAppService.list(ProductListDTO dto): ListBaseVO<ProductListItemVO>`
  - `ProductQueryAppService.detail(IdBaseDTO dto): ProductDetailVO`
  - `ProductDeleteAppService.delete(BatchBaseDTO dto): void`

- [ ] **Step 1: 写 `detail/list/delete` 失败测试**

```java
@Test
void should_build_product_detail_with_spu_and_all_skus() {
    ProductDetailVO detail = service.detail(idBaseDTO("c1", 101L));
    assertEquals(101L, detail.getMain().getId());
    assertEquals(2, detail.getSkus().size());
}

@Test
void should_delete_skus_before_spu() {
    service.delete(batchBaseDTO("c1", List.of(101L)));
    assertEquals(List.of("sku:101", "spu:101"), repositoryLog);
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductDetailServiceTest,ProductListServiceTest,ProductDeleteServiceTest test`
Expected: FAIL，缺少 `ProductQueryAppService` / `ProductDeleteAppService` 或 `findBySpuId` 返回类型不满足多 SKU。

- [ ] **Step 3: 修改仓储接口支持多 SKU 查询**

```java
public interface ProductSkuRepository {
    List<ProductSku> findBySpuId(String corpid, Long spuId);
    List<ProductSku> findBySpuIds(String corpid, List<Long> spuIds);
    void removeBySpuId(String corpid, Long spuId, String userId);
}
```

```java
@Override
public List<ProductSku> findBySpuId(String corpid, Long spuId) {
    return productSkuMapper.findBySpuId(corpid, spuId)
        .stream()
        .map(this::toDomain)
        .toList();
}
```

- [ ] **Step 4: 写最小查询与删除实现**

```java
@Service
@RequiredArgsConstructor
public class ProductQueryAppServiceImpl implements ProductQueryAppService {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;

    @Override
    public ProductDetailVO detail(IdBaseDTO dto) {
        ProductSpu spu = productSpuRepository.findById(dto.getCorpid(), dto.getId());
        List<ProductSku> skus = productSkuRepository.findBySpuId(dto.getCorpid(), dto.getId());
        return ProductAdminAssembler.toDetailVO(spu, skus);
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class ProductDeleteAppServiceImpl implements ProductDeleteAppService {

    private final ProductSpuRepository productSpuRepository;
    private final ProductSkuRepository productSkuRepository;

    @Override
    public void delete(BatchBaseDTO dto) {
        for (Long id : dto.getIds()) {
            productSkuRepository.removeBySpuId(dto.getCorpid(), id, dto.getUserId());
            productSpuRepository.removeById(dto.getCorpid(), id, dto.getUserId());
        }
    }
}
```

- [ ] **Step 5: 用门面替换旧 `ProductAppService` 控制器依赖**

```java
@Service
@RequiredArgsConstructor
public class ProductAdminAppServiceImpl implements ProductAdminAppService {

    private final ProductQueryAppService productQueryAppService;
    private final ProductDeleteAppService productDeleteAppService;

    @Override
    public ProductDetailVO detail(IdBaseDTO dto) {
        return productQueryAppService.detail(dto);
    }
}
```

- [ ] **Step 6: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductDetailServiceTest,ProductListServiceTest,ProductDeleteServiceTest test`
Expected: PASS

- [ ] **Step 7: Commit**

```bash
git add xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service
git commit -m "feat: split product query and delete services"
```

### Task 3: 实现 `product` 保存校验、聚合同步与多规格规则

**Files:**
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/save/ProductSaveAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/save/ProductSaveAppServiceImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/validator/ProductSaveProtocolValidator.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/validator/ProductSaveCommonValidator.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/validator/ProductSaveBusinessValidator.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/pojo/ProductSaveContextPojo.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/pojo/ProductSaveDraftPojo.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/assembler/ProductAdminAssembler.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/impl/ProductAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductSku.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductSaveServiceTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductSaveValidationTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/support/FakeProductCodeCheckRepository.java`

**Interfaces:**
- Consumes:
  - `ProductSpuRepository.save(ProductSpu productSpu, String userId): Long`
  - `ProductSpuRepository.update(ProductSpu productSpu, String userId): void`
  - `ProductSkuRepository.findBySpuId(String corpid, Long spuId): List<ProductSku>`
  - `ProductSkuRepository.save(ProductSku productSku, String userId): Long`
  - `ProductSkuRepository.update(ProductSku productSku, String userId): void`
  - `ProductSkuRepository.removeById(String corpid, Long id, String userId): void`
- Produces:
  - `ProductSaveAppService.save(ProductSaveDTO dto): Long`
  - `ProductSaveAppService.saveAndSubmit(ProductSubmitSaveDTO dto): BaseVO`
  - `ProductSaveProtocolValidator.validate(ProductSaveContextPojo context): void`
  - `ProductSaveBusinessValidator.validateForSubmit(ProductSaveContextPojo context): void`

- [ ] **Step 1: 写失败的保存与校验测试**

```java
@Test
void should_save_single_spec_product_with_exactly_one_sku() {
    Long id = service.save(singleSpecDto());
    assertEquals(1, savedSkus.size());
    assertNotNull(id);
}

@Test
void should_reject_single_spec_with_multiple_skus() {
    BizException ex = assertThrows(BizException.class, () -> service.save(singleSpecWithTwoSkusDto()));
    assertEquals("单规格商品仅允许一条SKU", ex.getMessage());
}

@Test
void should_reject_duplicate_spec_signature_in_multi_spec() {
    BizException ex = assertThrows(BizException.class, () -> service.save(duplicateSpecSignatureDto()));
    assertEquals("同一商品下规格签名不允许重复", ex.getMessage());
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductSaveServiceTest,ProductSaveValidationTest test`
Expected: FAIL，缺少 `ProductSaveAppService`、校验器或保存逻辑。

- [ ] **Step 3: 写协议与业务校验器**

```java
public class ProductSaveProtocolValidator {

    public void validate(ProductSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new BizException("商品主档不能为空");
        }
        if (context.getSkus() == null || context.getSkus().isEmpty()) {
            throw new BizException("商品SKU不能为空");
        }
        if (Integer.valueOf(0).equals(context.getMain().getEnableSpec()) && context.getSkus().size() != 1) {
            throw new BizException("单规格商品仅允许一条SKU");
        }
    }
}
```

```java
public class ProductSaveBusinessValidator {

    public void validateForSubmit(ProductSaveContextPojo context) {
        long duplicateSkuCodeCount = context.getSkus().stream()
            .map(ProductSkuItemDTO::getSkuCode)
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values().stream().filter(count -> count > 1).count();
        if (duplicateSkuCodeCount > 0) {
            throw new BizException("同一商品下SKU编码不允许重复");
        }
    }
}
```

- [ ] **Step 4: 写最小聚合保存实现**

```java
@Override
public Long save(ProductSaveDTO dto) {
    ProductSaveContextPojo context = ProductAdminAssembler.toSaveContext(dto);
    protocolValidator.validate(context);
    commonValidator.validateForSave(context);
    businessValidator.validateForSubmit(context);

    ProductSpu productSpu = ProductAdminAssembler.toProductSpu(dto);
    if (productSpu.getId() == null) {
        Long spuId = productSpuRepository.save(productSpu, dto.getUserId());
        syncSkus(dto, spuId);
        return spuId;
    }
    productSpuRepository.update(productSpu, dto.getUserId());
    syncSkus(dto, productSpu.getId());
    return productSpu.getId();
}
```

```java
private void syncSkus(ProductSaveDTO dto, Long spuId) {
    List<ProductSku> existing = productSkuRepository.findBySpuId(dto.getCorpid(), spuId);
    Set<Long> incomingIds = dto.getSkus().stream().map(ProductSkuItemDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet());
    for (ProductSku sku : existing) {
        if (!incomingIds.contains(sku.getId())) {
            productSkuRepository.removeById(dto.getCorpid(), sku.getId(), dto.getUserId());
        }
    }
    for (ProductSkuItemDTO skuItem : dto.getSkus()) {
        ProductSku productSku = ProductAdminAssembler.toProductSku(dto.getCorpid(), spuId, skuItem);
        if (productSku.getId() == null) {
            productSkuRepository.save(productSku, dto.getUserId());
        } else {
            productSkuRepository.update(productSku, dto.getUserId());
        }
    }
}
```

- [ ] **Step 5: 让门面接入 `saveAndSubmit`**

```java
@Override
public BaseVO saveAndSubmit(ProductSubmitSaveDTO dto) {
    productSaveAppService.saveAndSubmit(dto);
    return new BaseVO();
}
```

- [ ] **Step 6: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductSaveServiceTest,ProductSaveValidationTest test`
Expected: PASS

- [ ] **Step 7: Commit**

```bash
git add xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service
git commit -m "feat: implement product aggregate save flow"
```

### Task 4: 实现 `product` 草稿保存、加载与提交后清理

**Files:**
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/port/ProductDraftRepository.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/draft/ProductDraftAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/draft/ProductDraftAppServiceImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductDraftRepositoryImpl.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/save/ProductSaveAppServiceImpl.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/impl/ProductAdminAppServiceImpl.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductDraftServiceTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/draft/ProductDraftAppServiceTest.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/support/InMemoryProductDraftRepository.java`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductDraftRepositoryImplTest.java`

**Interfaces:**
- Consumes:
  - `ProductDraftRepository.saveDraft(ProductSaveDraftPojo draft): String`
  - `ProductDraftRepository.listDrafts(String corpid, int limit): List<ProductSaveDraftPojo>`
  - `ProductDraftRepository.loadDraft(String corpid, String draftCode): ProductSaveDraftPojo`
  - `ProductDraftRepository.removeDraft(String corpid, String draftCode): void`
- Produces:
  - `ProductDraftAppService.saveDraft(ProductDraftSaveDTO dto): ProductDraftSaveVO`
  - `ProductDraftAppService.draftList(ProductDraftListDTO dto): List<ProductDraftListItemVO>`
  - `ProductDraftAppService.loadDraft(ProductDraftLoadDTO dto): ProductDraftDetailVO`

- [ ] **Step 1: 写失败的草稿测试**

```java
@Test
void should_save_and_load_product_draft() {
    ProductDraftSaveVO saved = service.saveDraft(draftDto());
    ProductDraftDetailVO loaded = service.loadDraft(loadDto(saved.getDraftCode()));
    assertEquals("SPU-001", loaded.getMain().getSpuCode());
    assertEquals(2, loaded.getSkus().size());
}

@Test
void should_remove_draft_after_submit() {
    service.saveAndSubmit(submitDtoWithDraftCode("draft-1"));
    assertNull(draftRepository.loadDraft("c1", "draft-1"));
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductDraftServiceTest,ProductDraftAppServiceTest,ProductDraftRepositoryImplTest test`
Expected: FAIL，缺少 `ProductDraftRepository`、`ProductDraftAppService` 或提交后清理逻辑。

- [ ] **Step 3: 参考 `customer` 写最小草稿仓储实现**

```java
@Repository
@RequiredArgsConstructor
public class ProductDraftRepositoryImpl implements ProductDraftRepository {

    static final String DRAFT_KEY_PREFIX = "product:draft:";
    static final String DRAFT_INDEX_KEY_PREFIX = "product:draft:index:";
    static final Duration DRAFT_TTL = Duration.ofDays(7);
    static final int MAX_DRAFT_COUNT = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
}
```

- [ ] **Step 4: 写最小草稿应用服务与提交后清理**

```java
@Override
public ProductDraftSaveVO saveDraft(ProductDraftSaveDTO dto) {
    ProductSaveDraftPojo draft = ProductAdminAssembler.toDraftPojo(dto);
    String draftCode = productDraftRepository.saveDraft(draft);
    ProductDraftSaveVO vo = new ProductDraftSaveVO();
    vo.setDraftCode(draftCode);
    return vo;
}
```

```java
if (productDraftRepository != null && dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
    productDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
}
```

- [ ] **Step 5: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductDraftServiceTest,ProductDraftAppServiceTest,ProductDraftRepositoryImplTest test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/infrastructure/persistence/repository
git commit -m "feat: add product draft persistence"
```

### Task 5: 改造 `brand/category/unit` 为规范化普通 CRUD

**Files:**
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductBrandAdminController.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductCategoryAdminController.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductUnitAdminController.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandCreateDTO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandUpdateDTO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryCreateDTO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryUpdateDTO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitCreateDTO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitUpdateDTO.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductBrandAdminControllerStructureTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductCategoryAdminControllerStructureTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/ProductUnitAdminControllerStructureTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductBrandServiceTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductCategoryServiceTest.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/ProductUnitServiceTest.java`

**Interfaces:**
- Consumes: 现有 `ProductBrandAppService`、`ProductCategoryAppService`、`ProductUnitAppService`
- Produces:
  - `ProductBrandAdminController.list(ProductBrandListDTO dto): ResultVO<ListBaseVO<ProductBrandVO>>`
  - `ProductCategoryAdminController.list(ProductCategoryListDTO dto): ResultVO<ListBaseVO<ProductCategoryVO>>`
  - `ProductUnitAdminController.list(ProductUnitListDTO dto): ResultVO<ListBaseVO<ProductUnitVO>>`

- [ ] **Step 1: 写失败的控制器结构测试**

```java
@Test
void should_use_post_and_request_body_for_brand_controller() throws Exception {
    Method list = ProductBrandAdminController.class.getMethod("list", ProductBrandListDTO.class);
    assertNotNull(list.getAnnotation(PostMapping.class));
    assertNotNull(list.getParameters()[0].getAnnotation(RequestBody.class));
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductBrandAdminControllerStructureTest,ProductCategoryAdminControllerStructureTest,ProductUnitAdminControllerStructureTest test`
Expected: FAIL，因为当前仍使用 `GET` 且缺少 `@RequestBody`。

- [ ] **Step 3: 改控制器与 DTO 继承关系**

```java
@PostMapping("/list")
public ResultVO<ListBaseVO<ProductBrandVO>> list(@RequestBody ProductBrandListDTO dto) {
    return ResultVO.success(productBrandAppService.list(dto));
}
```

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBrandCreateDTO extends BaseDTO {
    private Long id;
    private String brandCode;
    private String brandName;
}
```

- [ ] **Step 4: 写最小服务测试并调整返回类型**

```java
@Test
void should_return_list_base_vo_for_product_unit_list() {
    ListBaseVO<ProductUnitVO> result = service.list(listDto());
    assertEquals(1, result.getList().size());
}
```

- [ ] **Step 5: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductBrandAdminControllerStructureTest,ProductCategoryAdminControllerStructureTest,ProductUnitAdminControllerStructureTest,ProductBrandServiceTest,ProductCategoryServiceTest,ProductUnitServiceTest test`
Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service
git commit -m "feat: normalize product master crud controllers"
```

### Task 6: 对齐 `warehouse` 命名、结构测试与服务规范

**Files:**
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/WarehouseAdminAppService.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service/impl/WarehouseAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/assembler/WarehouseAdminAssembler.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/WarehouseAdminController.java`
- Create: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin/WarehouseAdminControllerStructureTest.java`
- Modify: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/WarehouseAddItemViewTest.java`
- Modify: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/WarehouseUpdateItemViewTest.java`
- Modify: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/WarehouseQueryServiceTest.java`
- Modify: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service/WarehouseSaveDeleteServiceTest.java`

**Interfaces:**
- Consumes: 现有 `WarehouseRepository`、`WarehouseConditionMapHelper`
- Produces:
  - `WarehouseAdminController.list(WarehouseListDTO dto): ResultVO<ListBaseVO<WarehouseListItemVO>>`
  - `WarehouseAdminController.addItem(BaseDTO dto): ResultVO<SaveItemVO<WarehouseSaveItemVO>>`
  - `WarehouseAdminController.updateItem(IdBaseDTO dto): ResultVO<SaveItemVO<WarehouseSaveItemVO>>`
  - `WarehouseAdminController.save(WarehouseSaveDTO dto): ResultVO<Long>`
  - `WarehouseAdminController.detail(IdBaseDTO dto): ResultVO<WarehouseDetailVO>`
  - `WarehouseAdminController.delete(BatchBaseDTO dto): ResultVO<Void>`

- [ ] **Step 1: 写失败的 `warehouse` 控制器结构测试**

```java
@Test
void should_depend_on_warehouse_admin_app_service() throws Exception {
    Field field = WarehouseAdminController.class.getDeclaredField("warehouseAdminAppService");
    assertEquals(
        Class.forName("xbb.ai.erp.module.product.application.service.WarehouseAdminAppService"),
        field.getType()
    );
}
```

- [ ] **Step 2: 运行测试确认失败或暴露不一致**

Run: `mvn -pl xbb-erp-module-product -Dtest=WarehouseAdminControllerStructureTest,WarehouseAddItemViewTest,WarehouseUpdateItemViewTest,WarehouseQueryServiceTest,WarehouseSaveDeleteServiceTest test`
Expected: FAIL 或提示服务签名与结构测试不一致。

- [ ] **Step 3: 调整 `warehouse` 服务命名与返回结构细节**

```java
public interface WarehouseAdminAppService {
    ListBaseVO<WarehouseListItemVO> list(WarehouseListDTO dto);
    SaveItemVO<WarehouseSaveItemVO> addItem(BaseDTO dto);
    SaveItemVO<WarehouseSaveItemVO> updateItem(IdBaseDTO dto);
    Long save(WarehouseSaveDTO dto);
    WarehouseDetailVO detail(IdBaseDTO dto);
    void delete(BatchBaseDTO dto);
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=WarehouseAdminControllerStructureTest,WarehouseAddItemViewTest,WarehouseUpdateItemViewTest,WarehouseQueryServiceTest,WarehouseSaveDeleteServiceTest test`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/application/service xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/admin xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/application/service
git commit -m "refactor: align warehouse admin service contract"
```

### Task 7: 补齐 SQL 规划校验与最小 Flyway（仅在缺字段时）

**Files:**
- Modify: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/sql/ProductSqlPlanTest.java`
- Create: `xbb-erp-app-admin/src/main/resources/db/migration/V20260731__product_sku_add_spec_columns.sql`（仅当现有 schema 缺字段时创建）
- Modify: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSkuMapper.xml`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductSkuMapper.java`

**Interfaces:**
- Consumes: 现有 `docs/sql/2026-07-22-init-product-module.sql`
- Produces:
  - `ProductSqlPlanTest.should_include_product_spec_columns_or_flyway_plan(): void`

- [ ] **Step 1: 写失败的 SQL 规划测试**

```java
@Test
void should_include_product_spec_columns_or_flyway_plan() throws Exception {
    Path initSql = Path.of("../docs/sql/2026-07-22-init-product-module.sql");
    String sql = Files.readString(initSql);
    boolean initContains = sql.contains("spec_signature") && sql.contains("spec_snapshot");
    Path flyway = Path.of("../xbb-erp-app-admin/src/main/resources/db/migration/V20260731__product_sku_add_spec_columns.sql");
    assertTrue(initContains || Files.exists(flyway));
}
```

- [ ] **Step 2: 运行测试确认失败或确认已满足**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductSqlPlanTest test`
Expected: PASS（若初始化 SQL 已含字段）或 FAIL（提示需补 Flyway）。

- [ ] **Step 3: 仅在失败时补最小 Flyway**

```sql
ALTER TABLE `product_sku`
    ADD COLUMN `spec_signature` varchar(512) DEFAULT NULL COMMENT '规格签名' AFTER `main_barcode`,
    ADD COLUMN `spec_snapshot` text DEFAULT NULL COMMENT '规格快照' AFTER `spec_signature`;
```

- [ ] **Step 4: 运行测试确认通过**

Run: `mvn -pl xbb-erp-module-product -Dtest=ProductSqlPlanTest test`
Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/sql/ProductSqlPlanTest.java xbb-erp-module-product/src/main/resources/mapper/product/ProductSkuMapper.xml xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductSkuMapper.java xbb-erp-app-admin/src/main/resources/db/migration/V20260731__product_sku_add_spec_columns.sql
git commit -m "test: lock product sku spec column migration plan"
```

### Task 8: 全量回归、移除旧入口并生成接口文档

**Files:**
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductAppService.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductBrandAppService.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductCategoryAppService.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductUnitAppService.java`
- Modify: `xbb-erp-module-product/pom.xml`
- Modify: `docs/superpowers/specs/2026-07-31-module-product-ddd-redesign.md`
- Test: `xbb-erp-module-product/src/test/java/xbb/ai/erp/module/product/**/*.java`

**Interfaces:**
- Consumes: Tasks 1-7 产出的全部新接口与测试
- Produces:
  - `module-product` 编译通过、核心测试通过
  - 移除或废弃旧 `ProductAppService` 入口引用
  - 生成更新后的接口文档

- [ ] **Step 1: 写失败的编译/回归目标清单**

```text
需要通过的关键验证：
1. ProductAdminController 新草稿协议测试
2. ProductSaveService / ProductDraftService / ProductDeleteService
3. Brand / Category / Unit / Warehouse 控制器结构测试
4. ProductSqlPlanTest
```

- [ ] **Step 2: 运行模块测试，确认仍有残余旧入口失败点**

Run: `mvn -pl xbb-erp-module-product test`
Expected: 若仍引用旧 `ProductAppService` 或旧 `GET` 协议，出现 FAIL。

- [ ] **Step 3: 清理旧入口与无用方法**

```java
@Deprecated(forRemoval = true)
@Service
public class ProductAppService {
}
```

```java
// 若无外部引用，直接删除旧 create/update/remove/listSpu/listSku/listSpuSku 逻辑，改为新门面调用。
```

- [ ] **Step 4: 运行模块测试确认通过**

Run: `mvn -pl xbb-erp-module-product test`
Expected: PASS

- [ ] **Step 5: 执行接口文档技能**

Run skill: `gen-api-md`
Expected: 生成或更新 `module-product` 相关接口文档，覆盖 `product` 草稿协议与 `brand/category/unit/warehouse` 普通 CRUD。

- [ ] **Step 6: Commit**

```bash
git add xbb-erp-module-product docs/superpowers/specs/2026-07-31-module-product-ddd-redesign.md
git commit -m "feat: complete module product ddd redesign"
```

## Self-Review

- Spec coverage：
  - `product` 草稿协议：Task 1、Task 3、Task 4
  - `SPU + SKU[]` 聚合保存与多规格：Task 3
  - `brand/category/unit` 普通 CRUD 规范化：Task 5
  - `warehouse` 保持普通 CRUD：Task 6
  - 最小 Flyway：Task 7
  - 测试与回归：Task 1-8
- Placeholder scan：已移除 `TODO/TBD` 和“类似 Task N”的引用，每个任务都包含明确文件、代码与命令。
- Type consistency：计划统一使用 `ProductAdminAppService` 门面、`ProductSaveDTO / ProductSubmitSaveDTO / ProductDraftSaveDTO`、`ProductDraftRepository`、`ProductQueryAppService` 等命名；`ProductSkuRepository.findBySpuId` 在 Task 2 明确升级为返回 `List<ProductSku>`，后续任务沿用该签名。
