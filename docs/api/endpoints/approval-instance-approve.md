# 审批同意

- 路径：`POST /erp/v1/approval/instance/approve`
- 控制器：`ApprovalInstanceAdminController#approve`

请求体：`corpid`、`userId`、`instanceId` 必填，`comment` 可选。仅当前节点的冻结审批人可以同意；会签需要全部同意，或签任一人同意即推进后续节点。

审批完成后，审批平台向匹配的 `ApprovalResultHandler` 投递 `APPROVED` 事件，由业务单据执行生效副作用。

当前仅运行指定人员审批；配置角色或主管审批人的流程会在提交实例时返回明确业务错误，避免自动通过。
