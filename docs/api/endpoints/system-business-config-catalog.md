# 系统配置目录

`POST /erp/v1/system/businessConfig/catalog`

请求体为 `BaseDTO`，使用 `corpid` 和 `userId` 表示当前租户与操作人。

成功时返回 `ResultVO<List<BusinessConfigCategoryVO>>`；每项包含稳定目录编码 `code` 和显示名称 `name`。当前返回 `GLOBAL`（全局配置）、`SALES`（销售业务）与 `PURCHASE`（采购业务）：全局配置用于统一维护跨业务单据的审核规则，销售业务和采购业务分别保留发票的非审核控制项。
