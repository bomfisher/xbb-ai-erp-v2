# 保存审批流程

- 控制器：`ApprovalFlowAdminController#saveDraft`
- 请求方式：`POST /erp/v1/approval/flow/saveDraft`

新建流程默认保存为未启用（`DRAFT`）。编辑已有流程时按请求中的 `id` 原地保存，保留原流程版本和启用状态：已启用仍为 `PUBLISHED`，已停用仍为 `DISABLED`，未启用仍为 `DRAFT`。审批实例在提交时已冻结流程快照，因此不会受后续流程编辑影响。

## 请求示例

```json
{
  "corpid":"corp-001",
  "userId":"u-001",
  "businessCode":"SALES_ORDER",
  "approvalScene":"CREATE",
  "flowCode":"SALES_ORDER_CREATE_DEFAULT",
  "flowName":"销售订单新建审批",
  "priority":100,
  "scopeJson":"{}",
  "nodes":[
    {
      "nodeNo":1,
      "nodeName":"部门主管审批",
      "approvalMode":"ANY_SIGN",
      "approvers":[{"approverType":"SUPERIOR","superiorLevel":1}]
    }
  ]
}
```
