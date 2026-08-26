# 采购发票过账

## 文档信息

- 领域：`purchase-invoice`
- 控制器：`PurchaseInvoiceAdminController#post`
- 请求方式：`POST /erp/v1/purchase/purchaseInvoice/post`
- 聚合文档引用：`docs/kn/purchase-m.md`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","id":1001}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 操作人 ID |
| `id` | 是 | 采购发票 ID |

## 响应

返回 `ResultVO<BaseVO>`。仅已审核且业务状态为草稿的采购发票可以过账；成功后状态变为 `POSTED`，并记录过账时间。已过账采购发票可作为应付单的来源。
