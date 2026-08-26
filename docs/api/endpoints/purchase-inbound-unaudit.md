# 采购入库单反审核

## 文档信息

- 领域：`purchase-inbound`
- 控制器：`PurchaseInboundAdminController#unaudit`
- 请求方式：`POST /erp/v1/purchase/purchaseInbound/unaudit`
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

返回 `ResultVO<BaseVO>`。仅已审核但尚未确认入库的单据可反审核；已入账单据必须通过冲销或退货流程处理。
