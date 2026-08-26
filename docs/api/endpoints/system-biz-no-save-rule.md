# 保存编号规则

## 接口

`POST /erp/v1/system/bizNo/saveRule`

## 请求

请求体为 `BizNoRuleSaveDTO`，继承 `BaseDTO`，包含 `corpid`、`userId`、`businessCode`、`prefix`、`includeDate`、`suffixLength` 与 `serialMode`。

新页面使用的字段：

- `includeDate`：`0` 表示不包含时间编码，`1` 表示包含 `yyyyMMdd`；
- `suffixLength`：自增后缀位数，范围为 `1` 到 `18`；
- `serialMode`：`CONTINUOUS` 连续递增，`DAILY` 按日递增。

兼容旧调用方：未传新字段时仍可传 `ruleType`。`MASTER_DATA` 映射为不含时间编码、五位、连续递增；`DOCUMENT` 映射为含时间编码、五位、按日递增。

## 响应

返回 `ResultVO<BaseVO>`。

## 规则

规则以 `corpid + businessCode` 唯一。保存时只查询并更新当前 `corpid` 的记录：当前公司首次保存默认规则时创建一条公司覆盖记录；后续保存始终更新该公司记录，不会重复初始化，也不会更新 `corpid='0'` 默认规则。
