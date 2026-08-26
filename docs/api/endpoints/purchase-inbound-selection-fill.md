# 采购入库单采购订单回填

## 文档信息

- 领域：`purchase`
- 控制器：`PurchaseInboundAdminController#selectionFill`
- 请求方式：`POST /erp/v1/purchase/purchaseInbound/selectionFill`
- 聚合文档引用：`docs/kn/purchase-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "fieldAttr": "main.purchaseOrderId",
  "referenceId": 90001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户 |
| `fieldAttr` | 是 | 固定为 `main.purchaseOrderId` |
| `referenceId` | 是 | 所选采购订单 ID |

## 响应示例

```json
{
  "success": true,
  "data": {
    "referenceId": 90001,
    "patch": {
      "main.purchaseOrderId": 90001,
      "main.supplierId": 10001,
      "main.supplierName": "供应商 A",
      "main.totalAmount": 1000.00,
      "items": []
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `referenceId` | 是 | 已校验的采购订单 ID |
| `patch` | 是 | 仅包含采购入库表单可写路径 |
| `patch.main.totalAmount` | 是 | 所有待入库产品行的数量乘采购单价之和 |
| `patch.items` | 是 | 每行为未完成入库的订单行，数量为待入库数量 |

## 规则说明

- 仅采购入库模块启用的 `main.purchaseOrderId` 可以调用本接口；其他字段或空 ID 均拒绝。
- 所选订单必须属于当前租户，并至少存在一行 `qty > inboundQty` 的待入库产品。
- 回填供应商、采购金额，以及订单行 SKU、单位、仓库、待入库数量、采购单价和成本单价；前端只应用 `patch`，不得自行拼装订单明细。
