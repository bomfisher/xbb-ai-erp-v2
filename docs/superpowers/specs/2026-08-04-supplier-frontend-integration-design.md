# supplier 模块前端接入设计

## 背景

当前后端 `xbb-erp-module-supplier` 已具备供应商管理管理端接口，覆盖：

- 列表 `list`
- 新建初始化 `addItem`
- 编辑加载 `updateItem`
- 草稿保存 `saveDraft`
- 草稿列表 `draftList`
- 草稿恢复 `loadDraft`
- 正式提交 `saveAndSubmit`
- 详情 `detail`
- 删除 `delete`

前端管理后台当前已完成：

- 客户模块列表 + 侧开抽屉 + 草稿流
- 商品模块列表页
- 菜单工作区按 `componentPath` 注册页面后自动渲染

本次用户明确要求将供应商模块接入前端，已给定：

- `routePath`：`/management/master/suppliers`
- `componentPath`：`management/master/supplier/list`

同时用户已确认以下约束：

- 不创建 `worktree`
- 按后端现有接口全量接入前端能力
- `detail` 接口本期前端不暴露入口
- 新建 / 编辑 / 草稿使用侧开抽屉
- 首期按后端 `headList + data/ext/sectionState` 原样渲染，样式复用现有客户模块，不做额外 UI 定制

## 本次目标

### 目标 1：让菜单页真正进入供应商页面

当前菜单工作区只有当 `componentPath` 在页面注册表中存在时，才会渲染真实页面；否则只显示“页面内容待后续接入”。

本次需要将 `management/master/supplier/list` 注册为真实页面，并与路由 `/management/master/suppliers` 对齐。

### 目标 2：补齐供应商列表页

供应商列表页要与客户列表页保持一致的交互结构：

- 公共筛选区
- 顶部操作按钮区
- 列表数据区
- 行操作区
- 分页区
- 底部按钮区

列表数据来自：

- 公共列表协议 `/erp/v1/common/list/*`
- 供应商业务列表 `/erp/v1/supplier/list`

### 目标 3：补齐供应商侧开抽屉全流程

供应商抽屉本期承接：

- 新建空白表单加载 `addItem`
- 编辑表单加载 `updateItem`
- 草稿保存 `saveDraft`
- 草稿列表 `draftList`
- 草稿恢复 `loadDraft`
- 正式提交 `saveAndSubmit`

抽屉内部继续复用通用动态表单能力，不设计供应商专属页面布局。

### 目标 4：不在前端暴露 detail

虽然已有 `/erp/v1/supplier/detail`，但用户明确要求本期前端不接 `detail` 入口。

这意味着：

- 不新增详情页路由
- 不新增详情按钮
- 不在列表或抽屉里消费 `detail`

## 明确不做的事

- 不创建供应商独立详情页
- 不新增详情抽屉
- 不对客户模块做公共抽象重构
- 不为了供应商接入去重写通用表单组件
- 不基于 `doc/UI` 做供应商专属视觉贴合
- 不添加当前后端未提供的新接口
- 不凭空添加删除入口；只有当公共按钮或行操作真实返回删除动作时才接入删除调用

## 推荐方案

本次采用方案 A：按客户模块现有模式等价实现一个供应商版本。

采用该方案的原因：

- 供应商接口协议与客户模块足够接近，尤其都是 `main + ext + sectionState + draftMeta` 的表单语义。
- 前端已有完整参考实现，直接等价实现风险最低。
- 用户要求的是尽快把供应商模块接入前端，而不是顺手抽象出更高层框架。
- 若此时抽公共主数据模块，会把已稳定的客户模块也拖入重构，范围不必要扩大。

因此本次不采用“先抽一层通用主档框架”的路线，而采用“供应商按客户范式落地”的策略。

## 页面与文件设计

### 1. 路由与页面注册

需要补齐两个入口：

1. 路由层注册真实页面
2. 页面注册表登记 `componentPath`

目标映射关系：

- `routePath`：`/management/master/suppliers`
- `componentPath`：`management/master/supplier/list`
- 页面组件：`SupplierListPage`

落点建议：

- 修改 `apps/admin-web/src/router.ts`
- 修改 `apps/admin-web/src/pageRegistry.ts`

这样菜单工作区在选中供应商菜单叶子节点后，会像客户页一样进入真实页面，而不是停留在占位卡片。

### 2. 文件落点

本次新增 / 修改文件建议如下：

- 新增 `apps/admin-web/src/modules/supplier/list/SupplierListPage.vue`
- 新增 `apps/admin-web/src/modules/supplier/list/SupplierCreateDrawer.vue`
- 新增 `apps/admin-web/src/modules/supplier/list/supplierCreate.ts`
- 新增 `apps/admin-web/src/modules/supplier/list/SupplierListPage.spec.ts`
- 修改 `apps/admin-web/src/router.ts`
- 修改 `apps/admin-web/src/pageRegistry.ts`

如果后续发现纯转换逻辑较多，可再补：

- `apps/admin-web/src/modules/supplier/list/supplierCreate.spec.ts`

但这不是必选项，优先看 `SupplierListPage.spec.ts` 是否已经足够覆盖转换行为。

## 列表页设计

### 1. 页面职责

`SupplierListPage` 负责：

- 加载公共列表元数据
- 查询供应商列表
- 响应筛选提交与重置
- 响应顶部 `ADD`
- 响应行级 `EDIT`
- 在保存成功后刷新列表
- 在组件内持有抽屉打开状态与编辑中的供应商 ID

不负责：

- 直接处理草稿恢复协议细节
- 直接拼装提交载荷
- 处理详情展示

这些职责都应下沉到抽屉组件与 `supplierCreate.ts`。

### 2. 页面结构

页面结构直接对齐客户列表页：

- `ListPageLayout`
- `ListFilterPanel`
- `ListDataTable`
- `ListPagination`
- `ListPageFooterActions`
- `ListTableHeaderActions`
- `SupplierCreateDrawer`

这保证供应商页面和客户页面在页面心智、交互位置与测试模式上保持一致。

### 3. 启动加载流程

页面挂载后并发请求：

- `/erp/v1/common/list/filter`
- `/erp/v1/common/list/header`
- `/erp/v1/common/list/topButton`
- `/erp/v1/common/list/bottomButton`
- `/erp/v1/common/list/rowAction`

随后再请求：

- `/erp/v1/supplier/list`

业务上下文保持与现有页面一致，至少包含：

- `corpid`
- `userId`
- `businessCode`

其中 `businessCode` 改为供应商对应值，用于拉取公共列表元数据。

### 4. 列表查询协议

列表请求路径：`POST /erp/v1/supplier/list`

请求体基础字段：

- `corpid`
- `userId`
- `pageNum`
- `pageSize`

筛选字段按页面当前状态透传到供应商列表查询 DTO。若当前 `ListFilterPanel` 仍输出统一 `keyword + conditions` 结构，则优先复用现有客户模块实现方式；若供应商列表后端只接受离散字段，则在页面层做一层轻量映射，但不改公共筛选组件。

### 5. 列表字段映射

`ListDataTable` 渲染时，继续使用 `renderCell(row, attr)` 做 `headList.attr` 到列表行字段的映射。

建议首期映射至少覆盖：

- `main.supplierCode -> supplierCode`
- `main.supplierName -> supplierName`
- `main.supplierShortName -> supplierShortName`
- `main.supplierCategory -> supplierCategory`
- `main.mainBusinessCategory -> mainBusinessCategory`
- `main.ownerPurchaserId -> ownerPurchaserNameSnapshot`
- `main.bizStatus -> bizStatus`
- `main.refStatus -> refStatus`
- `main.addTime -> addTime`
- `main.updateTime -> updateTime`

若公共表头返回更多字段而当前列表响应未包含，则显示 `—`，不提前添加猜测字段。

### 6. 列表动作

顶部动作：

- 遇到 `buttonCode=ADD` 或 `actionCode=ADD` 时打开新建抽屉

行级动作：

- 遇到 `actionCode=EDIT` 时打开编辑抽屉
- 若真实返回删除动作，可再消费删除逻辑

本期不因为“供应商应该支持删除”就手工额外写死删除按钮，一切以前端公共元数据返回结果为准。

## 抽屉设计

### 1. 抽屉职责

`SupplierCreateDrawer` 负责：

- 根据 `mode=create/edit` 加载不同初始化载荷
- 复用动态表单渲染 `headList + data`
- 响应保存草稿
- 响应查看草稿列表
- 响应草稿恢复
- 响应正式保存并提交
- 在动作失败时展示短提示

### 2. 打开加载流程

当抽屉 `open=true` 时：

- `mode=create` 请求 `POST /erp/v1/supplier/addItem`
- `mode=edit` 请求 `POST /erp/v1/supplier/updateItem`

编辑态请求体需包含：

- `corpid`
- `userId`
- `id`

请求成功后，将响应 `data` 交给通用表单归一化逻辑，再渲染到 `DynamicFormSection`。

### 3. 渲染方式

本期不做供应商专属表单布局拆分。

直接复用当前通用动态表单：

- `headList` 决定字段定义
- `formData.main` 承接主档字段
- `formData.contacts / addresses / bankAccounts / invoiceProfiles` 承接可选子档数组
- `formData.sectionState` 控制可选区块启停

这样能最大化沿用客户模块已验证过的通用表单行为。

### 4. 抽屉动作区

抽屉头部保留 3 个动作：

- `查看草稿`
- `保存草稿`
- `正式保存`

动作状态约束：

- 页面加载中时动作禁用
- 正在保存 / 恢复草稿时动作禁用
- 动作失败时展示 toast，并允许用户继续操作

### 5. 错误状态

抽屉保留两类错误：

- `loadErrorMessage`：加载 `addItem/updateItem` 失败
- `actionErrorMessage`：保存草稿 / 恢复草稿 / 正式提交失败

加载失败时显示抽屉错误态；动作失败时显示短时提示，不直接关闭抽屉。

## supplierCreate.ts 设计

### 1. 文件职责

`supplierCreate.ts` 负责供应商抽屉相关接口封装与协议转换。

它是协议边界层，页面不应直接拼装提交体或直接解析草稿响应。

### 2. 建议导出函数

建议导出以下函数：

- `fetchSupplierCreatePayload()`
- `fetchSupplierUpdatePayload(id)`
- `saveSupplierDraft(formData, draftMeta)`
- `saveSupplierAndSubmit(formData, draftMeta)`
- `fetchSupplierDraftList()`
- `loadSupplierDraft(draftCode)`
- `toSupplierSubmitPayload(formData, draftMeta)`
- `fromSupplierDraftDetail(detail)`
- `compactSectionRows(rows)`

### 3. 业务上下文

文件内定义固定业务上下文：

- `corpid`
- `userId`

如客户模块当前未显式带 `businessCode` 到抽屉接口，则供应商模块也不额外添加，保持一致。

### 4. 保存与提交协议

`toSupplierSubmitPayload()` 统一生成以下结构：

- `corpid`
- `userId`
- `main`
- `ext.contacts`
- `ext.addresses`
- `ext.bankAccounts`
- `ext.invoiceProfiles`
- `sectionState`
- `draftMeta`

规则：

- 当某个 section 的开关值为 `1` 时，提交压缩后的有效行
- 当某个 section 的开关值为 `0` 时，提交空数组
- 空白行通过 `compactSectionRows()` 过滤

### 5. 草稿恢复协议

根据后端文档，供应商草稿加载返回：

- `main`
- `ext`
- `sectionState`
- `draftMeta`

因此 `fromSupplierDraftDetail()` 应负责把草稿详情转换为通用 `SaveItemFormData`，并同时产出草稿元信息，供抽屉本地状态使用。

### 6. HTTP 封装

模块内部保留 `postSupplierJson()`：

- 统一 `POST`
- 统一 `Content-Type: application/json`
- 统一处理外层返回包装 `ApiEnvelope<T>`
- 遇到 `response.ok=false` 或 `success=false` 时抛出错误

不把这些协议判断散到页面文件里。

## 接口消费范围

### 本期消费的接口

- `POST /erp/v1/supplier/list`
- `POST /erp/v1/supplier/addItem`
- `POST /erp/v1/supplier/updateItem`
- `POST /erp/v1/supplier/saveDraft`
- `POST /erp/v1/supplier/saveAndSubmit`
- `POST /erp/v1/supplier/draftList`
- `POST /erp/v1/supplier/loadDraft`
- `POST /erp/v1/supplier/delete`（仅在公共按钮 / 行操作真实返回删除动作时）

### 本期明确不消费的接口

- `POST /erp/v1/supplier/detail`

## 删除能力设计

删除能力不是本次核心目标，但因为后端已有 `/delete`，需要明确接入边界。

设计原则：

- 如果公共元数据没有返回删除动作，则本期不手动创建删除按钮
- 如果公共元数据返回删除动作，则前端沿用客户模块当前的确认式交互
- 删除成功后刷新第一页列表
- 删除失败时展示错误提示，不额外做复杂兜底

这样既不遗漏已有后端能力，也不在未确认公共协议前提下擅自增加前端入口。

## 类型设计

列表页内部需要定义供应商列表项类型，至少覆盖当前后端响应字段：

- `id`
- `supplierCode`
- `supplierName`
- `supplierShortName`
- `supplierCategory`
- `mainBusinessCategory`
- `ownerPurchaserNameSnapshot`
- `bizStatus`
- `refStatus`
- `addTime`
- `updateTime`

草稿相关类型建议在 `supplierCreate.ts` 局部定义：

- `SupplierDraftMeta`
- `SupplierDraftListItem`
- `SupplierDraftDetail`
- `SupplierDraftSaveResult`
- `SupplierDrawerMode`

这些类型不需要上提为全局共享类型，避免过早抽象。

## 测试设计

### 1. 主测试文件

优先新增 `apps/admin-web/src/modules/supplier/list/SupplierListPage.spec.ts`，参照客户模块测试方式，覆盖页面与抽屉联动主流程。

### 2. 必测场景

至少覆盖以下场景：

1. 加载公共列表元数据与供应商列表成功
2. 点击 `ADD` 打开抽屉并请求 `/erp/v1/supplier/addItem`
3. 点击 `EDIT` 打开抽屉并请求 `/erp/v1/supplier/updateItem`
4. 抽屉新建态正确回填 `main` 与子档默认值
5. 抽屉编辑态正确回填已有供应商数据
6. 抽屉加载失败时显示错误态，关闭后重新打开可重新请求
7. 点击保存草稿时请求 `/erp/v1/supplier/saveDraft`
8. 保存草稿请求体正确携带 `ext + sectionState + draftMeta`
9. 点击正式保存时请求 `/erp/v1/supplier/saveAndSubmit`
10. 点击查看草稿时请求 `/erp/v1/supplier/draftList`
11. 点击草稿项时请求 `/erp/v1/supplier/loadDraft` 并回填表单
12. 若接入删除，则覆盖删除确认、删除请求和列表刷新

### 3. 转换函数测试

如果 `SupplierListPage.spec.ts` 中已完整断言提交体与草稿回填结果，可以不单独新增 `supplierCreate.spec.ts`。

如果后续发现以下逻辑断言过重、页面测试难读，则再拆纯函数测试：

- `compactSectionRows()`
- `toSupplierSubmitPayload()`
- `fromSupplierDraftDetail()`

## 数据流总结

### 列表页数据流

1. 页面挂载
2. 并发拉取公共元数据
3. 请求 `/erp/v1/supplier/list`
4. 点击筛选提交 / 重置时重新请求列表
5. 点击 `ADD` 打开新建抽屉
6. 点击 `EDIT` 打开编辑抽屉
7. 抽屉保存成功后回到列表并刷新第一页

### 抽屉数据流

1. 抽屉打开
2. 根据 `mode` 请求 `addItem` 或 `updateItem`
3. 通用动态表单渲染 `headList + formData`
4. 用户可直接正式保存，或先保存草稿
5. 点击查看草稿时拉取 `draftList`
6. 选择草稿后请求 `loadDraft`
7. 草稿内容回填 `formData + draftMeta`
8. 正式保存成功后关闭抽屉并通知列表刷新

## 风险与处理

### 风险 1：供应商 `headList.fieldType` 与现有动态表单映射不完全一致

处理方式：

- 先按当前通用表单能力接入
- 若测试时发现供应商实际字段类型未被支持，再在现有字段类型解析逻辑上做最小兼容补丁
- 只补供应商实际出现的类型，不提前扩展大而全映射

### 风险 2：公共筛选条件与供应商列表 DTO 不完全一致

处理方式：

- 优先复用客户页面的 `keyword + conditions` 透传模式
- 若后端供应商列表确实要求离散字段，则仅在 `SupplierListPage` 内做轻量映射，不改公共筛选组件

### 风险 3：删除入口来源不确定

处理方式：

- 删除能力以公共元数据实际返回为准
- 设计和测试中给出“若返回则接入”的清晰边界
- 不在无元数据支撑时手工创造删除按钮

## 验收标准

本次完成后，应满足以下结果：

1. 菜单命中 `management/master/supplier/list` 时进入真实供应商页面
2. 路由 `/management/master/suppliers` 能正常渲染供应商列表页
3. 供应商列表可加载公共元数据与业务列表数据
4. 点击顶部 `ADD` 可打开侧开新建抽屉并拉取 `addItem`
5. 点击行 `EDIT` 可打开侧开编辑抽屉并拉取 `updateItem`
6. 抽屉中可保存草稿、查看草稿、恢复草稿、正式保存
7. 正式保存成功后抽屉关闭并刷新列表
8. 前端不暴露 `detail` 入口
9. 整体页面交互风格与客户模块保持一致
10. 相关 Vitest 用例通过
