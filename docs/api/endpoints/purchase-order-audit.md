# 采购订单审核

## 文档信息

- 领域：`purchase-order`
- 控制器：`PurchaseOrderAdminController#audit`
- 请求方式：`POST /erp/v1/purchase/purchaseOrder/audit`
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
| `id` | 是 | 采购订单 ID |

## 响应

返回 `ResultVO<BaseVO>`。审核成功后订单审核状态变为已审核。
