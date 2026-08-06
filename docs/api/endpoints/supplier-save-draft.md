# 供应商草稿保存

## 文档信息
- 领域：`supplier-save-draft`
- 控制器：`SupplierAdminController#saveDraft`
- 请求方式：`POST /erp/v1/supplier/saveDraft`
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
    "contacts": 1,
    "addresses": 0,
    "bankAccounts": 0,
    "invoiceProfiles": 0
  },
  "draftMeta": {
    "draftTitle": "供应商草稿",
    "updatedTime": 1721606400000
  }
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `main` | 是 | 供应商主档 |
| `ext` | 否 | 可选子档集合 |
| `sectionState` | 否 | 可选子档开关 |
| `draftMeta.draftCode` | 否 | 草稿编码；更新已有草稿时传入 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "draftCode": "draft-001"
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.draftCode` | 是 | 草稿编码 |

## 规则说明

- 草稿保存会返回草稿编码
- 同一 `draftCode` 再次保存时会覆盖旧草稿内容
