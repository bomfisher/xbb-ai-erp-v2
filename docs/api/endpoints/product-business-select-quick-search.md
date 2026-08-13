# SKU 快捷选择

## 文档信息
- 领域：`master-data`
- 控制器：`ProductSelectAdminController#quickSearch`
- 请求方式：`POST /erp/v1/product/businessSelect/quickSearch`
- 聚合文档引用：`docs/kn/product-spu-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "businessCode": "PURCHASE_ORDER",
  "productType": "product-sku",
  "keyword": "螺丝"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户 |
| `businessCode` | 是 | 消费该选择器的业务编码 |
| `productType` | 是 | 固定为 `product-sku` |
| `keyword` | 否 | 按 SKU 编码或名称搜索 |

## 响应示例

```json
{"success":true,"data":[{"id":1001,"code":"SKU-001","name":"螺丝","label":"SKU-001 - 螺丝"}]}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data[].id` | 是 | SKU 稳定 ID |
| `data[].code` | 是 | SKU 编码 |
| `data[].name` | 是 | SKU 名称 |
| `data[].label` | 是 | SKU 编码与名称的展示文案 |

## 规则说明

- 只返回当前租户且启用状态为 `1` 的 SKU。
- `productType` 非 `product-sku`、缺少租户或业务编码时返回业务异常。
