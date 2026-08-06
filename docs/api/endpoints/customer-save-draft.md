# 客户草稿保存

## 文档信息
- 领域：`customer-save-draft`
- 控制器：`CustomerAdminController#saveDraft`
- 请求方式：`POST /erp/v1/customer/saveDraft`
- 聚合文档引用：`docs/kn/customer-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
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
    "contacts": 1,
    "addresses": 0,
    "bankAccounts": 0,
    "invoiceProfiles": 0
  },
  "draftMeta": {
    "draftCode": "draft-001",
    "draftTitle": "杭州客户草稿",
    "updatedTime": 1722048000000
  }
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `main` | 是 | 客户主档编辑态 |
| `ext` | 否 | 可选子档集合 |
| `sectionState` | 否 | 可选子档开关，`1=开启`、`0=关闭` |
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

- 草稿保存采用宽松校验，允许主档未填完整
- 同一 `draftCode` 再次保存时会覆盖旧草稿内容
- `sectionState` 与草稿一起持久化，用于恢复可选子档开启/关闭状态
