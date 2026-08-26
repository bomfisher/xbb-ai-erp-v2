# 审批流程详情

- 控制器：`ApprovalFlowAdminController#detail`
- 请求方式：`POST /erp/v1/approval/flow/detail`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","id":1001}
```

## 响应

返回流程头、范围 JSON、顺序节点、会签或或签模式，以及人员、角色、层级主管审批人规则。
