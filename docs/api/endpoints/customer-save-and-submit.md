# 客户正式保存并提交

## 文档信息
- 领域：`customer-save-and-submit`
- 控制器：`CustomerAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/customer/saveAndSubmit`
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
| `main` | 是 | 客户主档 |
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

- 正式保存采用严格校验
- `sectionState=0` 的子档不会进入正式保存链路
- 若提交请求携带 `draftMeta.draftCode`，正式保存成功后删除对应草稿
