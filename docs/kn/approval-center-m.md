# 审批中心

审批中心是全局工作入口，前端路由为 `/management/approval/list`，不依赖侧边菜单配置。页面通过审批领域查询当前用户的待办、已提交和已完成审批，并在右侧展示实例详情、流程记录和冻结快照。

审批实例运行时已落地。列表按实例任务和提交人限定可见范围：`TODO` 为当前用户待处理任务，`SUBMITTED` 为本人提交，`COMPLETED` 为本人提交或曾处理过的终态实例。详情以提交时冻结的流程和业务快照为准，不受流程后续修改或删除影响。

- 列表：`docs/api/endpoints/approval-instance-list.md`
- 详情：`docs/api/endpoints/approval-instance-detail.md`
