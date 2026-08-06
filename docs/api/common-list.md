# 公共列表接口

## 迁移说明

- 当前完整接口事实已迁移到 `docs/api/endpoints/*.md` 下的 API 原子文档
- 本文档保留为旧入口索引页，避免已有引用立即失效
- 新增或修改公共列表接口时，应优先维护以下文档：
  - `docs/api/endpoints/common-filter.md`
  - `docs/api/endpoints/common-header.md`
  - `docs/api/endpoints/common-top-button.md`
  - `docs/api/endpoints/common-bottom-button.md`
  - `docs/api/endpoints/common-row-action.md`

## 公共列表筛选字段

`POST /erp/v1/common/list/filter`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "SUPPLIER"
}
```

### 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

### 响应示例

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

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 筛选字段列表 |
| `list[].attr` | 是 | 字段属性名 |
| `list[].attrName` | 是 | 字段显示名称 |
| `list[].fieldType` | 是 | 字段类型，当前返回 `TEXT`、`ENUM`、`ID`、`DATE` |
| `list[].supportedSymbols` | 是 | 当前字段支持的筛选运算符列表 |
| `list[].itemList` | 是 | 当前字段的候选项；无选项字段返回空数组 |

### 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ListFilterVO`
- 结果仅包含筛选字段元数据

## 公共列表表头字段

`POST /erp/v1/common/list/header`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "SUPPLIER"
}
```

### 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

### 响应示例

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

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 表头字段列表 |
| `list[].attr` | 是 | 字段属性名 |
| `list[].attrName` | 是 | 字段显示名称 |
| `list[].fieldType` | 是 | 字段类型 |
| `list[].required` | 是 | 是否必填 |
| `list[].editable` | 是 | 是否可编辑 |
| `list[].itemList` | 是 | 字段可选项列表 |

### 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ListHeaderVO`
- 结果仅包含表头字段元数据

## 公共列表顶部按钮

`POST /erp/v1/common/list/topButton`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "SUPPLIER"
}
```

### 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "buttonCode": "ADD",
        "buttonName": "新增",
        "sort": 10,
        "actionCode": "ADD"
      }
    ]
  }
}
```

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 顶部按钮列表 |
| `list[].buttonCode` | 是 | 按钮编码 |
| `list[].buttonName` | 是 | 按钮名称 |
| `list[].sort` | 是 | 排序值 |
| `list[].actionCode` | 是 | 动作编码 |

### 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ListTopButtonVO`
- 结果仅包含顶部按钮元数据

## 公共列表底部按钮

`POST /erp/v1/common/list/bottomButton`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "SUPPLIER"
}
```

### 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "buttonCode": "EXPORT",
        "buttonName": "导出",
        "sort": 20,
        "actionCode": "EXPORT"
      }
    ]
  }
}
```

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 底部按钮列表 |
| `list[].buttonCode` | 是 | 按钮编码 |
| `list[].buttonName` | 是 | 按钮名称 |
| `list[].sort` | 是 | 排序值 |
| `list[].actionCode` | 是 | 动作编码 |

### 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ListBottomButtonVO`
- 结果仅包含底部按钮元数据

## 公共列表行内动作

`POST /erp/v1/common/list/rowAction`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "CUSTOMER"
}
```

### 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "actionCode": "EDIT",
        "actionName": "编辑",
        "sort": 10,
        "showMode": "PRIMARY",
        "confirmType": "NONE"
      }
    ]
  }
}
```

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 行内动作列表 |
| `list[].actionCode` | 是 | 动作编码 |
| `list[].actionName` | 是 | 动作名称 |
| `list[].sort` | 是 | 排序值 |
| `list[].showMode` | 是 | 展示模式，当前客户列表首版返回 `PRIMARY` |
| `list[].confirmType` | 是 | 二次确认类型；当前客户列表首版固定返回 `NONE` |

### 规则说明

- 请求 URL：`POST /erp/v1/common/list/rowAction`
- 入参使用 `ListCommonQueryDTO`
- 出参使用 `ResultVO<ListRowActionVO>`
- 结果仅包含行内动作元数据
- `businessCode` 当前已覆盖 `CUSTOMER`、`SUPPLIER` 等已注册业务编码；若业务未注册会抛出 `BizException`
- 供应商列表首版返回如下元数据：
  - `filter`：`supplierCode`、`supplierName`、`supplierCategory`、`ownerPurchaserId`、`bizStatus`、`refStatus`
  - `header`：`main.supplierCode`、`main.supplierName`、`main.supplierShortName`、`main.supplierCategory`、`main.mainBusinessCategory`、`main.ownerPurchaserId`、`main.bizStatus`、`main.refStatus`、`main.addTime`、`main.updateTime`
  - `topButton`：`ADD`
  - `bottomButton`：`EXPORT`
  - `rowAction`：`EDIT`
- 客户列表当前仅落地 `EDIT` 行动作，不提前暴露其他动作
