# 系统配置管理

系统配置由 `xbb-erp-module-system` 管理，按租户和业务单据编码保存覆盖值。配置定义、类型化枚举与默认值由后端代码目录维护，业务模块通过 `BusinessConfigQueryApi` 传入配置键枚举获得强类型结果。

全局配置统一维护采购订单、采购入库单、采购发票、销售订单、销售出库单、销售发票、预付款、应付款、付款单、付款核销、预收款、应收款、收款单和收款核销的审核方式。勾选单据即保存为自动审核，未勾选即必须人工审核；采购入库已接入 `PurchaseInboundApprovalPolicy`。销售发票支持 `SALES_INVOICE_AUTO_CREATE_RECEIVABLE`（过账后自动生成应收），采购发票支持 `PURCHASE_INVOICE_AUTO_CREATE_PAYABLE`（过账后自动生成应付），二者默认关闭。配置快照缓存于 Redis，保存成功后按租户和业务编码精确失效。

- 设计：`docs/business/系统配置管理设计.md`
- 目录 API：`docs/api/endpoints/system-business-config-catalog.md`
- 详情 API：`docs/api/endpoints/system-business-config-detail.md`
- 保存 API：`docs/api/endpoints/system-business-config-save.md`
- 全局保存 API：`docs/api/endpoints/system-business-config-save-global.md`
