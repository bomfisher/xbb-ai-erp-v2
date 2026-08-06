# 供应商草稿列表查询

## 文档信息
- 领域：`supplier-draft-list`
- 控制器：`SupplierAdminController#draftList`
- 请求方式：`POST /erp/v1/supplier/draftList`
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
  "data": [
    {
      "draftCode": "draft-002",
      "draftTitle": "草稿2",
      "supplierCode": "SUP-002",
      "supplierName": "供应商2",
      "updatedTime": 300
    },
    {
      "draftCode": "draft-001",
      "draftTitle": "草稿1",
      "supplierCode": "SUP-001",
      "supplierName": "供应商1",
      "updatedTime": 100
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data[]` | 是 | 草稿列表 |
| `data[].draftCode` | 是 | 草稿编码 |
| `data[].draftTitle` | 否 | 草稿标题 |
| `data[].supplierCode` | 否 | 供应商编码 |
| `data[].supplierName` | 否 | 供应商名称 |
| `data[].updatedTime` | 否 | 更新时间 |

## 规则说明

- 仅返回当前 `corpid` 下的草稿
- 按 `updatedTime` 倒序返回，最多返回 `10` 条
- 返回字段为草稿摘要，不包含后端数据库 `id`
