# 客户业务选择快捷搜索

## 文档信息
- 领域：`master-data-customer`
- 控制器：`CustomerAdminController#businessSelectQuickSearch`
- 请求方式：`POST /erp/v1/masterData/customer/businessSelect/quickSearch`
- 聚合文档引用：`docs/kn/customer-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "userId": "115014265324309213",
  "keyword": "客户"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码 |
| `userId` | 否 | 当前操作人 |
| `keyword` | 否 | 匹配客户编码或名称 |

## 响应示例

```json
{
  "success": true,
  "data": [{ "id": 1001, "code": "CUS-001", "name": "杭州客户", "label": "CUS-001 杭州客户" }]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `id` | 是 | 客户主键 |
| `code` | 否 | 客户编码 |
| `name` | 否 | 客户名称 |
| `label` | 否 | 选择器展示文案 |

## 规则说明

- 仅返回当前租户内的客户。
- 未传关键字时返回当前租户的全部候选项。
