# 审批中心列表

## 文档信息

- 领域：`approval`
- 控制器：`ApprovalInstanceAdminController#list`
- 请求方式：`POST /erp/v1/approval/instance/list`
- 聚合文档引用：`docs/kn/approval-center-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "view": "TODO",
  "status": "IN_PROGRESS",
  "businessCode": "SALES_CONTRACT",
  "keyword": "合同"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 租户标识。 |
| `userId` | 是 | 当前用户标识。 |
| `view` | 否 | 审批中心视图：`TODO`、`SUBMITTED`、`COMPLETED`。 |
| `status` | 否 | 审批状态筛选。 |
| `businessCode` | 否 | 业务对象编码筛选。 |
| `keyword` | 否 | 单据摘要或提交人关键字。 |

## 响应示例

```json
{
  "success": true,
  "data": []
}
```

## 响应参数说明

列表项预留 `instanceId`、`businessCode`、`businessName`、`subjectId`、`subjectSummary`、`submitterName`、`status`、`currentNodeName`、`submittedAt` 和 `completedAt`。

## 规则说明

- 实例由审批运行时持久化；`TODO`、`SUBMITTED`、`COMPLETED` 分别表示待我处理、我提交、与我相关的已完成视图。已完成视图仅包含本人提交或本人曾处理过的终态实例。
- 运行时同时冻结流程版本、业务快照、节点任务和操作日志，后续流程定义修改不会影响历史实例。
- 前端状态筛选 `PENDING`、`IN_PROGRESS` 分别映射实例存储状态 `PENDING_APPROVAL`、`IN_APPROVAL`；终态编码保持不变。
- 申请人通过组织查询批量解析姓名；组织中不存在或已停用的人员保留其原员工标识。
