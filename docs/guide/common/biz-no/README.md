# 编号能力

`xbb-erp-base-bizno` 对调用方提供 `BizNoGenerator#next(corpid, businessCode)`；实现位于 `xbb-erp-module-system`，由其维护租户级规则和持久化计数器。规则支持前缀、是否包含时间编码、自增后缀位数与连续/按日两种自增方式。

业务模块在创建聚合时调用该接口并保存编号，不接受前端提交的编号值。主数据使用 `MASTER_DATA`，单据使用 `DOCUMENT`。规则、计数器和 Redis Key 均以 `corpid + businessCode` 隔离。

规则读取时优先匹配公司专属规则；没有专属规则时使用 `corpid='0'` 的默认规则。保存规则时只更新当前 `corpid`，公司首次保存默认规则会创建覆盖记录，后续保存更新该记录，不会重复初始化或误更新默认规则。
