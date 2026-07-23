# 采购模块接口（01）

> 说明：当前采购模块处于 CRUD 骨架阶段，`bizStatus`、`approvalStatus`、`sourceType`、`taskStatus` 等状态类字段在代码中尚未落具体枚举，以下文档仅按当前字段结构与接口骨架描述；明确选项值需以后续业务枚举为准。

## 待采购任务

### `/erp/v1/purchase/list` - 待采购任务列表

- 请求方式：`POST`
- 入参：`PurchasePendingTaskListDTO`
- 返回：`ListBaseVO<PurchasePendingTaskListItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "purchaseOrgId": 1001,
  "taskNo": "PT20260723001",
  "sourceType": "sales_order",
  "sourceDocId": 2001,
  "sourceLineId": 3001,
  "sourceDocNo": "SO20260723001",
  "skuId": 5001,
  "skuCodeSnapshot": "SKU-001",
  "skuNameSnapshot": "采购物料A",
  "suggestedVendorId": 9001,
  "suggestedDeliveryDate": 1721952000000,
  "priorityLevel": 10,
  "taskStatus": "pending",
  "salesLinkedFlag": 1,
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
  "id": "待采购任务ID，可选",
  "purchaseOrgId": "采购组织ID，可选",
  "taskNo": "任务号，可选",
  "sourceType": "来源类型，可选，当前骨架未定义具体枚举值",
  "sourceDocId": "来源单据ID，可选",
  "sourceLineId": "来源单据行ID，可选",
  "sourceDocNo": "来源单号快照，可选",
  "skuId": "SKU ID，可选",
  "skuCodeSnapshot": "SKU编码快照，可选",
  "skuNameSnapshot": "SKU名称快照，可选",
  "suggestedVendorId": "建议供应商ID，可选",
  "suggestedDeliveryDate": "建议交期时间戳，可选",
  "priorityLevel": "优先级，可选，整数值越大优先级越高",
  "taskStatus": "任务状态，可选，当前骨架未定义具体枚举值",
  "salesLinkedFlag": "是否以销定购，可选，0=否，1=是",
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
      "taskNo": "PT20260723001",
      "sourceType": "sales_order",
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "needQty": 120.5,
      "occupiedQty": 20,
      "generatedRequestQty": 50,
      "generatedOrderQty": 30,
      "closedQty": 0,
      "priorityLevel": 10,
      "taskStatus": "pending",
      "salesLinkedFlag": 1,
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

### `/erp/v1/purchase/addItem` - 待采购任务新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<PurchasePendingTaskSaveItemVO>`

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

### `/erp/v1/purchase/updateItem` - 待采购任务编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<PurchasePendingTaskSaveItemVO>`

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
      "taskNo": "PT20260723001",
      "sourceType": "sales_order",
      "sourceDocId": 2001,
      "sourceLineId": 3001,
      "sourceDocNo": "SO20260723001",
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "needQty": 120.5,
      "occupiedQty": 20,
      "generatedRequestQty": 50,
      "generatedOrderQty": 30,
      "closedQty": 0,
      "suggestedVendorId": 9001,
      "suggestedDeliveryDate": 1721952000000,
      "priorityLevel": 10,
      "taskStatus": "pending",
      "salesLinkedFlag": 1,
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

### `/erp/v1/purchase/save` - 待采购任务保存

- 请求方式：`POST`
- 入参：`PurchasePendingTaskSaveDTO`
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
    "taskNo": "PT20260723001",
    "sourceType": "sales_order",
    "sourceDocId": 2001,
    "sourceLineId": 3001,
    "sourceDocNo": "SO20260723001",
    "skuId": 5001,
    "skuCodeSnapshot": "SKU-001",
    "skuNameSnapshot": "采购物料A",
    "needQty": 120.5,
    "occupiedQty": 20,
    "generatedRequestQty": 50,
    "generatedOrderQty": 30,
    "closedQty": 0,
    "suggestedVendorId": 9001,
    "suggestedDeliveryDate": 1721952000000,
    "priorityLevel": 10,
    "taskStatus": "pending",
    "salesLinkedFlag": 1,
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
    "id": "待采购任务ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "purchaseOrgId": "采购组织ID",
    "taskNo": "任务号",
    "sourceType": "来源类型，当前骨架未定义具体枚举值",
    "sourceDocId": "来源单据ID",
    "sourceLineId": "来源单据行ID",
    "sourceDocNo": "来源单号快照",
    "skuId": "SKU ID",
    "skuCodeSnapshot": "SKU编码快照",
    "skuNameSnapshot": "SKU名称快照",
    "needQty": "需求数量",
    "occupiedQty": "已占用数量",
    "generatedRequestQty": "已生成申请数量",
    "generatedOrderQty": "已生成订单数量",
    "closedQty": "已关闭数量",
    "suggestedVendorId": "建议供应商ID",
    "suggestedDeliveryDate": "建议交期时间戳",
    "priorityLevel": "优先级",
    "taskStatus": "任务状态，当前骨架未定义具体枚举值",
    "salesLinkedFlag": "是否以销定购，0=否，1=是",
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

### `/erp/v1/purchase/detail` - 待采购任务详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`PurchasePendingTaskDetailVO`

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
      "taskNo": "PT20260723001",
      "sourceType": "sales_order",
      "sourceDocId": 2001,
      "sourceLineId": 3001,
      "sourceDocNo": "SO20260723001",
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "needQty": 120.5,
      "occupiedQty": 20,
      "generatedRequestQty": 50,
      "generatedOrderQty": 30,
      "closedQty": 0,
      "suggestedVendorId": 9001,
      "suggestedDeliveryDate": 1721952000000,
      "priorityLevel": 10,
      "taskStatus": "pending",
      "salesLinkedFlag": 1,
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

### `/erp/v1/purchase/delete` - 待采购任务删除

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
  "idList": "待删除待采购任务ID列表"
}
```

#### 响应

```json
null
```

## 采购申请单

### `/erp/v1/purchase/list` - 采购申请单列表

- 请求方式：`POST`
- 入参：`PurchaseRequestListDTO`
- 返回：`ListBaseVO<PurchaseRequestListItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "purchaseOrgId": 1001,
  "requestNo": "PR20260723001",
  "requestDeptId": 3001,
  "applicantId": "emp-001",
  "sourceType": "pending_task",
  "sourceNo": "PT20260723001",
  "suggestedVendorId": 9001,
  "suggestedDeliveryDate": 1721952000000,
  "bizStatus": "draft",
  "approvalStatus": "pending",
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
  "id": "采购申请单ID，可选",
  "purchaseOrgId": "采购组织ID，可选",
  "requestNo": "采购申请单号，可选",
  "requestDeptId": "申请部门ID，可选",
  "applicantId": "申请人ID，可选，字符串",
  "sourceType": "来源类型，可选，当前骨架未定义具体枚举值",
  "sourceNo": "来源单号，可选",
  "suggestedVendorId": "建议供应商ID，可选",
  "suggestedDeliveryDate": "建议交期时间戳，可选",
  "bizStatus": "业务状态，可选，当前骨架未定义具体枚举值",
  "approvalStatus": "审批状态，可选，当前骨架未定义具体枚举值",
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
      "requestNo": "PR20260723001",
      "sourceType": "pending_task",
      "bizStatus": "draft",
      "approvalStatus": "pending",
      "grossAmount": 1200.5,
      "netAmount": 1100,
      "taxAmount": 100.5,
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

### `/erp/v1/purchase/addItem` - 采购申请单新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<PurchaseRequestSaveItemVO>`

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

### `/erp/v1/purchase/updateItem` - 采购申请单编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<PurchaseRequestSaveItemVO>`

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
      "requestNo": "PR20260723001",
      "requestDeptId": 3001,
      "applicantId": "emp-001",
      "sourceType": "pending_task",
      "sourceNo": "PT20260723001",
      "suggestedVendorId": 9001,
      "suggestedDeliveryDate": 1721952000000,
      "bizStatus": "draft",
      "approvalStatus": "pending",
      "grossAmount": 1200.5,
      "netAmount": 1100,
      "taxAmount": 100.5,
      "version": 1,
      "remark": "首次采购申请",
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/save` - 采购申请单保存

- 请求方式：`POST`
- 入参：`PurchaseRequestSaveDTO`
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
    "requestNo": "PR20260723001",
    "requestDeptId": 3001,
    "applicantId": "emp-001",
    "sourceType": "pending_task",
    "sourceNo": "PT20260723001",
    "suggestedVendorId": 9001,
    "suggestedDeliveryDate": 1721952000000,
    "bizStatus": "draft",
    "approvalStatus": "pending",
    "grossAmount": 1200.5,
    "netAmount": 1100,
    "taxAmount": 100.5,
    "version": 1,
    "remark": "首次采购申请",
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
    "id": "采购申请单ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "purchaseOrgId": "采购组织ID",
    "requestNo": "采购申请单号",
    "requestDeptId": "申请部门ID",
    "applicantId": "申请人ID，字符串",
    "sourceType": "来源类型，当前骨架未定义具体枚举值",
    "sourceNo": "来源单号",
    "suggestedVendorId": "建议供应商ID",
    "suggestedDeliveryDate": "建议交期时间戳",
    "bizStatus": "业务状态，当前骨架未定义具体枚举值",
    "approvalStatus": "审批状态，当前骨架未定义具体枚举值",
    "grossAmount": "估算含税金额",
    "netAmount": "估算未税金额",
    "taxAmount": "估算税额",
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

### `/erp/v1/purchase/detail` - 采购申请单详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`PurchaseRequestDetailVO`

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
      "requestNo": "PR20260723001",
      "requestDeptId": 3001,
      "applicantId": "emp-001",
      "sourceType": "pending_task",
      "sourceNo": "PT20260723001",
      "suggestedVendorId": 9001,
      "suggestedDeliveryDate": 1721952000000,
      "bizStatus": "draft",
      "approvalStatus": "pending",
      "grossAmount": 1200.5,
      "netAmount": 1100,
      "taxAmount": 100.5,
      "version": 1,
      "remark": "首次采购申请",
      "deleted": 0,
      "addTime": 1721692800000,
      "updateTime": 1721779200000,
      "creatorId": "u-001",
      "modifyId": "u-002"
    }
  }
}
```

### `/erp/v1/purchase/delete` - 采购申请单删除

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
  "idList": "待删除采购申请单ID列表"
}
```

#### 响应

```json
null
```

## 采购申请单行

### `/erp/v1/purchase/list` - 采购申请单行列表

- 请求方式：`POST`
- 入参：`PurchaseRequestItemListDTO`
- 返回：`ListBaseVO<PurchaseRequestItemListItemVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "requestId": 10001,
  "lineNo": 1,
  "skuId": 5001,
  "skuCodeSnapshot": "SKU-001",
  "skuNameSnapshot": "采购物料A",
  "purchaseUnitId": 7001,
  "suggestedVendorId": 9001,
  "suggestedDeliveryDate": 1721952000000,
  "pageNum": 1,
  "pageSize": 20,
  "offset": 0,
  "groupByStr": "request_id",
  "orderByStr": "line_no asc"
}
```

#### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "id": "采购申请单行ID，可选",
  "requestId": "采购申请单头ID，可选",
  "lineNo": "行号，可选",
  "skuId": "SKU ID，可选",
  "skuCodeSnapshot": "SKU编码快照，可选",
  "skuNameSnapshot": "SKU名称快照，可选",
  "purchaseUnitId": "采购单位ID，可选",
  "suggestedVendorId": "建议供应商ID，可选",
  "suggestedDeliveryDate": "建议交期时间戳，可选",
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
      "requestId": 10001,
      "lineNo": 1,
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "requestQty": 100,
      "reservedQty": 30,
      "executedQty": 20,
      "closedQty": 0,
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

### `/erp/v1/purchase/addItem` - 采购申请单行新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<PurchaseRequestItemSaveItemVO>`

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

### `/erp/v1/purchase/updateItem` - 采购申请单行编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<PurchaseRequestItemSaveItemVO>`

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
      "requestId": 10001,
      "lineNo": 1,
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "specSnapshot": "10kg/箱",
      "purchaseUnitId": 7001,
      "requestQty": 100,
      "reservedQty": 30,
      "executedQty": 20,
      "closedQty": 0,
      "suggestedVendorId": 9001,
      "suggestedDeliveryDate": 1721952000000,
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

### `/erp/v1/purchase/save` - 采购申请单行保存

- 请求方式：`POST`
- 入参：`PurchaseRequestItemSaveDTO`
- 返回：`Long`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
    "id": 1,
    "corpid": "corp-001",
    "requestId": 10001,
    "lineNo": 1,
    "skuId": 5001,
    "skuCodeSnapshot": "SKU-001",
    "skuNameSnapshot": "采购物料A",
    "specSnapshot": "10kg/箱",
    "purchaseUnitId": 7001,
    "requestQty": 100,
    "reservedQty": 30,
    "executedQty": 20,
    "closedQty": 0,
    "suggestedVendorId": 9001,
    "suggestedDeliveryDate": 1721952000000,
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
    "id": "采购申请单行ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "requestId": "采购申请单头ID",
    "lineNo": "行号",
    "skuId": "SKU ID",
    "skuCodeSnapshot": "SKU编码快照",
    "skuNameSnapshot": "SKU名称快照",
    "specSnapshot": "规格快照",
    "purchaseUnitId": "采购单位ID",
    "requestQty": "申请数量",
    "reservedQty": "已占用下推量",
    "executedQty": "已正式下推量",
    "closedQty": "已关闭量",
    "suggestedVendorId": "建议供应商ID",
    "suggestedDeliveryDate": "建议交期时间戳",
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

### `/erp/v1/purchase/detail` - 采购申请单行详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`PurchaseRequestItemDetailVO`

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
      "requestId": 10001,
      "lineNo": 1,
      "skuId": 5001,
      "skuCodeSnapshot": "SKU-001",
      "skuNameSnapshot": "采购物料A",
      "specSnapshot": "10kg/箱",
      "purchaseUnitId": 7001,
      "requestQty": 100,
      "reservedQty": 30,
      "executedQty": 20,
      "closedQty": 0,
      "suggestedVendorId": 9001,
      "suggestedDeliveryDate": 1721952000000,
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

### `/erp/v1/purchase/delete` - 采购申请单行删除

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
  "idList": "待删除采购申请单行ID列表"
}
```

#### 响应

```json
null
```
