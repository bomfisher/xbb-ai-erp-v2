# 库存查询列表

适用范围：实时 SKU + 仓库库存成本查询。接口为 `POST /erp/v1/inventory/stockQuery/list`，首次进入页面不调用，用户点击查询后调用。

请求继承 `BaseDTO`，包含 `corpid`、`userId` 及可选的 `skuId`、`warehouseId`、`categoryName`、`specification`、`minQty`、`maxQty`、`minAvailableQty`、`maxAvailableQty`、`showZero`、`showDisabled`、`pageNum`、`pageSize`。`showZero/showDisabled` 为 `0/1`，默认 `0`。

返回 `ListBaseVO`，行包含 SKU、类别、规格、仓库、实存/锁定/可用数量、库存总成本和单位成本。筛选只接受上述字段，服务端不接收列名、排序 SQL 或任意过滤表达式。

## 子档即时库存

接口为 `POST /erp/v1/inventory/stockQuery/instantQty`，用于动态表单的产品行库存展示。

请求继承 `BaseDTO`，包含必填的 `corpid`、`skuId` 和可选的 `warehouseId`。`warehouseId` 有值时返回该仓库的库存余额数量；为空时汇总当前租户该 SKU 的全部仓库库存。SKU 在目标范围没有库存余额时返回 `0`。

响应为 `{ "stockQty": 36 }`；该查询只读，不创建库存快照、不锁库，也不参与单据保存。
