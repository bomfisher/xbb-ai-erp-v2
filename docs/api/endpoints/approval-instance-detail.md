# 审批实例详情

## 文档信息

- 领域：`approval`
- 控制器：`ApprovalInstanceAdminController#detail`
- 请求方式：`POST /erp/v1/approval/instance/detail`
- 聚合文档引用：`docs/kn/approval-center-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "instanceId": "approval-instance-id"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 租户标识。 |
| `userId` | 是 | 当前用户标识。 |
| `instanceId` | 是 | 审批实例标识。 |

## 响应参数说明

成功时返回列表项字段以及 `flowName`、`flowVersion`、`scene`、`subjectSnapshotJson`、`flowNodes` 和 `timeline`。`flowNodes` 包含整个冻结流程的节点名称、提交时冻结的实际审批人和节点状态；`timeline` 每项包含节点、处理人、动作、意见和处理时间。

## 规则说明

- 空 `instanceId` 返回业务错误“审批实例标识不能为空”。
- 详情以冻结的流程与业务快照展示，不受后续流程配置变更影响。
- 仅实例提交人或曾被分配该实例任务的人员可以查看详情；其他人员返回业务错误“无权查看该审批实例”。
