# 销售发票业务选择按 ID 回显

## 文档信息
- 领域：`sales-invoice`
- 控制器：`SalesInvoiceAdminController#businessSelectGetById`
- 请求方式：`POST /erp/v1/sales/salesInvoice/businessSelect/getById`
- 聚合文档引用：`docs/kn/sales-invoice-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "id": 2001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `id` | 是 | 销售发票主键 |

## 响应示例

```json
{
  "success": true,
  "data": { "id": 2001, "code": "SI-001", "name": "SI-001", "label": "SI-001" }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 否 | 目标不存在或不属于当前租户时为 `null` |

## 规则说明

- 仅允许回显当前租户内的销售发票。
