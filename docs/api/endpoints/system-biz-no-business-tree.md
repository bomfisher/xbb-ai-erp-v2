# 获取可配置编号业务树

## 接口

`POST /erp/v1/system/bizNo/businessTree`

## 请求

请求体为 `BizNoRuleQueryDTO`，继承 `BaseDTO`，包含 `corpid` 与 `userId`。

## 响应

返回 `ResultVO<List<BizNoBusinessTreeVO>>`。根节点按基础资料和单据分组，叶子节点包含 `businessCode`、`businessName`、`overridden` 和 `children`。

## 规则

- 数据来自 `sys_biz_no_rule` 中当前公司的编号规则与 `corpid='0'` 默认规则；
- 同一业务同时存在公司规则和默认规则时，仅返回公司规则；
- `overridden=1` 表示当前公司已覆盖，`0` 表示仍使用默认规则。
