# 销售出库单审核

## 文档信息

- 领域：`sales-outbound`
- 控制器：`SalesOutboundAdminController#audit`
- 请求方式：`POST /erp/v1/sales/salesOutbound/audit`
- 聚合文档引用：`docs/kn/sales-order-m.md`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","id":1001}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 操作人 ID |
| `id` | 是 | 销售出库单 ID |

## 响应

返回 `ResultVO<BaseVO>`。审核成功后单据审核状态变为已审核；已审核单据拒绝重复操作。
