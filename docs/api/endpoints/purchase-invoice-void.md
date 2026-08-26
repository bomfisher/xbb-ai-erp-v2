# 采购发票作废

## 文档信息

- 领域：`purchase-invoice`
- 控制器：`PurchaseInvoiceAdminController#voidInvoice`
- 请求方式：`POST /erp/v1/purchase/purchaseInvoice/void`
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

返回 `ResultVO<BaseVO>`。仅未过账且未处于已审核状态的采购发票可作废；已审核单据必须先反审核。成功后业务状态变为 `VOIDED`。
