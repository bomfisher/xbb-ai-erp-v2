# 创建审批流程下一版本

- 控制器：`ApprovalFlowAdminController#createNextVersion`
- 请求方式：`POST /erp/v1/approval/flow/createNextVersion`

该接口用于显式创建下一版本草稿，从已发布或已停用版本复制出 `version + 1` 的流程。审批设置页的常规编辑保存不调用此接口，而是在原流程上保存并保持其启用状态；历史审批实例继续使用提交时冻结的流程快照。
