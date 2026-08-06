# 客户编辑表单元数据

## 文档信息
- 领域：`customer-update-item`
- 控制器：`CustomerAdminController#updateItem`
- 请求方式：`POST /erp/v1/customer/updateItem`
- 聚合文档引用：`docs/kn/customer-m.md`

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
| `userId` | 是 | 员工 ID |
| `id` | 是 | 客户主键 |

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
      "main": {
        "id": 1,
        "customerCode": "CUST-001",
        "customerName": "杭州客户"
      },
      "contacts": [],
      "addresses": [],
      "bankAccounts": [],
      "invoiceProfiles": [],
      "sectionState": {
        "contacts": 1,
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
| `data.data.main` | 否 | 客户主档数据 |
| `data.data.sectionState` | 是 | 根据子档是否有数据回填开关态 |

## 规则说明

- `updateItem` 是客户列表编辑抽屉的初始化接口
- 返回结构与 `addItem` 一致，前端据此加载编辑表单
- 当前客户列表页的编辑入口由公共 `/erp/v1/common/list/rowAction` 下发 `EDIT` 动作后触发
