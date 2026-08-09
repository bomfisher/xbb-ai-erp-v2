# 部门选择按 ID 回显

## 文档信息
- 领域：`org`
- 控制器：`DepartmentSelectAdminController#getById`
- 请求方式：`POST /erp/v1/org/departmentSelect/getById`
- 聚合文档引用：`docs/kn/field-product-user-m.md`

## 请求示例

```json
{"corpid":"企业ID","id":10}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前企业上下文 |
| `id` | 是 | 稳定部门 ID |

## 响应示例

```json
{"success":true,"data":{"id":10,"code":"RD","name":"研发部","label":"RD 研发部"}}
```

## 规则说明

- 仅回显当前企业启用的部门；未找到或已停用时返回 `null`。
