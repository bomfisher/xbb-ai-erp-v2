# common-m.md

## 列表

### 公共列表筛选字段

- 接口：`POST /erp/v1/common/list/filter`
- 入参：`ListCommonQueryDTO`
- 出参：`ListFilterVO`
- 用途：按业务编码查询列表筛选字段元数据
- API 文档：`docs/api/common-list.md`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "customer"
}
```

#### 响应示例

```json
{
  "list": [
    {
      "attr": "customerName",
      "attrName": "客户名称",
      "fieldType": "TEXT",
      "supportedSymbols": ["EQ", "CONTAINS", "IS_EMPTY"],
      "itemList": []
    }
  ]
}
```

#### 参数说明

- `corpid`：企业 ID
- `userId`：员工 ID
- `businessCode`：业务编码

#### 响应字段说明

- `list`：筛选字段列表
- `list[].attr`：字段属性名
- `list[].attrName`：字段显示名称
- `list[].fieldType`：字段类型，当前返回 `TEXT`、`ENUM`、`ID`、`DATE`
- `list[].supportedSymbols`：当前字段支持的运算符列表
- `list[].itemList`：候选项列表；文本/日期字段通常为空数组

### 公共列表表头字段

- 接口：`POST /erp/v1/common/list/header`
- 入参：`ListCommonQueryDTO`
- 出参：`ListHeaderVO`
- 用途：按业务编码查询列表表头字段元数据
- API 文档：`docs/api/common-list.md`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "customer"
}
```

#### 响应示例

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

#### 参数说明

- `corpid`：企业 ID
- `userId`：员工 ID
- `businessCode`：业务编码

#### 响应字段说明

- `list`：表头字段列表
- `list[].attr`：字段属性名
- `list[].attrName`：字段显示名称
- `list[].fieldType`：字段类型
- `list[].required`：是否必填
- `list[].editable`：是否可编辑
- `list[].itemList`：字段可选项列表

### 公共列表顶部按钮

- 接口：`POST /erp/v1/common/list/topButton`
- 入参：`ListCommonQueryDTO`
- 出参：`ListTopButtonVO`
- 用途：按业务编码查询列表顶部按钮元数据
- API 文档：`docs/api/common-list.md`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "customer"
}
```

#### 响应示例

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

#### 参数说明

- `corpid`：企业 ID
- `userId`：员工 ID
- `businessCode`：业务编码

#### 响应字段说明

- `list`：顶部按钮列表
- `list[].buttonCode`：按钮编码
- `list[].buttonName`：按钮名称
- `list[].sort`：排序值
- `list[].actionCode`：动作编码

### 公共列表底部按钮

- 接口：`POST /erp/v1/common/list/bottomButton`
- 入参：`ListCommonQueryDTO`
- 出参：`ListBottomButtonVO`
- 用途：按业务编码查询列表底部按钮元数据
- API 文档：`docs/api/common-list.md`

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "customer"
}
```

#### 响应示例

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

#### 参数说明

- `corpid`：企业 ID
- `userId`：员工 ID
- `businessCode`：业务编码

#### 响应字段说明

- `list`：底部按钮列表
- `list[].buttonCode`：按钮编码
- `list[].buttonName`：按钮名称
- `list[].sort`：排序值
- `list[].actionCode`：动作编码

#### 规则说明

- 主文档维护当前主题下 4 个列表元数据接口的完整请求与响应事实
- 次文档只维护列表维度索引与差异说明，不复制完整字段说明
