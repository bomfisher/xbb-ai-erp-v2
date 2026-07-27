# 公共列表接口

## 公共列表筛选字段

`POST /erp/v1/common/list/filter`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "customer"
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
  "list": [
    {
      "attr": "customerName",
      "attrName": "客户名称",
      "fieldType": 1
    }
  ]
}
```

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 筛选字段列表 |
| `list[].attr` | 是 | 字段属性名 |
| `list[].attrName` | 是 | 字段显示名称 |
| `list[].fieldType` | 是 | 字段类型 |

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
  "businessCode": "customer"
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
  "businessCode": "customer"
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
  "list": [
    {
      "buttonCode": "add",
      "buttonName": "新增",
      "sort": 1,
      "actionCode": "create"
    }
  ]
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
  "businessCode": "customer"
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
  "list": [
    {
      "buttonCode": "confirm",
      "buttonName": "确认",
      "sort": 1,
      "actionCode": "submit"
    }
  ]
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
