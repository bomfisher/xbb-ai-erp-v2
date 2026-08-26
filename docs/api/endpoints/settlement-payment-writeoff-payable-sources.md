# 查询付款核销应付款来源

## 文档信息

- 领域：`settlement`
- 控制器：`PaymentWriteOffAdminController#payableSources`
- 请求方式：`POST /erp/v1/settlement/paymentWriteoff/payableSources`
- 聚合文档引用：`docs/kn/settlement-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "supplierId": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 公司标识。 |
| `supplierId` | 是 | 供应商 ID。 |

## 响应示例

```json
{
  "code": "1",
  "message": "success",
  "success": true,
  "data": [
    {
      "id": 20,
      "code": "YF-202608250001",
      "amount": 300.00,
      "writtenOffAmount": 100.00,
      "remainingAmount": 200.00
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `id` | 是 | 应付款 ID。 |
| `code` | 是 | 应付款编号。 |
| `amount` | 是 | 单据金额。 |
| `writtenOffAmount` | 是 | 已核销金额。 |
| `remainingAmount` | 是 | 剩余可核销金额。 |

## 规则说明

- 仅返回该供应商下剩余可核销金额大于零的应付款。
