# 获取下一个编号

## 接口

`POST /erp/v1/system/bizNo/next`

## 请求

请求体为 `BizNoNextDTO`，继承 `BaseDTO`，包含 `corpid`、`userId` 和 `businessCode`。

## 响应

返回 `ResultVO<BizNoNextVO>`，`code` 为本次已分配的唯一编号。

## 规则

- `DAILY` 自增方式以 Redis `INCR` 原子递增，序号缓存于次日零点后过期。
- `CONTINUOUS` 自增方式由数据库原子预占 1000 个号段，Redis 只消费已预占号段；缓存丢失可跳号但不会重复。
- 规则查询优先使用当前 `corpid` 的配置；不存在时回退到默认 `corpid='0'` 的配置。
- 业务模块可直接注入 `BizNoGenerator`，调用 `next(corpid, businessCode)`，并将结果保存到自身聚合；不允许由前端传入或覆盖编号。
