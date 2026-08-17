# 销售订单业务选择快捷搜索

## 文档信息
- 领域：`sales-order`
- 控制器：`SalesOrderAdminController#businessSelectQuickSearch`
- 请求方式：`POST /erp/v1/sales/salesOrder/businessSelect/quickSearch`
- 聚合文档引用：`docs/kn/sales-order-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "SO-"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `keyword` | 否 | 匹配订单编号或客户名称 |

## 响应示例

```json
{
  "success": true,
  "data": [{ "id": 2001, "code": "SO-001", "name": "杭州客户", "label": "SO-001 杭州客户" }]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `id` | 是 | 销售订单主键 |
| `code` | 否 | 销售订单编号 |
| `name` | 否 | 客户名称 |
| `label` | 否 | 选择器展示文案 |

## 规则说明

- 仅返回当前租户内的销售订单。
