# 客户草稿与保存接口

## 文档信息

- 领域：`customer-customer`
- 模块：`xbb-erp-module-customer`
- 控制器：`CustomerAdminController`
- 基础路径：`/erp/v1/customer`

## 通用说明

- 所有请求均返回 HTTP 200
- `ResultVO.success(...)` 表示成功，业务校验异常统一返回 `success=false`
- 系统异常或接口未按预定格式返回时，统一返回 HTTP 200，且 `success=false`、`message="接口未按预定格式返回，请联系客服"`
- 所有非脚本接口入参 DTO 继承 `BaseDTO`
- `BaseDTO` 公共字段：`corpid`、`userId`
- 保存类接口统一返回 `ResultVO<BaseVO>`，其中 `saveDraft` 返回 `ResultVO<CustomerDraftSaveVO>`
- `BaseVO` 当前固定输出 `{"ok":1}`，用于保证空业务返回体可稳定序列化
- 当前保存成功后暂不暴露后端数据库 `id`
- 客户主表 `id` 由数据库自增生成；新建时 `main.id` 不需要传值，更新时才传已有 `id`
- 草稿存储于 Redis，最近保留 `10` 条，TTL `7` 天，正式保存成功后移除对应草稿
- 新建页、编辑页字段元数据统一通过 `headList` 返回；组合框字段的下拉选项放在字段对象的 `itemList` 中
- 当前 `bizStatus` 选项统一为：`1=启用`、`0=停用`
- 联系人、地址、银行账户、开票信息这些子档默认允许为空；正式保存时仅在某一行开始填写后，才校验该行 `required=1` 的字段

## 1. 客户列表查询

- 请求 URL：`POST /erp/v1/customer/list`
- 入参：`CustomerListDTO`
- 出参：`ResultVO<ListBaseVO<CustomerListItemVO>>`

### 入参结构

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "pageNum": 1,
  "pageSize": 20,
  "keyword": "杭州",
  "conditions": [
    {
      "attr": "customerName",
      "fieldType": "TEXT",
      "symbol": "CONTAINS",
      "value": ["客户"]
    },
    {
      "attr": "bizStatus",
      "fieldType": "ENUM",
      "symbol": "IN",
      "value": ["1", "0"]
    }
  ]
}
```

### 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `pageNum` | 是 | 页码，从 1 开始 |
| `pageSize` | 是 | 每页条数 |
| `keyword` | 否 | 顶部关键字搜索，当前匹配客户编码与客户名称 |
| `conditions` | 否 | 高级动态筛选条件列表 |
| `conditions[].attr` | 是 | 筛选字段标识，当前支持 `customerCode`、`customerName`、`customerCategory`、`ownerSalesId`、`bizStatus`、`regionCode`、`createTime` |
| `conditions[].fieldType` | 是 | 字段类型，当前支持 `TEXT`、`ENUM`、`ID`、`DATE` |
| `conditions[].symbol` | 是 | 运算符 |
| `conditions[].value` | 否 | 运算符对应的值数组；`IS_EMPTY` / `IS_NOT_EMPTY` 传空数组 |

### 参数补充说明

| 字段 | 可选值 | 说明 |
| --- | --- | --- |
| `conditions[].symbol` | `EQ`、`NE` | 单值等于 / 不等于 |
| `conditions[].symbol` | `CONTAINS`、`NOT_CONTAINS` | 文本包含 / 不包含，仅 `TEXT` 可用 |
| `conditions[].symbol` | `IN` | 多值匹配，仅 `ENUM` / `ID` 可用 |
| `conditions[].symbol` | `GE`、`LE` | 日期大于等于 / 小于等于，仅 `DATE` 可用 |
| `conditions[].symbol` | `BETWEEN` | 日期区间，仅 `DATE` 可用，`value` 固定 2 个值 |
| `conditions[].symbol` | `IS_EMPTY`、`IS_NOT_EMPTY` | 判空 / 非空 |

### 成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "headList": null,
    "list": [
      {
        "id": 1,
        "customerCode": "CUST-001",
        "customerName": "杭州客户",
        "customerCategory": "A",
        "ownerSalesId": "emp-001",
        "defaultContactName": "张三",
        "defaultContactMobile": "13800000000",
        "defaultAddressSummary": "浙江杭州西湖区文三路 1 号",
        "defaultInvoiceTitle": "杭州客户有限公司",
        "bizStatus": "1"
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

### 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 响应数据主体 |
| `data.headList` | 否 | 当前列表接口固定返回 `null` |
| `data.list` | 是 | 客户列表数据 |
| `data.list[].id` | 是 | 客户主档 ID |
| `data.list[].customerCode` | 否 | 客户编码 |
| `data.list[].customerName` | 否 | 客户名称 |
| `data.list[].customerCategory` | 否 | 客户分类 |
| `data.list[].ownerSalesId` | 否 | 归属销售员工 ID |
| `data.list[].defaultContactName` | 否 | 默认联系人姓名 |
| `data.list[].defaultContactMobile` | 否 | 默认联系人手机号 |
| `data.list[].defaultAddressSummary` | 否 | 默认地址摘要 |
| `data.list[].defaultInvoiceTitle` | 否 | 默认开票抬头 |
| `data.list[].bizStatus` | 否 | 业务状态 |
| `data.pageHelper` | 是 | 分页信息 |
| `data.pageHelper.page` | 是 | 当前页码 |
| `data.pageHelper.count` | 是 | 总条数 |
| `data.pageHelper.hasLeft` | 是 | 是否有上一页 |
| `data.pageHelper.hasRight` | 是 | 是否有下一页 |

### 规则说明

- 客户列表接口已切换为 `keyword + conditions` 新协议，不再接收旧的平铺筛选字段。
- `keyword` 与 `conditions` 同时存在时，按 `keyword AND conditions...` 组合查询。
- 动态筛选字段会先在服务层按白名单校验，再映射为安全列名进入 SQL。
- `TEXT`、`ENUM`、`ID`、`DATE` 字段各自只允许约定的运算符组合。
- `IS_EMPTY` / `IS_NOT_EMPTY` 不传值；`IN` 传 1 个或多个值；`BETWEEN` 固定传 2 个值。
- 非法字段、字段类型不匹配、运算符不支持、值长度不合法时，统一按业务异常返回失败。

## 2. 新增表单元数据

- 请求 URL：`POST /erp/v1/customer/addItem`
- 入参：`BaseDTO`
- 出参：`ResultVO<SaveItemVO<CustomerSaveItemVO>>`

### 入参结构

```json
{
  "corpid": "corp-001",
  "userId": "user-001"
}
```

### 成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "headList": [
      {
        "attr": "main.bizStatus",
        "attrName": "业务状态",
        "fieldType": "8",
        "required": 0,
        "editable": 1,
        "itemList": [
          {
            "value": "1",
            "text": "启用"
          },
          {
            "value": "0",
            "text": "停用"
          }
        ]
      }
    ],
    "data": {
      "main": {},
      "contacts": [],
      "addresses": [],
      "bankAccounts": [],
      "invoiceProfiles": [],
      "sectionState": {
        "contacts": 0,
        "addresses": 0,
        "bankAccounts": 0,
        "invoiceProfiles": 0
      }
    }
  }
}
```

### 说明

- 新建页字段定义来自 `headList`
- 组合框字段通过 `itemList` 下发选项，前端不需要自行硬编码
- 当前 `main.bizStatus.required=0`，但选项仍由后端统一下发
- `contacts`、`addresses`、`bankAccounts`、`invoiceProfiles` 初始返回空数组，表示这些子档尚未开始填写
- `sectionState` 表示各可选子档的显示/启用状态，使用 `Integer`：`1=开启`、`0=关闭`
- `addItem` 初始返回时四个子档默认均为 `0`，前端应以 `sectionState` 控制子档显隐，而不是仅根据数组是否为空判断

## 2. 编辑表单元数据

- 请求 URL：`POST /erp/v1/customer/updateItem`
- 入参：`IdBaseDTO`
- 出参：`ResultVO<SaveItemVO<CustomerSaveItemVO>>`

### 入参结构

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "id": 1
}
```

### 说明

- 返回结构与 `addItem` 一致
- `headList` 同样包含组合框字段的 `itemList`
- 当前 `bizStatus` 选项仍为：`1=启用`、`0=停用`

## 3. 保存草稿

- 请求 URL：`POST /erp/v1/customer/saveDraft`
- 入参：`CustomerDraftSaveDTO`
- 出参：`ResultVO<CustomerDraftSaveVO>`

### 入参结构

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "main": {
    "id": 1,
    "customerCode": "CUST-001",
    "customerName": "杭州客户",
    "customerShortName": "杭州",
    "customerCategory": "A",
    "regionCode": "330100",
    "ownerSalesId": "emp-001",
    "ownerSalesNameSnapshot": "张三",
    "bizStatus": "1",
    "refStatus": "0",
    "defaultContactId": 11,
    "defaultAddressId": 21,
    "remark": "备注",
    "version": 1
  },
  "ext": {
    "contacts": [
      {
        "id": 11,
        "contactName": "张三",
        "mobile": "13800000000",
        "phone": "0571-12345678",
        "email": "a@example.com",
        "positionName": "采购",
        "defaultFlag": 1,
        "bizStatus": "1",
        "remark": "备注",
        "version": 1
      }
    ],
    "addresses": [
      {
        "id": 21,
        "addressType": "DELIVERY",
        "receiverName": "李四",
        "receiverMobile": "13900000000",
        "provinceCode": "330000",
        "cityCode": "330100",
        "districtCode": "330106",
        "detailAddress": "文三路 1 号",
        "postalCode": "310000",
        "defaultFlag": 1,
        "bizStatus": "1",
        "version": 1
      }
    ],
    "bankAccounts": [
      {
        "id": 31,
        "accountName": "杭州客户",
        "bankName": "招商银行",
        "accountNo": "6222000000000000",
        "accountUsage": "SETTLEMENT",
        "defaultFlag": 1,
        "bizStatus": "1",
        "remark": "备注",
        "version": 1
      }
    ],
    "invoiceProfiles": [
      {
        "id": 41,
        "invoiceTitle": "杭州客户有限公司",
        "taxNo": "91330100XXXX",
        "addressPhone": "浙江杭州 0571-87654321",
        "bankName": "招商银行",
        "bankAccountNo": "6222000000000000",
        "defaultFlag": 1,
        "bizStatus": "1",
        "remark": "备注",
        "version": 1
      }
    ]
  },
  "sectionState": {
    "contacts": 1,
    "addresses": 1,
    "bankAccounts": 1,
    "invoiceProfiles": 1
  },
  "draftMeta": {
    "draftCode": "draft-001",
    "draftTitle": "杭州客户草稿",
    "updatedTime": 1722048000000
  }
}
```

### 说明

- `draftMeta.draftCode` 为空时，后端生成新的草稿标识
- 草稿保存采用宽松校验，允许主档未填完整
- `bizStatus` 默认值为 `1`，表示有效/启用态；`refStatus` 默认值为 `0`，表示未引用，产生关联后置为 `1`
- 同一公司仅返回最近 `10` 条草稿
- `sectionState` 与草稿一起持久化，用于恢复联系人、地址、银行账户、开票信息四个子档的开启/关闭状态
- 草稿保存时即使某个子档当前为关闭状态，前端也可以保留历史输入；是否提交入库由正式保存时的 `sectionState` 决定

### 成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "draftCode": "draft-001"
  }
}
```

## 4. 保存并提交

- 请求 URL：`POST /erp/v1/customer/saveAndSubmit`
- 入参：`CustomerSubmitSaveDTO`
- 出参：`ResultVO<BaseVO>`

### 入参结构

与 `saveDraft` 相同；若来源于草稿，建议传入 `draftMeta.draftCode`。

### 说明

- 正式保存采用严格校验
- 草稿保存只校验字段格式与字段长度，不强制 `required` 字段必填
- `addItem` 返回字段中 `required=1` 的范围，就是正式保存的必填范围
- 主档字段仍按 `required=1` 直接校验必填
- 联系人、地址、银行账户、开票信息这些子档整体默认可不填；当某一行任意字段已开始填写时，该行内 `required=1` 的字段必须补齐
- 当前子档行内关键必填字段包括：`contacts.contactName`、`addresses.addressType`、`addresses.detailAddress`、`bankAccounts.accountName`、`bankAccounts.bankName`、`bankAccounts.accountNo`、`invoiceProfiles.invoiceTitle`、`invoiceProfiles.taxNo`
- 正式保存时，`String` 类型字段传空串或仅空白字符，按未填写处理
- 字段长度按 `fieldType` 使用统一规则，不再按客户模块单字段单独定义
- `sectionState` 用于决定四个可选子档是否参与本次正式保存；显式传 `0` 的子档即使存在历史行数据，也不会参与必填校验和落库
- 当 `sectionState` 未传或字段值为 `null` 时，后端会按对应子档数组是否有数据推断开启状态，兼容旧草稿与旧调用
- 正式保存成功后，如传入 `draftMeta.draftCode`，后端删除对应草稿
- 返回统一成功对象，不返回客户 `id`

### 成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "ok": 1
  }
}
```

## 5. 草稿列表

- 请求 URL：`POST /erp/v1/customer/draftList`
- 入参：`CustomerDraftListDTO`
- 出参：`ResultVO<List<CustomerDraftListItemVO>>`

### 入参结构

```json
{
  "corpid": "corp-001",
  "userId": "user-001"
}
```

### 成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "draftCode": "draft-002",
      "draftTitle": "杭州客户草稿",
      "customerName": "杭州客户",
      "customerCode": "CUST-001",
      "updatedTime": 1722048000000
    }
  ]
}
```

### 说明

- 仅返回当前公司最近 `10` 条草稿
- 返回字段为草稿摘要，不包含后端数据库 `id`

## 6. 加载草稿

- 请求 URL：`POST /erp/v1/customer/loadDraft`
- 入参：`CustomerDraftLoadDTO`
- 出参：`ResultVO<CustomerDraftDetailVO>`

### 入参结构

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "draftCode": "draft-002"
}
```

### 成功响应

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "main": {
      "customerCode": "CUST-001",
      "customerName": "杭州客户"
    },
    "ext": {
      "contacts": [],
      "addresses": [],
      "bankAccounts": [],
      "invoiceProfiles": []
    },
    "sectionState": {
      "contacts": 0,
      "addresses": 0,
      "bankAccounts": 0,
      "invoiceProfiles": 0
    },
    "draftMeta": {
      "draftCode": "draft-002",
      "draftTitle": "杭州客户草稿",
      "updatedTime": 1722048000000
    }
  }
}
```

### 说明

- 返回完整编辑态数据：`main + ext + sectionState + draftMeta`
- 草稿续编依赖 `draftCode`，不暴露后端数据库 `id`
- `sectionState` 返回的是草稿保存时各子档的开启/关闭状态，前端恢复草稿后应优先按该字段还原界面状态
- 对于历史草稿或旧数据，如果未返回 `sectionState`，前端可根据对应子档数组是否有数据推断为开启，以兼容旧协议
