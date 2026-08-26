# 销售发票业务选择快捷搜索

## 文档信息
- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#businessSelectQuickSearch`
- 请求方式：`POST /erp/v1/sales/salesInvoice/businessSelect/quickSearch`
- 聚合文档引用：`docs/kn/sales-invoice-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "SI-"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `keyword` | 否 | 匹配销售发票编号 |

## 响应示例

```json
{
  "success": true,
  "data": [{ "id": 2001, "code": "SI-001", "name": "SI-001", "label": "SI-001" }]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `id` | 是 | 销售发票主键 |
| `code` | 否 | 销售发票编号 |
| `name` | 否 | 销售发票名称，当前与编号相同 |
| `label` | 否 | 选择器展示文案 |

## 规则说明

- 仅返回当前租户内已审核、已过账且可开应收金额大于零的销售发票。
