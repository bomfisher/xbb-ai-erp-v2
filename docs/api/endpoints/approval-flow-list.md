# 审批流程列表

- 控制器：`ApprovalFlowAdminController#list`
- 请求方式：`POST /erp/v1/approval/flow/list`

## 请求示例

```json
{"corpid":"corp-001","userId":"u-001","businessCode":"SALES_ORDER","approvalScene":"CREATE"}
```

`businessCode` 和 `approvalScene` 均可省略，以查询当前租户全部流程版本。

## 响应

返回 `ResultVO<List<ApprovalFlowVO>>`，包含流程名称、版本、优先级、状态和范围。列表不展开节点审批人明细。
