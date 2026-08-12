# 客户正式保存

## 文档信息
- 领域：`customer`
- 控制器：`CustomerAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/masterData/customer/saveAndSubmit`
- 聚合文档引用：`docs/kn/customer-m.md`

## 请求示例

```json
{ "corpid": "demo-corp", "userId": "115014265324309213", "main": { "customerCode": "C001", "customerName": "示例客户" }, "contacts": [{ "name": "张三", "mobile": "13800000000", "defaultFlag": 1 }], "draftMeta": {} }
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `main` | 是 | 客户主档 |
| `contacts` | 否 | 联系人子表；编辑已有行时携带行 `id` |
| `contacts[].defaultFlag` | 否 | `1` 表示默认联系人，`0` 表示非默认 |

## 响应示例

```json
{ "code": 0, "success": true, "data": {} }
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 空对象表示保存成功 |

## 规则说明

- 保存事务内同步 `customer_contact`：新增、更新和删除缺失行。
- 每位客户最多一位默认联系人；联系人行保存成功后回写客户主档的 `defaultContactId`。
- 名称、手机号都为空的前端默认空行不会持久化。
