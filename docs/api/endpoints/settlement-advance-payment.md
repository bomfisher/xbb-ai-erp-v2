# 预付款接口

基础路径：`/erp/v1/settlement/advancePayment`。接口形态与付款单一致，但仅处理供应商预付款。

| 接口 | 用途 | 请求 | 返回 |
| --- | --- | --- | --- |
| `POST /list` | 分页查询预付款 | `ListBaseDTO` | `ListBaseVO<PaymentListItemVO>` |
| `POST /addItem` | 新建预付款表单 | `BaseDTO` | `SaveItemVO<PaymentSaveItemVO>` |
| `POST /updateItem` | 编辑预付款 | `IdBaseDTO` | `SaveItemVO<PaymentSaveItemVO>` |
| `POST /saveDraft` | 保存预付款草稿 | `PaymentDraftSaveDTO` | `DraftSaveVO` |
| `POST /saveAndSubmit` | 正式保存预付款 | `PaymentSubmitSaveDTO` | `BaseVO` |
| `POST /audit`、`/unaudit` | 审核、反审核预付款 | `IdBaseDTO` | `BaseVO` |

## 关键规则

- 列表查询固定附加 `payment_type=ADVANCE_PAYMENT`，不接受客户端修改该限制。
- 新建、草稿和正式保存均由后端写入 `ADVANCE_PAYMENT`；付款类型不在表单中展示。
- 编辑、审核与反审核只允许操作预付款；其他类型的单据按“不属于当前业务”拒绝。
