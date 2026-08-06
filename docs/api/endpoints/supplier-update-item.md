# 供应商编辑表单加载

## 文档信息
- 领域：`supplier-update-item`
- 控制器：`SupplierAdminController#updateItem`
- 请求方式：`POST /erp/v1/supplier/updateItem`
- 聚合文档引用：`docs/kn/supplier-m.md`

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
| `id` | 是 | 供应商主键 |

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
      "main": {
        "id": 1,
        "supplierCode": "SUP-001",
        "supplierName": "杭州供应商"
      },
      "contacts": [
        {
          "id": 11,
          "contactName": "张三",
          "defaultFlag": 1
        }
      ],
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
| `data.headList` | 是 | 表单字段定义 |
| `data.data.main` | 否 | 供应商主档数据 |
| `data.data.contacts` | 是 | 联系人列表 |
| `data.data.addresses` | 是 | 地址列表 |
| `data.data.bankAccounts` | 是 | 银行账户列表 |
| `data.data.invoiceProfiles` | 是 | 开票资料列表 |
| `data.data.sectionState` | 是 | 根据子档是否有数据回填开关态 |

## 规则说明

- 编辑接口返回结构与新增接口一致，但 `data` 会回填现有供应商主档与子档数据
- `sectionState` 会根据各子档列表是否为空自动回填为 `0/1`
- 当前 `headList` 由统一字段定义构建逻辑生成，并非空数组
- `main.supplierCategory`、`main.mainBusinessCategory`、`main.bizStatus`、`main.refStatus` 使用 `COMB(8)` 并通过 `itemList` 下发固定选项
- `main.ownerPurchaserId` 使用 `USER(12)`，并通过 `businessSelectConfig` 下发成员单选配置
