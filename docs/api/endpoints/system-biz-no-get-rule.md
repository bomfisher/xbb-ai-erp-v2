# 获取编号规则

## 接口

`POST /erp/v1/system/bizNo/getRule`

## 请求

请求体为 `BizNoRuleQueryDTO`，继承 `BaseDTO`，包含 `corpid`、`userId` 和 `businessCode`。

## 响应

返回 `ResultVO<BizNoRuleVO>`，包含：

- `businessCode`：业务编码；
- `prefix`：编号前缀；
- `includeDate`：是否包含时间编码，`0` 否、`1` 是；
- `suffixLength`：自增后缀位数；
- `serialMode`：自增方式，`CONTINUOUS` 连续递增、`DAILY` 按日递增；
- `overridden`：当前公司是否已覆盖默认规则，`0` 否、`1` 是。

## 规则

优先读取当前公司的规则；当前公司未覆盖时读取 `corpid='0'` 的默认规则。两者均不存在时返回业务异常。
