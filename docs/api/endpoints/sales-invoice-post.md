# 销售发票过账

## 文档信息

- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#post`
- 请求方式：`POST /erp/v1/sales/salesInvoice/post`
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
| `id` | 是 | 销售发票 ID |

## 响应

返回 `ResultVO<BaseVO>`。仅已审核且业务状态为草稿的销售发票可过账；成功后状态变为 `POSTED`。对来源销售订单或销售出库产品，过账前会按未作废发票汇总校验可开票数量，超过来源数量时拒绝过账；订单行会同时累计直接按订单开票及关联出库开票的数量。当系统配置 `SALES_INVOICE_AUTO_CREATE_RECEIVABLE` 为 `true` 时，在同一事务创建对应应收开放项；该配置默认关闭。
