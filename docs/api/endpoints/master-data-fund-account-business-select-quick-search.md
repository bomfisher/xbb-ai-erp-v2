# 资金账户业务选择快捷搜索

## 文档信息

- 领域：`master-data`
- 控制器：`FundAccountAdminController#businessSelectQuickSearch`
- 请求方式：`POST /erp/v1/masterData/fundAccount/businessSelect/quickSearch`
- 聚合文档引用：`docs/kn/fund-account-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "现金",
  "pageSize": 5
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户标识。 |
| `userId` | 否 | 当前操作人标识。 |
| `keyword` | 否 | 按账户编码、账户名称或银行账号包含匹配。 |
| `pageSize` | 否 | 前端快捷搜索传入；服务端最多返回 5 条。 |

## 响应示例

```json
{
  "code": "1",
  "success": true,
  "data": [
    {
      "id": 1001,
      "code": "FA-001",
      "name": "现金账户",
      "label": "FA-001 现金账户"
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `id` | 是 | 资金账户主键。 |
| `code` | 是 | 账户编码。 |
| `name` | 是 | 账户名称。 |
| `label` | 是 | 用于选择控件展示的账户编码和名称组合。 |

## 规则说明

- 仅返回当前租户下未删除且状态为可用的资金账户。
- 该接口用于 `FUND_ACCOUNT` 业务选择字段的输入联想。
