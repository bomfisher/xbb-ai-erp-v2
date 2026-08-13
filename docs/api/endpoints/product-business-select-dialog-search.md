# SKU 弹窗选择

## 文档信息
- 领域：`master-data`
- 控制器：`ProductSelectAdminController#dialogSearch`
- 请求方式：`POST /erp/v1/product/businessSelect/dialogSearch`
- 聚合文档引用：`docs/kn/product-spu-m.md`

## 请求示例

```json
{"corpid":"corp-001","businessCode":"PURCHASE_ORDER","productType":"product-sku","keyword":"螺丝","pageNum":1,"pageSize":20}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户 |
| `businessCode` | 是 | 消费该选择器的业务编码 |
| `productType` | 是 | 固定为 `product-sku` |
| `keyword` | 否 | 按 SKU 编码或名称搜索 |
| `pageNum` | 否 | 页码，默认 `1` |
| `pageSize` | 否 | 每页数量，默认 `20` |

## 响应示例

```json
{"success":true,"data":{"list":[{"id":1001,"code":"SKU-001","name":"螺丝","label":"SKU-001 - 螺丝"}],"pageHelper":{"page":1,"count":1}}}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 当前页 SKU 候选 |
| `data.pageHelper` | 是 | 页码与总条数 |

## 规则说明

- 候选 SKU 和过滤规则与快捷选择保持一致。
- 当前接口不返回 `headList`，前端使用产品选择器的固定列定义展示候选。
