# 应收开放项接口

基础路径：`/erp/v1/settlement/receivable`。所有接口使用 `ResultVO` 响应体，写入请求必须携带 `corpid` 与 `userId`。

| 接口 | 用途 | 请求 | 返回 |
| --- | --- | --- | --- |
| `POST /list` | 分页查询应收开放项 | `ListBaseDTO` | `ListBaseVO<ReceivableListItemVO>` |
| `POST /addItem` | 新建表单与编号初始化 | `BaseDTO` | `SaveItemVO<ReceivableSaveItemVO>` |
| `POST /updateItem` | 编辑表单回填 | `IdBaseDTO` | `SaveItemVO<ReceivableSaveItemVO>` |
| `POST /saveDraft` | 保存草稿 | `ReceivableDraftSaveDTO` | `DraftSaveVO` |
| `POST /saveAndSubmit` | 正式保存应收开放项 | `ReceivableSubmitSaveDTO` | `BaseVO` |
| `POST /draftList` | 查询当前租户草稿 | `ReceivableDraftListDTO` | 草稿摘要列表 |
| `POST /loadDraft` | 加载草稿 | `ReceivableDraftLoadDTO` | `ReceivableDraftDetailVO` |
| `POST /businessSelect/quickSearch` | 按客户快速检索未结清应收 | `ReceivableBusinessSelectQueryDTO` | 应收选择项列表 |
| `POST /businessSelect/dialogSearch` | 按客户分页选择未结清应收 | `ReceivableBusinessSelectQueryDTO` | 应收选择项分页列表 |
| `POST /businessSelect/getById` | 查询指定应收选择项 | `ReceivableBusinessSelectQueryDTO` | 应收选择项 |

## 关键规则

- 新建通过编号工厂使用 `RECEIVABLE` 业务编码生成应收开放项编号。
- 客户、来源类型、应收确认日期和初始应收金额必填，初始应收金额必须大于零。
- 来源类型为 `SALES_INVOICE` 时，必须选择已过账的来源销售发票，且客户与发票客户一致；同一发票可拆分创建多张应收，合计金额不得超过发票可开应收金额。
- 首次保存时后端强制设置已核销金额为 `0.00`、未核销余额等于初始应收金额、状态为未核销，忽略前端传入的这些初始值。
- 更新时保留已核销金额和核销状态，未核销余额按“当前初始应收金额 - 已核销金额”重新计算。
- 新增、编辑、删除以及销售发票自动开立应收时，均同步维护销售发票的已开应收金额和可开应收金额；金额不得小于已核销金额。
- 选择来源销售发票会自动回填客户；先选择客户时，销售发票快捷搜索和弹窗查询仅返回该客户的可开应收发票。修改或清空客户会清空已选来源发票。
- 收款单选择应收时必须传入 `customerId`；快速检索和弹窗列表仅返回该客户未核销余额大于零的应收项，并在标签中展示应收编号和未核销余额。
