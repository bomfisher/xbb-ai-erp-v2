# 供应商正式保存并提交

## 文档信息
- 领域：`supplier-save-and-submit`
- 控制器：`SupplierAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/supplier/saveAndSubmit`
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
    "draftCode": "draft-001"
  }
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `main` | 是 | 供应商主档 |
| `draftMeta.draftCode` | 否 | 若来源于草稿提交，则传原草稿编码 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "ok": 1
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.ok` | 是 | 空业务返回体标记 |

## 规则说明

- 提交阶段会校验 `main.supplierCode` 与 `main.supplierName` 必填
- 若提交请求携带 `draftMeta.draftCode`，正式保存成功后删除对应草稿
- `sectionState=0` 的子档不会进入正式保存链路
