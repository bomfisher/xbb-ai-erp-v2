# SKU 按 ID 回显

## 文档信息
- 领域：`master-data`
- 控制器：`ProductSelectAdminController#getById`
- 请求方式：`POST /erp/v1/product/businessSelect/getById`
- 聚合文档引用：`docs/kn/product-spu-m.md`

## 请求示例

```json
{"corpid":"corp-001","businessCode":"PURCHASE_ORDER","productType":"product-sku","id":1001}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户 |
| `businessCode` | 是 | 消费该选择器的业务编码 |
| `productType` | 是 | 固定为 `product-sku` |
| `id` | 否 | SKU 稳定 ID；未传时返回 `null` |

## 响应示例

```json
{"success":true,"data":{"id":1001,"code":"SKU-001","name":"螺丝","label":"SKU-001 - 螺丝"}}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 否 | SKU 不存在、跨租户或已禁用时为 `null` |

## 规则说明

- 回显按 `corpid + id` 查询，只允许启用 SKU。
- `productType` 非 `product-sku`、缺少租户或业务编码时返回业务异常。
