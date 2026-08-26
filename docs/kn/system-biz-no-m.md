# 编号管理

系统模块维护租户级编号规则，以 `corpid + businessCode` 隔离。当前公司未配置时回退至 `corpid='0'` 默认规则；公司首次保存默认规则时创建覆盖记录，后续保存更新该记录。规则可配置前缀、时间编码、自增后缀位数与自增方式。完整契约：

- `docs/api/endpoints/system-biz-no-save-rule.md`
- `docs/api/endpoints/system-biz-no-get-rule.md`
- `docs/api/endpoints/system-biz-no-next.md`
- `docs/api/endpoints/system-biz-no-business-tree.md`

主数据使用持久化号段，业务单据按日期在 Redis 中递增；两者均保证不会重复。
