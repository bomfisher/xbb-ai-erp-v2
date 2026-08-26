# 资金账户业务选择按 ID 回显

## 文档信息

- 领域：`master-data`
- 控制器：`FundAccountAdminController#businessSelectGetById`
- 请求方式：`POST /erp/v1/masterData/fundAccount/businessSelect/getById`
- 聚合文档引用：`docs/kn/fund-account-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "id": 1001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户标识。 |
| `userId` | 否 | 当前操作人标识。 |
| `id` | 是 | 需回显的资金账户主键。 |

## 响应示例

```json
{
  "code": "1",
  "success": true,
  "data": {
    "id": 1001,
    "code": "FA-001",
    "name": "现金账户",
    "label": "FA-001 现金账户"
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `id` | 否 | 找到可用资金账户时返回主键。 |
| `code` | 否 | 找到可用资金账户时返回账户编码。 |
| `name` | 否 | 找到可用资金账户时返回账户名称。 |
| `label` | 否 | 找到可用资金账户时返回选择控件展示文本。 |

## 规则说明

- 仅允许回显当前租户下未删除且状态为可用的资金账户。
- 目标不存在、已删除、已禁用或不属于当前租户时，`data` 为 `null`。
