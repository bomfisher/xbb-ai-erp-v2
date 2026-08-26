# 出入库流水查询列表

`POST /erp/v1/inventory/stockTransactionQuery/list`

首次进入页面不调用；点击查询后调用。请求继承 `BaseDTO`，可传 `startAt`、`endAt`、`warehouseId`、`skuId`、`categoryName`、`specification`、`sourceType`、`pageNum`、`pageSize`。当前返回每个库存事件一行，包含 SKU、单据类型/来源 ID、发生时间、仓库、入库/出库数量与成本、事件后数量和成本。

当前 `sourceId` 是来源单据内部 ID，不是展示单号；审核状态、批次、品牌、标签、条码、部门、辅助属性等暂不返回，待事件快照字段补齐后扩展。
