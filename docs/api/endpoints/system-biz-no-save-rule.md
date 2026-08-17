# 保存业务编号规则

## 接口

`POST /erp/v1/system/bizNo/saveRule`

## 请求

请求体为 `BizNoRuleSaveDTO`，继承 `BaseDTO`，包含 `corpid`、`userId`、`businessCode`、`prefix` 与 `ruleType`。

`ruleType` 仅允许：

- `MASTER_DATA`：生成 `前缀-五位序号`，例如 `SKU-00001`。
- `DOCUMENT`：生成 `前缀-yyyyMMdd-五位序号`，例如 `PO-20260813-00001`。

## 响应

返回 `ResultVO<BaseVO>`。

## 规则

规则以 `corpid + businessCode` 唯一。编号前缀和规则类型不能为空，保存后供发号入口与业务模块调用。
