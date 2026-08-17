# 客户业务选择按 ID 回显

## 文档信息
- 领域：`master-data-customer`
- 控制器：`CustomerAdminController#businessSelectGetById`
- 请求方式：`POST /erp/v1/masterData/customer/businessSelect/getById`
- 聚合文档引用：`docs/kn/customer-m.md`

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
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `id` | 是 | 客户主键 |

## 响应示例

```json
{
  "success": true,
  "data": { "id": 1001, "code": "CUS-001", "name": "杭州客户", "label": "CUS-001 杭州客户" }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 否 | 目标不存在或不属于当前租户时为 `null` |

## 规则说明

- 仅允许回显当前租户内的客户。
