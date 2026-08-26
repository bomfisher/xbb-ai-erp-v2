# 销售发票来源明细预览

## 文档信息

- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#sourcePreview`
- 请求方式：`POST /erp/v1/sales/salesInvoice/source/preview`
- 聚合文档引用：`docs/kn/sales-invoice-m.md`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","sourceType":"SALES_OUTBOUND","sourceId":1001}
```

## 响应

返回来源单据客户和可带入的发票行；每行携带 `sourceType`、`sourceId`、`sourceLineId`，正式保存时据此写入 `sales_invoice_line_source`。
