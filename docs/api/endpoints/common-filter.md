# 公共列表筛选字段

## 文档信息
- 领域：`common-filter`
- 控制器：`ListCommonController#filter`
- 请求方式：`POST /erp/v1/common/list/filter`
- 聚合文档引用：`docs/kn/common-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "SUPPLIER"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "attr": "customerName",
        "attrName": "客户名称",
        "fieldType": "TEXT",
        "supportedSymbols": ["EQ", "CONTAINS", "IS_EMPTY"],
        "itemList": []
      },
      {
        "attr": "bizStatus",
        "attrName": "业务状态",
        "fieldType": "ENUM",
        "supportedSymbols": ["EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"],
        "itemList": [
          { "value": "1", "text": "启用" },
          { "value": "0", "text": "停用" }
        ]
      }
    ]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 筛选字段列表 |
| `data.list[].attr` | 是 | 字段属性名 |
| `data.list[].attrName` | 是 | 字段显示名称 |
| `data.list[].fieldType` | 是 | 字段类型，当前可返回 `TEXT`、`ENUM`、`USER` 等 |
| `data.list[].supportedSymbols` | 是 | 当前字段支持的筛选运算符列表 |
| `data.list[].itemList` | 是 | 当前字段的候选项；无选项字段返回空数组 |
| `data.list[].businessSelectConfig` | 否 | 业务选择配置；当字段类型为 `USER` 时返回 |

## 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ResultVO<ListFilterVO>`
- 结果仅包含筛选字段元数据
- 供应商列表当前会返回 `ownerPurchaserId` 的 `USER` 筛选字段，并携带成员单选 `businessSelectConfig`
- 供应商列表当前会返回 `supplierCategory`、`mainBusinessCategory`、`bizStatus`、`refStatus` 的固定枚举 `itemList`
