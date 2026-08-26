# 销售发票业务选择弹窗搜索

## 文档信息
- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#businessSelectDialogSearch`
- 请求方式：`POST /erp/v1/sales/salesInvoice/businessSelect/dialogSearch`
- 聚合文档引用：`docs/kn/sales-invoice-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "SI-",
  "pageNum": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `keyword` | 否 | 匹配销售发票编号 |
| `pageNum` | 否 | 页码，默认 `1` |
| `pageSize` | 否 | 每页条数，默认 `20` |

## 响应示例

```json
{
  "success": true,
  "data": {
    "list": [{ "id": 2001, "code": "SI-001", "name": "SI-001", "label": "SI-001" }]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 当前页销售发票候选项 |
| `pageHelper` | 是 | 当前页码与总页数信息 |

## 规则说明

- 仅返回当前租户内的销售发票。
- 页码或每页条数小于 `1` 时使用默认值。
