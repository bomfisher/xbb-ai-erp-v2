# 产品 SKU 业务选择弹窗搜索

## 文档信息
- 领域：`product-business-select-dialog-search`
- 控制器：`ProductAdminController#businessSelectDialogSearch`
- 请求方式：`POST /erp/v1/product/businessSelect/dialogSearch`
- 聚合文档引用：待后续建立产品业务聚合文档

## 请求示例

```json
{
  "corpid": "demo-corp",
  "businessCode": "PURCHASE_ORDER",
  "keyword": "红色",
  "pageNum": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `businessCode` | 否 | 产品选择所在单据，决定 `headList` 和 `linePatch` |
| `keyword` | 否 | 匹配 SKU 编码、名称、助记码或主条码 |
| `pageNum` | 否 | 页码，小于 1 时按 1 处理 |
| `pageSize` | 否 | 每页条数，小于 1 时按 20 处理 |
| `id` | 否 | 可选单条定位条件 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": {
    "headList": [
      { "attr": "code", "attrName": "SKU编码", "fieldType": "1", "required": 0, "editable": 0, "itemList": [] },
      { "attr": "name", "attrName": "SKU名称", "fieldType": "1", "required": 0, "editable": 0, "itemList": [] }
    ],
    "list": [
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
    ],
    "pageHelper": {
      "page": 1,
      "count": 1,
      "hasLeft": false,
      "hasRight": false
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.headList` | 是 | 产品弹窗动态表头；采购申请含申请数量，采购订单额外含收货仓库、含税单价、税率 |
| `data.list` | 是 | 当前页产品 SKU 候选列表 |
| `data.list[].id` | 是 | SKU ID |
| `data.list[].code` | 否 | SKU 编码 |
| `data.list[].name` | 否 | SKU 名称 |
| `data.list[].label` | 否 | 前端直接展示文案 |
| `data.list[].linePatch` | 是 | 按 `businessCode` 返回的采购明细行回填字段，含临时占位值 |
| `data.list[].linePatch.skuId` | 是 | 回填 SKU ID |
| `data.list[].linePatch.skuCodeSnapshot` | 否 | 回填 SKU 编码快照 |
| `data.list[].linePatch.skuNameSnapshot` | 否 | 回填 SKU 名称快照 |
| `data.list[].linePatch.specSnapshot` | 否 | 回填规格快照 |
| `data.pageHelper` | 是 | 分页信息 |
| `data.pageHelper.page` | 是 | 当前页码 |
| `data.pageHelper.count` | 是 | 总页数 |
| `data.pageHelper.hasLeft` | 是 | 是否存在上一页 |
| `data.pageHelper.hasRight` | 是 | 是否存在下一页 |

## 规则说明

- 用于产品 SKU 业务选择弹窗内的列表搜索。
- 仅返回 `canPurchase = 1` 且 `enableStatus = 1` 的 SKU。
- `keyword` 同时匹配 `sku_code`、`sku_name`、`mnemonic_code` 与 `main_barcode`。
- `pageNum` 默认值为 1，`pageSize` 默认值为 20。
- 当前实现先查询符合条件的 SKU，再按页码做内存分页切片。
- 前端必须按本接口返回的 `headList` 渲染产品弹窗；不得硬编码产品列。
- `PURCHASE_REQUEST` 回填采购单位和申请数量占位值；`PURCHASE_ORDER` 额外回填收货仓库、订单数量、价格、税率和金额占位值。
