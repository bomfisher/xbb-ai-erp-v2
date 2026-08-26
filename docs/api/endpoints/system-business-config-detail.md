# 系统配置详情

`POST /erp/v1/system/businessConfig/detail`

请求体为 `BusinessConfigCategoryQueryDTO`，继承 `BaseDTO`，包含：

- `categoryCode`：目录编码，当前使用 `GLOBAL`。

成功时返回 `ResultVO<BusinessConfigDetailVO>`，其中 `documents` 按单据返回配置项定义和当前有效值。每个单据还返回所属 `categoryCode`、`categoryName`，用于按采购、销售、付款和收款业务分组展示。配置项包含 `code`、`title`、`helpText`、`controlType`、`value`、`defaultValue` 与 `options`。客户端不得提交或信任定义中的标题、帮助、控件类型和选项。

全局配置返回采购订单、采购入库单、采购发票、销售订单、销售出库单、销售发票、预付款、应付款、付款单、付款核销、预收款、应收款、收款单和收款核销的审核方式配置。

- 审核方式有效选项为 `REQUIRED`（必须审核）与 `AUTO`（自动审核）。
- 销售发票“过账后自动生成应收”有效选项为 `true`（开启）与 `false`（关闭），默认值为 `false`。
