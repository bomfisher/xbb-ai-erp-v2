# 删除审批流程

## 文档信息

- 领域：`approval`
- 控制器：`ApprovalFlowAdminController#delete`
- 请求方式：`POST /erp/v1/approval/flow/delete`
- 聚合文档引用：`docs/kn/approval-flow-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "id": 4
}
```

## 响应

成功时返回 `ResultVO<BaseVO>`，`data` 为空对象。

## 规则说明

- 状态为 `DRAFT`（未启用草稿）或 `DISABLED`（已停用）的流程可删除；已生效和已归档流程会被拒绝。
- 流程存在 `PENDING_APPROVAL` 或 `IN_APPROVAL` 审批实例时会被拒绝，错误信息为“流程存在待审批或审批中的实例，不允许删除”。
- 删除为逻辑删除：流程定义、节点和节点审批人均不再参与流程匹配或设置列表查询。
- 删除后重新创建相同业务、场景和流程编码时，系统会参考包含逻辑删除记录在内的历史最大版本号递增，不会重新使用已存在的版本号。
- 审批实例必须保存创建时冻结的流程版本与节点快照；因此删除定义不影响历史审批单据的流程详情查看。
