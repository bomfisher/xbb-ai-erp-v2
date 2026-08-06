# 跨模块新增保存基础字段补齐设计

## 目标

将 `customer` 已确认的“新增保存时基础字段未补齐”问题，按相同修复思路同步排查到 `supplier`、`product`、`purchase` 模块，并且只修复**新增保存分支**中的缺口。

本次设计只覆盖以下基础字段在新增保存时的补齐逻辑：
- `bizStatus`
- `version`
- `del` / `deleted`
- `addTime`
- `updateTime`
- `creatorId`
- `modifyId`

## 范围边界

### 包含范围

- `supplier` 模块中与 `customer` 同构的整单保存链路
- `product` 模块中的各个 `save(...)` 入口
- `purchase` 模块中的各个 `save(...)` 入口
- 仅处理“新增保存”场景，即对象 `id == null` 或等价的新增判定分支
- 仅处理基础字段缺失时的补齐，不覆盖调用方已显式传入的值

### 不包含范围

- 不修改 `update` 分支
- 不修改 `remove` / `delete` 分支
- 不将默认值补齐逻辑下沉到 repository 统一兜底
- 不新增跨模块公共抽象
- 不借机调整接口出入参、URL 或数据库结构
- 不处理与本次问题无关的格式化、重构或行为变更

## 设计原则

### 1. 修复位置与 `customer` 保持一致

默认值补齐优先落在各模块自己的 `AdminAppServiceImpl` 或保存编排层，而不是 repository 层。

这样做的原因：
- 与 `customer` 已落地方案保持一致，认知成本最低
- 只影响整单保存入口，不扩大到仓储层的所有调用方
- 更容易按“新增分支”精确控制范围

### 2. 只补缺，不覆盖

本次修复是“补齐缺失字段”，不是统一重写字段值。

具体规则：
- 字段为空时才补默认值
- 调用方已传值时保持原值
- 新增分支与更新分支严格分离

### 3. 命中才修，不预设全模块都有问题

`supplier` 高优先级，因为它和 `customer` 的主档 + 子档结构高度同构。

`product` 和 `purchase` 不预设一定存在同类缺口，而是逐个检查它们的 `save(...)` 入口：
- 若新增分支已补齐基础字段，则不改
- 若新增分支直接落库且字段可能为空，则按本设计补齐

## 模块级处理策略

### supplier

作为最高优先级模块，重点检查 `VendorAdminAppServiceImpl` 及其关联子档新增保存分支：
- 联系人
- 地址
- 银行账户
- 开票信息

预期修复方式与 `customer` 相同：
- 在新增子档分支 insert 前补齐基础字段
- 不改子档 update / remove 行为

### product

检查各个 `save(...)` 入口的新增分支，包括但不限于：
- `Warehouse`
- `ProductBrand`
- `ProductCategory`
- `ProductUnit`
- `ProductSpu`
- `ProductSku`

如果这些入口在新增分支直接调用 repository 保存，但领域对象基础字段未在 service 层补齐，则按本设计补齐。

### purchase

检查各个 `save(...)` 入口的新增分支，包括但不限于：
- `PurchaseRequest`
- `PurchaseRequestItem`
- `PurchaseOrder`
- `PurchaseOrderItem`
- `PurchasePendingTask`
- `PurchaseSourceRelation`

同样遵循“只查新增、命中才修”的原则。

## 字段补齐规则

对每个命中的新增保存分支，按以下规则处理：

- `bizStatus`：为空时补默认业务状态
- `version`：为空时补 `0`
- `del` / `deleted`：为空时补 `0`
- `addTime`：为空时补当前时间戳
- `updateTime`：为空时补当前时间戳
- `creatorId`：为空时补当前操作人 `userId`
- `modifyId`：为空时补当前操作人 `userId`

注意事项：
- 字段命名以各模块现有模型为准，例如有的模块使用 `del`，有的使用 `deleted`
- `bizStatus` 的默认值以模块现有业务约定为准；如果模块内已有同类主档默认值规则，则沿用现有约定
- 不新增额外防御逻辑，不扩展到非边界场景

## 测试策略

本次采用最小回归测试策略，按命中模块补测试。

### 测试原则

- 先写失败用例，证明新增保存时基础字段未被补齐
- 再做最小实现修复
- 最后运行对应测试类验证通过

### 测试内容

每个命中模块的新增保存测试至少校验：
- 保存后对象已生成主键（若当前保存路径负责生成）
- `bizStatus` 已补齐
- `version` 已补齐为 `0`
- `del` / `deleted` 已补齐为 `0`
- `addTime` / `updateTime` 非空
- `creatorId` / `modifyId` 已补齐为当前 `userId`

### 验证范围

- 优先跑对应测试类
- 不扩大到无关模块的全量测试
- 如果模块已有内存仓储或轻量测试支撑，优先复用现有测试模式

## 实施顺序

推荐按以下顺序执行：
1. `supplier`
2. `product`
3. `purchase`

原因：
- `supplier` 与 `customer` 结构最接近，能最快复用修复经验
- `product` / `purchase` 的保存入口更多，适合在规则已经稳定后继续扫描

## 风险与控制

### 风险

- 不同模块的字段名存在差异（`del` vs `deleted`）
- 不同模块的 `bizStatus` 默认值约定可能不同
- 某些保存入口虽然叫 `save`，但不一定承担完整新增初始化职责

### 控制方式

- 逐模块读取当前实现，不做跨模块假设
- 只在现有新增分支中补缺，不改 repository 语义
- 每个命中点补回归测试，防止修复漂移

## 产出物

本次设计对应的实现应只产出：
- 命中模块的最小代码修复
- 对应回归测试

如果后续需要统一沉淀成跨模块规范或抽公共能力，应另起设计，不在本次范围内。
