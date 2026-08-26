# 销售订单审核

## 文档信息
- 领域：`sales-order`
- 控制器：`SalesOrderAdminController#audit`
- 请求方式：`POST /erp/v1/sales/salesOrder/audit`
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
| `id` | 是 | 销售订单 ID |

## 响应
返回 `ResultVO<BaseVO>`。待审核、审核中、已拒绝的订单会变更为已审核；其他审核状态拒绝操作。
