# 供应商正式保存

## 文档信息
- 领域：`supplier-save`
- 控制器：`SupplierAdminController#save`
- 请求方式：`POST /erp/v1/supplier/save`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "main": {
    "supplierCode": "SUP-001",
    "supplierName": "杭州供应商"
  },
  "contacts": []
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `main` | 是 | 主档 |
| `contacts` | 否 | 联系人列表 |
| `addresses` | 否 | 地址列表 |
| `bankAccounts` | 否 | 银行账户列表 |
| `invoiceProfiles` | 否 | 开票信息列表 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": 1
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 保存后的供应商主键 |

## 规则说明

- 当前保存接口直接返回供应商主键
