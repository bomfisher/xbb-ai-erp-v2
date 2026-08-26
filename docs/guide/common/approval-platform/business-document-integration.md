# 业务单据接入审批运行时

## 适用范围

适用于需要“先生成审批中单据，审批通过后才执行业务副作用”的 ERP 单据。运行时契约以 `xbb-erp-module-approval-contract` 的 `ApprovalPlatformApi`、`ApprovalProgressHandler`、`ApprovalResultHandler` 为事实来源；销售订单是首个实现样例。

## 标准时序

```text
提交单据
  -> 完成业务字段、金额和引用校验
  -> 在同一事务中落库主子档，状态为待审批
  -> 调用 ApprovalPlatformApi.submit，subjectId 绑定已落库单据 ID
  -> 无需审批：状态改为无需审批，立即执行业务副作用
  -> 命中流程：首节点处理中时保持待审批，不执行库存、出入库、结算等副作用

首节点通过且仍有后续节点
  -> ApprovalProgressHandler 接收进行中事件
  -> 单据更新为审批中，继续禁止业务副作用

审批通过
  -> ApprovalResultHandler 接收 APPROVED 事件
  -> 幂等地将单据改为已通过
  -> 执行业务副作用

审批拒绝或撤回
  -> 单据改为已拒绝
  -> 不执行副作用；仅允许用户基于该单据复制新建
```

## 接入要求

1. `businessCode` 必须使用 `BusinessCodeEnum` 的 `getCode()`，不可使用 `flowCode`。
2. 提交命令的 `requestId` 必须可重试且租户内唯一；推荐 `<业务编码>-create:<单据号>`。
3. `subjectSnapshotJson` 是审批判断和历史展示的冻结快照，审批域不得读取业务 Mapper。
4. 主子档落库、审批实例提交与审批状态更新必须在同一应用事务中；提交失败应整体回滚。
5. `ApprovalProgressHandler` 和 `ApprovalResultHandler` 均以 `eventId` 或目标状态实现幂等；审批回调可重复投递。
6. 所有下游入口仍必须服务端校验单据是否具备业务资格，不能只依赖页面禁用。
7. 业务副作用只能在 `NO_APPROVAL` 或 `APPROVED` 后执行；不得在 `IN_APPROVAL`、`REJECTED`、`WITHDRAWN` 时执行。

## 销售订单参考

- 提交快照与 `ApprovalPlatformApi#submit`：`xbb-erp-module-sales/src/main/java/xbb/ai/erp/module/sales/application/approval/SalesOrderApprovalSubmitService.java`
- 待审批/审批中状态与无需审批直接生效：`xbb-erp-module-sales/src/main/java/xbb/ai/erp/module/sales/application/service/save/SalesOrderSaveAppServiceImpl.java`
- 通过/拒绝回调：`xbb-erp-module-sales/src/main/java/xbb/ai/erp/module/sales/application/approval/SalesOrderApprovalResultHandler.java`

## 当前运行时边界

首期审批实例执行线性审批节点。提交时会按当时的组织数据将指定人员、角色成员和 N 级主管全部解析为实例任务：首节点任务立即待处理，后续节点任务保持待激活，后续推进不再重新计算组织关系。审批详情会展示完整冻结流程和未来节点。条件节点路由、抄送、代理、加签、超时催办等能力仍需在流程定义图模型完善后接入，业务模块不得绕过审批平台自行实现。
