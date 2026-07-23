# Product Module Initialization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build `xbb-erp-module-product` as the minimal product-domain module with five core tables, dictionary CRUD endpoints, product aggregate endpoints, SQL DDL, and module navigation wiring.

**Architecture:** The module stays inside the existing modular monolith and follows the repo's DDD layering. `product_category` / `product_brand` / `product_unit` are single-table CRUD resources; `product_spu` / `product_sku` stay separate in persistence but are orchestrated as one product aggregate in the app/controller layer. Admin endpoints are exposed from the module and loaded by `xbb-erp-app-admin` through package scanning and Maven dependency wiring.

**Tech Stack:** JDK 21, Spring Boot 3.3.2, Maven, Spring Web, MyBatis-Plus 3.5.7, JUnit 5, Lombok, MySQL-style Mapper XML.

## Global Constraints

- 对话和代码落地遵循当前仓库中文语境。
- 运行时固定为 `JDK 21`。
- 应用框架固定为 `Spring Boot 3.3.2`。
- 构建工具固定为 `Maven`。
- ORM 固定为 `MyBatis-Plus`。
- 直接对接数据库的对象实体必须添加 `PO` 后缀。
- 直接对接数据库的字段不允许使用布尔值，改用整数型。
- 枚举类必须使用 `Enum` 结尾。
- 系统内 pojo 尾缀规范：前端入参 `DTO`、接口出参 `VO`、其余中转对象 `Pojo`。
- 所有接口 DTO 作为参数，非脚本接口入参 DTO 必须继承 `BaseDTO`。
- `userId` / 员工 ID 使用字符串类型。
- getter / setter 用 Lombok 管理。
- 本次仅初始化 `product_category`、`product_brand`、`product_unit`、`product_spu`、`product_sku` 五张表。
- 本次不接入 `xbb-erp-module-common` 的通用列表能力；列表只做简单分页。
- 商品列表必须同时提供 `SPU`、`SKU`、`SPU+SKU` 三个接口。
- 模块导航真实文件路径是 `docs/base/项目顶部和底部module导航.md`。

---

## File Structure

### New module root

- Create: `xbb-erp-module-product/pom.xml`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductCategoryAdminController.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductBrandAdminController.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductUnitAdminController.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductAdminController.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/...`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/...`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductCategoryAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductBrandAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductUnitAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductCategory.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductBrand.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductUnit.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductSpu.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductSku.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/...`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po/...`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/...`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/...`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductCategoryMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductBrandMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductUnitMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSpuMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSkuMapper.xml`

### Existing files to modify

- Modify: `pom.xml`
- Modify: `xbb-erp-app-admin/pom.xml`
- Modify: `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`
- Modify: `docs/base/项目顶部和底部module导航.md`
- Modify: `docs/sql/2026-07-22-init-product-module.sql` (create if missing)

### Suggested DTO / VO set

- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDeleteDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDetailDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSpuListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSkuListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSpuSkuListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductCategoryVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductBrandVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductUnitVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductSpuListVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductSkuListVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductSpuSkuListVO.java`

---

### Task 1: Scaffold the product module and wire it into admin

**Files:**
- Modify: `pom.xml`
- Modify: `xbb-erp-app-admin/pom.xml`
- Create: `xbb-erp-module-product/pom.xml`

**Interfaces:**
- Consumes: existing parent Maven layout from `pom.xml`, Spring Boot scanning from `xbb-erp-app-admin/src/main/java/xbb/ai/erp/app/admin/AdminApplication.java`
- Produces: Maven module `xbb-erp-module-product`; admin-app dependency on `org.bomfish:xbb-erp-module-product:${project.version}`

- [ ] **Step 1: Write the failing build check**

```bash
mvn -q -pl xbb-erp-module-product -am test
```

Expected: FAIL with a Maven error that `xbb-erp-module-product` is not part of the reactor.

- [ ] **Step 2: Create the module entry in the parent POM**

Add this line under `<modules>` in `pom.xml`:

```xml
<module>xbb-erp-module-product</module>
```

- [ ] **Step 3: Create `xbb-erp-module-product/pom.xml`**

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

    <artifactId>xbb-erp-module-product</artifactId>

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
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
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

- [ ] **Step 4: Add the product module dependency to `xbb-erp-app-admin/pom.xml`**

```xml
<dependency>
    <groupId>org.bomfish</groupId>
    <artifactId>xbb-erp-module-product</artifactId>
    <version>${project.version}</version>
</dependency>
```

Place it alongside the other internal `org.bomfish` dependencies.

- [ ] **Step 5: Run the module build to verify the scaffold passes**

Run: `mvn -q -pl xbb-erp-module-product -am test`

Expected: PASS (or PASS with `0 tests` for the new module).

- [ ] **Step 6: Commit**

```bash
git add pom.xml xbb-erp-app-admin/pom.xml xbb-erp-module-product/pom.xml
git commit -m "feat: scaffold product module"
```

### Task 2: Add DDL and navigation docs for the five-table minimal product scope

**Files:**
- Create: `docs/sql/2026-07-22-init-product-module.sql`
- Modify: `docs/base/项目顶部和底部module导航.md`
- Test: `mvn -q -pl xbb-erp-module-product -am test`

**Interfaces:**
- Consumes: product spec in `docs/superpowers/specs/2026-07-22-product-module-design.md`
- Produces: SQL DDL file for five tables; product-module entry in navigation docs

- [ ] **Step 1: Write the failing content check**

Run: `rg -n "xbb-erp-module-product|product_category|product_spu" docs/base/项目顶部和底部module导航.md docs/sql/2026-07-22-init-product-module.sql`

Expected: FAIL because the SQL file is missing and the navigation doc has no product-module section.

- [ ] **Step 2: Create `docs/sql/2026-07-22-init-product-module.sql` with the five tables**

Include these table names and shared audit columns exactly:

```sql
CREATE TABLE `product_category` (
  `id` bigint NOT NULL,
  `corpid` varchar(50) NOT NULL,
  `category_code` varchar(64) NOT NULL,
  `category_name` varchar(128) NOT NULL,
  `parent_id` bigint NOT NULL,
  `category_level` int NOT NULL,
  `sort_no` int NOT NULL,
  `enable_status` tinyint NOT NULL,
  `del` tinyint NOT NULL,
  `add_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `creator_id` varchar(50) NOT NULL,
  `modify_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`corpid`,`category_code`,`del`)
);

CREATE TABLE `product_brand` (...);
CREATE TABLE `product_unit` (...);
CREATE TABLE `product_spu` (...);
CREATE TABLE `product_sku` (...);
```

Fill `product_brand` / `product_unit` / `product_spu` / `product_sku` with the exact minimal columns from the approved spec.

- [ ] **Step 3: Update `docs/base/项目顶部和底部module导航.md`**

Append a new section under `### xbb-erp-module-*` using this shape:

```markdown
#### xbb-erp-module-product
- 功能定位：商品主数据最小初始化模块
- 责任范围：商品分类、品牌、单位、SPU、SKU 的基础主档、持久化映射、管理端最小 CRUD 与商品聚合查询入口
- 当前表范围：`product_category`、`product_brand`、`product_unit`、`product_spu`、`product_sku`
- 当前代码落位：`admin`、`app/service`、`domain/model`、`domain/repository`、`infrastructure/persistence/po`、`infrastructure/persistence/mapper`、`infrastructure/persistence/repository`
- 不负责什么：规格、多条码、多单位、库存控制、默认业务属性、制造属性、facade 对外能力
```

- [ ] **Step 4: Run the content check again**

Run: `rg -n "xbb-erp-module-product|product_category|product_spu" docs/base/项目顶部和底部module导航.md docs/sql/2026-07-22-init-product-module.sql`

Expected: PASS with matching lines from both files.

- [ ] **Step 5: Commit**

```bash
git add docs/sql/2026-07-22-init-product-module.sql docs/base/项目顶部和底部module导航.md
git commit -m "docs: add product module ddl and navigation"
```

### Task 3: Implement dictionary persistence and admin CRUD for category, brand, and unit

**Files:**
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductCategory.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductBrand.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductUnit.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductCategoryRepository.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductBrandRepository.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductUnitRepository.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po/ProductCategoryPO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po/ProductBrandPO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po/ProductUnitPO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductCategoryMapper.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductBrandMapper.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductUnitMapper.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductCategoryRepositoryImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductBrandRepositoryImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductUnitRepositoryImpl.java`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductCategoryMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductBrandMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductUnitMapper.xml`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductCategoryAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductBrandAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductUnitAppService.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCategoryListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductBrandListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUnitListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductCategoryVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductBrandVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductUnitVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductCategoryAdminController.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductBrandAdminController.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductUnitAdminController.java`
- Modify: `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`

**Interfaces:**
- Consumes: `BaseDTO`, `ResultVO<T>`, Spring MVC conventions, `@RestController`
- Produces: 
  - `ProductCategoryAppService#create(ProductCategoryCreateDTO dto): Long`
  - `ProductCategoryAppService#update(ProductCategoryUpdateDTO dto): void`
  - `ProductCategoryAppService#remove(String corpid, Long id): void`
  - `ProductCategoryAppService#detail(String corpid, Long id): ProductCategoryVO`
  - `ProductCategoryAppService#list(ProductCategoryListDTO dto): List<ProductCategoryVO>`
  - Equivalent `ProductBrandAppService` and `ProductUnitAppService` method families

- [ ] **Step 1: Write failing admin endpoint tests**

Add these tests to `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`:

```java
@Test
void should_expose_product_category_list_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/category/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}

@Test
void should_expose_product_brand_list_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/brand/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}

@Test
void should_expose_product_unit_list_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/unit/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}
```

- [ ] **Step 2: Run tests to verify the endpoints fail correctly**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: FAIL with `404` for the three `/erp/v1/product/.../list` endpoints.

- [ ] **Step 3: Create the minimal dictionary domain / PO / repository model**

Use this PO shape pattern for each table:

```java
@Data
public class ProductCategoryPO {
    private Long id;
    private String corpid;
    private String categoryCode;
    private String categoryName;
    private Long parentId;
    private Integer categoryLevel;
    private Integer sortNo;
    private Integer enableStatus;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
```

Use the same audit-field pattern for `ProductBrandPO` and `ProductUnitPO`, mapped to their table-specific fields.

- [ ] **Step 4: Create Mapper interfaces and XML CRUD methods**

Use this Mapper interface pattern:

```java
@Mapper
public interface ProductCategoryMapper {
    int insert(ProductCategoryPO po);
    int insertBatch(@Param("list") List<ProductCategoryPO> list);
    int removeById(@Param("corpid") String corpid, @Param("id") Long id, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);
    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids, @Param("modifyId") String modifyId, @Param("updateTime") Long updateTime);
    int update(ProductCategoryPO po);
    ProductCategoryPO findById(@Param("corpid") String corpid, @Param("id") Long id);
    List<ProductCategoryPO> findByCondition(Map<String, Object> condition);
    long count(Map<String, Object> condition);
}
```

Define the shared `<sql id="Base_Condition">` in the XML and let `findByCondition` / `count` reuse it.

- [ ] **Step 5: Create app services and controllers with minimal success-path wiring**

Use this controller shape:

```java
@RestController
@RequestMapping("/erp/v1/product/category")
@RequiredArgsConstructor
public class ProductCategoryAdminController {

    private final ProductCategoryAppService productCategoryAppService;

    @GetMapping("/list")
    public ResultVO<List<ProductCategoryVO>> list(ProductCategoryListDTO dto) {
        return ResultVO.success(productCategoryAppService.list(dto));
    }
}
```

Create equivalent list endpoints for brand and unit, plus POST/PUT/DELETE/detail endpoints in the same controller family.

- [ ] **Step 6: Run the admin tests to verify they pass**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: PASS and all three new product dictionary list endpoints return `{"code":0,...}`.

- [ ] **Step 7: Commit**

```bash
git add xbb-erp-module-product xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java
git commit -m "feat: add product dictionary crud"
```

### Task 4: Implement SPU / SKU persistence and the product aggregate service

**Files:**
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductSpu.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/model/ProductSku.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductSpuRepository.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/domain/repository/ProductSkuRepository.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po/ProductSpuPO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po/ProductSkuPO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductSpuMapper.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper/ProductSkuMapper.java`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSpuMapper.xml`
- Create: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSkuMapper.xml`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductSpuRepositoryImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository/ProductSkuRepositoryImpl.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductCreateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductUpdateDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDeleteDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductDetailDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductAppService.java`

**Interfaces:**
- Consumes: category / brand / unit repository conventions from Task 3
- Produces:
  - `ProductAppService#create(ProductCreateDTO dto): Long`
  - `ProductAppService#update(ProductUpdateDTO dto): void`
  - `ProductAppService#remove(ProductDeleteDTO dto): void`
  - `ProductAppService#detail(ProductDetailDTO dto): ProductVO`

- [ ] **Step 1: Write a failing product aggregate endpoint test**

Add this test to `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`:

```java
@Test
void should_expose_product_detail_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/detail"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: FAIL with `404` for `/erp/v1/product/detail`.

- [ ] **Step 3: Create SPU / SKU domain objects and persistence classes**

Use these minimal field sets:

```java
@Data
public class ProductSpu {
    private Long id;
    private String corpid;
    private String spuCode;
    private String spuName;
    private Long categoryId;
    private Long brandId;
    private String productType;
    private Integer enableSpec;
    private String description;
    private String imageUrl;
    private Integer enableStatus;
}

@Data
public class ProductSku {
    private Long id;
    private String corpid;
    private Long spuId;
    private String skuCode;
    private String skuName;
    private String mnemonicCode;
    private String mainBarcode;
    private String specSignature;
    private String specSnapshot;
    private Integer canPurchase;
    private Integer canSale;
    private Integer canInventory;
    private Integer canProduce;
    private Integer enableStatus;
    private Integer listingStatus;
}
```

Mirror these in `ProductSpuPO` and `ProductSkuPO`, adding audit fields.

- [ ] **Step 4: Implement `ProductAppService` as a thin aggregate orchestrator**

Use this orchestration contract:

```java
public interface ProductAppService {
    Long create(ProductCreateDTO dto);
    void update(ProductUpdateDTO dto);
    void remove(ProductDeleteDTO dto);
    ProductVO detail(ProductDetailDTO dto);
}
```

Implementation rules:

- `create` inserts one `ProductSpuPO`, then one `ProductSkuPO`
- `update` updates both tables by ID and `corpid`
- `remove` logically deletes the `SPU` and its `SKU`
- `detail` loads both records and merges them into one `ProductVO`

- [ ] **Step 5: Add the `/erp/v1/product/detail` success endpoint**

Controller method:

```java
@GetMapping("/detail")
public ResultVO<ProductVO> detail(ProductDetailDTO dto) {
    return ResultVO.success(productAppService.detail(dto));
}
```

- [ ] **Step 6: Run the admin test to verify the endpoint passes**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: PASS and the detail endpoint returns `code=0`.

- [ ] **Step 7: Commit**

```bash
git add xbb-erp-module-product xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java
git commit -m "feat: add product aggregate service"
```

### Task 5: Implement the three product list endpoints (`SPU`, `SKU`, `SPU+SKU`)

**Files:**
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSpuListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSkuListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/dto/ProductSpuSkuListDTO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductSpuListVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductSkuListVO.java`
- Create: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/vo/ProductSpuSkuListVO.java`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/app/service/ProductAppService.java`
- Modify: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSpuMapper.xml`
- Modify: `xbb-erp-module-product/src/main/resources/mapper/product/ProductSkuMapper.xml`
- Modify: `xbb-erp-module-product/src/main/java/xbb/ai/erp/module/product/admin/ProductAdminController.java`
- Modify: `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`

**Interfaces:**
- Consumes:
  - `ProductAppService#detail(ProductDetailDTO dto): ProductVO`
  - `ProductSpuMapper#findByCondition(Map<String, Object> condition): List<ProductSpuPO>`
  - `ProductSkuMapper#findByCondition(Map<String, Object> condition): List<ProductSkuPO>`
- Produces:
  - `ProductAppService#listSpu(ProductSpuListDTO dto): List<ProductSpuListVO>`
  - `ProductAppService#listSku(ProductSkuListDTO dto): List<ProductSkuListVO>`
  - `ProductAppService#listSpuSku(ProductSpuSkuListDTO dto): List<ProductSpuSkuListVO>`

- [ ] **Step 1: Write failing tests for the three list endpoints**

Add these tests to `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`:

```java
@Test
void should_expose_product_spu_list_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/spu/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}

@Test
void should_expose_product_sku_list_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/sku/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}

@Test
void should_expose_product_spu_sku_list_endpoint() throws Exception {
    mockMvc.perform(get("/erp/v1/product/spu-sku/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0));
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: FAIL with `404` for all three new endpoints.

- [ ] **Step 3: Extend `ProductAppService` with the three list methods**

Add these signatures:

```java
List<ProductSpuListVO> listSpu(ProductSpuListDTO dto);
List<ProductSkuListVO> listSku(ProductSkuListDTO dto);
List<ProductSpuSkuListVO> listSpuSku(ProductSpuSkuListDTO dto);
```

- [ ] **Step 4: Add minimal pagination-aware SQL**

Use `offset` and `pageSize` only. Example for the joined list:

```xml
<select id="findSpuSkuList" resultType="xbb.ai.erp.module.product.admin.vo.ProductSpuSkuListVO">
    select
        sku.id as skuId,
        sku.sku_code as skuCode,
        sku.sku_name as skuName,
        sku.main_barcode as mainBarcode,
        spu.id as spuId,
        spu.spu_code as spuCode,
        spu.spu_name as spuName,
        spu.product_type as productType
    from product_sku sku
    inner join product_spu spu on spu.id = sku.spu_id and spu.corpid = sku.corpid and spu.del = 0
    where sku.corpid = #{corpid}
      and sku.del = 0
    limit #{offset}, #{pageSize}
</select>
```

For `SPU` / `SKU` lists, keep the query single-table.

- [ ] **Step 5: Add endpoints to `ProductAdminController`**

```java
@GetMapping("/spu/list")
public ResultVO<List<ProductSpuListVO>> listSpu(ProductSpuListDTO dto) {
    return ResultVO.success(productAppService.listSpu(dto));
}

@GetMapping("/sku/list")
public ResultVO<List<ProductSkuListVO>> listSku(ProductSkuListDTO dto) {
    return ResultVO.success(productAppService.listSku(dto));
}

@GetMapping("/spu-sku/list")
public ResultVO<List<ProductSpuSkuListVO>> listSpuSku(ProductSpuSkuListDTO dto) {
    return ResultVO.success(productAppService.listSpuSku(dto));
}
```

- [ ] **Step 6: Run the tests to verify they pass**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: PASS and all six product-facing endpoints remain green.

- [ ] **Step 7: Commit**

```bash
git add xbb-erp-module-product xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java
git commit -m "feat: add product list endpoints"
```

### Task 6: Final verification and cleanup

**Files:**
- Modify: any files required to fix compile/test failures discovered in verification
- Test: `xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java`

**Interfaces:**
- Consumes: all previous tasks' public endpoints and repository wiring
- Produces: a buildable, testable worktree with product module initialized and wired

- [ ] **Step 1: Run the product-module build**

Run: `mvn -q -pl xbb-erp-module-product -am test`

Expected: PASS.

- [ ] **Step 2: Run the admin integration test suite**

Run: `mvn -q -pl xbb-erp-app-admin -Dtest=AdminApplicationTest test`

Expected: PASS.

- [ ] **Step 3: Run the full repository test suite**

Run: `mvn -q test`

Expected: PASS.

- [ ] **Step 4: Inspect the final diff**

Run: `git status --short && git diff --stat`

Expected: only the intended product-module, SQL, doc, and test changes appear.

- [ ] **Step 5: Commit**

```bash
git add pom.xml xbb-erp-app-admin/pom.xml xbb-erp-module-product docs/sql/2026-07-22-init-product-module.sql docs/base/项目顶部和底部module导航.md xbb-erp-app-admin/src/test/java/xbb/ai/erp/app/admin/AdminApplicationTest.java
git commit -m "feat: initialize product module"
```

## Self-Review

- Spec coverage: module scaffold, five-table DDL, dictionary CRUD, `SPU/SKU` aggregate, three list endpoints, and navigation update are all mapped to Tasks 1-6.
- Placeholder scan: no `TODO` / `TBD` / “implement later” text remains.
- Type consistency: `userId` is always string in DTOs; all endpoint DTOs extend `BaseDTO`; list endpoints are exactly `/erp/v1/product/spu/list`, `/erp/v1/product/sku/list`, `/erp/v1/product/spu-sku/list`.

## Execution Handoff

Plan complete and saved to `docs/superpowers/plans/2026-07-22-product-module-init.md`.

Auto-selected execution approach: **Inline Execution** — execute tasks in this session using `superpowers:executing-plans`, because the user already authorized automatic continuation through the remaining workflow.