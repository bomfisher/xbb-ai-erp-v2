# 客户加载草稿

## 文档信息
- 领域：`customer-load-draft`
- 控制器：`CustomerAdminController#loadDraft`
- 请求方式：`POST /erp/v1/customer/loadDraft`
- 聚合文档引用：`docs/kn/customer-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "draftCode": "draft-002"
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

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.main` | 是 | 草稿主档 |
| `data.ext` | 是 | 草稿子档 |
| `data.sectionState` | 是 | 草稿分段开关 |
| `data.draftMeta` | 是 | 草稿元信息 |

## 规则说明

- 返回完整编辑态数据：`main + ext + sectionState + draftMeta`
- 草稿续编依赖 `draftCode`，不暴露后端数据库 `id`
- 历史草稿若未返回 `sectionState`，前端可根据子档数组是否有数据推断为开启
