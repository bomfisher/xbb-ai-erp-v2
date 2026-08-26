# 付款单接口

基础路径：`/erp/v1/settlement/payment`。所有接口使用统一响应体 `ResultVO`，写入请求必须提供 `corpid` 与 `userId`。

| 接口 | 用途 | 请求 | 返回 |
| --- | --- | --- | --- |
| `POST /list` | 分页查询供应商付款 | `ListBaseDTO` | `ListBaseVO<PaymentListItemVO>` |
| `POST /addItem` | 新建供应商付款表单 | `BaseDTO` | `SaveItemVO<PaymentSaveItemVO>` |
| `POST /updateItem` | 编辑供应商付款 | `IdBaseDTO` | `SaveItemVO<PaymentSaveItemVO>` |
| `POST /saveDraft` | 保存供应商付款草稿 | `PaymentDraftSaveDTO` | `DraftSaveVO` |
| `POST /saveAndSubmit` | 正式保存供应商付款 | `PaymentSubmitSaveDTO` | `BaseVO` |
| `POST /audit`、`/unaudit` | 审核、反审核供应商付款 | `IdBaseDTO` | `BaseVO` |

## 关键规则

- 列表查询固定附加 `payment_type=SUPPLIER_PAYMENT`，不接受客户端修改该限制。
- 新建、草稿和正式保存均由后端写入 `SUPPLIER_PAYMENT`；付款类型不在表单中展示。
- 编辑、审核与反审核只允许操作供应商付款；其他类型的单据按“不属于当前业务”拒绝。
- 新建和编辑可填写“核销应付款”明细；付款金额不得小于明细合计，保存时同步生成核销记录并扣减应付款余额。
- 若付款金额大于核销合计，用户确认后可保存；审核该付款单时，系统将超额余额自动创建为同供应商预付款。
