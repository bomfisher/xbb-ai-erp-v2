# 采购入库单审核

## 文档信息

- 领域：`purchase-inbound`
- 控制器：`PurchaseInboundAdminController#audit`
- 请求方式：`POST /erp/v1/purchase/purchaseInbound/audit`
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
| `id` | 是 | 采购入库单 ID |

## 响应

返回 `ResultVO<BaseVO>`。审核成功后立即调用确认入库流程，库存入账与状态更新在同一事务完成。
