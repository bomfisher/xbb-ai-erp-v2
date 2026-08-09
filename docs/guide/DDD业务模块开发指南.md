# DDD 业务模块开发指南

## 目的与适用范围

本指南以 `xbb-erp-module-customer` 为当前参考实现，约束后续业务模块从列表页、表单页到保存链路的后端开发。它描述的是跨模块稳定的架构契约；领域字段、状态机和校验规则必须由各业务自行定义，不能直接复制客户领域语义。

使用本指南时，先阅读目标模块已有代码和相关 API 原子文档，再选择与需求最接近的客户实现。接口请求、响应的完整字段事实只维护在 `docs/api/endpoints/`。

## 总体结构与依赖规则

```text
admin (HTTP DTO/VO/Controller)
             |
             v
application (用例编排、组装、校验、页面字段/列表元数据)
             |
             v
domain (领域模型、查询 Pojo、Repository 接口)
             ^
             |
infrastructure (PO、Mapper、Convertor、Repository 实现)
```

- `admin` 只处理 HTTP 边界和 DTO/VO，不写查询拼装、持久化或领域规则。
- `application` 编排一个用例，负责 DTO/VO 与领域对象的转换、校验顺序、事务边界以及跨 Repository 协作。
- `domain` 不依赖 `admin`、`application`、`infrastructure`；Repository 仅定义领域所需的读写语义。
- `infrastructure` 实现 `domain` 或 `application.port` 定义的接口，承载 MyBatis-Plus、SQL、缓存和草稿存储细节。
- 禁止 Controller 直接注入 Mapper；禁止 Domain 依赖 PO、DTO、VO 或 Spring Web；禁止 Application 向前端泄漏 PO。

保存主档与多个子档属于一个业务动作时，应由应用服务在同一事务边界内完成；任一校验或持久化失败必须整体回滚。

## 客户模块包地图

| 包 | 存放内容 | 不应存放 |
| --- | --- | --- |
| `customer.admin` | `CustomerAdminController`、仅 HTTP 可见的业务枚举 | 持久化、领域模型、复杂业务编排 |
| `customer.admin.dto` | 前端入参 DTO：列表、表单、草稿、保存、子档行 | PO、数据库查询对象 |
| `customer.admin.vo` | 前端出参 VO：列表行、详情、表单初始化、草稿 | 内部应用上下文 |
| `customer.application.service` | 应用服务接口与总入口 `CustomerAdminAppService` | SQL 与 Mapper 调用 |
| `customer.application.service.query` | 列表、表单初始化、详情读取用例 | 正式保存、删除实现 |
| `customer.application.service.save` | 提交保存、主子档同步、保存编排 | HTTP 参数解析 |
| `customer.application.service.draft` | 草稿保存、草稿列表、草稿加载 | 正式领域表持久化细节 |
| `customer.application.service.delete` | 删除用例及引用/子档处理 | 列表或表单查询 |
| `customer.application.assembler` | DTO/VO、领域模型、应用 Pojo 的单向转换 | Repository 或 Mapper 查询 |
| `customer.application.validator` | 协议、字段、业务三类校验 | HTTP 响应包装 |
| `customer.application.field` | 场景字段工厂、字段元数据和可插拔字段规则 | 业务数据持久化 |
| `customer.application.config` | 字段规则等 Spring 装配 | 业务用例逻辑 |
| `customer.application.provider` | 面向公共能力的列表元数据 Provider | 列表数据查询 |
| `customer.application.schema` | 动态筛选协议到领域查询条件的适配与白名单 | SQL 字符串拼接 |
| `customer.application.port` | 不属于领域表的应用端口，例如草稿仓储 | MyBatis 实现 |
| `customer.application.pojo` | 应用层内部上下文、扩展数据、分区状态 | HTTP DTO/VO |
| `customer.domain.model` | 客户主档及联系人、地址等领域模型 | 注解 PO、Mapper |
| `customer.domain.pojo` | Repository 查询条件和领域内部传递对象 | 页面字段元数据 |
| `customer.domain.repository` | 领域读写接口 | MyBatis-Plus 实现 |
| `customer.domain.config`、`customer.domain.field` | 预留给纯领域配置、领域字段规则；无明确领域必要性时不要新增 | Spring Web、持久化实现 |
| `customer.infrastructure.persistence.po` | 与表一一对应的 `*PO` | DTO/VO、`Boolean` 数据库字段 |
| `customer.infrastructure.persistence.mapper` | MyBatis-Plus Mapper 和专用 SQL 映射 | 业务校验与 VO 转换 |
| `customer.infrastructure.persistence.convertor` | Domain Model 与 PO 的转换 | Controller 参数处理 |
| `customer.infrastructure.persistence.repository` | Repository/Port 实现、查询条件落地 | HTTP 返回对象 |

`CustomerAdminAppServiceImpl` 是总入口的薄分派层。复杂用例继续拆分到 `query`、`save`、`draft`、`delete` 子服务，避免一个 AppService 演变为全能类。

## 列表页契约

列表由“公共元数据 + 业务数据”两个独立请求组成，前端不能把字段、按钮和筛选条件写死在页面中。

```text
前端 businessCode
  ├─ POST /erp/v1/common/list/filter|header|topButton|bottomButton|rowAction
  │    -> ListCommonController -> ListCommonService
  │    -> ListMetaRegistry -> 目标模块 ListMetaProvider
  └─ POST /erp/v1/{business}/list
       -> {Business}AdminController -> QueryAppService
       -> ListQueryAdapter -> Domain Repository -> Assembler -> ListBaseVO
```

### 公共列表元数据

- 公共入口位于 `xbb/ai/erp/module/common/admin/ListCommonController.java`，每个接口接收 `ListCommonQueryDTO`，按 `businessCode` 从 `ListMetaRegistry` 定位 `ListMetaProvider`。
- 业务模块创建 `*ListMetaProvider` 并注册为 Spring 组件，实现 `businessCode()`、筛选字段、表头、顶部按钮、底部按钮、行操作。客户模块的参考类是 `CustomerListMetaProvider`。
- 筛选元数据必须同时维护“前端属性 → 允许的数据库列 → 字段类型 → 操作符白名单”的映射。`*ListQueryAdapter` 只能接受白名单属性和操作符，绝不能直接拼接前端传入的列名或操作符。
- 表头字段从 `*FieldFactory` 的 `LIST` 场景生成；按钮和行操作以稳定的 `actionCode` 标识。前端根据动作编码跳转或提交，不通过按钮文字判断行为。

### 业务列表数据

- 业务列表接口直接使用 `ListBaseDTO`，承载分页、关键词和动态 `conditions`；不得仅为这些公共字段重新生成 `*ListDTO`，Controller 始终使用一个 DTO 接收请求。
- Query AppService 将 DTO 转成 `*QueryPojo`，Repository 返回领域模型，Assembler 再转换为 `*ListItemVO` 和 `ListBaseVO`。
- 列表需要子档摘要时，先收集当前页主键，再按 `customerIds` 等批量条件一次查询并建 Map 回填；禁止在行循环中查询数据库。
- 分页总数应通过 Repository 的 `count` 或数据库分页能力获得，不能因实现方便而把全量记录读入内存。
- 列表数据接口只负责数据；字段、筛选器、按钮和行操作仍由公共列表接口下发。

## 新建页与编辑页契约

新建和编辑统一返回 `SaveItemVO<T>`：`headList` 描述字段，`data` 保存当前表单数据。字段配置是后端契约，前端必须按 `attr`、`fieldType`、`required`、`editable` 和 `itemList` 渲染。

### 新建页

1. 前端调用 `POST /erp/v1/{business}/addItem`，入参为 `BaseDTO`。
2. Query AppService 用 `*FieldFactory.getFields(SceneTypeEnum.CREATE)` 生成 `headList`。
3. Assembler 构造空的 `*SaveItemVO`：主档为空对象、子档为空列表、可选子档 `sectionState` 默认为 `0`。
4. 前端根据字段元数据渲染新建表单；用户开启一个可选分区时，将对应 `sectionState` 设为 `1`。

### 编辑页

1. 前端从列表的 `EDIT` 行动作进入编辑，调用 `POST /erp/v1/{business}/updateItem`，入参为 `IdBaseDTO`。
2. Query AppService 用 `UPDATE` 场景生成 `headList`，查询主档与各子档并批量/按主键加载。
3. Assembler 将领域模型回填为表单 DTO 形状，并根据每个子档是否有数据生成 `sectionState`：有数据为 `1`，无数据为 `0`。
4. 无效的公司、缺少主键、主档不存在等情况都以 `BizException` 中断，不返回半成品表单。

字段定义集中在 `*FieldFactory`、字段枚举和 `*FieldRule` 中；创建、更新、详情和列表的差异由 `SceneTypeEnum` 表达，不应散落在 Controller 或前端硬编码。

## 保存、提交与草稿契约

客户保存以 `saveAndSubmit` 为参考，主档与联系人、地址、银行账户、开票信息等子档一起提交。

```text
CustomerSubmitSaveDTO
  -> Assembler.toSubmitContext
  -> ProtocolValidator（结构、租户等协议）
  -> CommonValidator（字段必填、格式、默认项唯一性）
  -> BusinessValidator（领域业务规则）
  -> 按 sectionState 过滤关闭的子档
  -> 保存主档（新增/更新）
  -> 同步子档（删除缺失项、插入新项、更新已有项）
  -> 提交成功后按 draftCode 删除草稿
```

- 保存入参使用专用 `*SubmitSaveDTO`，包含主档、子档、`sectionState` 和可选 `draftMeta`；不要把表单字段拆成散列参数。
- 三层校验顺序不可颠倒：协议校验保障上下文可用，通用字段校验保障数据形态，业务校验保障领域约束；所有主动失败均抛 `BizException`。
- `sectionState=0` 的子档不参与正式保存。仅因前端关闭分区而删除已有数据是高风险行为，必须明确该业务的删除语义后再实现。
- 子档同步前先读取已有记录，利用传入 ID 集合识别新增、更新、删除；涉及默认项或被引用记录的删除必须在应用层显式拦截。
- 所有 PO 的逻辑删除、版本、审计人和时间等默认值在应用保存编排或统一基础设施中一致处理，禁止让前端伪造。
- Controller 使用 `ResultVO.success()` 包装所有成功结果；无业务返回值使用 `BaseVO`，不要以 `null` 表达成功业务对象。

草稿保存与正式提交必须分开：草稿做可恢复所需的宽松字段校验，存入 `application.port` 定义的草稿仓储；正式提交做严格校验并写领域表。仅当正式保存完全成功且请求携带原 `draftCode` 时，才删除草稿。

## 对象、命名与代码规范

| 类型 | 后缀与位置 | 规则 |
| --- | --- | --- |
| 前端请求 | `*DTO`，`admin.dto` | 非脚本接口继承 `BaseDTO`；所有参数封装为 DTO |
| 前端响应 | `*VO`，`admin.vo` | Controller 用 `ResultVO.success()` 包装 |
| 数据库实体 | `*PO`，`infrastructure.persistence.po` | 字段与表映射；布尔状态使用 `Integer`，不用 `Boolean` |
| 领域模型 | 无 PO/DTO/VO 后缀，`domain.model` | 面向业务含义，不暴露 ORM 细节 |
| Repository 查询/内部传递 | `*Pojo` | 不跨越 HTTP 或持久化边界暴露 |
| 枚举 | `*Enum` | 枚举值、展示项与转换逻辑集中维护 |

- 使用 Lombok 管理 getter/setter，避免手写样板代码。
- `userId` 是字符串员工 ID；租户/公司字段统一使用 `corpid`。
- 主动捕获异常、参数错误、业务规则失败统一使用 `BizException`；不要以 `RuntimeException`、`null` 或静默吞错代替。
- 所有数据库访问都经过 Repository；批量场景优先批量查询、批量更新或预加载 Map。
- 新建接口、字段、状态、动作编码和数据库字段时，先检查现有 `BaseDTO`、`FieldTypeEnum`、`BusinessCodeEnum` 和同类模块，避免引入并行标准。

## Agent 执行约束与交付清单

每次开发业务模块或扩展页面能力，Agent 必须按以下顺序执行：

1. 阅读本指南、目标模块已有实现、对应 API 原子文档和模块导航；先确认是新增业务、扩展列表还是扩展表单/保存。
2. 先定义领域模型、Repository 接口和用例边界，再实现 Infrastructure 与 Application；最后增加 Controller、DTO、VO 和接口文档。
3. 新增列表时，同时实现数据查询、`ListMetaProvider`、筛选白名单适配；不得只实现 Controller 列表接口。
4. 新增表单时，同时实现 `CREATE`、`UPDATE` 场景字段元数据和可回填的保存数据结构；不得让前端猜字段或分区状态。
5. 新增保存时，定义协议/通用/业务校验、事务边界、主子档同步及草稿清理条件；不得只调用 Mapper 写表。
6. 修改前先检查工作区已有改动，只触碰与当前需求相关的文件；不重置、不覆盖用户改动。
7. 变更接口事实时，按 `.claude/commands/multi-player/SKILL.md` 维护 API 原子文档、业务/功能聚合文档和 `docs/kn/总目录.md`。
8. 交付前运行目标模块的相关测试或最小 Maven 验证，并说明未运行验证的原因；同时检查层次依赖、`BizException`、对象后缀、批量查询和文档同步。

## 参考入口

- 公共列表入口：`xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- 客户 HTTP 入口：`xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`
- 客户列表元数据：`xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- 客户查询与表单初始化：`xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/query/CustomerQueryAppServiceImpl.java`
- 客户正式保存：`xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/save/CustomerSaveAppServiceImpl.java`
- 客户接口原子文档：`docs/api/endpoints/customer-*.md`
