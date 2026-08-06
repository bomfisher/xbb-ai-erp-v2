# module-purchase 按 module-customer 规范全量镜像改造设计

## 背景

当前 `xbb-erp-module-purchase` 已具备 `admin`、`application`、`domain`、`infrastructure/persistence` 四层基础结构，但整体仍停留在采购首期 CRUD 骨架阶段，与 `xbb-erp-module-customer` 的完整 DDD 组织方式、接口协议和前端交互规范存在明显差距。

当前主要问题包括：

- `purchase` 以多个子模块各自 controller 和单体式应用服务实现为主，缺少 `customer` 风格的用例拆分。
- 接口协议未完全对齐项目规范，例如部分保存接口直接返回 `Long`，删除接口返回 `Void`，列表 DTO 继承体系也不统一。
- `purchase` 尚未形成与 `customer` 对齐的 `list`、`addItem`、`updateItem`、`saveDraft`、`draftList`、`loadDraft`、`saveAndSubmit`、`detail`、`delete` 全链路能力。
- `purchase` 前端页面、路由、服务封装、类型定义尚未完整落地，和 `customer` 现有交互体验不一致。
- 当前采购模块文档与代码已有部分偏差，本次以 `module-customer` 当前代码实现为最高对齐基准，不以既有 `purchase` 文档作为兼容约束。

本次改造目标不是局部修补，而是将 `xbb-erp-module-purchase` 的后端协议、DDD 分层和前端页面整体收敛到与 `xbb-erp-module-customer` 一致的最终态风格。

## 本次目标

### 目标 1：以后端 `module-customer` 当前实现为最高基准

本次 `purchase` 改造遵循以下优先级：

1. `xbb-erp-module-customer` 当前代码实现
2. 项目根规范 `CLAUDE.md`
3. `purchase` 现有代码与文档

这意味着本次允许直接调整 `purchase` 现有接口 URL、入参结构、出参结构、页面交互与代码组织方式，只要最终形态与 `customer` 对齐。

### 目标 2：覆盖 `purchase` 现有全部子模块

本次范围不是仅改 `PurchaseRequest` 单链路，而是覆盖 `purchase` 现有全部子模块，包括但不限于：

- `PurchasePendingTask`
- `PurchaseRequest`
- `PurchaseOrder`
- 与其相关的明细、来源关系和管理端页面

如某些子模块天然偏查询视图，也要在接口协议与前端体验层面尽量向 `customer` 标准模块靠拢。

### 目标 3：前后端全量镜像 `customer`

后端不仅要对齐分层名称，还要对齐接口组织方式、应用服务拆分方式、DTO / VO 体系、草稿能力与提交流程。

前端不仅要补出页面，还要尽量复用 `customer` 的完整心智模型，包括：

- 列表
- 筛选
- 新建空白表单
- 编辑加载表单
- 详情
- 保存草稿
- 草稿恢复
- 保存并提交
- 删除

### 目标 4：尽量不改数据库表结构

本次允许调整数据库脚本，但原则是“尽量不改表”。

只有在现有表结构无法承载以下能力时，才允许做最小化表结构调整：

- 草稿识别与草稿加载
- 业务状态表达
- 详情页必要字段补齐
- 与 `customer` 对齐所必需的持久化字段

如果必须改表，应优先新增必要字段或补齐缺口，不做大规模重命名和大迁移。

## 明确不做的事

- 不保留旧 `purchase` 接口 URL、DTO/VO、返回结构的运行时兼容层。
- 不为了兼容旧前端调用而保留双套接口语义。
- 不机械复制 `customer` 中仅适用于客户业务的字段和校验规则。
- 不在本次顺手重构与 `purchase` 无关的其他模块。
- 不因为“完全镜像”而把所有采购领域对象硬揉成一个超大聚合。

## 推荐方案

本次采用方案 A：以 `module-customer` 为模板，对 `module-purchase` 进行整体重塑。

采用该方案的原因：

- 用户已明确要求覆盖全部子模块，并前后端全量镜像 `customer`。
- 用户已明确允许破坏兼容，不需要背负旧接口形态。
- 若仅做协议表层对齐或 facade 过渡层，会在 `purchase` 内部留下两套设计语义，后续维护成本更高。

因此本次不采用“保留旧结构，仅补外观协议”的折中路线，而是直接收敛到最终态。

## 重构后的目标架构

### 1. 后端总体架构

`purchase` 模块重构后遵循“协议统一、领域分治”的原则。

对外：每个采购核心子模块都表现为 `customer` 风格的标准管理模块。

对内：各采购实体仍保留各自的领域模型、仓储、持久化实现与业务规则，不把所有采购业务糅成一个实现类。

### 2. admin 层设计

`admin` 层负责管理端协议接入，controller 只做参数接收、结果包装与应用服务转发，不承担业务编排。

对“可编辑型核心采购子模块”，应具备与 `customer` 对齐的完整接口集合：

- `list`
- `addItem`
- `updateItem`
- `saveDraft`
- `draftList`
- `loadDraft`
- `saveAndSubmit`
- `detail`
- `delete`

对“查询型子模块”，至少也要对齐统一的 `list` 与 `detail` 协议，并在页面交互和返回结构上保持与 `customer` 风格一致；如果其业务本身不承担编辑与提交流程，则不强行补写能力。

接口约束：

- 非脚本接口统一使用 DTO，不使用散列参数。
- DTO 继承项目现有基础 DTO 体系，列表查询 DTO 优先对齐 `ListBaseDTO` 风格。
- 所有接口返回统一使用 `ResultVO.success(...)` 包装。
- 无需业务数据返回的接口统一返回 `BaseVO`。
- 保存类接口返回统一对齐 `customer` 的 `SaveItemVO` 风格，不再保留 `ResultVO<Long>` 形式。

### 3. application 层设计

保留各子模块管理端门面服务接口，但实现方式对齐 `customer`，按用例职责拆分，而不是继续把逻辑堆在单一 `*AdminAppServiceImpl` 中。

建议最少补齐以下子结构：

- `application/service/query`
- `application/service/save`
- `application/service/draft`
- `application/service/delete`
- `application/service/impl`
- `application/assembler`
- `application/validator`
- `application/pojo`
- `application/port`
- `application/schema` 或等价查询适配层

职责原则：

- 门面 `impl` 只负责编排和转发。
- `query` 承接列表、详情、编辑加载和草稿列表读取。
- `save` 承接保存、提交流程和必要状态流转。
- `draft` 承接草稿保存与草稿加载。
- `delete` 承接删除校验与删除执行。
- `assembler` 承接 DTO/VO 与领域对象、表单结构之间的转换。
- `validator` 承接入参校验补充、业务规则校验与状态校验。
- `pojo` 承接用例上下文和中转对象，避免松散 `Map`。
- `port` 承接用例级依赖抽象，例如草稿存储或外部查询依赖。

### 4. domain 层设计

`domain` 层继续按采购实体拆分，保持采购领域边界清晰。

领域模型至少包括：

- `PurchaseRequest`
- `PurchaseRequestItem`
- `PurchaseOrder`
- `PurchaseOrderItem`
- `PurchasePendingTask`
- `PurchaseSourceRelation`

领域层要求：

- 状态字段尽量通过 `Enum` 表达业务语义。
- 领域对象保留业务语义方法，不让 application 层直接拼装状态变化。
- 仓储接口按实体拆分，不通过一个大仓储承接全部采购对象。
- 查询型对象和中转对象若有必要，可通过领域侧或应用侧 `Pojo` 承接，但不回退到散装参数。

### 5. infrastructure/persistence 层设计

持久化层整体命名与职责对齐 `customer`：

- `PO`
- `Mapper`
- `Convertor`
- `RepositoryImpl`
- `src/main/resources/mapper/purchase`

持久化要求：

- 所有直连数据库的实体统一使用 `PO` 后缀。
- 直接映射数据库的状态字段不使用布尔值，统一使用 `Integer`。
- 能对齐项目基础实体风格的 PO 尽量统一对齐。
- `RepositoryImpl` 负责领域仓储接口的完整落地。
- Mapper XML、PO、查询条件映射命名保持一致。

## 接口协议设计

### 1. 协议统一原则

本次 `purchase` 接口协议采用“最终态优先”策略：

- URL 组织方式向 `customer` 靠拢。
- DTO / VO 结构与命名向 `customer` 靠拢。
- 返回值类型与包装方式向 `customer` 靠拢。
- 不保留旧协议兼容层。

### 2. 表单与详情协议

各采购子模块的 `addItem`、`updateItem`、`detail`、`loadDraft` 应尽量返回同一类表单消费协议，保证前端能按一致模型渲染页面，而不是为每个子模块单独拼接结构。

协议组织原则：

- `addItem` 返回空白表单初始化结构。
- `updateItem` 返回编辑态表单回填结构。
- `detail` 返回详情页只读展示结构。
- `loadDraft` 返回草稿恢复结构。

如果 `customer` 当前已有“主表单 + 扩展块 + 元数据”的组织方式，本次 `purchase` 应尽量沿用同类语义，而不是自造新协议。

### 3. 草稿与提交协议

各核心采购子模块统一支持：

- `saveDraft`
- `draftList`
- `loadDraft`
- `saveAndSubmit`

设计原则：

- `saveDraft` 用于保存当前编辑中的临时状态。
- `draftList` 用于列出当前用户可恢复的草稿。
- `loadDraft` 用于恢复指定草稿内容。
- `saveAndSubmit` 用于完成正式提交与状态流转。

即使个别子模块业务上更偏查询，也优先保持统一能力边界，避免前端为“是否支持草稿”写过多分支。

## 前端设计

### 1. 前端总体目标

前端以 `module-customer` 当前页面体验为最高基准，对 `purchase` 进行全量镜像，不仅补页面，还要对齐页面行为、页面状态机、路由方式、接口封装和类型组织。

### 2. 页面能力范围

每个采购核心子模块至少应补齐：

- 列表页
- 筛选区
- 新建页或新建抽屉
- 编辑页或编辑抽屉
- 详情页
- 草稿保存
- 草稿恢复
- 保存并提交
- 删除操作

具体使用独立页面还是抽屉，应以 `customer` 当前实现形态为最高基准，优先复用相同交互模式。

### 3. 路由与目录组织

前端不继续沿用零散 demo 或临时平铺结构，而是按统一业务模块方式落地。

建议目录组织：

- `modules/purchase/<子模块>/pages`
- `modules/purchase/<子模块>/components`
- `modules/purchase/<子模块>/services`
- `modules/purchase/<子模块>/types`

目录职责：

- `pages` 承接列表、详情、新建编辑页面。
- `components` 承接筛选区、表单区块、详情区块等复用组件。
- `services` 承接接口请求封装。
- `types` 承接页面模型、接口 DTO/VO、交互状态类型。

### 4. 前端交互原则

前端交互尽量复用 `customer` 已有心智模型：

- 列表页负责筛选、分页、打开新建、编辑和详情。
- 新建/编辑页负责表单初始化、草稿保存、恢复、提交。
- 详情页负责只读展示和跳转编辑。
- 草稿列表与恢复流程与 `customer` 使用习惯保持一致。

不为兼容旧 `purchase` 接口保留双轨逻辑，页面直接消费新的统一协议。

## 数据流设计

### 1. 列表流

列表页加载 `list` 接口，渲染表格、筛选条件和分页信息。

点击“新建”后调用 `addItem` 获取空白表单结构。

点击“编辑”后调用 `updateItem` 获取回填结构。

点击“详情”后调用 `detail` 获取只读展示结构。

### 2. 编辑流

进入新建或编辑页面后，前端先拉取初始化或回填数据。

用户编辑过程中可调用 `saveDraft` 保存草稿。

草稿列表通过 `draftList` 展示。

用户从草稿恢复时调用 `loadDraft`。

最终通过 `saveAndSubmit` 完成正式提交。

### 3. 删除流

列表或详情页发起删除后，后端统一进行状态校验和删除执行，返回 `BaseVO`。

### 4. 子模块联动流

若 `PurchaseOrder`、`PurchaseRequest`、`PurchasePendingTask` 存在来源或状态联动关系，由 application 层负责编排，不让 controller 或前端承接复杂联动规则。

## 异常与校验设计

- 所有主动捕获的报错、业务主动抛错统一使用 `BizException`。
- controller 不写业务规则校验。
- DTO 层仅做边界输入校验。
- 业务规则校验放在 `application/validator` 或领域对象方法中。
- 对重复提交、非法状态流转、不可删除、不可编辑等场景统一抛出 `BizException`。
- 前端不自行推断复杂业务规则，仅消费后端返回状态和错误信息。

## 数据库策略

### 1. 总体策略

本次以“不大改表”为前提完成 `customer` 风格能力对齐。

优先顺序如下：

1. 优先复用现有表和字段完成协议重构。
2. 通过应用层与草稿存储策略承接缺失能力。
3. 只有在现有表无法支撑统一协议时，才补最小化字段。

### 2. 允许改表的触发条件

以下场景允许最小化调整表结构：

- 缺少草稿唯一标识或恢复所需关键字段。
- 缺少必要业务状态字段，无法支撑提交与删除限制。
- 缺少详情页必须展示且无法通过现有关联推导的字段。
- 缺少与 `customer` 对齐所必需的基础持久化能力。

### 3. 改表约束

如需改表：

- 优先补字段，不优先改表名。
- 优先最小增量，不做大规模迁移。
- 需同时补齐 SQL 与代码映射。
- SQL 作为正式交付物的一部分。

## 测试与验证设计

### 1. 后端验证

后端至少覆盖以下能力测试：

- 列表查询
- 新建初始化
- 编辑回填
- 草稿保存
- 草稿列表
- 草稿恢复
- 保存并提交
- 详情
- 删除
- 非法状态校验

测试优先参考 `customer` 已有测试模式进行镜像。

### 2. 前端验证

前端至少覆盖以下验证：

- 页面路由接入正确
- 列表页加载与筛选
- 新建/编辑页表单初始化与回填
- 草稿保存与恢复
- 提交成功路径
- 删除成功路径
- 关键异常提示路径

### 3. 联调验证

联调按完整黄金路径进行：

- 列表
- 新建
- 保存草稿
- 恢复草稿
- 保存并提交
- 详情
- 删除

验证时以后端新协议为准，不再验证旧接口兼容性。

### 4. 文档验证

本次实现完成后，需要按项目要求执行 `gen-api-md`，同步 `purchase` 模块接口文档。

## 实施顺序

### 阶段 1：统一标准与模板抽取

先对照 `customer` 梳理 `purchase` 统一协议、分层模板和页面模板，明确所有子模块的对齐目标。

阶段目标：

- 统一接口命名与返回规范
- 统一应用层拆分方式
- 统一前端页面能力边界

### 阶段 2：后端优先对齐核心链路

优先以后端 `PurchaseRequest` 为首个完整样板完成对齐，沉淀可复制模式。

阶段目标：

- 打通一条完整标准链路
- 产出可复制的后端模板

### 阶段 3：复制到其余采购子模块

将同一模式扩展到 `PurchaseOrder`、`PurchasePendingTask` 及其他关联对象。

阶段目标：

- 全部子模块接口和应用层收敛到统一风格
- 消除当前 `purchase` 内部的协议分叉

### 阶段 4：前端页面与路由全量对齐

按照 `customer` 交互方式为各采购子模块补齐页面、路由、服务与类型。

阶段目标：

- 前端页面能力完整
- 页面行为与 `customer` 对齐

### 阶段 5：测试、文档与收尾

补齐测试、联调验证和接口文档，完成改造闭环。

阶段目标：

- 核心链路可验证
- 文档与代码一致
- 接口文档同步完成

## 风险与应对

### 风险 1：现有表结构无法承载完整草稿协议

应对：优先通过应用层与草稿存储方式承接，只有必要时才做最小字段补充。

### 风险 2：`customer` 前端现状并非完全标准模块化

应对：以 `customer` 当前交互和协议为最高基准，但 `purchase` 新代码的前端目录组织尽量更规整，不复制明显的临时结构。

### 风险 3：采购子模块间存在联动规则

应对：联动编排收敛到 application 层，不扩散到 controller 或前端。

## 结论

本次 `module-purchase` 改造采用“整体重塑、最终态优先”策略，以 `module-customer` 当前实现为唯一高优先级模板，在尽量不改表的前提下，对 `purchase` 后端协议、DDD 分层和前端页面进行全量镜像收敛。

最终交付应满足以下判断标准：

- `purchase` 全部核心子模块都表现为 `customer` 风格标准模块。
- 后端接口协议、应用层拆分、异常处理、DTO/VO 体系符合项目规范。
- 前端页面、路由、草稿、提交、详情与列表体验与 `customer` 保持一致。
- 必要测试与接口文档同步补齐。