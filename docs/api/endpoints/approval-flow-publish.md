# 发布审批流程

- 控制器：`ApprovalFlowAdminController#publish`
- 请求方式：`POST /erp/v1/approval/flow/publish`

草稿流程可首次启用；已停用流程可通过同一接口恢复启用。

仅草稿可发布。发布校验至少一个节点、节点顺序连续、节点模式完整、审批人规则完整。发布后流程版本只读。
