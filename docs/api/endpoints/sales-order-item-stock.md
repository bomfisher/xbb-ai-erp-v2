# 销售订单子档即时库存

## 文档信息

- 领域：`sales`
- 控制器：`SalesOrderAdminController#queryItemStock`
- 请求方式：`POST /erp/v1/sales/salesOrder/itemStock`
- 聚合文档引用：`docs/kn/sales-order-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "skuId": 1001,
  "warehouseId": 2001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户 |
| `skuId` | 是 | 产品 SKU ID |
| `warehouseId` | 否 | 子档锁库仓库；为空时汇总该 SKU 在当前租户全部仓库的即时库存 |

## 响应示例

```json
{"success":true,"data":{"stockQty":36}}
```

## 规则说明

- `stockQty` 是库存余额数量（即时库存），不是可用库存，且只用于订单表单展示。
- 仓库存在但该 SKU 无库存余额时返回 `0`。
- 该接口不锁库、不创建库存快照，也不参与订单保存。
