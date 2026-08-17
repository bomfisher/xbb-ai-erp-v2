# 库存与成本查询

## 适用范围

本功能面向 `xbb-erp-module-inventory`，规划按产品（SKU）和仓库维度提供实时库存、实时成本及可追溯的历史库存成本查询。

## 事实来源与关联入口

- 当前执行表结构：`xbb-erp-app-admin/src/main/resources/db/migration/V7__create_inventory_stock_tables.sql`
- 当前库存记账：`xbb-erp-module-inventory/.../InventoryPostingService.java`
- 详细方案与分阶段验收：[design.md](design.md)

## 范围

本设计包含实时查询、日结/月结快照、允许插单后的库存与移动加权成本重算；不包含批次、货位、辅助属性、财务凭证重算及 FIFO/LIFO 成本法。后续扩展库存维度时，必须同步扩展余额、流水、快照及重算任务的唯一维度键。
