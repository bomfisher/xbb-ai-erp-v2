# 部门选择快捷搜索

## 文档信息
- 领域：`org`
- 控制器：`DepartmentSelectAdminController#quickSearch`
- 请求方式：`POST /erp/v1/org/departmentSelect/quickSearch`
- 聚合文档引用：`docs/kn/field-product-user-m.md`

## 请求示例

```json
{"corpid":"企业ID","keyword":"研发"}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前企业上下文 |
| `keyword` | 否 | 匹配部门编码或名称 |

## 响应示例

```json
{"success":true,"data":[{"id":10,"code":"RD","name":"研发部","label":"RD 研发部"}]}
```

## 规则说明

- 仅返回当前企业启用的部门。
- 候选使用稳定部门 ID，供动态表单与列表筛选保存或提交。
