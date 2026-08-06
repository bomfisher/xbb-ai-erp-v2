# 产品 SKU 业务选择快捷搜索

## 文档信息
- 领域：`product-business-select-quick-search`
- 控制器：`ProductAdminController#businessSelectQuickSearch`
- 请求方式：`POST /erp/v1/product/businessSelect/quickSearch`
- 聚合文档引用：待后续建立产品业务聚合文档

## 请求示例

```json
{
  "corpid": "demo-corp",
  "businessCode": "PURCHASE_REQUEST",
  "keyword": "SKU-001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `businessCode` | 否 | 产品选择所在单据，采购申请传 `PURCHASE_REQUEST`，采购订单传 `PURCHASE_ORDER`；决定回填字段 |
| `keyword` | 否 | 匹配 SKU 编码、名称、助记码或主条码 |
| `id` | 否 | 可选单条定位条件 |
| `pageNum` | 否 | 当前接口不依赖该字段 |
| `pageSize` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": [
    {
      "id": 1001,
      "code": "SKU-001",
      "name": "红色款",
      "label": "SKU-001 红色款",
      "linePatch": {
        "skuId": 1001,
        "skuCodeSnapshot": "SKU-001",
        "skuNameSnapshot": "红色款",
        "specSnapshot": "红色 / M"
      }
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 产品 SKU 业务选择候选列表 |
| `data[].id` | 是 | SKU ID |
| `data[].code` | 否 | SKU 编码 |
| `data[].name` | 否 | SKU 名称 |
| `data[].label` | 否 | 前端直接展示文案，优先由编码和名称组合 |
| `data[].linePatch` | 是 | 按 `businessCode` 返回的采购明细行回填字段 |
| `data[].linePatch.skuId` | 是 | 回填 SKU ID |
| `data[].linePatch.skuCodeSnapshot` | 否 | 回填 SKU 编码快照 |
| `data[].linePatch.skuNameSnapshot` | 否 | 回填 SKU 名称快照 |
| `data[].linePatch.specSnapshot` | 否 | 回填规格快照 |
| `data[].linePatch.purchaseUnitId` | 是 | 采购单位 ID，当前固定返回 `1` 占位 |
| `data[].linePatch.requestQty` | 条件返回 | 采购申请返回 `1` 占位 |
| `data[].linePatch.warehouseId`、金额税率字段 | 条件返回 | 采购订单返回占位值，详见接口规则 |

## 规则说明

- 用于产品 SKU 业务选择输入框的快捷搜索下拉候选。
- 仅返回 `canPurchase = 1` 且 `enableStatus = 1` 的 SKU。
- `keyword` 同时匹配 `sku_code`、`sku_name`、`mnemonic_code` 与 `main_barcode`。
- 返回轻量候选对象，并通过 `linePatch` 支持采购申请、采购订单明细行回填。
- `PURCHASE_REQUEST` 补充 `purchaseUnitId=1`、`requestQty=1`；`PURCHASE_ORDER` 补充 `purchaseUnitId=1`、`warehouseId=1`、`orderQty=1`，金额与税率字段均为 `0`。这些值均为后端临时占位。
