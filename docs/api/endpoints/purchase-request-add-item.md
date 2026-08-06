# 采购申请新增表单初始化

## 文档信息
- 领域：`purchase-request-add-item`
- 控制器：`PurchaseRequestAdminController#addItem`
- 请求方式：`POST /erp/v1/purchase/request/addItem`
- 聚合文档引用：`docs/kn/purchase-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 否 | 员工 ID，当前控制器接收 `BaseDTO`，查询服务未使用该字段 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "headList": [
      {
        "attr": "main.purchaseOrgId",
        "attrName": "采购组织",
        "fieldType": "1",
        "required": 1,
        "editable": 1,
        "itemList": []
      }
    ],
    "data": {
      "main": {},
      "items": [],
      "sectionState": {}
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.headList` | 是 | 采购申请表单字段定义列表 |
| `data.data` | 是 | 新建页初始化数据 |
| `data.data.main` | 是 | 采购申请主档初始化对象 |
| `data.data.items` | 是 | 采购申请明细初始化列表，默认空数组 |
| `data.data.sectionState` | 是 | 分段状态对象，当前返回默认实例 |

## 规则说明

- `headList` 由 `PurchaseRequestQueryAppServiceImpl` 直接构造并返回
- 新建接口当前至少返回采购组织、申请单号、申请部门、申请人、来源信息、金额信息与明细字段定义
- 字段类型使用 `FieldTypeEnum` 的数值字符串，例如文本=`1`、整数=`2`、金额=`4`、日期=`6`
- 产品明细由一个 `attr="items"`、`fieldType="50"` 的外层字段描述；外层 `attr` 与 `data.items` 动态对齐，明细行字段位于 `subField`。
- `subField.skuId` 使用 `fieldType="12"` 和 `businessSelectConfig` 下发产品 SKU 选择配置；`subField` 中其他字段的 `attr` 是明细行对象属性，不带 `items.` 前缀。
- `data` 部分由 `PurchaseRequestAdminAssembler.buildEmptySaveItemVO()` 初始化，主档为空对象、明细默认为空数组
