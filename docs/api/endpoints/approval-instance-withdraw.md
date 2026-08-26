# 撤回审批

- 路径：`POST /erp/v1/approval/instance/withdraw`
- 控制器：`ApprovalInstanceAdminController#withdraw`

请求体：`corpid`、`userId`、`instanceId` 必填，`comment` 可选。仅提交人可撤回处于待审批或审批中的实例；撤回后业务单据不执行审批通过后的副作用。
