# 审批实例表设计

审批运行时由 `V41__create_approval_instance_tables.sql` 创建以下三张表：

- `approval_instance`：冻结业务快照、流程快照、当前节点和实例状态；以 `(corpid, request_id)` 保证提交幂等。
- `approval_instance_task`：按审批节点冻结的审批任务，支持会签、或签及撤回后的取消状态。
- `approval_instance_action_log`：提交、自动通过、同意、拒绝和撤回的不可变操作轨迹。

`V42__add_approval_instance_audit_columns.sql` 为实例和任务补齐 `del`、`add_time`、`update_time`，与持久化 `BaseEntity` 保持一致。历史环境必须按 Flyway 顺序执行 `V41`、`V42`；禁止手工修改已执行迁移。
