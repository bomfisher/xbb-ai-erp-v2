# 业务编号

系统模块维护租户级业务编号规则，以 `corpid + businessCode` 隔离。规则完整契约：

- `docs/api/endpoints/system-biz-no-save-rule.md`
- `docs/api/endpoints/system-biz-no-get-rule.md`
- `docs/api/endpoints/system-biz-no-next.md`

主数据使用持久化号段，业务单据按日期在 Redis 中递增；两者均保证不会重复。
