# 库存查询列表

适用范围：实时 SKU + 仓库库存成本查询。接口为 `POST /erp/v1/inventory/stockQuery/list`，首次进入页面不调用，用户点击查询后调用。

请求继承 `BaseDTO`，包含 `corpid`、`userId` 及可选的 `skuId`、`warehouseId`、`categoryName`、`specification`、`minQty`、`maxQty`、`minAvailableQty`、`maxAvailableQty`、`showZero`、`showDisabled`、`pageNum`、`pageSize`。`showZero/showDisabled` 为 `0/1`，默认 `0`。

返回 `ListBaseVO`，行包含 SKU、类别、规格、仓库、实存/锁定/可用数量、库存总成本和单位成本。筛选只接受上述字段，服务端不接收列名、排序 SQL 或任意过滤表达式。
