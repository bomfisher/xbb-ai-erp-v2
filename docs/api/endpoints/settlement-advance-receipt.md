# 预收款接口

基础路径：`/erp/v1/settlement/advanceReceipt`。接口形态与客户收款一致，但列表只返回 `receipt_type=ADVANCE_PAYMENT` 的数据；新建与正式保存均强制该类型。

预收款单号通过 `BizNoGenerator` 使用独立业务编码 `ADVANCE_RECEIPT` 生成，因而不与普通收款的 `RECEIPT` 编号序列混用。

收款信息使用可增删的子表行；每行选择资金账户、收款方式并填写金额。若当前租户存在默认且可用的资金账户，新建预收款时首行自动预选该账户。
