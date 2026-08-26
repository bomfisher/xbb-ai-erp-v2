# 客户收款接口

基础路径：`/erp/v1/settlement/receipt`。所有接口均以统一响应体 `ResultVO` 返回，写入请求必须提供 `corpid` 与 `userId`。

| 接口 | 用途 | 请求 | 返回 |
| --- | --- | --- | --- |
| `POST /list` | 分页查询收款单 | `ListBaseDTO` | `ListBaseVO<ReceiptListItemVO>` |
| `POST /addItem` | 新建表单与编号初始化 | `BaseDTO` | `SaveItemVO<ReceiptSaveItemVO>` |
| `POST /updateItem` | 编辑表单回填 | `IdBaseDTO` | `SaveItemVO<ReceiptSaveItemVO>` |
| `POST /saveDraft` | 保存草稿 | `ReceiptDraftSaveDTO` | `DraftSaveVO` |
| `POST /saveAndSubmit` | 正式保存收款单 | `ReceiptSubmitSaveDTO` | `BaseVO` |
| `POST /writeOff` | 对既有收款执行核销 | `ReceiptWriteOffDTO` | `BaseVO` |
| `POST /reverseWriteOff` | 反核销一条核销明细 | `IdBaseDTO` | `BaseVO` |
| `POST /draftList` | 查询当前租户草稿 | `ReceiptDraftListDTO` | 草稿摘要列表 |
| `POST /loadDraft` | 加载草稿 | `ReceiptDraftLoadDTO` | `ReceiptDraftDetailVO` |

## 关键规则

- 新建表单调用编号工厂，以 `RECEIPT` 规则生成“前缀 + 日期 + 自增序号”收款单编号。
- 客户和收款日期为必填；“收款信息”子表至少一行，每行均需选择资金账户、收款方式并填写大于零的收款金额。
- 主表实际收款金额由收款信息子表金额汇总，后端保存时重新计算，不采信前端汇总值。
- 新建时若当前租户存在默认且可用的资金账户，首行自动预选该账户。
- 普通收款（`CUSTOMER_PAYMENT`）必须填写至少一条 `writeOffs` 核销明细；每条包含 `receivableId` 与大于零的 `amount`。同一收款可关联多笔同客户、未结清的应收开放项。
- 普通收款保存时，在同一事务内创建收款和 `receipt_writeoff` 核销记录，并同步更新收款与应收的已核销金额、未核销余额和核销状态；核销金额不得超过任一余额。
- 编辑普通收款时回显当前有效的 `writeOffs`。提交编辑会先反核销原明细，再按本次明细重新核销；任一校验失败时整体回滚。
- 预收款（`ADVANCE_PAYMENT`）创建时不得填写 `writeOffs`，可在后续通过核销接口关联应收款。
- 新建普通收款时，已核销金额初始化为 `0.00`、未核销余额等于实际收款金额、状态初始化为 `0`（未核销）；提交核销后按实际分配额更新。
- 收款类型为 `CUSTOMER_PAYMENT`、`ADVANCE_PAYMENT` 或 `OPENING_BALANCE`；本期资金账户字段留空。
- 收款方式为商业承兑汇票、银行承兑汇票、微信支付、支付宝支付、银行转账、银行支票或现金支付。
