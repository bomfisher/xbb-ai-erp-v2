# 部门选择弹窗搜索

## 文档信息
- 领域：`org`
- 控制器：`DepartmentSelectAdminController#dialogSearch`
- 请求方式：`POST /erp/v1/org/departmentSelect/dialogSearch`
- 聚合文档引用：`docs/kn/field-product-user-m.md`

## 请求示例

```json
{"corpid":"企业ID","keyword":"研发","pageNum":1,"pageSize":20}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前企业上下文 |
| `keyword` | 否 | 匹配部门编码或名称 |
| `pageNum` | 否 | 页码，默认 `1` |
| `pageSize` | 否 | 每页条数，默认 `20` |

## 响应示例

```json
{"success":true,"data":{"list":[{"id":10,"code":"RD","name":"研发部","label":"RD 研发部"}],"pageHelper":{"page":1,"count":1}}}
```

## 规则说明

- 仅返回当前企业启用的部门。
- 响应结构兼容公共 `BusinessDataSelectField` 弹窗选择协议。
