# 采购模块接口（01）

> 说明：当前采购模块处于 CRUD 骨架阶段，`bizStatus`、`approvalStatus`、`sourceType`、`taskStatus` 等状态类字段在代码中尚未落具体枚举，以下文档仅按当前字段结构与接口骨架描述；明确选项值需以后续业务枚举为准。

## 待采购任务

### `/erp/v1/purchase/pending-task/list` - 待采购任务列表

- 请求方式：`POST`
- 入参：`PurchasePendingTaskListDTO`
- 返回：`ResultVO<ListBaseVO<PurchasePendingTaskListItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/pending-task/addItem` - 待采购任务新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchasePendingTaskSaveItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": null,
    "data": {
      "main": null
    }
  }
}
```

### `/erp/v1/purchase/pending-task/updateItem` - 待采购任务编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchasePendingTaskSaveItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/pending-task/save` - 待采购任务保存

- 请求方式：`POST`
- 入参：`PurchasePendingTaskSaveDTO`
- 返回：`ResultVO<Long>`

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
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 1
}
```

### `/erp/v1/purchase/pending-task/detail` - 待采购任务详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<PurchasePendingTaskDetailVO>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/pending-task/delete` - 待采购任务删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`ResultVO<Void>`（实际返回 `ResultVO.success(null)`，即 `data = null`）

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
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": null
}
```

## 采购来源关系

### `/erp/v1/purchase/source-relation/list` - 采购来源关系列表

- 请求方式：`POST`
- 入参：`PurchaseSourceRelationListDTO`
- 返回：`ResultVO<ListBaseVO<PurchaseSourceRelationListItemVO>>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "sourceDocType": "purchase_request",
  "sourceDocId": 2001,
  "sourceLineId": 3001,
  "targetDocType": "purchase_order",
  "targetDocId": 4001,
  "targetLineId": 5001,
  "relationStatus": "linked",
  "pageNum": 1,
  "pageSize": 20,
  "offset": 0,
  "groupByStr": "source_doc_id",
  "orderByStr": "id desc"
}
```

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": null,
    "list": [
      {
        "id": 1,
        "sourceDocType": "purchase_request",
        "sourceDocId": 2001,
        "sourceLineId": 3001,
        "targetDocType": "purchase_order",
        "targetDocId": 4001,
        "targetLineId": 5001,
        "relationStatus": "linked",
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
}
```

### `/erp/v1/purchase/source-relation/addItem` - 采购来源关系新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseSourceRelationSaveItemVO>>`

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": null,
    "data": {
      "main": null
    }
  }
}
```

### `/erp/v1/purchase/source-relation/updateItem` - 采购来源关系编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseSourceRelationSaveItemVO>>`

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": null,
    "data": {
      "main": {
        "id": 1,
        "corpid": "corp-001",
        "sourceDocType": "purchase_request",
        "sourceDocId": 2001,
        "sourceLineId": 3001,
        "targetDocType": "purchase_order",
        "targetDocId": 4001,
        "targetLineId": 5001,
        "relationStatus": "linked",
        "version": 1,
        "deleted": 0,
        "addTime": 1721692800000,
        "updateTime": 1721779200000,
        "creatorId": "u-001",
        "modifyId": "u-002"
      }
    }
  }
}
```

### `/erp/v1/purchase/source-relation/save` - 采购来源关系保存

- 请求方式：`POST`
- 入参：`PurchaseSourceRelationSaveDTO`
- 返回：`ResultVO<Long>`

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 1
}
```

### `/erp/v1/purchase/source-relation/detail` - 采购来源关系详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<PurchaseSourceRelationDetailVO>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "mainData": {
      "main": {
        "id": 1,
        "corpid": "corp-001",
        "sourceDocType": "purchase_request",
        "sourceDocId": 2001,
        "sourceLineId": 3001,
        "targetDocType": "purchase_order",
        "targetDocId": 4001,
        "targetLineId": 5001,
        "relationStatus": "linked",
        "version": 1,
        "deleted": 0,
        "addTime": 1721692800000,
        "updateTime": 1721779200000,
        "creatorId": "u-001",
        "modifyId": "u-002"
      }
    }
  }
}
```

### `/erp/v1/purchase/source-relation/delete` - 采购来源关系删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`ResultVO<Void>`（实际返回 `ResultVO.success(null)`，即 `data = null`）

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": null
}
```

## 采购申请单

### `/erp/v1/purchase/request/list` - 采购申请单列表

- 请求方式：`POST`
- 入参：`PurchaseRequestListDTO`
- 返回：`ResultVO<ListBaseVO<PurchaseRequestListItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/request/addItem` - 采购申请单新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseRequestSaveItemVO>>`
- `headList` 中 `items.skuId` 使用 `fieldType = "50"` 和 `productSelectConfig`；其业务码为 `PURCHASE_REQUEST`，接口地址由前端产品常量维护。

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": null,
    "data": {
      "main": {},
      "items": [],
      "sectionState": {
        "items": null
      }
    }
  }
}
```

### `/erp/v1/purchase/request/updateItem` - 采购申请单编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseRequestSaveItemVO>>`
- 产品字段协议与新增表单一致；采购明细列严格以本接口返回的 `headList` 渲染。

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
      },
      "items": [
        {
          "id": 11,
          "corpid": "corp-001",
          "requestId": 1,
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
      ],
      "sectionState": {
        "items": 1
      }
    }
  }
}
```

### `/erp/v1/purchase/request/saveDraft` - 采购申请单保存草稿

- 请求方式：`POST`
- 入参：`PurchaseRequestDraftSaveDTO`
- 返回：`ResultVO<PurchaseRequestDraftSaveVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
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
    "grossAmount": 1200.5,
    "netAmount": 1100,
    "taxAmount": 100.5,
    "remark": "首次采购申请"
  },
  "items": [
    {
      "id": 11,
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
      "suggestedDeliveryDate": 1721952000000
    }
  ],
  "sectionState": {
    "items": 1
  },
  "draftMeta": {
    "draftCode": "draft-001",
    "draftTitle": "采购申请草稿",
    "updatedTime": 1721779200000
  }
}
```

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "draftCode": "draft-001"
  }
}
```

### `/erp/v1/purchase/request/saveAndSubmit` - 采购申请单保存并提交

- 请求方式：`POST`
- 入参：`PurchaseRequestSubmitSaveDTO`
- 返回：`ResultVO<BaseVO>`（实际返回 `ResultVO.success(new BaseVO())`）

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
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
    "grossAmount": 1200.5,
    "netAmount": 1100,
    "taxAmount": 100.5,
    "remark": "首次采购申请"
  },
  "items": [
    {
      "id": 11,
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
      "suggestedDeliveryDate": 1721952000000
    }
  ],
  "sectionState": {
    "items": 1
  },
  "draftMeta": {
    "draftCode": "draft-001",
    "draftTitle": "采购申请草稿",
    "updatedTime": 1721779200000
  }
}
```

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

### `/erp/v1/purchase/request/draftList` - 采购申请单草稿列表

- 请求方式：`POST`
- 入参：`PurchaseRequestDraftListDTO`
- 返回：`ResultVO<List<PurchaseRequestDraftListItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": [
    {
      "draftCode": "draft-001",
      "draftTitle": "采购申请草稿",
      "requestNo": "PR20260723001",
      "applicantId": "emp-001",
      "updatedTime": 1721779200000
    }
  ]
}
```

### `/erp/v1/purchase/request/loadDraft` - 采购申请单加载草稿

- 请求方式：`POST`
- 入参：`PurchaseRequestDraftLoadDTO`
- 返回：`ResultVO<PurchaseRequestDraftDetailVO>`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "draftCode": "draft-001"
}
```

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "main": {
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
      "grossAmount": 1200.5,
      "netAmount": 1100,
      "taxAmount": 100.5,
      "remark": "首次采购申请"
    },
    "items": [
      {
        "id": 11,
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
        "suggestedDeliveryDate": 1721952000000
      }
    ],
    "sectionState": {
      "items": 1
    },
    "draftMeta": {
      "draftCode": "draft-001",
      "draftTitle": "采购申请草稿",
      "updatedTime": 1721779200000
    }
  }
}
```

### `/erp/v1/purchase/request/save` - 采购申请单保存

- 请求方式：`POST`
- 入参：`PurchaseRequestSaveDTO`
- 返回：`ResultVO<Long>`

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
  },
  "items": [
    {
      "id": 11,
      "corpid": "corp-001",
      "requestId": 1,
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
  ]
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
  },
  "items": [
    {
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
  ]
}
```

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 1
}
```

### `/erp/v1/purchase/request/detail` - 采购申请单详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<PurchaseRequestDetailVO>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
      },
      "items": [
        {
          "id": 11,
          "corpid": "corp-001",
          "requestId": 1,
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
      ],
      "sectionState": {
        "items": 1
      }
    }
  }
}
```

### `/erp/v1/purchase/request/delete` - 采购申请单删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`ResultVO<Void>`（实际返回 `ResultVO.success(null)`，即 `data = null`）

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
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": null
}
```

## 采购订单

### `/erp/v1/purchase/order/list` - 采购订单列表

- 请求方式：`POST`
- 入参：`PurchaseOrderListDTO`
- 返回：`ResultVO<ListBaseVO<PurchaseOrderListItemVO>>`

### `/erp/v1/purchase/order/addItem` - 采购订单新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseOrderSaveItemVO>>`
- `headList` 中 `items.skuId` 使用 `fieldType = "50"` 和 `productSelectConfig`；其业务码为 `PURCHASE_ORDER`，接口地址由前端产品常量维护。

### `/erp/v1/purchase/order/updateItem` - 采购订单编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseOrderSaveItemVO>>`
- 产品字段协议与新增表单一致；采购明细列严格以本接口返回的 `headList` 渲染。

### `/erp/v1/purchase/order/saveDraft` - 采购订单保存草稿

- 请求方式：`POST`
- 入参：`PurchaseOrderDraftSaveDTO`
- 返回：`ResultVO<PurchaseOrderDraftSaveVO>`

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "draftCode": "draft-001"
  }
}
```

### `/erp/v1/purchase/order/saveAndSubmit` - 采购订单保存并提交

- 请求方式：`POST`
- 入参：`PurchaseOrderSubmitSaveDTO`
- 返回：`ResultVO<BaseVO>`

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

### `/erp/v1/purchase/order/draftList` - 采购订单草稿列表

- 请求方式：`POST`
- 入参：`PurchaseOrderDraftListDTO`
- 返回：`ResultVO<List<PurchaseOrderDraftListItemVO>>`

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": [
    {
      "draftCode": "draft-001",
      "draftTitle": "采购订单草稿",
      "orderNo": "PO20260804001",
      "vendorId": 20,
      "updatedTime": 1721779200000
    }
  ]
}
```

### `/erp/v1/purchase/order/loadDraft` - 采购订单加载草稿

- 请求方式：`POST`
- 入参：`PurchaseOrderDraftLoadDTO`
- 返回：`ResultVO<PurchaseOrderDraftDetailVO>`

### `/erp/v1/purchase/order/save` - 采购订单保存

- 请求方式：`POST`
- 入参：`PurchaseOrderSaveDTO`
- 返回：`ResultVO<Long>`

### `/erp/v1/purchase/order/detail` - 采购订单详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<PurchaseOrderDetailVO>`

### `/erp/v1/purchase/order/delete` - 采购订单删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`ResultVO<Void>`（实际返回 `ResultVO.success(null)`，即 `data = null`）

#### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": null
}
```

## 采购申请单行

### `/erp/v1/purchase/request-item/list` - 采购申请单行列表

- 请求方式：`POST`
- 入参：`PurchaseRequestItemListDTO`
- 返回：`ResultVO<ListBaseVO<PurchaseRequestItemListItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/request-item/addItem` - 采购申请单行新增表单

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseRequestItemSaveItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": null,
    "data": {
      "main": null
    }
  }
}
```

### `/erp/v1/purchase/request-item/updateItem` - 采购申请单行编辑表单

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<SaveItemVO<PurchaseRequestItemSaveItemVO>>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/request-item/save` - 采购申请单行保存

- 请求方式：`POST`
- 入参：`PurchaseRequestItemSaveDTO`
- 返回：`ResultVO<Long>`

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
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 1
}
```

### `/erp/v1/purchase/request-item/detail` - 采购申请单行详情

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`ResultVO<PurchaseRequestItemDetailVO>`

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
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
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
}
```

### `/erp/v1/purchase/request-item/delete` - 采购申请单行删除

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`ResultVO<Void>`（实际返回 `ResultVO.success(null)`，即 `data = null`）

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
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": null
}
```
