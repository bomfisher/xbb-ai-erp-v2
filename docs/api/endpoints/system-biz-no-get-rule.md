# 获取业务编号规则

## 接口

`POST /erp/v1/system/bizNo/getRule`

## 请求

请求体为 `BizNoRuleQueryDTO`，继承 `BaseDTO`，包含 `corpid`、`userId` 和 `businessCode`。

## 响应

返回 `ResultVO<BizNoRuleVO>`，其中 `businessCode`、`prefix`、`ruleType` 为当前公司对应的生效规则。

## 规则

未配置当前 `corpid + businessCode` 的规则时返回业务异常。
