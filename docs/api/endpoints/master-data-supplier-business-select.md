# 供应商业务选择接口

控制器：`SupplierAdminController`；业务编码：`SUPPLIER`；所有接口均使用 `POST`，并要求 `corpid` 与 `userId`。

## 快捷搜索

`/erp/v1/masterData/supplier/businessSelect/quickSearch`

入参：`SupplierBusinessSelectQueryDTO`，可选 `keyword`。返回 `ResultVO<List<SupplierBusinessSelectOptionVO>>`。

## 弹窗搜索

`/erp/v1/masterData/supplier/businessSelect/dialogSearch`

入参：`SupplierBusinessSelectQueryDTO`，可选 `keyword`、`pageNum`、`pageSize`。返回 `ResultVO<ListBaseVO<SupplierBusinessSelectOptionVO>>`。

## 按 ID 回显

`/erp/v1/masterData/supplier/businessSelect/getById`

入参：`SupplierBusinessSelectQueryDTO`，必须提供 `id`。返回 `ResultVO<SupplierBusinessSelectOptionVO>`；目标不存在或不属于当前租户时 `data` 为 `null`。

候选项固定包含 `id`、`code`、`name`、`label`，其中 `label` 优先按“供应商编码 + 空格 + 供应商名称”生成。
