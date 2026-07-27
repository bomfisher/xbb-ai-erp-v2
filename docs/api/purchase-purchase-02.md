# 采购模块接口（02）

> 说明：当前采购模块处于 CRUD 骨架阶段，`bizStatus`、`approvalStatus`、`executionStatus`、`receiptStatus`、`inboundStatus`、`payableStatus`、`invoiceStatus`、`paymentStatus`、`relationStatus`、`sourceDocType`、`targetDocType` 等状态/类型字段在代码中尚未落具体枚举，以下文档仅按当前字段结构与接口骨架描述；明确选项值需以后续业务枚举为准。

## 采购订单

### `/erp/v1/purchase/order/list` - 采购订单列表

- 请求方式：`POST`
- 入参：`PurchaseOrderListDTO`
- 返回：`ListBaseVO<PurchaseOrderListItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "purchaseOrgId": 1001,
  "orderNo": "PO20260723001",
  "vendorId": 9001,
  "vendorNameSnapshot": "杭州供应商A",
  "purchaserId": "buyer-001",
  "warehouseId": 8001,
  "settlementMethodId": 6001,
  "currencyCode": "CNY",
  "deliveryDate": 1721952000000,
  "sourceType": "purchase_request",
  "sourceNo": "PR20260723001",
  "salesLinkedFlag": 1,
  "bizStatus": "draft",
  "approvalStatus": "pending",
  "executionStatus": "not_started",
  "receiptStatus": "not_received",
  "inboundStatus": "not_inbounded",
  "payableStatus": "not_confirmed",
  "invoiceStatus": "not_received",
  "paymentStatus": "not_paid",
  "periodLockedFlag": 0,
  "pageNum": 1,
  "pageSize": 20,
  "offset": 0,
  "groupByStr": "purchase_org_id",
  "orderByStr": "id desc"
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "id": "采购订单ID，可选",
  "purchaseOrgId": "采购组织ID，可选",
  "orderNo": "采购订单号，可选",
  "vendorId": "供应商ID，可选",
  "vendorNameSnapshot": "供应商名称快照，可选",
  "purchaserId": "采购员ID，可选，字符串",
  "warehouseId": "默认收货仓库ID，可选",
  "settlementMethodId": "结算方式ID，可选",
  "currencyCode": "币种编码，可选",
  "deliveryDate": "交货日期时间戳，可选",
  "sourceType": "来源类型，可选，当前骨架未定义具体枚举值",
  "sourceNo": "来源单号，可选",
  "salesLinkedFlag": "是否以销定购，可选，0=否，1=是",
  "bizStatus": "业务状态，可选，当前骨架未定义具体枚举值",
  "approvalStatus": "审批状态，可选，当前骨架未定义具体枚举值",
  "executionStatus": "执行状态，可选，当前骨架未定义具体枚举值",
  "receiptStatus": "收料状态，可选，当前骨架未定义具体枚举值",
  "inboundStatus": "入库状态，可选，当前骨架未定义具体枚举值",
  "payableStatus": "应付状态，可选，当前骨架未定义具体枚举值",
  "invoiceStatus": "收票状态，可选，当前骨架未定义具体枚举值",
  "paymentStatus": "付款状态，可选，当前骨架未定义具体枚举值",
  "periodLockedFlag": "期间锁定标志，可选，0=未锁定，1=已锁定",
  "pageNum": "页码，可选，默认1",
  "pageSize": "每页条数，可选",
  "offset": "起始偏移量，可选",
  "groupByStr": "分组字段，可选，直接透传到 group by",
  "orderByStr": "排序字段，可选，直接透传到 order by"
}
```

#### 响应示例

```json
{
  "headList": null,
  "list": [
    {
      "id": 1,
      "purchaseOrgId": 1001,
      "orderNo": "PO20260723001",
      "vendorId": 9001,
      "vendorNameSnapshot": "杭州供应商A",
      "purchaserNameSnapshot": "张三",
      "warehouseNameSnapshot": "杭州一号仓",
      "currencyCode": "CNY",
      "deliveryDate": 1721952000000,
      "bizStatus": "draft",
      "approvalStatus": "pending",
      "executionStatus": "not_started",
      "receiptStatus": "not_received",
      "inboundStatus": "not_inbounded",
      "grossAmount": 2500,
      "netAmount": 2300,
      "taxAmount": 200,
      "inboundedQtySummary": 0,
      "uninboundedQtySummary": 100,
      "closedQtySummary": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000
    }
  ],
  "pageHelper": {
    "page": 1,
    "count": 1,
    "hasLeft": false,
    "hasRight": false
  }
}
```

### `/erp/v1/purchase/order/addItem` - 采购订单新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<PurchaseOrderSaveItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001"
}
```

#### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": null
  }
}
```

### `/erp/v1/purchase/order/updateItem` - 采购订单编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<PurchaseOrderSaveItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

#### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "purchaseOrgId": 1001,
      "orderNo": "PO20260723001",
      "vendorId": 9001,
      "vendorNameSnapshot": "杭州供应商A",
      "purchaserId": "buyer-001",
      "purchaserNameSnapshot": "张三",
      "warehouseId": 8001,
      "warehouseNameSnapshot": "杭州一号仓",
      "settlementMethodId": 6001,
      "settlementMethodSnapshot": "月结30天",
      "paymentTermSnapshot": "月底对账后30天付款",
      "currencyCode": "CNY",
      "deliveryDate": 1721952000000,
      "sourceType": "purchase_request",
      "sourceNo": "PR20260723001",
      "salesLinkedFlag": 1,
      "bizStatus": "draft",
      "approvalStatus": "pending",
      "executionStatus": "not_started",
      "receiptStatus": "not_received",
      "inboundStatus": "not_inbounded",
      "payableStatus": "not_confirmed",
      "invoiceStatus": "not_received",
      "paymentStatus": "not_paid",
      "grossAmount": 2500,
      "netAmount": 2300,
      "taxAmount": 200,
      "inboundedQtySummary": 0,
      "uninboundedQtySummary": 100,
      "closedQtySummary": 0,
      "payableAmountSummary": 0,
      "paidAmountSummary": 0,
      "invoicedAmountSummary": 0,
      "lastInboundTime": null,
      "lastPayableTime": null,
      "periodLockedFlag": 0,
      "version": 1,
      "remark": "首次采购订单",
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/order/save` - 采购订单保存

- 请求方式：`POST`
- 入参：`PurchaseOrderSaveDTO`
- 返回：`Long`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
    "id": 1,
    "corpid": "corp-001",
    "purchaseOrgId": 1001,
    "orderNo": "PO20260723001",
    "vendorId": 9001,
    "vendorNameSnapshot": "杭州供应商A",
    "purchaserId": "buyer-001",
    "purchaserNameSnapshot": "张三",
    "warehouseId": 8001,
    "warehouseNameSnapshot": "杭州一号仓",
    "settlementMethodId": 6001,
    "settlementMethodSnapshot": "月结30天",
    "paymentTermSnapshot": "月底对账后30天付款",
    "currencyCode": "CNY",
    "deliveryDate": 1721952000000,
    "sourceType": "purchase_request",
    "sourceNo": "PR20260723001",
    "salesLinkedFlag": 1,
    "bizStatus": "draft",
    "approvalStatus": "pending",
    "executionStatus": "not_started",
    "receiptStatus": "not_received",
    "inboundStatus": "not_inbounded",
    "payableStatus": "not_confirmed",
    "invoiceStatus": "not_received",
    "paymentStatus": "not_paid",
    "grossAmount": 2500,
    "netAmount": 2300,
    "taxAmount": 200,
    "inboundedQtySummary": 0,
    "uninboundedQtySummary": 100,
    "closedQtySummary": 0,
    "payableAmountSummary": 0,
    "paidAmountSummary": 0,
    "invoicedAmountSummary": 0,
    "lastInboundTime": null,
    "lastPayableTime": null,
    "periodLockedFlag": 0,
    "version": 1,
    "remark": "首次采购订单",
    "deleted": 0,
    "addTime": 1721692800000,
    "updateTime": 1721779200000,
    "creatorId": "u-001",
    "modifyId": "u-001"
  }
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "main": {
    "id": "采购订单ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "purchaseOrgId": "采购组织ID",
    "orderNo": "采购订单号",
    "vendorId": "供应商ID",
    "vendorNameSnapshot": "供应商名称快照",
    "purchaserId": "采购员ID，字符串",
    "purchaserNameSnapshot": "采购员名称快照",
    "warehouseId": "默认收货仓库ID",
    "warehouseNameSnapshot": "仓库名称快照",
    "settlementMethodId": "结算方式ID",
    "settlementMethodSnapshot": "结算方式快照",
    "paymentTermSnapshot": "付款条件快照",
    "currencyCode": "币种编码",
    "deliveryDate": "交货日期时间戳",
    "sourceType": "来源类型，当前骨架未定义具体枚举值",
    "sourceNo": "来源单号",
    "salesLinkedFlag": "是否以销定购，0=否，1=是",
    "bizStatus": "业务状态，当前骨架未定义具体枚举值",
    "approvalStatus": "审批状态，当前骨架未定义具体枚举值",
    "executionStatus": "执行状态，当前骨架未定义具体枚举值",
    "receiptStatus": "收料状态，当前骨架未定义具体枚举值",
    "inboundStatus": "入库状态，当前骨架未定义具体枚举值",
    "payableStatus": "应付状态，当前骨架未定义具体枚举值",
    "invoiceStatus": "收票状态，当前骨架未定义具体枚举值",
    "paymentStatus": "付款状态，当前骨架未定义具体枚举值",
    "grossAmount": "含税金额",
    "netAmount": "未税金额",
    "taxAmount": "税额",
    "inboundedQtySummary": "已入库数量摘要",
    "uninboundedQtySummary": "未入库数量摘要",
    "closedQtySummary": "已关闭数量摘要",
    "payableAmountSummary": "已确认应付金额摘要",
    "paidAmountSummary": "已付金额摘要",
    "invoicedAmountSummary": "已收票金额摘要",
    "lastInboundTime": "最近入库时间戳",
    "lastPayableTime": "最近应付确认时间戳",
    "periodLockedFlag": "期间锁定标志，0=未锁定，1=已锁定",
    "version": "版本号",
    "remark": "备注",
    "deleted": "删除标记，0=未删除，1=已删除",
    "addTime": "创建时间戳",
    "updateTime": "更新时间戳",
    "creatorId": "创建人ID",
    "modifyId": "修改人ID"
  }
}
```

#### 响应示例

```json
1
```

### `/erp/v1/purchase/order/detail` - 采购订单详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`PurchaseOrderDetailVO`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

#### 响应示例

```json
{
  "mainData": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "purchaseOrgId": 1001,
      "orderNo": "PO20260723001",
      "vendorId": 9001,
      "vendorNameSnapshot": "杭州供应商A",
      "purchaserId": "buyer-001",
      "purchaserNameSnapshot": "张三",
      "warehouseId": 8001,
      "warehouseNameSnapshot": "杭州一号仓",
      "settlementMethodId": 6001,
      "settlementMethodSnapshot": "月结30天",
      "paymentTermSnapshot": "月底对账后30天付款",
      "currencyCode": "CNY",
      "deliveryDate": 1721952000000,
      "sourceType": "purchase_request",
      "sourceNo": "PR20260723001",
      "salesLinkedFlag": 1,
      "bizStatus": "draft",
      "approvalStatus": "pending",
      "executionStatus": "not_started",
      "receiptStatus": "not_received",
      "inboundStatus": "not_inbounded",
      "payableStatus": "not_confirmed",
      "invoiceStatus": "not_received",
      "paymentStatus": "not_paid",
      "grossAmount": 2500,
      "netAmount": 2300,
      "taxAmount": 200,
      "inboundedQtySummary": 0,
      "uninboundedQtySummary": 100,
      "closedQtySummary": 0,
      "payableAmountSummary": 0,
      "paidAmountSummary": 0,
      "invoicedAmountSummary": 0,
      "lastInboundTime": null,
      "lastPayableTime": null,
      "periodLockedFlag": 0,
      "version": 1,
      "remark": "首次采购订单",
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/order/delete` - 采购订单删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`void`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "idList": [1, 2]
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "idList": "待删除采购订单ID列表"
}
```

#### 响应

```json
null
```

## 采购订单行

### `/erp/v1/purchase/order-item/list` - 采购订单行列表

- 请求方式：`POST`
- 入参：`PurchaseOrderItemListDTO`
- 返回：`ListBaseVO<PurchaseOrderItemListItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "orderId": 20001,
  "lineNo": 1,
  "skuId": 5001,
  "skuCodeSnapshot": "SKU-001",
  "skuNameSnapshot": "采购物料A",
  "purchaseUnitId": 7001,
  "warehouseId": 8001,
  "isGift": 0,
  "pageNum": 1,
  "pageSize": 20,
  "offset": 0,
  "groupByStr": "order_id",
  "orderByStr": "line_no asc"
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "id": "采购订单行ID，可选",
  "orderId": "采购订单头ID，可选",
  "lineNo": "行号，可选",
  "skuId": "SKU ID，可选",
  "skuCodeSnapshot": "SKU编码快照，可选",
  "skuNameSnapshot": "SKU名称快照，可选",
  "purchaseUnitId": "采购单位ID，可选",
  "warehouseId": "行级收货仓库ID，可选",
  "isGift": "是否赠品，可选，0=否，1=是",
  "pageNum": "页码，可选，默认1",
  "pageSize": "每页条数，可选",
  "offset": "起始偏移量，可选",
  "groupByStr": "分组字段，可选，直接透传到 group by",
  "orderByStr": "排序字段，可选，直接透传到 order by"
}
```

#### 响应示例

```json
{
  "headList": null,
  "list": [
    {
      "id": 1,
      "orderId": 20001,
      "lineNo": 1,
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "orderQty": 100,
      "receivedQty": 0,
      "inboundedQty": 0,
      "closedQty": 0,
      "returnedQty": 0,
      "grossPrice": 25,
      "netPrice": 23,
      "taxRate": 0.13,
      "taxAmount": 200,
      "grossAmount": 2500,
      "netAmount": 2300,
      "addTime": 1721692800000,
      "updateTime": 1721779200000
    }
  ],
  "pageHelper": {
    "page": 1,
    "count": 1,
    "hasLeft": false,
    "hasRight": false
  }
}
```

### `/erp/v1/purchase/order-item/addItem` - 采购订单行新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<PurchaseOrderItemSaveItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001"
}
```

#### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": null
  }
}
```

### `/erp/v1/purchase/order-item/updateItem` - 采购订单行编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<PurchaseOrderItemSaveItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

#### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "orderId": 20001,
      "lineNo": 1,
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "specSnapshot": "10kg/箱",
      "purchaseUnitId": 7001,
      "warehouseId": 8001,
      "orderQty": 100,
      "receivedQty": 0,
      "inboundedQty": 0,
      "closedQty": 0,
      "returnedQty": 0,
      "grossPrice": 25,
      "netPrice": 23,
      "taxRate": 0.13,
      "taxAmount": 200,
      "grossAmount": 2500,
      "netAmount": 2300,
      "payableAmount": 0,
      "paidAmount": 0,
      "invoicedAmount": 0,
      "isGift": 0,
      "deliveryPlanSnapshot": "2026-07-25 首批到货 60，2026-07-28 到货 40",
      "version": 1,
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/order-item/save` - 采购订单行保存

- 请求方式：`POST`
- 入参：`PurchaseOrderItemSaveDTO`
- 返回：`Long`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
    "id": 1,
    "corpid": "corp-001",
    "orderId": 20001,
    "lineNo": 1,
    "skuId": 5001,
    "skuCodeSnapshot": "SKU-001",
    "skuNameSnapshot": "采购物料A",
    "specSnapshot": "10kg/箱",
    "purchaseUnitId": 7001,
    "warehouseId": 8001,
    "orderQty": 100,
    "receivedQty": 0,
    "inboundedQty": 0,
    "closedQty": 0,
    "returnedQty": 0,
    "grossPrice": 25,
    "netPrice": 23,
    "taxRate": 0.13,
    "taxAmount": 200,
    "grossAmount": 2500,
    "netAmount": 2300,
    "payableAmount": 0,
    "paidAmount": 0,
    "invoicedAmount": 0,
    "isGift": 0,
    "deliveryPlanSnapshot": "2026-07-25 首批到货 60，2026-07-28 到货 40",
    "version": 1,
    "deleted": 0,
    "addTime": 1721692800000,
    "updateTime": 1721779200000,
    "creatorId": "u-001",
    "modifyId": "u-001"
  }
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "main": {
    "id": "采购订单行ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "orderId": "采购订单头ID",
    "lineNo": "行号",
    "skuId": "SKU ID",
    "skuCodeSnapshot": "SKU编码快照",
    "skuNameSnapshot": "SKU名称快照",
    "specSnapshot": "规格快照",
    "purchaseUnitId": "采购单位ID",
    "warehouseId": "行级收货仓库ID",
    "orderQty": "订单数量",
    "receivedQty": "已收料数量",
    "inboundedQty": "已入库数量",
    "closedQty": "已关闭数量",
    "returnedQty": "已退料数量",
    "grossPrice": "含税单价",
    "netPrice": "未税单价",
    "taxRate": "税率，如 0.13 表示 13%",
    "taxAmount": "税额",
    "grossAmount": "含税金额",
    "netAmount": "未税金额",
    "payableAmount": "已确认应付金额",
    "paidAmount": "已付金额",
    "invoicedAmount": "已收票金额",
    "isGift": "是否赠品，0=否，1=是",
    "deliveryPlanSnapshot": "交货计划快照",
    "version": "版本号",
    "deleted": "删除标记，0=未删除，1=已删除",
    "addTime": "创建时间戳",
    "updateTime": "更新时间戳",
    "creatorId": "创建人ID",
    "modifyId": "修改人ID"
  }
}
```

#### 响应示例

```json
1
```

### `/erp/v1/purchase/order-item/detail` - 采购订单行详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`PurchaseOrderItemDetailVO`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

#### 响应示例

```json
{
  "mainData": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "orderId": 20001,
      "lineNo": 1,
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "specSnapshot": "10kg/箱",
      "purchaseUnitId": 7001,
      "warehouseId": 8001,
      "orderQty": 100,
      "receivedQty": 0,
      "inboundedQty": 0,
      "closedQty": 0,
      "returnedQty": 0,
      "grossPrice": 25,
      "netPrice": 23,
      "taxRate": 0.13,
      "taxAmount": 200,
      "grossAmount": 2500,
      "netAmount": 2300,
      "payableAmount": 0,
      "paidAmount": 0,
      "invoicedAmount": 0,
      "isGift": 0,
      "deliveryPlanSnapshot": "2026-07-25 首批到货 60，2026-07-28 到货 40",
      "version": 1,
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/order-item/delete` - 采购订单行删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`void`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "idList": [1, 2]
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "idList": "待删除采购订单行ID列表"
}
```

#### 响应

```json
null
```

## 采购来源关系

### `/erp/v1/purchase/list` - 采购来源关系列表

- 请求方式：`POST`
- 入参：`PurchaseSourceRelationListDTO`
- 返回：`ListBaseVO<PurchaseSourceRelationListItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "sourceDocType": "purchase_request",
  "sourceDocId": 10001,
  "sourceLineId": 100011,
  "targetDocType": "purchase_order",
  "targetDocId": 20001,
  "targetLineId": 200011,
  "relationStatus": "active",
  "pageNum": 1,
  "pageSize": 20,
  "offset": 0,
  "groupByStr": "source_doc_type",
  "orderByStr": "id desc"
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "id": "采购来源关系ID，可选",
  "sourceDocType": "来源单据类型，可选，当前骨架未定义具体枚举值",
  "sourceDocId": "来源单据ID，可选",
  "sourceLineId": "来源单据行ID，可选",
  "targetDocType": "目标单据类型，可选，当前骨架未定义具体枚举值",
  "targetDocId": "目标单据ID，可选",
  "targetLineId": "目标单据行ID，可选",
  "relationStatus": "关系状态，可选，当前骨架未定义具体枚举值",
  "pageNum": "页码，可选，默认1",
  "pageSize": "每页条数，可选",
  "offset": "起始偏移量，可选",
  "groupByStr": "分组字段，可选，直接透传到 group by",
  "orderByStr": "排序字段，可选，直接透传到 order by"
}
```

#### 响应示例

```json
{
  "headList": null,
  "list": [
    {
      "id": 1,
      "sourceDocType": "purchase_request",
      "sourceDocId": 10001,
      "targetDocType": "purchase_order",
      "targetDocId": 20001,
      "sourceQty": 100,
      "reservedQty": 40,
      "executedQty": 20,
      "closedQty": 0,
      "reversedQty": 0,
      "relationStatus": "active",
      "addTime": 1721692800000,
      "updateTime": 1721779200000
    }
  ],
  "pageHelper": {
    "page": 1,
    "count": 1,
    "hasLeft": false,
    "hasRight": false
  }
}
```

### `/erp/v1/purchase/addItem` - 采购来源关系新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<PurchaseSourceRelationSaveItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001"
}
```

#### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": null
  }
}
```

### `/erp/v1/purchase/updateItem` - 采购来源关系编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<PurchaseSourceRelationSaveItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

#### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "sourceDocType": "purchase_request",
      "sourceDocId": 10001,
      "sourceLineId": 100011,
      "targetDocType": "purchase_order",
      "targetDocId": 20001,
      "targetLineId": 200011,
      "sourceQty": 100,
      "reservedQty": 40,
      "executedQty": 20,
      "closedQty": 0,
      "reversedQty": 0,
      "relationStatus": "active",
      "version": 1,
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/save` - 采购来源关系保存

- 请求方式：`POST`
- 入参：`PurchaseSourceRelationSaveDTO`
- 返回：`Long`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
    "id": 1,
    "corpid": "corp-001",
    "sourceDocType": "purchase_request",
    "sourceDocId": 10001,
    "sourceLineId": 100011,
    "targetDocType": "purchase_order",
    "targetDocId": 20001,
    "targetLineId": 200011,
    "sourceQty": 100,
    "reservedQty": 40,
    "executedQty": 20,
    "closedQty": 0,
    "reversedQty": 0,
    "relationStatus": "active",
    "version": 1,
    "deleted": 0,
    "addTime": 1721692800000,
    "updateTime": 1721779200000,
    "creatorId": "u-001",
    "modifyId": "u-001"
  }
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "main": {
    "id": "采购来源关系ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "sourceDocType": "来源单据类型，当前骨架未定义具体枚举值",
    "sourceDocId": "来源单据ID",
    "sourceLineId": "来源单据行ID",
    "targetDocType": "目标单据类型，当前骨架未定义具体枚举值",
    "targetDocId": "目标单据ID",
    "targetLineId": "目标单据行ID",
    "sourceQty": "来源总量",
    "reservedQty": "已占用量",
    "executedQty": "已执行量",
    "closedQty": "已关闭量",
    "reversedQty": "已逆向回退量",
    "relationStatus": "关系状态，当前骨架未定义具体枚举值",
    "version": "版本号",
    "deleted": "删除标记，0=未删除，1=已删除",
    "addTime": "创建时间戳",
    "updateTime": "更新时间戳",
    "creatorId": "创建人ID",
    "modifyId": "修改人ID"
  }
}
```

#### 响应示例

```json
1
```

### `/erp/v1/purchase/detail` - 采购来源关系详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`PurchaseSourceRelationDetailVO`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

#### 响应示例

```json
{
  "mainData": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "sourceDocType": "purchase_request",
      "sourceDocId": 10001,
      "sourceLineId": 100011,
      "targetDocType": "purchase_order",
      "targetDocId": 20001,
      "targetLineId": 200011,
      "sourceQty": 100,
      "reservedQty": 40,
      "executedQty": 20,
      "closedQty": 0,
      "reversedQty": 0,
      "relationStatus": "active",
      "version": 1,
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/delete` - 采购来源关系删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`void`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "idList": [1, 2]
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "idList": "待删除采购来源关系ID列表"
}
```

#### 响应

```json
null
```
