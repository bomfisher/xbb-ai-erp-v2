# 销售出库单销售订单回填

## 文档信息

- 领域：`sales`
- 控制器：`SalesOutboundAdminController#selectionFill`
- 请求方式：`POST /erp/v1/sales/salesOutbound/selectionFill`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "fieldAttr": "main.salesOrderId",
  "referenceId": 90001
}
```

## 规则说明

- `fieldAttr` 固定为 `main.salesOrderId`，订单必须属于当前租户。
- 订单已全部出库时拒绝回填；明细数量按订单数量减已出库数量计算。
- 回填客户、销售订单行、产品、仓库、单位、待出库数量和销售单价。
