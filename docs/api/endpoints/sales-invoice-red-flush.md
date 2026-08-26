# 销售发票红冲

## 文档信息

- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#redFlush`
- 请求方式：`POST /erp/v1/sales/salesInvoice/redFlush`
- 聚合文档引用：`docs/kn/sales-invoice-m.md`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","id":1001}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 操作人 ID |
| `id` | 是 | 原正常销售发票 ID |

## 响应

返回 `ResultVO<BaseVO>`。仅正常、已过账销售发票可红冲，且每张原发票只允许红冲一次；存在下游应收开放项时必须先处理应收款，禁止直接红冲。操作会生成独立的 `CREDIT_NOTE` 负数贷项发票、复制负数明细和来源行关联，并同步创建负数应收开放项；负数来源数量会冲回原单的已开票数量，原发票不被修改或删除。
