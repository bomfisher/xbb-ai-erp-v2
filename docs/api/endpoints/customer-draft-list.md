# 客户草稿列表

## 文档信息
- 领域：`customer-draft-list`
- 控制器：`CustomerAdminController#draftList`
- 请求方式：`POST /erp/v1/customer/draftList`
- 聚合文档引用：`docs/kn/customer-m.md`

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
  "data": [
    {
      "draftCode": "draft-002",
      "draftTitle": "杭州客户草稿",
      "customerName": "杭州客户",
      "customerCode": "CUST-001",
      "updatedTime": 1722048000000
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data[]` | 是 | 草稿摘要列表 |
| `data[].draftCode` | 是 | 草稿编码 |
| `data[].draftTitle` | 否 | 草稿标题 |
| `data[].customerName` | 否 | 客户名称 |
| `data[].customerCode` | 否 | 客户编码 |
| `data[].updatedTime` | 否 | 更新时间 |

## 规则说明

- 仅返回当前公司最近 `10` 条草稿
- 返回字段为草稿摘要，不包含后端数据库 `id`
