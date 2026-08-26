# 手动创建付款核销

## 文档信息

- 领域：`settlement`
- 控制器：`PaymentWriteOffAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/settlement/paymentWriteoff/saveAndSubmit`
- 聚合文档引用：`docs/kn/settlement-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "supplierId": 1,
  "writeoffDate": 1780000000000,
  "remark": "预付款核销采购应付款",
  "allocations": [
    {
      "paymentId": 10,
      "payableId": 20,
      "amount": 100.00
    }
  ]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 公司标识。 |
| `userId` | 是 | 操作人标识。 |
| `supplierId` | 是 | 供应商；付款与应付款必须都归属该供应商。 |
| `writeoffNo` | 否 | 核销批次号；未传时由系统生成。 |
| `writeoffDate` | 否 | 核销日期时间戳；未传时使用当前时间。 |
| `remark` | 否 | 核销备注。 |
| `allocations` | 是 | 核销分配明细，至少一条。 |
| `allocations[].paymentId` | 是 | 预付款单或供应商付款单 ID。 |
| `allocations[].payableId` | 是 | 应付款 ID。 |
| `allocations[].amount` | 是 | 本次核销金额，必须大于零。 |

## 响应示例

```json
{
  "code": "1",
  "message": "success",
  "success": true,
  "data": {}
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 空对象，表示正式保存成功。 |

## 规则说明

- 每条明细的付款、应付款和核销金额都不能为空，且核销金额必须大于零。
- 两端单据必须归属同一供应商，且付款类型只能为预付款或供应商付款。
- 本次核销金额不得超过付款或应付款的剩余未核销金额。
- 保存后同步更新两端已核销金额、剩余金额和未核销/部分核销/已核销状态。
