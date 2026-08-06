# 客户新增表单元数据

## 文档信息
- 领域：`customer-add-item`
- 控制器：`CustomerAdminController#addItem`
- 请求方式：`POST /erp/v1/customer/addItem`
- 聚合文档引用：`docs/kn/customer-m.md`

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
| `userId` | 是 | 员工 ID |

## 响应示例

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
          { "value": "1", "text": "启用" },
          { "value": "0", "text": "停用" }
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

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.headList` | 是 | 字段定义列表 |
| `data.data` | 是 | 新建页初始化数据 |

## 规则说明

- 新建页字段定义来自 `headList`
- `sectionState` 表示各可选子档的显示/启用状态，使用 `Integer`：`1=开启`、`0=关闭`
- `addItem` 初始返回时四个子档默认均为 `0`
