# 库存与成本查询设计

## 适用范围与现状

本文规划 `xbb-erp-module-inventory` 的产品（SKU）+ 仓库库存成本查询能力，实施顺序固定为：实时查询、日结/月结、插单与重算。执行结构以 Flyway 迁移为准；本文仅定义后续迁移与用例的设计事实。

当前 `stock_balance` 已按 `corpid + warehouse_id + sku_id` 唯一保存数量、锁定数量、可用数量、总成本与单位成本。数量流水和成本流水已有 `occurred_at`，但其 `*_before`、`*_after` 是根据记账当时的当前余额计算的结果。因此，当前结构可支持实时查询，不能在允许追溯插单后直接作为可靠历史成本的事实来源。

## 统一口径

- 库存维度：本阶段固定为 `corpid + warehouse_id + sku_id`。
- 实时库存：当前已成功入账的 `stock_balance`；`qty` 是实存，`available_qty` 是可用，`locked_qty` 是锁定。
- 历史库存：目标业务日结束（`[00:00:00, 次日 00:00:00)`）的期末余额；查询筛选必须施加于该期末结果。
- 成本法：当前采用移动加权平均；历史金额和单位成本以该日有效计算版本为准。
- 时间：每笔业务同时保存业务生效时间 `effective_at` 和实际过账时间 `posted_at`。前者决定历史归属和成本回放顺序，后者满足审计“当时系统已知什么”的追溯。
- 零库存：仅是查询展示规则。任何将某维度余额变为零的流水或快照增量必须保存，不能在存储阶段过滤。

## 一、实时库存与成本查询

### 目标

新增产品 + 仓库维度的库存成本列表读模型，不建立数据库 SQL View，也不从流水实时汇总；读取 `stock_balance` 并批量关联 SKU、仓库主数据展示名称和编码。

### 查询字段与筛选

返回 SKU 编码/名称、仓库编码/名称、实存数量、锁定数量、可用数量、库存总成本、移动平均单位成本、余额更新时间。筛选支持 SKU、仓库和“显示零库存”；后者默认关闭时为 `qty <> 0`，负库存保留。成本金额为零不影响零库存判断。

该查询以余额表的一行对应一个库存维度，过滤可直接使用 `WHERE balance.qty <> 0`。未来若扩展为产品汇总（跨仓库），必须先 `GROUP BY sku_id`，再用 `HAVING SUM(qty) <> 0`。

### 用例边界

查询链路为 `admin DTO/VO -> application query service -> StockBalanceRepository -> 主数据批量查询 -> VO`。不得让 Controller 直连 Mapper，也不得逐行访问 SKU 或仓库表。列表字段、筛选白名单和分页遵循公共列表协议；本阶段不改变既有入库、出库和锁库事务。

### 验收

1. 同一 SKU 在不同仓库分别显示，数量和成本与 `stock_balance` 完全一致。
2. 关闭“显示零库存”时排除 `qty = 0`，保留负数；开启后显示全部余额记录。
3. SKU/仓库名称批量回填，无 N+1 查询；分页计数与数据使用同一筛选条件。

### 页面筛选项逐项落地矩阵

下表以截图中的库存查询条件为准。结论区分“当前表结构可以直接支持”“可以通过主数据关联支持”和“当前没有事实来源”。

| 页面项 | 当前数据来源 | 当前是否支持 | 实现方式 | 需要补充的内容 |
| --- | --- | --- | --- | --- |
| 日期 | `stock_balance` 无历史日期；流水有 `occurred_at` | 仅支持“即时库存” | 即时模式固定显示当前业务日；指定日期必须走第二阶段快照/流水回放 | 历史模式依赖快照表和统一 `effective_at` |
| 汇总依据：商品+仓库 | `stock_balance` 唯一键 `warehouse_id + sku_id` | 支持 | 按该粒度分页；不要再对余额表做二次汇总 | 若将来支持商品、仓库等其他汇总粒度，增加专用聚合查询 |
| 商品类别 | `product_spu.category_name`，通过 `product_sku.spu_id` 关联 | 可支持 | `stock_balance b JOIN product_sku s JOIN product_spu p`，按 `p.category_name` 筛选 | 建议将类别从自由文本升级为分类 ID，增加分类层级/停用语义 |
| 商品 | `stock_balance.sku_id`、`product_sku.id` | 支持 | 商品选择器传 SKU ID；服务端按租户校验后使用 `s.id = b.sku_id` | 主数据删除/停用策略需明确；库存查询不应因 SKU 停用而丢失历史余额 |
| 商品品牌 | 当前 `product_sku`、`product_spu` 均无品牌字段 | 不支持 | 不允许用备注或商品名称模糊替代品牌筛选 | 新增 `brand_id`（推荐）或 `brand_name`，并建立租户索引；产品 SKU/SPU 关联品牌 |
| 仓库分类 | 当前 `warehouse` 只有编码、名称、地址、负责人、启用状态 | 不支持 | 不允许从仓库名称推断分类 | 新增 `warehouse_category_id` 或独立 `warehouse_category` 关联表，并定义分类树/停用规则 |
| 仓库 | `stock_balance.warehouse_id`、`warehouse.id` | 支持 | 仓库选择器传 ID；按租户关联 `warehouse`，支持多选转 `IN` | 仓库逻辑删除时仍需保留已有库存行的展示名称/快照策略 |
| 商品标签 | 当前无标签表或 SKU 标签关联 | 不支持 | 不允许对 `remark` 做标签语义筛选 | 新增 `product_tag` 与 `product_sku_tag`（或 SPU 标签）及租户唯一约束 |
| 商品条码 | 当前 SKU 无条码字段 | 不支持 | 不允许把 `sku_code` 当条码，二者业务语义不同 | 新增 `product_sku_barcode`，支持一 SKU 多条码、条码类型和租户唯一索引 |
| 规格型号 | `product_sku.specification` | 支持 | 以 SKU 规格字段做精确/包含筛选；查询只读主数据 | 若规格需要结构化属性，另建 SKU 属性表；本阶段文本字段足够 |
| 助记码 | 当前产品和 SKU 均无助记码字段 | 不支持 | 不允许由 SKU 编码截取生成 | 新增 SKU `mnemonic_code`，租户内建立前缀检索索引 |
| 默认供应商 | 当前产品/SKU 无默认供应商关系 | 不支持 | 不允许从最近采购单推断“默认”关系 | 新增 `product_sku_supplier`，包含 `is_default`、供应商、有效期和唯一默认约束 |
| 即时库存范围 | `stock_balance.qty` | 支持 | `b.qty >= :minQty AND b.qty <= :maxQty`；边界是否包含由筛选协议固定 | 无；需补充数量精度和负库存展示规则 |
| 可用库存范围 | `stock_balance.available_qty` | 支持 | `b.available_qty >= :minAvailable AND b.available_qty <= :maxAvailable` | 无；锁库更新必须与余额同事务 |
| 辅助属性 | 当前库存唯一维度没有辅助属性列 | 不支持 | 不能把批次、规格或备注混作辅助属性 | 新增辅助属性定义及库存维度关联；同时改造余额唯一键、流水、快照和重算任务 |
| 显示零库存商品 | `stock_balance.qty` | 支持当前余额 | 关闭时 `WHERE b.qty <> 0`，打开时不加该条件；负库存保留 | 若要显示“从未产生余额”的 SKU，需产品全集左连接余额表，且明确仓库范围 |
| 显示已禁用商品 | `product_sku.enabled`、`product_spu.enabled` | 可支持 | 默认 `s.enabled = 1 AND p.enabled = 1`；勾选后移除启用条件，但保留租户和逻辑删除条件 | 需确定 SPU 禁用是否连带隐藏 SKU；历史库存建议允许查看禁用 SKU |
| 显示无发生额商品 | 当前只有余额，没有“发生额/期间发生量”字段 | 当前不支持 | 不能用 `qty <> 0` 代替发生额；应按指定期间汇总流水的 `qty_change` | 历史模式需按期间计算 `SUM(stock_transaction.qty_change)`，或在快照中保存期间发生量 |
| 显示成本 | `stock_balance.total_cost`、`unit_cost` | 支持当前余额 | 勾选时返回成本字段；未勾选时可不返回或脱敏，不能改变库存计算 | 历史成本必须依赖有效快照/成本计算版本，不可仅取当前余额 |
| 显示批次 | 当前库存维度没有批次字段 | 不支持 | 不允许从来源单据号推断批次 | 新增 `batch_id/batch_no` 并加入余额、流水、快照唯一键及成本层；批次成本规则另行确认 |
| 显示小计/按辅助属性小计 | 当前维度只有一行余额，无辅助属性层级 | 部分支持 | 产品+仓库结果可在应用层按页面分组小计；不能提供辅助属性小计 | 辅助属性落地后，增加分组键和 `GROUP BY` 白名单；小计必须在过滤后计算 |

### 实时查询 SQL 形态

当前主数据名称、类别和规格通过 contract 批量回填；类别、规格和启用状态在应用层过滤后分页。为避免 N+1，单次回填上限为 10000 行。库存规模超过该上限时，应增加库存查询索引表或将可筛选的 SKU 维度冗余到库存查询表，再恢复数据库分页。

当前第一阶段只实现有事实来源的筛选，SQL 由服务端白名单拼装，前端不得传列名或 SQL：

实现接口：`POST /erp/v1/inventory/stockQuery/list`。前端首次进入只展示固定筛选与空态，用户点击“查询”才请求该接口；表头和筛选字段当前由前端固定，保留 API Adapter 的元数据替换入口，后续不改变查询请求语义即可改为后端下发。

```sql
SELECT
    b.warehouse_id,
    b.sku_id,
    s.sku_code,
    s.sku_name,
    s.specification,
    p.category_name,
    w.warehouse_code,
    w.warehouse_name,
    b.qty,
    b.locked_qty,
    b.available_qty,
    b.total_cost,
    b.unit_cost
FROM stock_balance b
JOIN product_sku s
  ON s.corpid = b.corpid AND s.id = b.sku_id AND s.del = 0
JOIN product_spu p
  ON p.corpid = b.corpid AND p.id = s.spu_id AND p.del = 0
JOIN warehouse w
  ON w.corpid = b.corpid AND w.id = b.warehouse_id AND w.del = 0
WHERE b.corpid = :corpid
  AND b.del = 0
  AND (:showZero = 1 OR b.qty <> 0)
  AND (:skuId IS NULL OR b.sku_id = :skuId)
  AND (:warehouseId IS NULL OR b.warehouse_id = :warehouseId)
  AND (:categoryName IS NULL OR p.category_name = :categoryName)
  AND (:specification IS NULL OR s.specification LIKE CONCAT('%', :specification, '%'))
  AND (:minQty IS NULL OR b.qty >= :minQty)
  AND (:maxQty IS NULL OR b.qty <= :maxQty)
  AND (:minAvailableQty IS NULL OR b.available_qty >= :minAvailableQty)
  AND (:maxAvailableQty IS NULL OR b.available_qty <= :maxAvailableQty)
  AND (:showDisabled = 1 OR (s.enabled = 1 AND p.enabled = 1))
```

实际 MyBatis 实现必须将上述条件转换为现有 `ListBaseDTO.conditions` 的字段白名单和操作符白名单；示例中的 `:showZero` 等仅表达逻辑，不允许原样接收前端 SQL。数量范围过滤发生在分页前，任何小计发生在过滤后的结果集上。

### 当前结构的索引要求

现有 `stock_balance` 唯一键适合产品+仓库定位，但联表筛选还需要确认以下索引是否已存在并在迁移中补齐：

- `stock_balance(corpid, warehouse_id, sku_id, del)`；
- `stock_balance(corpid, sku_id, del)`；
- `stock_balance(corpid, qty, del)`、`stock_balance(corpid, available_qty, del)`（仅在数量范围查询频繁且数据量足够大时增加）；
- `product_sku(corpid, spu_id, enabled, del)`、`product_sku(corpid, specification, del)`；
- `product_spu(corpid, category_name, enabled, del)`；
- `warehouse(corpid, warehouse_code, enabled, del)`。

数量索引是否有效取决于筛选选择性，必须以 `EXPLAIN` 和真实数据量验证，不能为了每个筛选项盲目建索引。

## 二、日结、月结与快照

### 目标和原则

快照是库存余额在一个业务截止点的可重建副本，不是数据库备份，也不复制产品、仓库或业务单据。月结生成全量基线；日结只保存发生变动的库存维度。快照生成后不就地篡改：历史业务变化时将其标记失效，并生成新的快照版本。

### 新增表

下列名称为拟定迁移对象，正式实施前应以新的 Flyway 版本和 `docs/sql` 说明固化。

| 表 | 核心字段 | 用途与约束 |
| --- | --- | --- |
| `stock_snapshot_batch` | `id`, `corpid`, `snapshot_date`, `snapshot_type`, `snapshot_version`, `cutoff_at`, `source_watermark`, `status`, `parent_batch_id` | 快照头。`snapshot_type` 为 `MONTHLY_FULL` 或 `DAILY_DELTA`；同一租户、日期、类型、版本唯一。 |
| `stock_snapshot_detail` | `batch_id`, `warehouse_id`, `sku_id`, `qty`, `locked_qty`, `available_qty`, `total_cost`, `unit_cost`, `zero_balance` | 快照明细。`batch_id + warehouse_id + sku_id` 唯一；日增量只写发生变化的维度，变为零也写一行。 |
| `stock_close_period` | `corpid`, `period_type`, `period_key`, `closed_at`, `status`, `close_batch_id`, `closed_by` | 期间控制。月结成功后冻结该月，后续反结账必须显式解锁并留下操作记录。 |
| `stock_rebuild_task` | `corpid`, `warehouse_id`, `sku_id`, `affected_from`, `rebuild_type`, `status`, `attempts`, `calculation_version`, `error_message` | 重算队列；按库存维度合并任务，避免同一维度重复回放。 |

所有新表均包含租户隔离、逻辑删除和标准审计字段，PO 继承 `BaseEntity`。明细索引至少包含 `(batch_id, warehouse_id, sku_id)`；批次索引至少包含 `(corpid, snapshot_date, snapshot_type, status)`。

### 日结脚本

日结任务在业务日结束后运行，入参为 `targetDate`。任务的步骤如下：

1. 创建 `DAILY_DELTA/BUILDING` 批次，记录 `cutoff_at=targetDate` 的次日零点和本次可见的流水水位。
2. 找出上一日结水位之后、截止点之前发生变化的产品 + 仓库维度；对每个维度取截止点的有效数量与成本余额，写入增量快照。余额为零同样写入。
3. 校验快照明细数、数量/金额汇总与该截止点余额一致后，将批次置为 `VALID`；失败置为 `FAILED`，保留错误并允许幂等重试。
4. 不在日结中删除旧版本；查询只选择同日期最新的 `VALID` 版本。

日结执行期间，记账事务仍可继续，但脚本必须按 `cutoff_at` 和流水水位读取，不能把晚到但业务日期较早的流水静默混入一个已经声称完成的版本。

### 月结脚本

月结以月末日结完成为前置条件，生成 `MONTHLY_FULL` 批次：对该月所有仍需追溯的产品 + 仓库维度保存完整余额。成功后创建或更新对应 `stock_close_period` 为 `CLOSED`，并记录所用批次。

月结不删除日增量。历史查询先取目标日所在或之前最近的有效月度全量批次，再按日期顺序覆盖各日增量明细，最后叠加目标日之后至查询截止点的有效流水；“零库存”过滤在结果集形成后执行。月度全量的保留范围、跨年归档策略与任务调度时区需由运维/财务确认。

### 历史查询降级与状态

- 快照链完整：使用“最近有效月度全量 + 日增量 + 少量流水”计算。
- 目标日期或后续快照为 `STALE/BUILDING`：回退至最近有效基线并回放流水；回放量超过阈值则返回“重算中”，不得展示混合版本数据。
- 已关账月：默认只读取 `CLOSED` 关联的有效版本；反结账前不允许改变该月的业务生效库存。

## 三、插单与重算

### 插单规则

插单是今天过账、业务生效时间属于过去的单据。例如 8 月 17 日确认一张 8 月 10 日入库：`effective_at=8 月 10 日`，`posted_at=8 月 17 日`。未关账期间允许经权限/审批插单；已月结期间默认拒绝，必须先反结账，或改走当期库存调整单。已入账单据不得物理删除；反审核、作废和退货使用反向/冲销流水。

### 流水结构演进

现有 `stock_transaction` 与 `stock_cost_transaction` 需在后续迁移中补齐或由新的不可变事件表承载以下事实：

| 字段 | 目的 |
| --- | --- |
| `source_line_id` | 区分同一业务单据的不同产品行，支持精确冲销与来源追溯。 |
| `effective_at`、`posted_at` | 区分业务归属与系统过账审计；现有 `occurred_at` 迁移为兼容字段，不能继续同时表达两者。 |
| `sequence_key` | 在同一产品 + 仓库、同一生效时点稳定排序；推荐由 `effective_at + 单据优先级 + source_id + source_line_id + event_id` 组成。 |
| `event_id`、`reversal_of_event_id` | 使数量和成本记录属于同一业务事件，并明确冲销目标。 |
| `event_status` | 至少区分 `POSTED`、`REVERSED`、`SUPERSEDED`，禁止直接删除原流水。 |
| `calculation_version` | 标识成本结果属于哪一次重算；旧计算结果保留审计，查询只取当前有效版本。 |

移动加权成本的输入（数量变化、入库金额、时间、来源）必须不可变；`qty_before/after`、`total_cost_before/after`、`unit_cost_after` 是派生结果。为避免重算覆盖审计记录，建议新增 `stock_cost_calculation`，以 `(calculation_version, event_id)` 唯一保存每次回放结果，而不是更新旧的成本流水。

### 重算流程

1. 插单、反审核、冲销或经审批的数据修正写入一条新的已过账业务事件；直接 `UPDATE stock_balance`、直接修改快照或物理删除流水均禁止。
2. 校验 `effective_at` 未处于关闭期间；失败时拒绝或要求反结账。通过后以产品 + 仓库维度合并 `stock_rebuild_task`，`affected_from` 取最早受影响业务时间。
3. 将该时点及之后关联的日/月快照批次标记 `STALE`，不修改其余额明细；当前查询视图对应维度标记“重算中”。
4. 重算任务以最近有效的前置快照为起点，按 `effective_at + sequence_key` 回放该维度的不可变流水，重建数量、移动平均总成本和单位成本。
5. 在一个受控事务/切换步骤中发布新的成本计算版本、更新当前 `stock_balance`、重建受影响日增量和月度全量快照版本；全部成功后任务置为 `SUCCEEDED`，新快照置为 `VALID`。
6. 任一步失败时不发布半成品版本，任务置为 `FAILED`；人工修复后可从同一前置快照幂等重试。

### 成本与并发约束

当前 `InventoryPostingService` 根据当下 `stock_balance` 立即计算移动加权成本，适合正常顺序的实时记账。插单期间，受影响的产品 + 仓库应进入“重算排他”状态：同一维度的新记账要么排队，要么基于正在构建的新版本串行回放，不能与重算并发覆盖余额。不同 SKU 或仓库可以并行。

数量可简单求和，但移动加权成本依赖顺序，不能只对历史成本流水做 `SUM(total_cost_change)` 或取 `occurred_at <= 目标日` 的最后一条 `*_after`。历史成本必须读取已发布快照或按稳定顺序回放有效事件。

## 迁移与实施顺序

1. 先实现实时读模型、列表元数据、筛选白名单和测试，不改记账语义。
2. 新增快照、期间和重算任务表，补充日结/月结 Job；先灰度验证快照结果与实时余额逐维一致。
3. 引入双时间、来源行、事件关联和成本计算版本；完成旧流水兼容迁移后再开放插单。
4. 开放插单前，补齐反审核/冲销、期间控制、重算排他锁、失败告警和查询降级；先限租户或限未关账期间灰度。

## 验收矩阵

| 场景 | 预期 |
| --- | --- |
| 即时查询 + 隐藏零库存 | 直接从余额维度过滤，负库存保留。 |
| 日结后无业务变动 | 次日历史查询命中月度基线/日增量，数量和金额等于当时余额。 |
| 日结后余额归零 | 日增量保存零余额，历史查询不回退到旧的非零余额。 |
| 8 月 17 日补录 8 月 10 日入库 | 8 月 10 日及以后相关快照失效；重算完成后数量、移动平均成本和历史查询一致。 |
| 已月结期间插单 | 默认拒绝；反结账成功后才允许写入并触发重算。 |
| 已入账单据作废 | 写冲销事件，不删除原流水；来源和计算版本均可追溯。 |
| 重算失败 | 旧有效版本继续可读或明确显示重算中，绝不展示部分重算余额。 |

## 待确认事项

1. 日结与月结的实际触发时区、业务日截止时间、全量快照保留周期和允许的查询回放上限。
2. 已月结单据的策略：强制反结账，还是允许跨期更正并生成财务调整凭证。
3. 成本调整是否必须同步销售成本、毛利和财务凭证；若需要，应作为独立的跨模块功能设计。
4. 后续是否启用批次、货位、辅助属性或多单位；启用后库存维度键、唯一索引和所有快照/重算任务必须同时扩展。
