# xbb-erp-scene-meta Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 抽出 `xbb-erp-scene-meta` 作为字段场景协议层，并把客户档案字段能力迁移成第一个业务对象样板。

**Architecture:** 新增独立 Maven 模块承接场景枚举、字段元数据、provider、rule 和 assembler 的公共协议；业务模块只保留各自字段目录和具体 provider 实现。第一版先迁客户档案，应用层通过公共协议获取列表、新建、编辑、详情四个场景的字段，避免把客户子表语义抬到公共层。

**Tech Stack:** JDK 21、Spring Boot 3.3.2、Maven、Lombok、JUnit 5、现有 `FieldEntity` / `FieldTypeEnum`

## Global Constraints

- 对话永远在中文语境下。
- 运行时：`JDK 21`
- 应用框架：`Spring Boot 3.3.2`
- 构建工具：`Maven`
- Web：`Spring Web`
- ORM：`MyBatis-Plus`
- 数据库：本地`MySQL 5.6` 其余环境`MySQL 8.0`
- 数据库迁移：`Flyway`
- 缓存：`Redis 7`
- 日志：`Logback`
- 测试：`JUnit 5 + Testcontainers`
- 直接对接数据库的对象实体需要添加PO后缀，并且对象内字段不允许使用布尔值对接，改用Integer;
- 枚举类需要Enum结尾
- 系统内pojo尾缀规范：对接前端入参DTO、对接接口出参VO。其余中转参数的对象Pojo
- 所有接口接口DTO作为参数，而不是散列的参数。非脚本接口，入参DTO都需要继承BaseDTO
- userId 员工Id是字符串id
- getter setter用Lombok管理
- 第一版不做动态字段平台、不做数据库配置化字段中心、不做全局字段注册中心。
- 第一版不在公共层抽象客户联系人、客户地址、采购明细行等业务概念，也不预设主表/子表结构。
- 第一版只迁移客户档案这一类业务对象作为样板，不一次性迁移多个业务模块。
- `scene-meta` 统一的是能力模型，业务模块负责业务语义。

---

### Task 1: 新建 scene-meta 模块骨架

**Files:**
- Modify: `pom.xml`
- Create: `xbb-erp-scene-meta/pom.xml`
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/.gitkeep`
- Create: `xbb-erp-scene-meta/src/test/java/xbb/ai/erp/scene/meta/.gitkeep`

**Interfaces:**
- Consumes: 父工程现有模块声明、`xbb-erp-module-customer/pom.xml`
- Produces: 新模块可被父工程识别，可供后续公共协议类落位

- [ ] **Step 1: 先写失败测试，确认父工程当前还没有 scene-meta 模块**

```bash
mvn -pl xbb-erp-scene-meta -am -DskipTests compile
```

Expected: FAIL，提示模块不存在。

- [ ] **Step 2: 在父工程 `pom.xml` 中加入 scene-meta 模块**

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

- [ ] **Step 3: 写 `xbb-erp-scene-meta/pom.xml`**

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

    <artifactId>xbb-erp-scene-meta</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.bomfish</groupId>
            <artifactId>xbb-erp-base-common</artifactId>
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

- [ ] **Step 4: 创建最小目录占位**

Run: `mkdir -p xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta xbb-erp-scene-meta/src/test/java/xbb/ai/erp/scene/meta`

Expected: 模块目录可被 Maven 识别。

- [ ] **Step 5: 编译验证**

Run: `mvn -pl xbb-erp-scene-meta -am -DskipTests compile`

Expected: PASS。

- [ ] **Step 6: Commit**

```bash
git add pom.xml xbb-erp-scene-meta/pom.xml xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/.gitkeep xbb-erp-scene-meta/src/test/java/xbb/ai/erp/scene/meta/.gitkeep
git commit -m "feat: bootstrap scene meta module"
```

### Task 2: 落地 scene-meta 公共协议类

**Files:**
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/SceneTypeEnum.java`
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/SceneFieldMeta.java`
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/SceneFieldRule.java`
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/SceneFieldProvider.java`
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/SceneFieldAssembler.java`
- Create: `xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/AbstractSceneFieldProvider.java`
- Create: `xbb-erp-scene-meta/src/test/java/xbb/ai/erp/scene/meta/SceneFieldAssemblerTest.java`

**Interfaces:**
- Consumes: `FieldEntity`, `FieldTypeEnum`, `List` semantics from `xbb-erp-base-common`
- Produces: 公共协议和装配器，供客户模块改造后直接依赖

- [ ] **Step 1: 先写失败测试，锁定元数据到 `FieldEntity` 的装配行为**

```java
@Test
void should_build_field_entity_from_scene_field_meta() {
    SceneFieldMeta meta = new SceneFieldMeta("main.customerName", "客户名称", 1, 1, 1);

    FieldEntity entity = SceneFieldAssembler.build(meta);

    assertEquals("main.customerName", entity.getAttr());
    assertEquals("客户名称", entity.getAttrName());
    assertEquals("1", entity.getFieldType());
    assertEquals(1, entity.getRequired());
    assertEquals(1, entity.getEditable());
}
```

- [ ] **Step 2: 实现公共协议类**

```java
public enum SceneTypeEnum {
    LIST,
    CREATE,
    UPDATE,
    DETAIL
}
```

```java
import lombok.Getter;

@Getter
public class SceneFieldMeta {
    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final Integer required;
    private final Integer editable;

    public SceneFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.required = required;
        this.editable = editable;
    }
}
```

```java
public interface SceneFieldRule {
    List<SceneFieldMeta> apply(List<SceneFieldMeta> fields);
}
```

```java
public interface SceneFieldProvider {
    String sceneKey();
    List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);
}
```

```java
public final class SceneFieldAssembler {
    private SceneFieldAssembler() {
    }

    public static List<FieldEntity> buildHeadList(List<SceneFieldMeta> definitions) {
        return definitions.stream().map(SceneFieldAssembler::build).toList();
    }

    public static FieldEntity build(SceneFieldMeta definition) {
        FieldEntity entity = new FieldEntity();
        entity.setAttr(definition.getAttr());
        entity.setAttrName(definition.getAttrName());
        entity.setFieldType(String.valueOf(definition.getFieldType()));
        entity.setRequired(definition.getRequired());
        entity.setEditable(definition.getEditable());
        return entity;
    }
}
```

```java
public abstract class AbstractSceneFieldProvider implements SceneFieldProvider {

    private final List<SceneFieldRule> rules;

    protected AbstractSceneFieldProvider(List<SceneFieldRule> rules) {
        this.rules = rules;
    }

    @Override
    public final List<SceneFieldMeta> getFields(SceneTypeEnum sceneType) {
        List<SceneFieldMeta> fields = new ArrayList<>(buildFields(sceneType));
        for (SceneFieldRule rule : rules) {
            fields = rule.apply(fields);
        }
        return fields;
    }

    protected abstract List<SceneFieldMeta> buildFields(SceneTypeEnum sceneType);
}
```

- [ ] **Step 3: 运行测试确认通过**

Run: `mvn -pl xbb-erp-scene-meta -Dtest=SceneFieldAssemblerTest test`

Expected: PASS。

- [ ] **Step 4: Commit**

```bash
git add xbb-erp-scene-meta/src/main/java/xbb/ai/erp/scene/meta/*.java xbb-erp-scene-meta/src/test/java/xbb/ai/erp/scene/meta/SceneFieldAssemblerTest.java
git commit -m "feat: add scene meta protocol"
```

### Task 3: 让客户模块依赖 scene-meta 并改造字段装配

**Files:**
- Modify: `xbb-erp-module-customer/pom.xml`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerFieldAssembler.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/field/CustomerFieldMeta.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldAssemblerTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldFactoryTest.java`

**Interfaces:**
- Consumes: `xbb-erp-scene-meta` 的 `SceneFieldMeta`、`SceneFieldAssembler`
- Produces: 客户模块可基于公共协议继续组装字段，但仍保留客户独有字段目录

- [ ] **Step 1: 先写失败测试，确认客户模块仍能把字段转成 `FieldEntity`**

```java
@Test
void should_build_customer_head_list_from_scene_meta() {
    List<SceneFieldMeta> fields = List.of(
        new SceneFieldMeta("main.customerName", "客户名称", 1, 1, 1),
        new SceneFieldMeta("contacts.contactName", "联系人姓名", 1, 0, 1)
    );

    List<FieldEntity> headList = SceneFieldAssembler.buildHeadList(fields);

    assertTrue(headList.stream().anyMatch(field -> "main.customerName".equals(field.getAttr())));
    assertTrue(headList.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())));
}
```

- [ ] **Step 2: 把客户模块的 assembler 切到 scene-meta**

```java
public final class CustomerFieldAssembler {

    private CustomerFieldAssembler() {
    }

    public static List<FieldEntity> buildHeadList(List<SceneFieldMeta> definitions) {
        return SceneFieldAssembler.buildHeadList(definitions);
    }
}
```

- [ ] **Step 3: 把客户模块字段元数据迁到公共类型**

```java
public class CustomerFieldMeta extends SceneFieldMeta {

    public CustomerFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        super(attr, attrName, fieldType, required, editable);
    }
}
```

- [ ] **Step 4: 调整客户模块测试以使用公共协议类型**

把 `CustomerFieldAssemblerTest` 和 `CustomerFieldFactoryTest` 中用于字段断言的构造对象切到 `SceneFieldMeta` / `SceneFieldAssembler`，保留客户字段断言不变。

- [ ] **Step 5: 编译并跑客户模块相关测试**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerFieldAssemblerTest,CustomerFieldFactoryTest test`

Expected: PASS。

- [ ] **Step 6: Commit**

```bash
git add xbb-erp-module-customer/pom.xml xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerFieldAssembler.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/field/CustomerFieldMeta.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldAssemblerTest.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldFactoryTest.java
git commit -m "refactor: move customer field protocol to scene meta"
```

### Task 4: 将客户档案工厂改造成 provider

**Files:**
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/field/CustomerFieldFactory.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/field/DefaultCustomerFieldFactory.java`
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerAddItemViewTest.java`
- Modify: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerUpdateItemViewTest.java`

**Interfaces:**
- Consumes: `SceneTypeEnum`, `SceneFieldProvider`, `AbstractSceneFieldProvider`
- Produces: `CustomerArchiveSceneFieldProvider`，应用服务按 LIST / CREATE / UPDATE / DETAIL 四场景调用

- [ ] **Step 1: 先写失败测试，锁定客户档案 provider 的四场景输出**

```java
@Test
void should_return_fields_for_list_create_update_detail() {
    SceneFieldProvider provider = new CustomerArchiveSceneFieldProvider(List.of());

    List<SceneFieldMeta> listFields = provider.getFields(SceneTypeEnum.LIST);
    List<SceneFieldMeta> createFields = provider.getFields(SceneTypeEnum.CREATE);
    List<SceneFieldMeta> updateFields = provider.getFields(SceneTypeEnum.UPDATE);
    List<SceneFieldMeta> detailFields = provider.getFields(SceneTypeEnum.DETAIL);

    assertTrue(listFields.stream().anyMatch(field -> "main.customerCode".equals(field.getAttr())));
    assertTrue(createFields.stream().anyMatch(field -> "main.customerName".equals(field.getAttr())));
    assertTrue(updateFields.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())));
    assertTrue(detailFields.stream().anyMatch(field -> "invoiceProfiles.taxNo".equals(field.getAttr())));
}
```

- [ ] **Step 2: 实现客户档案 provider**

```java
public class CustomerArchiveSceneFieldProvider extends AbstractSceneFieldProvider {

    public CustomerArchiveSceneFieldProvider(List<SceneFieldRule> rules) {
        super(rules);
    }

    @Override
    public String sceneKey() {
        return "customer.archive";
    }

    @Override
    protected List<SceneFieldMeta> buildFields(SceneTypeEnum sceneType) {
        return switch (sceneType) {
            case LIST -> buildListFields();
            case CREATE -> buildCreateFields();
            case UPDATE -> buildUpdateFields();
            case DETAIL -> buildDetailFields();
        };
    }
}
```

```java
private List<SceneFieldMeta> buildListFields() { ... }
private List<SceneFieldMeta> buildCreateFields() { ... }
private List<SceneFieldMeta> buildUpdateFields() { ... }
private List<SceneFieldMeta> buildDetailFields() { ... }
```

- [ ] **Step 3: 把 `CustomerAdminAppServiceImpl` 切到公共协议入口**

```java
private final SceneFieldProvider sceneFieldProvider;

vo.setHeadList(SceneFieldAssembler.buildHeadList(sceneFieldProvider.getFields(SceneTypeEnum.LIST)));
vo.setHeadList(SceneFieldAssembler.buildHeadList(sceneFieldProvider.getFields(SceneTypeEnum.CREATE)));
vo.setHeadList(SceneFieldAssembler.buildHeadList(sceneFieldProvider.getFields(SceneTypeEnum.UPDATE)));
```

- [ ] **Step 4: 调整测试断言，确认新建/编辑视图的字段来源仍正确**

保留当前 `CustomerAddItemViewTest`、`CustomerUpdateItemViewTest` 的结构断言，只把字段来源切到 provider。

- [ ] **Step 5: 运行客户模块测试**

Run: `mvn -pl xbb-erp-module-customer -am test`

Expected: PASS。

- [ ] **Step 6: Commit**

```bash
git add xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/field/CustomerFieldFactory.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/field/DefaultCustomerFieldFactory.java xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerAddItemViewTest.java xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerUpdateItemViewTest.java
git commit -m "refactor: adopt scene meta provider in customer module"
```

### Task 5: 更新采购与模块初始化计划中的字段协议引用

**Files:**
- Modify: `docs/superpowers/specs/2026-07-24-scene-meta-design.md`（如需补充实现偏差）
- Modify: `docs/superpowers/plans/2026-07-24-scene-meta.md`（如需同步实现范围）
- Modify: `docs/superpowers/plans/2026-07-23-purchase-module-init.md`
- Modify: `xbb-erp-codegen/src/main/resources/examples/purchase/*.yaml`（如购买单据的字段场景命名需要统一）

**Interfaces:**
- Consumes: `scene-meta` 的最终接口命名
- Produces: 采购模块初始化任务可以直接复用同一套字段场景协议

- [ ] **Step 1: 检查采购计划中是否还引用旧的 customer-only 命名**

Run: `rg -n "CustomerFieldFactory|CustomerFieldMeta|DefaultCustomerFieldFactory|module-common" docs/superpowers/plans/2026-07-23-purchase-module-init.md xbb-erp-codegen/src/main/resources/examples/purchase`

Expected: 只保留与客户历史背景相关的引用，不再作为新方案命名。

- [ ] **Step 2: 将采购计划中的字段协议描述改成 scene-meta 术语**

示例替换：

```md
- 列表/创建/编辑/详情字段定义统一依赖 `xbb-erp-scene-meta`
- 各业务对象通过 `SceneFieldProvider` 提供本对象在四个场景下的字段定义
```

- [ ] **Step 3: 如采购 YAML 仍写有客户语义，修正为采购语义**

示例：

```yaml
sceneKey: purchase.request
```

- [ ] **Step 4: 无需改采购业务代码，只同步说明文本**

Expected: 采购代码继续按原计划推进，字段协议层复用新的 `scene-meta`。

- [ ] **Step 5: Commit**

```bash
git add docs/superpowers/plans/2026-07-23-purchase-module-init.md docs/superpowers/specs/2026-07-24-scene-meta-design.md docs/superpowers/plans/2026-07-24-scene-meta.md
git commit -m "docs: align purchase plan with scene meta protocol"
```

### Task 6: 全量验证与收口

**Files:**
- Test: `xbb-erp-scene-meta/src/test/java/xbb/ai/erp/scene/meta/SceneFieldAssemblerTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldAssemblerTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerFieldFactoryTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerAddItemViewTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerUpdateItemViewTest.java`

**Interfaces:**
- Consumes: 前 4 个任务输出
- Produces: 可验证的字段协议闭环

- [ ] **Step 1: 跑 scene-meta 单测**

Run: `mvn -pl xbb-erp-scene-meta -Dtest=SceneFieldAssemblerTest test`

Expected: PASS。

- [ ] **Step 2: 跑客户模块相关单测**

Run: `mvn -pl xbb-erp-module-customer -Dtest=CustomerFieldAssemblerTest,CustomerFieldFactoryTest,CustomerAddItemViewTest,CustomerUpdateItemViewTest test`

Expected: PASS。

- [ ] **Step 3: 跑客户模块编译**

Run: `mvn -pl xbb-erp-module-customer -am -DskipTests compile`

Expected: PASS。

- [ ] **Step 4: 如果出现无关失败，只记录不修**

```text
命令: <command>
失败模块: <module>
首个报错: <first error line>
判断: 与 scene-meta 迁移无关，仅记录
```

- [ ] **Step 5: Commit 收口**

```bash
git add xbb-erp-scene-meta xbb-erp-module-customer docs/superpowers/plans/2026-07-24-scene-meta.md
git commit -m "feat: introduce scene meta protocol"
```
