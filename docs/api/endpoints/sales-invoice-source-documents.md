# 销售发票来源单据查询

## 文档信息

- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#sourceDocuments`
- 请求方式：`POST /erp/v1/sales/salesInvoice/source/documents`
- 聚合文档引用：`docs/kn/sales-invoice-m.md`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","sourceType":"SALES_OUTBOUND","customerId":1001}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 操作人 ID |
| `sourceType` | 是 | `SALES_OUTBOUND` 或 `SALES_ORDER` |
| `customerId` | 否 | 已选客户时按客户筛选 |

## 响应

返回已审核来源单据的 ID、单据编号、客户 ID 和来源类型。单张发票可选择多张同类型来源单据。
