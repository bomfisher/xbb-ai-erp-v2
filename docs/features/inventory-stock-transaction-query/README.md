# 出入库流水查询

## 适用范围

面向 `xbb-erp-module-inventory` 的 SKU + 仓库库存数量、成本流水查询。页面筛选和表头当前由前端固定，后续可由后端元数据接管。

详细方案见 [design.md](design.md)。当前执行事实源为 `V7__create_inventory_stock_tables.sql` 与库存记账服务。
