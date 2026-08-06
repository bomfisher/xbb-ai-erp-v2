# 客户列表统一动态筛选改造设计文档

## 1. 文档信息

| 项目 | 内容 |
|---|---|
| 文档名称 | 客户列表统一动态筛选改造设计文档 |
| 所属系统 | 进销存系统 |
| 所属模块 | 客户模块 / 管理端列表页 |
| 关联接口 | `POST /erp/v1/customer/list` `POST /erp/v1/common/list/filter` |
| 对应后端实现 | `xbb-erp-module-customer` |
| 对应前端实现 | `apps/admin-web/src/modules/customer/list` |
| 版本号 | V1.0 |
| 创建日期 | 2026-07-29 |
| 最后更新日期 | 2026-07-29 |

## 2. 背景与目标

当前客户列表查询链路存在以下问题：

- 后端 `CustomerListDTO` 采用平铺筛选字段结构，协议扩展性差
- 后端 `CustomerMapper.xml` 通过固定 `if` 条件承接筛选，无法复用到其他列表页
- 前端 `CustomerListPage.vue` 自行拼装平铺筛选请求体，与页面绑定过深
- 前端 `ListFilterPanel.vue` 只支持单输入框形态，无法表达字段、运算符、区间、多值等统一动态筛选能力
- `common/list/filter` 当前返回的筛选元数据过于简化，无法支撑真正的动态筛选公共组件

本次改造目标如下：

1. 将客户列表接口改造为统一动态筛选协议：`keyword + conditions + 分页基础字段`
2. 删除客户列表旧的平铺筛选字段协议，不做双轨兼容
3. 在后端沉淀可复用的统一动态筛选底座，客户列表作为首个接入方
4. 在前端 `apps/admin-web` 内抽取全局公共筛选组件，统一产出 `keyword` 与 `conditions`
5. 扩展 `common/list/filter` 元数据，使前端可按后端配置渲染筛选字段、运算符与选项
6. 保证动态筛选列名映射安全，所有值参数继续使用绑定变量传递

## 3. 设计范围

### 3.1 本次范围

本次仅落地以下内容：

- 客户列表接口 `POST /erp/v1/customer/list` 的筛选协议切换
- 客户列表前端页面筛选交互改造
- 管理端公共动态筛选组件抽取
- 后端统一动态筛选模型、校验器、字段元数据、公共 SQL 片段
- `POST /erp/v1/common/list/filter` 的筛选元数据扩展

### 3.2 首期开放字段

客户列表首期开放以下动态筛选字段：

- `customerCode`
- `customerName`
- `customerCategory`
- `ownerSalesId`
- `bizStatus`
- `regionCode`
- `createTime`

同时保留 `keyword` 作为高频顶部搜索入口。

### 3.3 非目标

本次不包含以下能力：

- 顶层 `OR` 条件组合
- 条件分组嵌套
- 任意 SQL 表达式透传
- 列表排序协议重构
- 其他业务列表页同步接入
- 将公共筛选组件提升到 monorepo 级共享包

## 4. 设计结论

### 4.1 总体方案

本次采用“客户列表先落地，前后端公共底座同步抽取”的方式实施。

具体结论如下：

- 后端 `CustomerListDTO` 切换为 `keyword + conditions + 分页基础字段`
- 后端新增统一动态筛选模型，以 `attr / fieldType / symbol / value[]` 四元组承载条件
- 后端在公共层新增统一筛选校验与安全列名映射能力
- 客户模块只维护自己的字段白名单与元数据，不再维护平铺筛选字段解析逻辑
- `CustomerMapper.xml` 引入公共动态筛选 SQL 片段，仅保留固定条件与主查询结构
- 前端抽取公共动态筛选组件，输出 `keyword` 与 `conditions`，客户列表页不再维护平铺筛选拼装逻辑
- `POST /erp/v1/common/list/filter` 扩展返回筛选字段元数据，驱动公共组件渲染

### 4.2 接口协议结论

`POST /erp/v1/customer/list` 改造后仅接受以下三类参数：

1. 基础上下文参数：`corpid`、`userId`
2. 分页参数：继承列表基础 DTO 的 `pageNum`、`pageSize`
3. 查询参数：`keyword`、`conditions`

旧的平铺字段：

- `customerCode`
- `customerName`
- `customerCategory`
- `regionCode`
- `ownerSalesId`
- `bizStatus`
- `refStatus`

从客户列表查询 DTO 中移除。

## 5. 后端设计

### 5.1 DTO 设计

`CustomerListDTO` 保留列表基础字段，并调整为：

- `keyword`
- `conditions`

建议结构如下：

```java
public class CustomerListDTO extends ListBaseDTO {
    private String keyword;
    private List<ListFilterConditionDTO> conditions;
}
```

其中 `ListFilterConditionDTO` 为统一动态筛选条件模型，字段如下：

- `attr`
- `fieldType`
- `symbol`
- `value`

### 5.2 统一筛选模型设计

建议在公共层新增以下模型：

- `ListFilterConditionDTO`：承接前端原始条件
- `ListFilterFieldTypeEnum`：字段类型枚举
- `ListFilterSymbolEnum`：运算符枚举
- `ListFilterMetaPojo`：后端字段白名单元数据
- `ListFilterSafeConditionPojo`：经服务层校验与安全映射后的条件对象

其中：

```java
public class ListFilterConditionDTO {
    private String attr;
    private String fieldType;
    private String symbol;
    private List<String> value;
}
```

字段说明：

- `attr`：前端字段标识，不允许直接进入 SQL
- `fieldType`：字段类型，用于决定可选运算符与值控件类型
- `symbol`：运算符
- `value`：统一数组结构，兼容单值、多值、区间值

### 5.3 字段类型与运算符约束

客户列表首期字段类型建议如下：

| 字段 | 列名 | 字段类型 |
|---|---|---|
| `customerCode` | `customer_code` | `TEXT` |
| `customerName` | `customer_name` | `TEXT` |
| `customerCategory` | `customer_category` | `ENUM` |
| `ownerSalesId` | `owner_sales_id` | `ID` |
| `bizStatus` | `biz_status` | `ENUM` |
| `regionCode` | `region_code` | `ENUM` |
| `createTime` | `add_time` | `DATE` |

首期运算符范围如下：

- `TEXT`：`EQ`、`NE`、`CONTAINS`、`NOT_CONTAINS`、`IS_EMPTY`、`IS_NOT_EMPTY`
- `ENUM`：`EQ`、`NE`、`IN`、`IS_EMPTY`、`IS_NOT_EMPTY`
- `ID`：`EQ`、`NE`、`IN`、`IS_EMPTY`、`IS_NOT_EMPTY`
- `DATE`：`EQ`、`GE`、`LE`、`BETWEEN`、`IS_EMPTY`、`IS_NOT_EMPTY`

### 5.4 值长度约束

不同运算符对 `value` 的长度有硬约束：

- `EQ / NE / GE / LE / CONTAINS / NOT_CONTAINS`：`value.size() == 1`
- `IN`：`value.size() >= 1`
- `BETWEEN`：`value.size() == 2`
- `IS_EMPTY / IS_NOT_EMPTY`：`value == null` 或 `value.size() == 0`

该约束由服务层统一校验，Mapper 不承担兜底职责。

### 5.5 应用层分工

客户列表查询逻辑调整为两层参数：

1. 固定条件：`corpid`、`keyword`、分页/排序等稳定参数
2. 动态条件：`conditions`

应用服务职责如下：

- 接收 `CustomerListDTO`
- 组装固定条件 map
- 调用统一筛选构建器校验并转换 `conditions`
- 将固定条件与安全条件一并交给仓储层

原先 `buildListConditionMap(CustomerListDTO dto)` 的平铺筛选组装逻辑移除，不再向 `conditionMap` 填充旧的平铺筛选字段。

### 5.6 客户字段白名单元数据

客户模块维护客户列表专属字段元数据，建议落在 application 层独立 provider/registry，而不是散落在 controller 或 service 方法中。

建议结构如下：

```java
public class ListFilterMetaPojo {
    private String attr;
    private String column;
    private String fieldType;
    private Set<String> supportedSymbols;
}
```

客户列表示意：

```java
Map<String, ListFilterMetaPojo> metaMap = Map.of(
    "customerCode", new ListFilterMetaPojo("customerCode", "customer_code", "TEXT", Set.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY")),
    "customerName", new ListFilterMetaPojo("customerName", "customer_name", "TEXT", Set.of("EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY")),
    "customerCategory", new ListFilterMetaPojo("customerCategory", "customer_category", "ENUM", Set.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY")),
    "ownerSalesId", new ListFilterMetaPojo("ownerSalesId", "owner_sales_id", "ID", Set.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY")),
    "bizStatus", new ListFilterMetaPojo("bizStatus", "biz_status", "ENUM", Set.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY")),
    "regionCode", new ListFilterMetaPojo("regionCode", "region_code", "ENUM", Set.of("EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY")),
    "createTime", new ListFilterMetaPojo("createTime", "add_time", "DATE", Set.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"))
);
```

### 5.7 统一筛选构建器职责

统一筛选构建器负责以下事项：

1. 校验 `attr` 是否存在于当前列表页白名单
2. 校验前端 `fieldType` 是否与白名单配置一致
3. 校验 `symbol` 是否在当前字段允许范围内
4. 校验 `value` 数组长度是否合法
5. 校验 `value` 内容是否为空、是否满足日期/枚举基本格式要求
6. 将前端 `attr` 映射为可安全进入 SQL 的列名

输出结果为安全条件集合，例如：

```json
[
  {
    "attr": "customer_name",
    "fieldType": "TEXT",
    "symbol": "CONTAINS",
    "value": ["华东"]
  }
]
```

### 5.8 仓储层与 Mapper 调整

仓储层仍保留 `ConditionMapHelper.prepare(conditionMap)` 的分页偏移处理职责。

传入仓储的查询参数新增：

- `keyword`
- `conditions`

`CustomerMapper.xml` 调整为：

- `BaseCondition` 保留：`corpid`、`del = 0`、`keyword`
- 删除旧的平铺筛选 `if` 条件
- 新增对 `conditions` 的 `<foreach>` + 公共 `<include>` 引用

## 6. 公共动态筛选 SQL 设计

### 6.1 复用方式

建议在公共模块新增独立 Mapper XML，仅承载公共 `<sql>` 片段，不定义完整 `<select>`。

业务 Mapper 通过 `<include>` 引入，公共片段仅负责生成 `AND` 条件。

### 6.2 客户 Mapper 侧使用方式

客户 Mapper 最终结构如下：

```xml
<select id="findByCondition" resultType="xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO">
    select <include refid="BaseColumns"/>
    from customer
    where <include refid="BaseCondition"/>
    <if test="conditionMap.conditions != null and conditionMap.conditions.size() > 0">
        <foreach collection="conditionMap.conditions" item="c">
            <include refid="CommonDynamicFilterMapper.dynamicCondition"/>
        </foreach>
    </if>
    <include refid="QueryTail"/>
</select>
```

### 6.3 公共 SQL 片段职责

公共片段仅负责：

- 按 `fieldType` 分发
- 按 `symbol` 组装条件
- 所有值通过 `#{}` 绑定

安全约束如下：

- `${c.attr}` 只承接服务层映射后的安全列名
- 不允许前端原始 `attr` 直接进入 `${}`
- 公共片段不重复做白名单判断，默认输入已安全

### 6.4 固定关键字搜索保留策略

`keyword` 继续作为业务预置搜索条件存在，不并入 `conditions`。

原因如下：

- `keyword` 本质上是多字段模糊搜索入口
- 属于高频轻量搜索，不适合与高级筛选行混为一体
- 保留后可使客户列表在升级协议后仍具备快速搜索体验

最终逻辑关系为：

```text
keyword AND condition1 AND condition2 AND ...
```

## 7. 前端设计

### 7.1 公共组件设计结论

本次不继续沿用当前 `ListFilterPanel.vue` 的固定表单模式，而是在 `apps/admin-web/src/components/list/` 内抽取可复用的动态筛选公共组件。

组件职责调整为：

- 管理 `keyword`
- 管理 `conditions` 列表
- 根据字段元数据联动运算符与值控件
- 对外统一输出查询协议

### 7.2 组件拆分建议

建议拆分为两层：

1. 筛选面板壳组件
2. 条件行组件

#### 筛选面板壳组件职责

- 渲染标题说明
- 渲染 `keyword` 输入框
- 渲染条件列表容器
- 提供新增条件、查询、重置操作
- 输出 `keyword` 与 `conditions`

#### 条件行组件职责

- 字段选择
- 运算符选择
- 值控件渲染
- 删除当前条件
- 根据字段类型和运算符切换输入形态

### 7.3 输出协议

前端公共组件不再输出：

- `queryForm: Record<string, string>`

改为统一输出：

- `keyword: string`
- `conditions: ListFilterCondition[]`

客户列表页面仅负责接收这两个值，并和分页基础参数一起发往后端。

### 7.4 首期控件支持范围

为覆盖客户列表首期字段，公共组件首期支持以下控件：

- `TEXT`：文本输入框
- `ENUM / ID`：单选下拉、多选下拉
- `DATE`：单日期、日期区间

控件联动规则如下：

- 选择 `TEXT` 字段时，仅展示文本类运算符
- 选择 `DATE` 字段时，仅展示日期类运算符
- 选择 `ENUM / ID` 字段时，按是否支持 `IN` 决定单选或多选
- 选择 `IS_EMPTY / IS_NOT_EMPTY` 时，隐藏值输入区域
- 选择 `BETWEEN` 时，展示两个值输入位

### 7.5 客户列表页改造

`CustomerListPage.vue` 需要做以下调整：

- 不再维护平铺 `queryForm`
- 不再按字段逐项拼装请求体
- 改为维护：`keyword`、`conditions`
- `loadList()` 请求体只保留：
  - `corpid`
  - `userId`
  - `pageNum`
  - `pageSize`
  - `keyword`
  - `conditions`

这样客户列表页面只保留：

- 元数据请求
- 列表数据请求
- 分页状态
- 业务表格渲染

筛选逻辑从页面中下沉到公共组件。

## 8. 筛选元数据接口设计

### 8.1 改造目标

当前 `POST /erp/v1/common/list/filter` 返回的筛选元数据仅包含：

- `attr`
- `attrName`
- `fieldType`

该结构无法支撑动态筛选组件。

本次需要扩展为真正的筛选字段定义协议。

### 8.2 元数据建议结构

建议最少包含以下字段：

- `attr`
- `attrName`
- `fieldType`
- `supportedSymbols`
- `options`

示意结构如下：

```json
{
  "attr": "bizStatus",
  "attrName": "业务状态",
  "fieldType": "ENUM",
  "supportedSymbols": ["EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"],
  "options": [
    { "label": "启用", "value": "ENABLED" },
    { "label": "停用", "value": "DISABLED" }
  ]
}
```

### 8.3 客户列表首期字段元数据要求

客户列表字段元数据要求如下：

- `customerCode`：无选项，文本类运算符
- `customerName`：无选项，文本类运算符
- `customerCategory`：返回枚举选项
- `ownerSalesId`：返回负责人选项
- `bizStatus`：返回业务状态选项
- `regionCode`：返回区域选项
- `createTime`：无选项，日期类运算符

公共筛选组件完全按元数据决定字段选择项、运算符与值控件，不再在页面内硬编码这些规则。

## 9. 异常处理与边界约束

### 9.1 后端异常分类

统一动态筛选的失败需收敛为明确业务异常，不做静默兜底。

至少区分以下场景：

- 字段不支持
- 字段类型不匹配
- 运算符不支持
- 值格式非法

### 9.2 前端交互边界

公共筛选组件遵循以下边界：

- 未选择字段时，不允许提交空条件行
- 同一字段允许重复添加条件
- `IN` 输出数组值
- `BETWEEN` 固定输出两个值
- `IS_EMPTY / IS_NOT_EMPTY` 不传值
- `keyword` 与 `conditions` 可同时存在

### 9.3 兼容策略

本次不做旧协议兼容。

前端与后端同步切换到新协议，避免双轨逻辑带来的复杂度与维护成本。

## 10. 测试与验收

### 10.1 后端测试

后端建议新增或补充以下测试：

- `CustomerListDTO` 新结构测试
- 统一筛选校验器测试
- 字段白名单映射测试
- `CustomerListServiceTest` 的 `conditions` 场景测试
- 仓储/Mapper 相关测试，验证传入 SQL 前的列名已是安全列名

重点覆盖：

- 文本包含查询
- 枚举单值查询
- 枚举多值 `IN` 查询
- 日期区间查询
- `keyword + conditions` 组合查询
- 非法字段/非法运算符/非法 value 长度拦截

### 10.2 前端测试

前端建议新增或补充以下测试：

- 公共筛选组件字段切换测试
- 运算符联动测试
- 值输入区域显隐测试
- 重置测试
- 客户列表页请求体结构测试

重点验证客户列表查询请求体不再包含旧的平铺筛选字段。

### 10.3 联调验收标准

验收标准如下：

1. `POST /erp/v1/customer/list` 仅接受新协议
2. 客户列表可通过公共筛选组件完成首期 7 个字段筛选
3. `keyword` 与 `conditions` 可组合使用
4. 前端不再保留客户列表专属平铺筛选拼装逻辑
5. 后端动态 SQL 只消费安全映射后的列名
6. 公共前后端底座可支撑后续其他列表页接入

## 11. 实施建议

建议按以下顺序实施：

1. 后端先补统一筛选模型、枚举、校验器与客户字段元数据
2. 改造客户列表 DTO、应用服务与 Mapper
3. 扩展 `POST /erp/v1/common/list/filter` 元数据返回结构
4. 前端抽取公共动态筛选组件
5. 客户列表页接入公共组件并切换请求协议
6. 补齐前后端测试并联调验证

## 12. 最终结论

本次客户列表改造采用“`keyword` 顶部搜索 + `conditions` 统一动态筛选”的新协议，同时在前后端沉淀可复用底座。

其中：

- 后端以统一筛选模型、字段白名单、服务层安全映射和公共 SQL 片段为核心
- 客户模块只维护客户列表自己的字段元数据
- 前端在 `apps/admin-web` 内抽取公共动态筛选组件，按后端元数据渲染字段、运算符与值控件
- 客户列表页从页面自拼筛选参数转为消费公共组件输出

该方案能够在直接切换新协议的前提下，完成客户列表筛选能力升级，并为后续其他列表页复用统一动态筛选方案提供稳定基础。