# 销售订单业务选择弹窗搜索

## 文档信息
- 领域：`sales-order`
- 控制器：`SalesOrderAdminController#businessSelectDialogSearch`
- 请求方式：`POST /erp/v1/sales/salesOrder/businessSelect/dialogSearch`
- 聚合文档引用：`docs/kn/sales-order-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "SO-",
  "pageNum": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `keyword` | 否 | 匹配订单编号或客户名称 |
| `pageNum` | 否 | 页码，默认 `1` |
| `pageSize` | 否 | 每页条数，默认 `20` |

## 响应示例

```json
{
  "success": true,
  "data": {
    "list": [{ "id": 2001, "code": "SO-001", "name": "杭州客户", "label": "SO-001 杭州客户" }]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 当前页销售订单候选项 |
| `pageHelper` | 是 | 当前页码与总页数信息 |

## 规则说明

- 仅返回当前租户内的销售订单。
- 页码或每页条数小于 `1` 时使用默认值。
