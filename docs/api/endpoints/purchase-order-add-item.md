# 采购订单新增表单

## 文档信息
- 领域：`purchase`
- 控制器：`PurchaseOrderAdminController#addItem`
- 请求方式：`POST /erp/v1/purchase/purchaseOrder/addItem`
- 聚合文档引用：`docs/kn/purchase-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001"
}
```

## 响应规则

响应为 `ResultVO<SaveItemVO<PurchaseOrderSaveItemVO>>`。`data.headList` 提供字段元数据，`data.formSections` 提供可选的表单分组布局，`data.data` 提供初始化表单数据。

采购订单当前返回以下分组，按 `order` 升序渲染：

| key | 标题 | order | fields |
|---|---|---:|---|
| `basic` | 基本信息 | 10 | `main.orderNo`、`main.orderDate`、`main.supplierId`、`main.expectedDate` |
| `items` | 商品信息 | 20 | `items` |
| `amount` | 金额信息 | 30 | `main.totalAmount` |
| `remark` | 备注信息 | 40 | `main.remark` |

`formSections` 为空或缺失时，前端必须回退到按 `headList[].attr` 前缀分组的旧逻辑。分组只影响展示，不改变字段属性路径和保存请求结构。

## 规则说明

- `main.orderNo` 由服务端生成并写入新建数据。
- `items` 是 `SUB_ITEM(49)`，其列定义由 `headList[].subField` 下发。
- `formSections[].fields` 只引用 `headList[].attr`；未被引用的字段不能丢失，应由前端放入兼容的其他信息分组。
- `linkageConfig.itemStock` 指定产品行的 `skuId`、`warehouseId` 和 `currentStock` 字段；前端在选择产品或改变仓库时通过库存公共接口刷新当前库存数量。
- `linkageConfig.rowAmount` 指定产品行的 `qty × unitPrice` 计算结果写入临时 `amount` 字段；`linkageConfig.aggregateAmount` 将所有产品行金额合计回写到只读的 `main.totalAmount`。
