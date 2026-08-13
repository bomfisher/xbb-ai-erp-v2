# 采购入库单确认入库

- 控制器：`PurchaseInboundAdminController#confirmInbound`
- 请求方式：`POST /erp/v1/purchase/purchaseInbound/confirmInbound`
- 请求 DTO：`PurchaseInboundConfirmDTO`
- 响应：`ResultVO<BaseVO>`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "id": 1001
}
```

## 规则

- 仅状态为 `SUBMITTED` 的采购入库单可确认；`INVENTORY_POSTED` 重复确认直接成功，不重复增加库存。
- 服务端读取已保存的入库主单和明细，组装库存入库命令；不信任确认请求传入的数量或成本。
- 入库数量来自明细 `qty`，入库总成本来自服务端计算后的 `costAmount`。
- 采购入库单、明细、库存余额、数量流水和成本流水在同一事务中提交；任一步失败均整体回滚。
- 成功后主单状态更新为 `INVENTORY_POSTED`；已确认单据不可通过保存接口修改。
