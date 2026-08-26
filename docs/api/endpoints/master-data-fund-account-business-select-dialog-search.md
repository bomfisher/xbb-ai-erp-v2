# 资金账户业务选择弹窗搜索

## 文档信息

- 领域：`master-data`
- 控制器：`FundAccountAdminController#businessSelectDialogSearch`
- 请求方式：`POST /erp/v1/masterData/fundAccount/businessSelect/dialogSearch`
- 聚合文档引用：`docs/kn/fund-account-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "银行",
  "pageNum": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户标识。 |
| `userId` | 否 | 当前操作人标识。 |
| `keyword` | 否 | 按账户编码、账户名称或银行账号包含匹配。 |
| `pageNum` | 否 | 页码，默认 `1`。 |
| `pageSize` | 否 | 每页条数，默认 `20`。 |

## 响应示例

```json
{
  "code": "1",
  "success": true,
  "data": {
    "list": [
      {
        "id": 1002,
        "code": "FA-002",
        "name": "招商银行基本户",
        "label": "FA-002 招商银行基本户"
      }
    ],
    "pageHelper": {
      "page": 1,
      "count": 1,
      "hasLeft": false,
      "hasRight": false
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `list` | 是 | 当前页资金账户候选项。 |
| `pageHelper` | 是 | 分页信息，包含当前页、总页数和前后页标识。 |
| `list[].id` | 是 | 资金账户主键。 |
| `list[].code` | 是 | 账户编码。 |
| `list[].name` | 是 | 账户名称。 |
| `list[].label` | 是 | 选择控件展示文本。 |

## 规则说明

- 仅返回当前租户下未删除且状态为可用的资金账户。
- 该接口用于 `FUND_ACCOUNT` 业务选择字段的弹窗检索。
