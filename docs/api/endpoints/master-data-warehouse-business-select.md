# 仓库业务选择接口

控制器：`WarehouseAdminController`；业务编码：`WAREHOUSE`；所有接口均使用 `POST`，并要求 `corpid` 与 `userId`。

## 快捷搜索

`/erp/v1/masterData/warehouse/businessSelect/quickSearch`

入参：`WarehouseBusinessSelectQueryDTO`，可选 `keyword`。返回 `ResultVO<List<WarehouseBusinessSelectOptionVO>>`。

## 弹窗搜索

`/erp/v1/masterData/warehouse/businessSelect/dialogSearch`

入参：`WarehouseBusinessSelectQueryDTO`，可选 `keyword`、`pageNum`、`pageSize`。返回 `ResultVO<ListBaseVO<WarehouseBusinessSelectOptionVO>>`。

## 按 ID 回显

`/erp/v1/masterData/warehouse/businessSelect/getById`

入参：`WarehouseBusinessSelectQueryDTO`，必须提供 `id`。返回 `ResultVO<WarehouseBusinessSelectOptionVO>`；目标不存在或不属于当前租户时 `data` 为 `null`。
