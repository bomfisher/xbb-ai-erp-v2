# 审批中台接入契约

## 目的

`xbb-erp-module-approval-contract` 是业务系统与审批中台之间的最小稳定契约。它不依赖 ERP 单据、组织表、数据库或 Spring，实现可以先在同一进程内调用，后续替换为 HTTP、消息或 SDK 调用而不改变业务模块的领域代码。

业务单据接入运行时的固定做法见 [business-document-integration.md](business-document-integration.md)。后续 Agent 实现单据审批时必须先按该文档检查事务、快照、状态和审批通过副作用；审批操作 HTTP 接口见 `docs/api/endpoints/approval-instance-*.md`。

流程设置页首期接口为 `/erp/v1/approval/flow/*`，由 ERP 保留菜单、当前用户和租户权限边界。流程定义当前保存于 ERP 的 `approval_flow_*` 表；中台拆分后，ERP 接口改为网关转发，中台拥有流程定义、实例、任务和审计表，ERP 不跨库读写。

## 业务字段目录

审批运行时已提供 `ApprovalSubjectSchemaProvider` 和 `ApprovalConditionMatcher` 通用扩展点。业务模块只注册字段目录，不把合同、订单等业务判断写入审批模块；审批平台根据提交快照和已发布流程的结构化规则执行匹配。

销售合同使用 `SALES_CONTRACT` 作为 `businessCode`。当前由 `xbb-erp-module-sales` 注册 `ApprovalSubjectSchemaProvider`，公开客户、合同金额、币种和签订日期的字段白名单；`xbb-erp-module-sales-contract` 公开 `SalesContractApprovalQueryApi` 与 `SalesContractApprovalSnapshot`，供合同聚合落地后按租户查询审批快照。合同保存用例应在字段校验和快照构建后调用 `ApprovalPlatformApi#submit`，不得调用流程配置接口或复制审批条件判断。

业务模块接入审批时，必须登记可用于流程匹配和条件分支的字段白名单。目录通过 `POST /erp/v1/approval/flow/catalog` 提供业务编码、业务名称、可审批场景、字段路径、字段类型、允许操作符和业务选择编码；设置页在创建流程前先选择业务对象，再加载该目录。

审批流程的 `businessCode` 必须与 `BusinessCodeEnum` 的 `code` 完全一致；`flowCode` 仅用于同一业务和场景下的流程版本识别，不能作为 `businessCode`。审批设置目录只展示满足上述枚举约束且已注册审批字段目录的业务对象。

审批目录由业务模块注册的 `ApprovalSubjectSchemaProvider` 聚合，审批模块不维护销售、合同等业务字段。拆分为中台后，目录继续作为 SPI：各业务模块或接入方注册自身字段 schema，中台持久化流程定义时冻结 schema 版本；已生成流程和审批实例继续按冻结版本解释。

目录字段必须来自业务新建或编辑表单的稳定字段元数据，且只开放适合审批判断的字段。禁止开放数据库列名、明细行、审计字段、运行状态字段或任意表达式入口。

## 接入方职责

业务系统提交审批时调用 `ApprovalPlatformApi#submit`，并提供：

- `tenantId`：租户边界；
- `businessCode`：接入方定义的业务对象编码；
- `scene`：`CREATE` 或 `UPDATE`；
- `requestId`：业务系统生成的提交幂等键；
- `subjectSummary`：待办列表展示摘要；
- `subjectSnapshotJson`：审批时冻结的完整数据快照。

业务系统实现 `ApprovalResultHandler`，在收到 `ApprovalResultEvent` 后按 `eventId` 幂等处理：

- `CREATE + APPROVED`：创建正式业务对象；
- `UPDATE + APPROVED`：原地应用已冻结的变更快照；
- `REJECTED` 或 `WITHDRAWN`：结束本地待审批数据，不修改当前有效业务版本。

审批实例为 `NO_APPROVAL` 或 `APPROVED` 时才具有业务资格。业务系统应在创建下游单据、出入库、结算和过账等入口再次校验，不能只依赖页面按钮。

## 销售合同试点接入时机

销售合同聚合、保存接口和数据库表尚未在当前仓库落地，因此本次先提供可编译的审批查询契约和字段目录。合同聚合实现后，应在提交保存入口完成字段校验和快照构建后调用 `submit`，不应在审批通过前插入正式合同数据。`ApprovalResultHandler` 在审批通过时调用合同模块内部的创建或原地更新用例。

合同审批快照与 `SalesContractApprovalSnapshot` 保持相同字段语义，序列化后的 JSON 根路径为 `/customerId`、`/totalAmount`、`/currencyCode`、`/signDate`。合同实现方应基于提交 DTO 生成快照，编辑审批则可通过 `SalesContractApprovalQueryApi` 查询已生效合同进行差异校验。
