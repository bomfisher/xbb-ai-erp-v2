# 审批拒绝

- 路径：`POST /erp/v1/approval/instance/reject`
- 控制器：`ApprovalInstanceAdminController#reject`

请求体：`corpid`、`userId`、`instanceId` 必填，`comment` 可选。仅当前节点冻结审批人可拒绝；拒绝后取消当前剩余任务并结束实例。
