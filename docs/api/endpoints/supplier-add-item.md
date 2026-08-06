# 供应商新增表单初始化

## 文档信息
- 领域：`supplier-add-item`
- 控制器：`SupplierAdminController#addItem`
- 请求方式：`POST /erp/v1/supplier/addItem`
- 聚合文档引用：`docs/kn/supplier-m.md`

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
        "attr": "main.supplierCode",
        "attrName": "供应商编码",
        "fieldType": "1",
        "required": 1,
        "editable": 1,
        "itemList": []
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
| `data.headList` | 是 | 表单字段定义 |
| `data.data` | 是 | 保存表单初始化数据 |
| `data.data.main` | 是 | 供应商主档表单 |
| `data.data.sectionState` | 是 | 子档开关态 |

## 规则说明

- 返回主档、联系人、地址、银行账户、开票资料、子档开关态的完整抽屉初始化结构
- 所有子档列表默认返回空数组
- `headList` 字段定义由查询服务端统一构造
- `main.supplierCategory`、`main.mainBusinessCategory`、`main.bizStatus`、`main.refStatus` 使用 `COMB(8)` 并通过 `itemList` 下发固定选项
- `main.ownerPurchaserId` 使用 `USER(12)`，并通过 `businessSelectConfig` 下发成员单选的快捷搜索、弹窗搜索和按 ID 回显配置
