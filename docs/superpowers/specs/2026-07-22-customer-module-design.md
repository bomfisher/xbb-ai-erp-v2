# 客户模块基础 CRUD 接口设计

**日期：** 2026-07-22  
**模块名：** `xbb-erp-module-customer`  
**Java 根包：** `xbb.ai.erp.module.customer`

## 1. 目标

在现有客户模块持久层初始化基础上，补齐客户管理模块首期 6 个基础接口：

- `/list`
- `/addItem`
- `/updateItem`
- `/save`
- `/detail`
- `/delete`

本次接口覆盖以下业务对象：

- 客户主档 `customer`
- 客户联系人 `customer_contact`
- 客户地址 `customer_address`
- 客户银行账户 `customer_bank_account`
- 客户开票信息 `customer_invoice_profile`

本次不包含：

- 附件能力
- 引用与上下游真实数据聚合
- 操作流水真实数据聚合
- 启用 / 停用独立接口
- 附件上传
- 外部模块真实联动实现

其中“引用与上下游”“操作流水”在详情结构中先以明确的空列表结构返回，首期不提供真实业务数据。

## 2. 已确认约束

### 2.1 工作区

- 不创建 `worktree`
- 直接在当前分支设计与后续实现

### 2.2 接口范围

本次必须补齐客户模块 6 个基础接口，且不是只做客户主档，而是把以下子资料一起纳入：

- 联系人
- 地址
- 银行账户
- 开票信息

### 2.3 保存策略

`/save` 采用 ERP 主数据场景更合适的**整单覆盖式保存**：

- 前端一次提交主档与全部子资料
- 后端在单事务内统一完成新增、更新、删除判定、默认项校验
- 支持新建与编辑共用同一个保存接口

### 2.4 删除规则

删除规则按设计文档执行，不做简化：

- 默认项不能直接删除
- 已形成历史快照的子资料优先停用
- 已被引用的客户主档不允许物理删除，应提示改为停用

### 2.5 状态策略

允许前端在 `/save` 中直接保存为两种状态：

- `DRAFT`
- `ENABLED`

本次不要求统一先落草稿再启用。

### 2.6 附件策略

附件本期不做，因此：

- `/save` 不接收附件数据
- `/addItem`、`/updateItem`、`/detail` 不暴露附件字段

## 3. 整体方案

采用**单控制器 + 单保存 DTO + 聚合式保存应用服务**方案。

### 3.1 推荐原因

这是最贴合 ERP 主数据维护的接口模式：

- 前端以“整张主档表单”维护客户
- 后端集中处理主档与子资料的一致性
- 方便后续继续叠加默认项规则、引用校验、启停用规则和审计能力

### 3.2 不采用的方案

#### 方案 B：主档与子资料拆多个独立保存接口

不采用原因：

- 与已确认的整单覆盖式保存冲突
- 前端交互会碎片化
- 默认项与删除校验更难统一落地

#### 方案 C：控制器直接串调多个仓储

不采用原因：

- 会破坏 DDD 分层
- 后续业务规则会迅速堆积在控制器中
- 不利于测试和扩展

## 4. 分层设计

在当前 `domain + infrastructure.persistence` 基础上，新增 `admin` 与 `application` 两层。

### 4.1 新增目录

- `admin`
- `application/service`
- `application/assembler`
- `admin/dto`
- `admin/vo`

### 4.2 各层职责

#### `admin`

只承接 HTTP 接口定义、参数接收和回参输出。

#### `application/service`

负责编排：

- 列表查询
- 新建页字段定义与默认值
- 编辑页历史值查询
- 详情页聚合
- 整单覆盖保存
- 删除前校验与删除执行

#### `application/assembler`

负责：

- `DTO -> Domain`
- `Domain -> VO`
- `FieldEnum -> FieldEntity`

#### `domain`

继续承接：

- 领域对象
- 查询 `Pojo`
- 仓储接口

本次补齐：

- `CustomerBankAccount`
- `CustomerInvoiceProfile`

#### `infrastructure.persistence`

继续承接：

- `PO`
- `Mapper`
- `Convertor`
- `RepositoryImpl`

本次补齐：

- `customer_bank_account`
- `customer_invoice_profile`

## 5. 接口设计

### 5.1 控制器

新增：

- `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`

接口路径统一使用客户模块单控制器承载。

### 5.2 `/list`

**入参：** `CustomerListDTO extends ListBaseDTO`  
**回参：** `ListBaseVO<CustomerListItemVO>`

支持字段：

- 客户编码
- 客户名称
- 客户简称
- 客户分类
- 所属区域
- 归属销售
- 默认联系人
- 默认联系电话
- 默认地址摘要
- 默认开票抬头
- 业务状态
- 引用状态
- 创建时间
- 最后更新时间

首期以最小可用为目标：

- 先支持核心筛选和列表返回
- 分页沿用 `ListBaseDTO.pageNum/pageSize`
- `headList` 使用字段枚举生成

### 5.3 `/addItem`

**入参：** `BaseDTO`  
**回参：** `SaveItemVO<CustomerSaveItemVO>`

返回：

- 客户主档字段定义
- 联系人字段定义
- 地址字段定义
- 银行账户字段定义
- 开票信息字段定义
- 默认空数据结构

### 5.4 `/updateItem`

**入参：** `IdBaseDTO`  
**回参：** `SaveItemVO<CustomerSaveItemVO>`

返回：

- 与 `/addItem` 相同的字段定义
- 当前客户历史值
- 按编辑冻结规则计算后的 `editable`

### 5.5 `/save`

**入参：** `CustomerSaveDTO extends BaseDTO`  
**回参：** 统一成功结果对象，`data` 返回保存后的客户主档 `id`

`CustomerSaveDTO` 结构：

- `CustomerMainDTO main`
- `List<CustomerContactItemDTO> contacts`
- `List<CustomerAddressItemDTO> addresses`
- `List<CustomerBankAccountItemDTO> bankAccounts`
- `List<CustomerInvoiceProfileItemDTO> invoiceProfiles`

### 5.6 `/detail`

**入参：** `IdBaseDTO`  
**回参：** `CustomerDetailVO`

返回结构：

- `main`
- `contacts`
- `addresses`
- `bankAccounts`
- `invoiceProfiles`
- `referenceTodoSections`
- `operateLogTodoSections`

其中：

- `referenceTodoSections` 先返回空列表
- `operateLogTodoSections` 先返回空列表

### 5.7 `/delete`

**入参：** `BatchBaseDTO`  
**回参：** 统一成功结果对象

首期支持批量删除客户主档，但删除前必须完成：

- 引用校验
- 子资料可删校验
- 默认项规则校验

## 6. 字段枚举与表单结构

### 6.1 字段枚举

新增：

- `CustomerFieldEnum`

参考：

- `xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldDemoEnum.java`

统一维护以下字段元信息：

- 字段别名 `attr`
- 字段名称 `attrName`
- 字段类型 `fieldType`
- 必填规则
- 可编辑规则

### 6.2 字段分组策略

由于 `SaveItemVO.headList` 当前是单层 `List<FieldEntity>`，本次不改基础公共 VO，采用平铺字段 + 分组前缀命名：

- `main.customerCode`
- `main.customerName`
- `contacts.contactName`
- `addresses.detailAddress`
- `bankAccounts.accountNo`
- `invoiceProfiles.taxNo`

### 6.3 字段类型建议

优先复用已有 `FieldTypeEnum`：

- 文本
- 下拉
- 员工单选
- 日期/时间

本期不暴露附件字段。

### 6.4 `required` 与 `editable`

- `required`：仅标首期明确的核心必填字段
- `editable`：
  - `/addItem` 默认可编辑
  - `/updateItem` 按文档冻结规则计算

例如：

- 客户编码被正式引用后不可编辑
- 默认联系人 / 默认地址 / 默认开票信息仍可调整

## 7. 保存规则设计

### 7.1 新建 / 编辑判定

- `main.id == null`：新建
- `main.id != null`：编辑

### 7.2 整单覆盖规则

在 `/save` 中：

- 提交中带 `id` 的子项：更新
- 提交中不带 `id` 的子项：新增
- 数据库已存在、但本次提交未带回的子项：进入移除判定流程

### 7.3 子项移除判定

移除判定严格按文档执行：

1. 若该子项是默认项：不允许直接删除
2. 若该子项已形成历史快照：优先停用
3. 若未引用且满足条件：允许逻辑删除

### 7.4 默认项规则

以下四类子资料每类最多一条默认项：

- 联系人
- 地址
- 银行账户
- 开票信息

首期策略：

- 保存前校验默认项数量不得大于 1
- 若同类出现多条默认项，直接报错
- 不做自动纠偏

### 7.5 主档删除规则

`/delete` 删除客户主档时：

1. 先校验是否已被业务引用
2. 已引用：阻断删除，提示改为停用
3. 未引用：继续校验子资料默认项与删除条件
4. 满足条件：逻辑删除

### 7.6 状态规则

`/save` 允许直接保存：

- `DRAFT`
- `ENABLED`

后端仅校验状态值是否合法，不强制先落草稿。

## 8. 领域与持久化补齐范围

在现有三张表基础上，新增两张表的全套领域与持久化对象。

### 8.1 `customer_bank_account`

新增：

- `CustomerBankAccount`
- `CustomerBankAccountQueryPojo`
- `CustomerBankAccountRepository`
- `CustomerBankAccountPO`
- `CustomerBankAccountMapper`
- `CustomerBankAccountConvertor`
- `CustomerBankAccountRepositoryImpl`

建议字段：

- `id`
- `corpid`
- `customerId`
- `accountName`
- `bankName`
- `accountNo`
- `accountUsage`
- `defaultFlag`
- `bizStatus`
- `remark`
- `creatorId`
- `modifyId`
- `version`
- `del`
- `addTime`
- `updateTime`

### 8.2 `customer_invoice_profile`

新增：

- `CustomerInvoiceProfile`
- `CustomerInvoiceProfileQueryPojo`
- `CustomerInvoiceProfileRepository`
- `CustomerInvoiceProfilePO`
- `CustomerInvoiceProfileMapper`
- `CustomerInvoiceProfileConvertor`
- `CustomerInvoiceProfileRepositoryImpl`

建议字段：

- `id`
- `corpid`
- `customerId`
- `invoiceTitle`
- `taxNo`
- `addressPhone`
- `bankName`
- `bankAccountNo`
- `defaultFlag`
- `bizStatus`
- `remark`
- `creatorId`
- `modifyId`
- `version`
- `del`
- `addTime`
- `updateTime`

### 8.3 SQL 扩展

扩展：

- `docs/sql/2026-07-22-init-customer-module.sql`

新增两张表：

- `customer_bank_account`
- `customer_invoice_profile`

并补对应索引。

## 9. 关键类清单

### 9.1 Controller / Service / Assembler

- `CustomerAdminController`
- `CustomerAdminAppService`
- `CustomerAdminAssembler`
- `CustomerFieldAssembler`

### 9.2 DTO

- `CustomerListDTO`
- `CustomerSaveDTO`
- `CustomerMainDTO`
- `CustomerContactItemDTO`
- `CustomerAddressItemDTO`
- `CustomerBankAccountItemDTO`
- `CustomerInvoiceProfileItemDTO`

### 9.3 VO

- `CustomerListItemVO`
- `CustomerSaveItemVO`
- `CustomerDetailVO`

### 9.4 字段枚举

- `CustomerFieldEnum`

### 9.5 新增领域与持久化对象

- `CustomerBankAccount*`
- `CustomerInvoiceProfile*`

## 10. 测试策略

遵循 TDD：先写失败测试，再写最小实现。

### 10.1 接口层 / 应用层测试

优先补以下测试：

- `/list` 返回结构测试
- `/addItem` 字段定义与默认值测试
- `/updateItem` 历史值与可编辑状态测试
- `/save` 新建测试
- `/save` 编辑覆盖测试
- `/save` 默认项冲突测试
- `/save` 子项删除判定测试
- `/detail` 聚合结构测试
- `/delete` 引用阻断测试

### 10.2 持久化补齐测试

补两张新表的：

- 领域模型测试
- `PO` 测试
- 转换器测试
- 仓储接口签名测试
- 仓储实现测试

### 10.3 回归测试

至少回归：

- `xbb-erp-module-customer` 模块测试
- 与 `BaseEntity` / 审计自动填充相关的基础持久化测试

## 11. 文档同步要求

实现时同步维护：

- `docs/api` 中客户模块接口文档
- `docs/sql/2026-07-22-init-customer-module.sql`

领域名称与文档维护方式遵循：

- `.claude/commands/init-crud-ctrl/cal-domain.md`

## 12. 边界与留白

### 12.1 本期明确不做

- 附件关联与上传
- 引用与上下游真实数据
- 操作流水真实数据
- 独立启用/停用接口
- 自动默认项纠偏
- 审计日志落表
- 外部模块真实联动

### 12.2 留白结构约定

以下内容在接口结构中预留位置，但首期统一返回空列表，不实现真实能力：

- 引用与上下游
- 操作流水

## 13. 结论

本次客户模块基础 CRUD 接口实现采用“单控制器 + 单保存 DTO + 聚合式保存应用服务”方案，以最小但完整的方式覆盖客户主档与四类核心子资料，并以整单覆盖保存支撑 ERP 主数据场景。

接口层、应用层、领域层、持久化层边界清晰；后续即便继续补启停用、引用校验、审计留痕，也能在当前结构上自然扩展。