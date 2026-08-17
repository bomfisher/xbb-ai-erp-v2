# 库存持久化表设计

执行事实源为 `xbb-erp-app-admin/src/main/resources/db/migration/V7__create_inventory_stock_tables.sql`。

| 表 | 用途 | 关键约束与索引 |
| --- | --- | --- |
| `stock_balance` | 仓库 SKU 的可用、锁定数量与成本台账 | 租户、仓库、SKU 唯一 |
| `stock_transaction` | 库存数量变更流水 | 租户与幂等键唯一；按库存维度、来源维度查询 |
| `stock_cost_transaction` | 库存成本变更流水 | 租户与幂等键唯一；按库存维度、业务来源查询 |
| `stock_reservation` | 出库单锁库记录 | 来源行、幂等键在租户内唯一；按库存状态查询 |

四表均以 `corpid` 作为租户隔离字段，使用 `del` 逻辑删除字段，并由应用层维护审计字段。

`stock_reservation.status` 的稳定存储值由 `StockReservationStatusEnum` 管理：`RESERVED` 表示已锁库待审核，`PARTIALLY_OUTBOUNDED` 表示部分消费锁库，`FULLY_OUTBOUNDED` 表示锁库已全部转为出库，`RELEASED` 表示审核驳回或撤回后已释放。
