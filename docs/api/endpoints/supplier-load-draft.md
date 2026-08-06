# 供应商草稿加载

## 文档信息
- 领域：`supplier-load-draft`
- 控制器：`SupplierAdminController#loadDraft`
- 请求方式：`POST /erp/v1/supplier/loadDraft`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "draftCode": "draft-001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `draftCode` | 是 | 草稿编码 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
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
      "contacts": 1,
      "addresses": 0,
      "bankAccounts": 0,
      "invoiceProfiles": 0
    },
    "draftMeta": {
      "draftCode": "draft-001",
      "draftTitle": "供应商草稿",
      "updatedTime": 1721606400000
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.main` | 是 | 草稿主档 |
| `data.ext` | 是 | 草稿子档 |
| `data.sectionState` | 是 | 草稿分段开关 |
| `data.draftMeta` | 是 | 草稿元信息 |

## 规则说明

- 返回保存时的主档、子档、分段状态与草稿元信息
- 草稿续编依赖 `draftCode`，不暴露后端数据库 `id`
