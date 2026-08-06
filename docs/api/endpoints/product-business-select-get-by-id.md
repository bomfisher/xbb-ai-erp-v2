# 产品 SKU 业务选择按 ID 回显

## 文档信息
- 领域：`product-business-select-get-by-id`
- 控制器：`ProductAdminController#businessSelectGetById`
- 请求方式：`POST /erp/v1/product/businessSelect/getById`
- 聚合文档引用：待后续建立产品业务聚合文档

## 请求示例

```json
{
  "corpid": "demo-corp",
  "businessCode": "PURCHASE_ORDER",
  "id": 1001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `businessCode` | 否 | 产品选择所在单据，决定 `linePatch` 的单据字段 |
| `id` | 是 | SKU ID |
| `keyword` | 否 | 当前接口不依赖该字段 |
| `pageNum` | 否 | 当前接口不依赖该字段 |
| `pageSize` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": {
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
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 否 | 匹配不到 SKU 时返回 `null` |
| `data.id` | 是 | SKU ID |
| `data.code` | 否 | SKU 编码 |
| `data.name` | 否 | SKU 名称 |
| `data.label` | 否 | 前端直接展示文案 |
| `data.linePatch` | 是 | 按 `businessCode` 返回的采购明细行回填字段，含临时占位值 |
| `data.linePatch.skuId` | 是 | 回填 SKU ID |
| `data.linePatch.skuCodeSnapshot` | 否 | 回填 SKU 编码快照 |
| `data.linePatch.skuNameSnapshot` | 否 | 回填 SKU 名称快照 |
| `data.linePatch.specSnapshot` | 否 | 回填规格快照 |

## 规则说明

- 用于业务选择字段已有 SKU ID 时的回显展示。
- `id` 不能为空，缺失时后端抛业务异常。
- 按 `corpid + id` 查询 SKU，匹配不到时返回 `null`。
- 结果统一转换为产品 SKU 业务选择候选对象结构。
- `PURCHASE_REQUEST` 与 `PURCHASE_ORDER` 的回填字段及占位规则与快捷搜索接口保持一致。
