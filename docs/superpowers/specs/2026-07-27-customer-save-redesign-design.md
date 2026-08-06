# 客户保存接口重写设计文档

## 1. 文档信息

| 项目 | 内容 |
|---|---|
| 文档名称 | 客户保存接口重写设计文档 |
| 所属系统 | 进销存系统 |
| 所属模块 | 客户模块 |
| 关联接口 | `CustomerAdminController#saveDraft` `CustomerAdminController#saveAndSubmit` `CustomerAdminController#draftList` `CustomerAdminController#loadDraft` |
| 对应实现 | `xbb-erp-module-customer` |
| 版本号 | V1.0 |
| 创建日期 | 2026-07-27 |
| 最后更新日期 | 2026-07-27 |

## 2. 背景与目标

当前客户模块保存能力集中在 `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java:42` 与 `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java:164`：

- 对外只有单一 `save` 接口
- 返回值为裸 `Long`
- 保存流程未区分草稿保存与正式保存
- 校验、持久化、子表同步耦合在单一方法中
- 前端无法在不暴露后端主键的前提下完成草稿续编

本次重写目标：

- 删除旧 `/save`，拆分为 `saveDraft` 与 `saveAndSubmit`
- 新增 `draftList` 与 `loadDraft`，形成草稿闭环
- 保存接口统一使用 `ResultVO.success()` 包装，保存结果使用 `BaseVO`，暂不暴露后端 `id`
- 吸收《单据保存接口与校验体系设计文档》的分层思想，但不把客户主数据强行建模为标准单据 `lines`
- 为客户场景建立“主档 + 扩展子表 + 草稿元数据”的特化统一壳
- 将校验、草稿缓存、正式持久化拆分为清晰职责

## 3. 设计结论

### 3.1 接口结论

删除：

- `POST /erp/v1/customer/save`

新增：

- `POST /erp/v1/customer/saveDraft`
- `POST /erp/v1/customer/saveAndSubmit`
- `POST /erp/v1/customer/draftList`
- `POST /erp/v1/customer/loadDraft`

其中：

- `saveDraft`：保存客户编辑态到 Redis 草稿池
- `saveAndSubmit`：执行完整校验并正式落库客户主档与子表
- `draftList`：查询最近 10 条草稿摘要
- `loadDraft`：按草稿标识加载完整草稿

### 3.2 返回结论

- 四个接口都使用 `ResultVO.success()` 包装
- `saveDraft` 与 `saveAndSubmit` 返回 `BaseVO`
- 当前阶段不向前端暴露后端数据库主键 `id`
- 草稿续编通过 `draftCode` 完成，而不是通过客户 `id`

### 3.3 DTO 结论

客户不是典型交易单据，不适合硬套 `header + lines + ext` 中的 `lines` 模型。

本次采用客户特化请求壳：

- `main`：客户主档
- `ext`：`contacts`、`addresses`、`bankAccounts`、`invoiceProfiles`
- `draftMeta`：草稿元数据，例如 `draftCode`、草稿标题、最近编辑时间

本次不再让正式保存 DTO 直接复用旧 `CustomerSaveDTO` 作为最终契约，而是演进为围绕上述统一壳思想的新 DTO 结构。

## 4. 接口与 DTO 设计

### 4.1 `saveDraft`

**URL**：`POST /erp/v1/customer/saveDraft`

**入参**：`CustomerDraftSaveDTO extends BaseDTO`

建议字段：

- `main`：客户主档编辑态
- `ext.contacts`
- `ext.addresses`
- `ext.bankAccounts`
- `ext.invoiceProfiles`
- `draftMeta.draftCode`
- `draftMeta.draftTitle`

**回参**：`ResultVO<BaseVO>`

说明：

- 若请求未传 `draftCode`，后端生成新的 `draftCode`
- 返回仍使用 `BaseVO` 成功结果，不透出后端 `id`
- 若项目现有 `BaseVO` 无法承载提示信息，则只返回统一成功；草稿续编标识以草稿列表与加载接口为准

### 4.2 `saveAndSubmit`

**URL**：`POST /erp/v1/customer/saveAndSubmit`

**入参**：`CustomerSubmitSaveDTO extends BaseDTO`

建议字段：

- `main`
- `ext.contacts`
- `ext.addresses`
- `ext.bankAccounts`
- `ext.invoiceProfiles`
- `draftMeta.draftCode`（可选，若来源于草稿则传入）

**回参**：`ResultVO<BaseVO>`

说明：

- 正式保存成功后，若存在 `draftCode`，删除对应草稿
- 不返回客户 `id`

### 4.3 `draftList`

**URL**：`POST /erp/v1/customer/draftList`

**入参**：`CustomerDraftListDTO extends BaseDTO`

建议字段：

- 无额外筛选字段，首期按当前公司取最近 10 条

**回参**：`ResultVO<List<CustomerDraftListItemVO>>`

建议摘要字段：

- `draftCode`
- `draftTitle`
- `customerName`
- `customerCode`
- `updatedTime`

### 4.4 `loadDraft`

**URL**：`POST /erp/v1/customer/loadDraft`

**入参**：`CustomerDraftLoadDTO extends BaseDTO`

建议字段：

- `draftCode`

**回参**：`ResultVO<CustomerDraftDetailVO>`

返回完整草稿编辑态：

- `main`
- `ext.contacts`
- `ext.addresses`
- `ext.bankAccounts`
- `ext.invoiceProfiles`
- `draftMeta`

## 5. 保存流程与校验分层

### 5.1 `saveDraft` 流程

```text
Controller
  -> AppService.saveDraft()
    -> 构建客户保存上下文
    -> 协议校验
    -> 轻量通用校验
    -> 生成或复用 draftCode
    -> 写入 Redis 草稿内容
    -> 更新最近草稿索引
    -> 返回成功
```

特点：

- 允许部分字段为空
- 不做完整业务必填校验
- 不落正式 MySQL 主表与子表
- 刷新 Redis 过期时间到 7 天

### 5.2 `saveAndSubmit` 流程

```text
Controller
  -> AppService.saveAndSubmit()
    -> 构建客户保存上下文
    -> 协议校验
    -> 完整通用校验
    -> 完整业务校验
    -> 保存客户主表
    -> 同步联系人/地址/银行账户/开票信息子表
    -> 若来源于草稿则删除草稿内容与索引
    -> 返回成功
```

特点：

- 必须满足完整必填与业务规则
- 按正式保存路径落库
- 成功后移除已使用草稿

### 5.3 校验分层职责

建议拆分为三层：

1. 协议校验
2. 通用校验
3. 业务校验

#### 协议校验

只判断请求结构是否合法，例如：

- 请求对象不能为空
- `main` 不能为空
- 子表集合结构合法
- 字段长度、枚举、数值格式正确

#### 通用校验

只判断客户保存共性规则，例如：

- 正式保存时关键字段必填
- 默认联系人只能一个
- 默认地址只能一个
- 默认银行账户只能一个
- 默认开票信息只能一个

#### 业务校验

只判断客户领域规则，例如：

- 客户编码唯一性
- 客户名称唯一性
- 状态是否允许变更
- 子表数据间的领域约束是否成立

### 5.4 双严格度策略

- `saveDraft`：协议校验 + 轻量通用校验
- `saveAndSubmit`：协议校验 + 完整通用校验 + 完整业务校验

本次需要通过测试明确证明这两条链路严格度不同，而不是只更换接口名称。

## 6. Redis 草稿设计

### 6.1 存储模型

建议拆成两层键：

#### 草稿内容键

- `customer:draft:{corpid}:{draftCode}`

值为完整草稿内容，包含：

- `main`
- `ext`
- `draftMeta`

TTL：`7 天`

#### 最近草稿索引键

- `customer:draft:index:{corpid}`

用途：

- 按最近编辑时间维护草稿顺序
- `draftList` 只返回最近 `10` 条

### 6.2 草稿标识策略

- 草稿通过独立 `draftCode` 识别
- `draftCode` 对前端可见
- `draftCode` 不等于客户正式主键
- 前端加载、覆盖草稿、正式提交时均以 `draftCode` 为唯一标识

### 6.3 草稿行为规则

- 首次保存草稿：生成新的 `draftCode`
- 覆盖保存草稿：复用 `draftCode`，覆盖内容并刷新 TTL
- 查询草稿：返回最近 10 条摘要
- 加载草稿：按 `draftCode` 返回完整草稿
- 正式保存成功：删除草稿内容与索引项

## 7. 代码落点与职责拆分

### 7.1 控制器

修改 `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java`：

- 删除 `save`
- 新增 `saveDraft`
- 新增 `saveAndSubmit`
- 新增 `draftList`
- 新增 `loadDraft`

### 7.2 应用服务接口

修改 `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java`：

- 移除 `save(CustomerSaveDTO dto)`
- 新增 `saveDraft(...)`
- 新增 `saveAndSubmit(...)`
- 新增 `draftList(...)`
- 新增 `loadDraft(...)`

### 7.3 应用编排层

避免继续把所有逻辑堆入单一 `save` 方法。

建议拆分出：

- 草稿保存编排方法
- 正式提交编排方法
- 构建上下文方法

允许保留在同一个应用服务实现类中，但职责必须清晰分段。

### 7.4 校验器

建议新增：

- `CustomerSaveProtocolValidator`
- `CustomerSaveCommonValidator`
- `CustomerSaveBusinessValidator`

### 7.5 草稿缓存网关

建议新增独立草稿缓存访问层，例如：

- `CustomerDraftRepository`
- 或 `CustomerDraftCacheGateway`

职责：

- 统一生成 Redis key
- 保存草稿
- 查询最近草稿
- 加载草稿
- 删除草稿

### 7.6 装配器

现有 `CustomerAdminAssembler` 可以继续负责正式领域对象装配，但需要补充：

- 草稿 DTO 与 Redis 草稿对象的转换
- 草稿摘要 VO 装配
- 草稿详情 VO 装配

## 8. 测试与验收

### 8.1 控制器结构测试

更新 `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/admin/CustomerAdminControllerStructureTest.java`，校验：

- 旧 `save` 不存在
- 新 `saveDraft` 存在
- 新 `saveAndSubmit` 存在
- 新 `draftList` 存在
- 新 `loadDraft` 存在

### 8.2 草稿行为测试

至少覆盖：

- 首次 `saveDraft` 生成草稿
- 二次 `saveDraft` 覆盖同一草稿
- `draftList` 只返回最近 10 条
- `loadDraft` 返回完整草稿
- `saveAndSubmit` 成功后删除草稿

### 8.3 正式保存测试

至少覆盖：

- 正式保存客户主表成功
- 四类子表同步成功
- 默认项唯一性校验仍成立
- 客户编码/名称相关业务规则仍成立

### 8.4 双严格度测试

至少覆盖：

- 同一份缺关键字段的数据可以 `saveDraft`
- 同一份数据不能 `saveAndSubmit`

## 9. 范围边界

本次设计负责：

- 客户保存双接口拆分
- 草稿列表与加载闭环
- Redis 草稿缓存方案
- 校验分层
- 控制器与应用服务重构方向

本次设计不负责：

- 前端页面具体交互实现
- Redis 集群/序列化底层治理细节
- 客户引用链查询与操作日志完整功能补齐
- 其它主数据模块统一抽象的立即推广

## 10. 最终结论

本次客户保存接口重写采用“客户特化版统一壳”方案：

1. 删除旧 `/save`
2. 新增 `saveDraft`、`saveAndSubmit`、`draftList`、`loadDraft`
3. 保存接口返回 `ResultVO.success(BaseVO)`，暂不暴露后端 `id`
4. 请求体采用 `main + ext + draftMeta` 结构
5. 草稿存 Redis，最近 10 条，TTL 7 天
6. 正式保存成功后移除已使用草稿
7. 草稿保存与正式保存使用不同严格度校验
8. 将保存流程拆分为编排、校验、草稿缓存三个清晰职责

该方案既吸收统一保存设计文档中的分层思想，又保留客户主数据“主档 + 多子表”的天然结构，能够在不暴露后端主键的前提下，完成客户草稿与正式保存的完整闭环。