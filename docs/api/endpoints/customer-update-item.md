# 客户编辑表单加载

## 文档信息
- 领域：`customer`
- 控制器：`CustomerAdminController#updateItem`
- 请求方式：`POST /erp/v1/masterData/customer/updateItem`
- 聚合文档引用：`docs/kn/customer-m.md`

## 请求示例

```json
{ "corpid": "demo-corp", "userId": "115014265324309213", "id": 1 }
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户标识 |
| `userId` | 是 | 当前操作人标识 |
| `id` | 是 | 客户主键 |

## 响应示例

```json
{ "code": 0, "success": true, "data": { "headList": [{ "attr": "contacts", "fieldType": "49" }], "data": { "main": { "id": 1, "defaultContactId": 10 }, "contacts": [{ "id": 10, "name": "张三", "mobile": "13800000000", "defaultFlag": 1 }] } } }
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.contacts` | 是 | 当前客户的全部未删除联系人；每行 `id` 用于后续同步更新 |

## 规则说明

- 联系人以顶层 `contacts` 数组回填，不再嵌套到 `main.defaultContactId`。
