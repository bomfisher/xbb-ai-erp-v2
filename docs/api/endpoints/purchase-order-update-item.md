# 采购订单编辑表单初始化

## 文档信息
- 领域：`purchase-order-update-item`
- 控制器：`PurchaseOrderAdminController#updateItem`
- 请求方式：`POST /erp/v1/purchase/order/updateItem`
- 聚合文档引用：`docs/kn/purchase-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "id": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 否 | 员工 ID，当前控制器接收 `IdBaseDTO`，查询服务未使用该字段 |
| `id` | 是 | 采购订单主键 |

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
      "main": {
        "id": 1,
        "orderNo": "PO-UPD-001"
      },
      "items": [],
      "sectionState": {}
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.headList` | 是 | 采购订单表单字段定义列表；`main.deliveryDate` 的 `fieldType` 为 `6`（`FieldTypeEnum.DATE`） |
| `data.data` | 是 | 编辑页回填数据 |
| `data.data.main` | 否 | 采购订单主档回填对象；未命中数据时返回默认对象 |
| `data.data.items` | 是 | 采购订单明细列表；未命中或无明细时返回空数组 |
| `data.data.sectionState` | 是 | 分段状态对象，当前返回默认实例 |

## 规则说明

- 返回结构与 `addItem` 一致，但 `data` 部分通过 `loadSaveItem` 按 `id` 回填
- `headList` 与新增接口共用同一套字段定义，不因编辑态切换字段集合
- `main.deliveryDate` 使用毫秒时间戳回填，前端公共组件展示为 `YYYY-MM-DD`
- 产品明细由一个 `attr="items"`、`fieldType="50"` 的外层字段描述；外层 `attr` 与 `data.items` 动态对齐，明细行字段位于 `subField`。
- `subField.skuId` 使用 `fieldType="12"` 和 `businessSelectConfig` 下发产品 SKU 选择配置；`subField` 中其他字段的 `attr` 是明细行对象属性，不带 `items.` 前缀。
- 当使用仅传主仓储的旧构造方式时，明细仓储为空会按空列表处理，不再抛出空指针
