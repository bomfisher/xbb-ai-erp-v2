# 公共列表表头字段

## 文档信息
- 领域：`common-header`
- 控制器：`ListCommonController#header`
- 请求方式：`POST /erp/v1/common/list/header`
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
        "fieldType": "INPUT",
        "required": 0,
        "editable": 1,
        "itemList": []
      }
    ]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 表头字段列表 |
| `data.list[].attr` | 是 | 字段属性名 |
| `data.list[].attrName` | 是 | 字段显示名称 |
| `data.list[].fieldType` | 是 | 字段类型；当前可返回如 `1`、`8`、`12` |
| `data.list[].required` | 是 | 是否必填 |
| `data.list[].editable` | 是 | 是否可编辑 |
| `data.list[].itemList` | 是 | 字段可选项列表 |
| `data.list[].businessSelectConfig` | 否 | 业务选择配置；当字段类型为 `12` 时返回 |

## 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ResultVO<ListHeaderVO>`
- 结果仅包含表头字段元数据
- 供应商列表当前会返回枚举字段的 `itemList`，供前端把码值转为显示文案
- 供应商列表当前会返回 `ownerPurchaserId` 的 `USER(12)` 表头语义和成员单选配置
