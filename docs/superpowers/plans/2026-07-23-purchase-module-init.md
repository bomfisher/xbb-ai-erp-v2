# Purchase Module Initialization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 新增 `xbb-erp-module-purchase`，基于 `xbb-erp-codegen` 初始化采购首期 6 张核心表的 CRUD 骨架、SQL 与导航文档，并完成模块级验证。

**Architecture:** 先校正当前工作区基线到包含 `xbb-erp-codegen`、`xbb-erp-module-common`、`xbb-erp-module-product`、`xbb-erp-module-supplier` 的特性分支内容，再按 6 份 YAML 独立驱动生成，最后对生成结果做最小必要补齐。控制层、应用层、领域层、持久层沿用 `supplier`/`product` 模块现有分层；查询、逻辑删除、批量插入规则统一在 Mapper XML 与 Repository 侧落地。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、MyBatis-Plus 3.5.7、Maven、JUnit 5、仓库内 `xbb-erp-codegen`

## Global Constraints

- 对话与输出保持中文语境。
- 当前目标模块固定为 `xbb-erp-module-purchase`，`moduleCode=purchase`，`packageBase=xbb.ai.erp.module.purchase`。
- 采购首期只覆盖 6 张表：`purchase_pending_task`、`purchase_request`、`purchase_request_item`、`purchase_order`、`purchase_order_item`、`purchase_source_relation`。
- 供应商主数据复用 `xbb-erp-module-supplier`，采购模块不复制 `vendor` 及其子表。
- 持久层代码优先使用 `xbb-erp-codegen` 生成，不手工逐个创建 Mapper / RepositoryImpl / Mapper XML。
- `pathStrategy` 使用 `ddd-mybatis-plus`，真实落位以 `xbb-erp-codegen/src/main/resources/presets/ddd-mybatis-plus.yaml` 为准，即 `infrastructure/persistence/po`，不是 `entity`。
- 数据库直连对象当前生成器后缀为 `PO`，实现必须遵循现有分支中的真实 codegen 产物与目录结构，不擅自全面改成其他后缀。
- 直连数据库字段禁止使用布尔值，`tinyint` 语义字段统一用 `Integer`。
- 非脚本接口入参 DTO 必须继承 `BaseDTO`；`userId` 为字符串语义，但当前基类行为需遵循现有项目实现。
- `insertBatch` 必须是 SQL 批量插入，禁止循环逐条入库。
- `removeById`、`removeBatchByIds` 必须是逻辑删除。
- `findByCondition` 与 `count` 必须共用同一个筛选条件片段。
- 所有有 `corpid` 的表，查询与统计都必须传 `corpid`。
- `findByCondition` 必须支持 `offset`、`pageSize`、`groupByStr`、`orderByStr`。
- 生成建表 SQL 到 `docs/sql/2026-07-23-init-purchase-module.sql`。
- 回写 `docs/base/项目顶部和底部module导航.md`。
- 至少验证 `xbb-erp-codegen` 与 `xbb-erp-module-purchase`；若出现无关失败，只记录不顺手修。

---

### Task 1: 校正工作区基线并接入采购模块到父工程

**Files:**
- Modify: `pom.xml`
- Create: `xbb-erp-module-purchase/pom.xml`
- Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/.gitkeep`
- Create: `xbb-erp-module-purchase/src/main/resources/mapper/purchase/.gitkeep`
- Test: `mvn -pl xbb-erp-module-purchase -am -DskipTests compile`

**Interfaces:**
- Consumes: 父工程现有模块列表、`xbb-erp-module-product/pom.xml`、`xbb-erp-module-supplier/pom.xml`
- Produces: `xbb-erp-module-purchase` Maven 模块，可被后续 YAML 生成目标路径识别

- [ ] **Step 1: 先确认当前 worktree 的 HEAD 与目标特性分支差异**

Run: `git log --oneline --decorate -2 && git ls-tree --name-only HEAD && git ls-tree --name-only feature/v2-init-domain-demo`
Expected: 看到当前 `worktree-purchase-module-init` 缺少 `xbb-erp-codegen`、`xbb-erp-module-common`、`xbb-erp-module-product`、`xbb-erp-module-supplier`，确认需要先对齐到 `feature/v2-init-domain-demo` 内容后再实施。

- [ ] **Step 2: 将当前 worktree 基线对齐到 `feature/v2-init-domain-demo`**

Run: `git merge --ff-only feature/v2-init-domain-demo`
Expected: fast-forward 成功，工作区顶层出现 `xbb-erp-codegen`、`xbb-erp-module-common`、`xbb-erp-module-product`、`xbb-erp-module-supplier`。

- [ ] **Step 3: 写一个会失败的基线检查测试命令**

Run: `test -f xbb-erp-codegen/pom.xml && test -f xbb-erp-module-supplier/pom.xml && test -f xbb-erp-module-product/pom.xml`
Expected before Step 2: FAIL；Expected after Step 2: PASS。

- [ ] **Step 4: 在父工程 `pom.xml` 中新增采购模块声明**

```xml
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
    <module>xbb-erp-module-common</module>
    <module>xbb-erp-module-customer</module>
    <module>xbb-erp-module-product</module>
    <module>xbb-erp-module-supplier</module>
    <module>xbb-erp-module-purchase</module>
    <module>xbb-erp-app-admin</module>
    <module>xbb-erp-app-mobile</module>
    <module>xbb-erp-app-job</module>
</modules>
```

- [ ] **Step 5: 新建采购模块 `pom.xml`，依赖对齐现有业务模块模式**

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

    <artifactId>xbb-erp-module-purchase</artifactId>

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
            <artifactId>xbb-erp-base-idgen</artifactId>
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

- [ ] **Step 6: 仅创建模块最小 package 结构，不预写业务文件**

Run: `mkdir -p xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase xbb-erp-module-purchase/src/main/resources/mapper/purchase`
Expected: 仅有空目录或 `.gitkeep`，无手工业务类。

- [ ] **Step 7: 运行模块编译确认父工程能识别新模块**

Run: `mvn -pl xbb-erp-module-purchase -am -DskipTests compile`
Expected: 新模块被 Maven 识别；若因后续未生成代码导致编译失败，失败点只应集中在采购模块空实现。

- [ ] **Step 8: Commit**

```bash
git add pom.xml xbb-erp-module-purchase/pom.xml xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/.gitkeep xbb-erp-module-purchase/src/main/resources/mapper/purchase/.gitkeep
git commit -m "feat: bootstrap purchase module"
```

### Task 2: 为 6 张核心表补齐 codegen YAML 与生成器验证

**Files:**
- Create: `xbb-erp-codegen/src/main/resources/examples/purchase/purchase-pending-task.yaml`
- Create: `xbb-erp-codegen/src/main/resources/examples/purchase/purchase-request.yaml`
- Create: `xbb-erp-codegen/src/main/resources/examples/purchase/purchase-request-item.yaml`
- Create: `xbb-erp-codegen/src/main/resources/examples/purchase/purchase-order.yaml`
- Create: `xbb-erp-codegen/src/main/resources/examples/purchase/purchase-order-item.yaml`
- Create: `xbb-erp-codegen/src/main/resources/examples/purchase/purchase-source-relation.yaml`
- Modify: `xbb-erp-codegen/src/test/java/xbb/ai/erp/codegen/CodeGeneratorTest.java`
- Test: `mvn -pl xbb-erp-codegen -Dtest=CodeGeneratorTest test`

**Interfaces:**
- Consumes: `xbb-erp-codegen/src/main/resources/examples/supplier-vendor.yaml`, `xbb-erp-codegen/src/main/resources/presets/ddd-mybatis-plus.yaml`, 采购设计文档中的 6 张表字段
- Produces: 6 份 purchase YAML，可被 `CodegenCli dry-run/generate` 正确识别

- [ ] **Step 1: 先写一个失败的生成器测试，锁定 purchase YAML 的目标路径**

```java
@Test
void should_resolve_purchase_paths_without_hard_coding_supplier_directory() throws Exception {
    ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
    new SpecValidator().validate(moduleSpec);
    PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
    Map<String, String> pathMap = new CodeGenerator().dryRun(moduleSpec, pathStrategySpec);
    assertEquals("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java", pathMap.get("ADMIN_CONTROLLER"));
    assertEquals("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseRequestAdminAppService.java", pathMap.get("APP_SERVICE"));
    assertEquals("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseRequest.java", pathMap.get("DOMAIN_MODEL"));
    assertEquals("xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml", pathMap.get("MAPPER_XML"));
}
```

- [ ] **Step 2: 运行测试，确认因 YAML 不存在而失败**

Run: `mvn -pl xbb-erp-codegen -Dtest=CodeGeneratorTest#should_resolve_purchase_paths_without_hard_coding_supplier_directory test`
Expected: FAIL，报 `purchase-request.yaml` 不存在或校验失败。

- [ ] **Step 3: 按 supplier/customer 示例分别编写 6 份 YAML**

```yaml
moduleCode: purchase
moduleName: 采购申请
packageBase: xbb.ai.erp.module.purchase
pathStrategy: ddd-mybatis-plus
aggregate:
  aggregateName: PurchaseRequest
  tableName: purchase_request
  fields:
    - name: id
      column: id
      javaType: Long
      primaryKey: true
      queryable: true
      visibleInList: true
      visibleInDetail: true
    - name: corpid
      column: corpid
      javaType: String
      queryable: true
      visibleInList: false
      visibleInDetail: false
    - name: purchaseOrgId
      column: purchase_org_id
      javaType: Long
      queryable: true
      visibleInList: true
      visibleInDetail: true
    - name: requestNo
      column: request_no
      javaType: String
      queryable: true
      visibleInList: true
      visibleInDetail: true
    - name: bizStatus
      column: biz_status
      javaType: String
      queryable: true
      visibleInList: true
      visibleInDetail: true
    - name: approvalStatus
      column: approval_status
      javaType: String
      queryable: true
      visibleInList: true
      visibleInDetail: true
    - name: grossAmount
      column: gross_amount
      javaType: java.math.BigDecimal
      queryable: false
      visibleInList: true
      visibleInDetail: true
    - name: netAmount
      column: net_amount
      javaType: java.math.BigDecimal
      queryable: false
      visibleInList: true
      visibleInDetail: true
    - name: taxAmount
      column: tax_amount
      javaType: java.math.BigDecimal
      queryable: false
      visibleInList: true
      visibleInDetail: true
    - name: version
      column: version
      javaType: Integer
      queryable: false
      visibleInList: false
      visibleInDetail: true
    - name: deleted
      column: del
      javaType: Integer
      queryable: false
      visibleInList: false
      visibleInDetail: false
    - name: addTime
      column: add_time
      javaType: Long
      queryable: false
      visibleInList: true
      visibleInDetail: true
    - name: updateTime
      column: update_time
      javaType: Long
      queryable: false
      visibleInList: true
      visibleInDetail: true
generate:
  admin: true
  application: true
  domain: true
  persistence: true
  xml: true
```

- [ ] **Step 4: 补齐其他 5 份 YAML 的字段并保持以下约束**

```text
purchase_pending_task: task_no/source_type/source_doc_id/source_line_id/sku_id/need_qty/occupied_qty/generated_request_qty/generated_order_qty/closed_qty/suggested_vendor_id/suggested_delivery_date/priority_level/task_status/sales_linked_flag/version/del
purchase_request_item: request_id/line_no/sku_id/purchase_unit_id/request_qty/reserved_qty/executed_qty/closed_qty/suggested_vendor_id/suggested_delivery_date/version/del
purchase_order: order_no/vendor_id/vendor_name_snapshot/purchaser_id/warehouse_id/settlement_method_id/currency_code/delivery_date/source_type/source_no/sales_linked_flag/biz_status/approval_status/execution_status/receipt_status/inbound_status/payable_status/invoice_status/payment_status/gross_amount/net_amount/tax_amount/inbounded_qty_summary/uninbounded_qty_summary/closed_qty_summary/payable_amount_summary/paid_amount_summary/invoiced_amount_summary/period_locked_flag/version/del
purchase_order_item: order_id/line_no/sku_id/purchase_unit_id/warehouse_id/order_qty/received_qty/inbounded_qty/closed_qty/returned_qty/gross_price/net_price/tax_rate/tax_amount/gross_amount/net_amount/payable_amount/paid_amount/invoiced_amount/is_gift/version/del
purchase_source_relation: source_doc_type/source_doc_id/source_line_id/target_doc_type/target_doc_id/target_line_id/source_qty/reserved_qty/executed_qty/closed_qty/reversed_qty/relation_status/version/del
```

- [ ] **Step 5: 运行单测确认 dry-run 路径通过**

Run: `mvn -pl xbb-erp-codegen -Dtest=CodeGeneratorTest#should_resolve_purchase_paths_without_hard_coding_supplier_directory test`
Expected: PASS。

- [ ] **Step 6: 再增加一个生成测试，确认 purchase_request 至少能生成关键文件**

```java
@Test
void should_generate_purchase_request_core_files() throws Exception {
    ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
    PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
    Path outputRoot = Files.createTempDirectory("xbb-codegen-purchase-");
    new CodeGenerator().generate(outputRoot, moduleSpec, pathStrategySpec);
    assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java")));
    assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseRequestAdminAppService.java")));
    assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseRequest.java")));
    assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/po/PurchaseRequestPO.java")));
    assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml")));
}
```

- [ ] **Step 7: 跑完整 codegen 单测**

Run: `mvn -pl xbb-erp-codegen -Dtest=CodeGeneratorTest test`
Expected: PASS。

- [ ] **Step 8: Commit**

```bash
git add xbb-erp-codegen/src/main/resources/examples/purchase/*.yaml xbb-erp-codegen/src/test/java/xbb/ai/erp/codegen/CodeGeneratorTest.java
git commit -m "feat: add purchase codegen specs"
```

### Task 3: 逐份 dry-run/generate 生成采购模块骨架

**Files:**
- Modify/Create: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/**`
- Modify/Create: `xbb-erp-module-purchase/src/main/resources/mapper/purchase/*.xml`
- Test: `mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run <spec>'`

**Interfaces:**
- Consumes: Task 2 的 6 份 YAML、`CodegenCli`
- Produces: 6 个聚合的 controller / dto / vo / app service / domain / po / mapper / repository / xml 初始骨架

- [ ] **Step 1: 先对 `purchase-request` 做 dry-run 验证路径**

Run: `mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run src/main/resources/examples/purchase/purchase-request.yaml'`
Expected: 输出中出现 `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java` 等路径。

- [ ] **Step 2: 对其余 5 份 YAML 分别执行 dry-run**

Run:
```bash
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run src/main/resources/examples/purchase/purchase-pending-task.yaml'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run src/main/resources/examples/purchase/purchase-request-item.yaml'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run src/main/resources/examples/purchase/purchase-order.yaml'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run src/main/resources/examples/purchase/purchase-order-item.yaml'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run src/main/resources/examples/purchase/purchase-source-relation.yaml'
```
Expected: 每份都落到 `xbb-erp-module-purchase` 对应目录，无误入其他模块目录。

- [ ] **Step 3: 如果 dry-run 输出路径不符，先改 YAML 再重跑，直到全部路径正确**

Run: `rg -n "xbb-erp-module-purchase|mapper/purchase" xbb-erp-codegen/src/main/resources/examples/purchase/*.yaml`
Expected: 所有 purchase YAML 都指向 `purchase` 模块与 `xbb.ai.erp.module.purchase` 包。

- [ ] **Step 4: 逐份执行 generate，把骨架生成到当前工作树**

Run:
```bash
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate src/main/resources/examples/purchase/purchase-pending-task.yaml .'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate src/main/resources/examples/purchase/purchase-request.yaml .'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate src/main/resources/examples/purchase/purchase-request-item.yaml .'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate src/main/resources/examples/purchase/purchase-order.yaml .'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate src/main/resources/examples/purchase/purchase-order-item.yaml .'
mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate src/main/resources/examples/purchase/purchase-source-relation.yaml .'
```
Expected: `xbb-erp-module-purchase` 下生成完整骨架文件。

- [ ] **Step 5: 用文件列表确认 6 个聚合的核心文件都已生成**

Run: `rg --files xbb-erp-module-purchase | rg 'Purchase(PendingTask|Request|RequestItem|Order|OrderItem|SourceRelation)'`
Expected: controller、dto、vo、service、domain、po、mapper、repository、xml 均可见。

- [ ] **Step 6: Commit**

```bash
git add xbb-erp-module-purchase
git commit -m "feat: generate purchase module skeleton"
```

### Task 4: 补齐生成器未覆盖的 CRUD 细节与 SQL 规则

**Files:**
- Modify: `xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/**`
- Modify: `xbb-erp-module-purchase/src/main/resources/mapper/purchase/*.xml`
- Test: `mvn -pl xbb-erp-module-purchase -DskipTests compile`

**Interfaces:**
- Consumes: Task 3 生成出的骨架
- Produces: 满足 `insertBatch`、逻辑删除、共用条件片段、分页排序规则的采购模块持久层与应用层实现

- [ ] **Step 1: 先写一个会失败的编译检查，确认当前生成骨架还缺补齐项**

Run: `mvn -pl xbb-erp-module-purchase -DskipTests compile`
Expected: FAIL 或存在实现不完整点，为后续修正提供基线。

- [ ] **Step 2: 统一修正 DTO 与控制层路径，保持与 `supplier` 风格一致**

```java
@RestController
@RequestMapping("/erp/v1/purchase/request")
@RequiredArgsConstructor
public class PurchaseRequestAdminController {

    private final PurchaseRequestAdminAppService purchaseRequestAdminAppService;

    @PostMapping("/list")
    public ListBaseVO<PurchaseRequestListItemVO> list(@RequestBody PurchaseRequestListDTO dto) {
        return purchaseRequestAdminAppService.list(dto);
    }

    @PostMapping("/save")
    public Long save(@RequestBody PurchaseRequestSaveDTO dto) {
        return purchaseRequestAdminAppService.save(dto);
    }
}
```

- [ ] **Step 3: 在所有 `*ListDTO` 中确认继承 `BaseDTO` 且包含分页/排序字段**

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderListDTO extends BaseDTO {
    private Long id;
    private String corpid;
    private Long vendorId;
    private String orderNo;
    private String bizStatus;
    private String approvalStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
```

- [ ] **Step 4: 在 RepositoryImpl 中去掉任何逐条批量写入实现，统一依赖 Mapper `insertBatch`**

```java
@Override
public void insertBatch(List<PurchaseRequest> purchaseRequestList) {
    purchaseRequestMapper.insertBatch(
        purchaseRequestList.stream()
            .map(PurchaseRequestConvertor::toPO)
            .toList()
    );
}
```

- [ ] **Step 5: 在每个 Mapper XML 中补齐统一条件片段**

```xml
<sql id="BaseCondition">
    <if test="conditionMap.corpid != null and conditionMap.corpid != ''">
        and corpid = #{conditionMap.corpid}
    </if>
    <if test="conditionMap.id != null">
        and id = #{conditionMap.id}
    </if>
    <if test="conditionMap.orderNo != null and conditionMap.orderNo != ''">
        and order_no = #{conditionMap.orderNo}
    </if>
</sql>

<select id="count" resultType="java.lang.Long">
    select count(1)
    from purchase_order
    where del = 0
    <include refid="BaseCondition"/>
</select>

<select id="findByCondition" resultMap="BaseResultMap">
    select *
    from purchase_order
    where del = 0
    <include refid="BaseCondition"/>
    <if test="conditionMap.groupByStr != null and conditionMap.groupByStr != ''">
        group by ${conditionMap.groupByStr}
    </if>
    <if test="conditionMap.orderByStr != null and conditionMap.orderByStr != ''">
        order by ${conditionMap.orderByStr}
    </if>
    <if test="conditionMap.pageSize != null and conditionMap.offset != null">
        limit #{conditionMap.offset}, #{conditionMap.pageSize}
    </if>
    <if test="conditionMap.pageSize != null and conditionMap.offset == null">
        limit #{conditionMap.pageSize}
    </if>
</select>
```

- [ ] **Step 6: 对 `groupByStr` / `orderByStr` 的原样拼接做白名单约束实现**

```java
private static final Set<String> ORDER_BY_COLUMNS = Set.of("add_time", "update_time", "order_no", "request_no", "task_no");

private static String normalizeOrderBy(String rawOrderBy) {
    if (rawOrderBy == null || rawOrderBy.isBlank()) {
        return null;
    }
    return Arrays.stream(rawOrderBy.split(","))
        .map(String::trim)
        .filter(item -> ORDER_BY_COLUMNS.contains(item.replace(" desc", "").replace(" asc", "")))
        .collect(Collectors.joining(", "));
}
```

- [ ] **Step 7: 在应用服务 `list()` 方法中只传规范化后的 `groupByStr` / `orderByStr`，避免 SQL 注入**

```java
conditionMap.put("groupByStr", PurchaseQueryOrderNormalizer.normalizeGroupBy(dto.getGroupByStr()));
conditionMap.put("orderByStr", PurchaseQueryOrderNormalizer.normalizeOrderBy(dto.getOrderByStr()));
```

- [ ] **Step 8: 为 6 个 XML 都补齐逻辑删除 SQL**

```xml
<update id="removeById">
    update purchase_request
    set del = 1,
        update_time = unix_timestamp() * 1000,
        modify_id = #{corpid}
    where corpid = #{corpid}
      and id = #{id}
      and del = 0
</update>

<update id="removeBatchByIds">
    update purchase_request
    set del = 1,
        update_time = unix_timestamp() * 1000,
        modify_id = #{corpid}
    where corpid = #{corpid}
      and del = 0
      and id in
    <foreach collection="ids" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</update>
```

- [ ] **Step 9: 为 6 个 XML 都补齐批量插入 SQL**

```xml
<insert id="insertBatch">
    insert into purchase_request (
        id, corpid, purchase_org_id, request_no, biz_status, approval_status,
        gross_amount, net_amount, tax_amount, version, del, add_time, update_time, creator_id, modify_id
    ) values
    <foreach collection="list" item="item" separator=",">
        (
            #{item.id}, #{item.corpid}, #{item.purchaseOrgId}, #{item.requestNo}, #{item.bizStatus}, #{item.approvalStatus},
            #{item.grossAmount}, #{item.netAmount}, #{item.taxAmount}, #{item.version}, #{item.deleted}, #{item.addTime}, #{item.updateTime}, #{item.creatorId}, #{item.modifyId}
        )
    </foreach>
</insert>
```

- [ ] **Step 10: 重新编译采购模块，确认骨架补齐后通过**

Run: `mvn -pl xbb-erp-module-purchase -DskipTests compile`
Expected: PASS。

- [ ] **Step 11: Commit**

```bash
git add xbb-erp-module-purchase
git commit -m "feat: complete purchase persistence rules"
```

### Task 5: 产出建表 SQL 与模块导航文档

**Files:**
- Create: `docs/sql/2026-07-23-init-purchase-module.sql`
- Modify: `docs/base/项目顶部和底部module导航.md`
- Test: `rg -n "xbb-erp-module-purchase|purchase_order|purchase_request" docs/sql/2026-07-23-init-purchase-module.sql docs/base/项目顶部和底部module导航.md`

**Interfaces:**
- Consumes: 采购数据库设计文档、Task 2 的 YAML 字段定义
- Produces: 6 张表建表 SQL 与导航说明

- [ ] **Step 1: 先写 SQL 文件草稿，只覆盖 6 张核心表与索引**

```sql
create table if not exists purchase_request (
    id bigint not null,
    corpid varchar(50) not null,
    purchase_org_id bigint not null,
    request_no varchar(64) not null,
    request_dept_id bigint null,
    applicant_id varchar(50) null,
    source_type varchar(32) not null,
    source_no varchar(64) null,
    suggested_vendor_id bigint null,
    suggested_delivery_date bigint null,
    biz_status varchar(32) not null,
    approval_status varchar(32) not null,
    gross_amount decimal(18,2) not null,
    net_amount decimal(18,2) not null,
    tax_amount decimal(18,2) not null,
    version int not null,
    remark varchar(500) null,
    del tinyint not null default 0,
    add_time bigint not null,
    update_time bigint not null,
    creator_id varchar(50) not null,
    modify_id varchar(50) not null,
    primary key (id),
    unique key uk_request_no (corpid, request_no, del),
    key idx_request_org_status (corpid, purchase_org_id, biz_status, approval_status, del),
    key idx_request_source (corpid, source_type, source_no, del),
    key idx_request_time (corpid, add_time, del)
);
```

- [ ] **Step 2: 按同样规则补齐其他 5 张表与索引**

Run: `rg -n "## 5\.|## 6\.|## 7\.|## 8\.|## 9\.|## 18\." "/Users/bomfish/Documents/aicoding/进销存2/技术文档/相对可用的文档/14B-采购管理模块数据库表设计文档.md"`
Expected: 从设计文档对应章节逐表转成 SQL。

- [ ] **Step 3: 在导航文档中新增采购模块说明**

```md
#### xbb-erp-module-purchase
- 功能定位：采购管理首期核心链路模块
- 责任范围：待采购任务、采购申请、采购订单、来源关系的领域模型、仓储接口、持久层映射与管理端 CRUD 骨架
- 当前表范围：`purchase_pending_task`、`purchase_request`、`purchase_request_item`、`purchase_order`、`purchase_order_item`、`purchase_source_relation`
- 不负责什么：收料、入库、退料、变更、回写、幂等等第二期能力
```

- [ ] **Step 4: 运行 grep 确认 SQL 与导航都已包含 purchase 模块信息**

Run: `rg -n "xbb-erp-module-purchase|purchase_pending_task|purchase_order|purchase_source_relation" docs/sql/2026-07-23-init-purchase-module.sql docs/base/项目顶部和底部module导航.md`
Expected: 能命中对应行。

- [ ] **Step 5: Commit**

```bash
git add docs/sql/2026-07-23-init-purchase-module.sql docs/base/项目顶部和底部module导航.md
git commit -m "docs: add purchase module schema and navigation"
```

### Task 6: 完整验证并记录无关失败项

**Files:**
- Modify: `docs/superpowers/specs/2026-07-23-purchase-module-design.md`（仅当发现规格需同步修正）
- Test: `mvn -pl xbb-erp-codegen -Dtest=CodeGeneratorTest test`
- Test: `mvn -pl xbb-erp-module-purchase -am test`
- Test: `mvn -pl xbb-erp-app-admin -am -DskipTests compile`

**Interfaces:**
- Consumes: 前 5 个任务的全部产物
- Produces: 可复现的验证结论与无关失败记录

- [ ] **Step 1: 运行 codegen 单测，确认 purchase YAML 与生成能力稳定**

Run: `mvn -pl xbb-erp-codegen -Dtest=CodeGeneratorTest test`
Expected: PASS。

- [ ] **Step 2: 运行采购模块测试/编译验证**

Run: `mvn -pl xbb-erp-module-purchase -am test`
Expected: PASS；若当前模块还没有测试类，则至少 compile/test 生命周期通过。

- [ ] **Step 3: 运行管理端编译，确认新增模块不会破坏装配链**

Run: `mvn -pl xbb-erp-app-admin -am -DskipTests compile`
Expected: PASS，或仅出现与本任务无关的既有失败。

- [ ] **Step 4: 如果出现无关失败，记录失败模块、命令、首个报错，不顺手修**

```text
命令: mvn -pl xbb-erp-app-admin -am -DskipTests compile
失败模块: <module>
首个报错: <first error line>
判断: 与 purchase 模块初始化无关，仅记录
```

- [ ] **Step 5: 若验证暴露设计文档与真实 codegen 行为不一致，回写规格文档**

```md
- 当前实现遵循 `infrastructure/persistence/po` 与 `*PO` 命名，这是现有 `ddd-mybatis-plus` 预设的真实输出。
```

- [ ] **Step 6: Commit**

```bash
git add docs/superpowers/specs/2026-07-23-purchase-module-design.md
git commit -m "test: verify purchase module initialization"
```
