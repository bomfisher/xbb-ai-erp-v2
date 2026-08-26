# 停用审批流程

- 控制器：`ApprovalFlowAdminController#disable`
- 请求方式：`POST /erp/v1/approval/flow/disable`

仅已发布版本可停用。停用后不再匹配新提交；已经绑定该版本的审批实例应继续按冻结版本执行。
