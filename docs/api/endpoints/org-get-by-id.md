# 成员单选按 ID 回显

## 文档信息
- 领域：`org`
- 控制器：`MemberSelectAdminController#getById`
- 请求方式：`POST /erp/v1/org/memberSelect/getById`
- 聚合文档引用：`docs/kn/field-business-select-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": "EMP-1001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID，为空时后端抛 `BizException` |
| `id` | 是 | 员工 ID，字符串类型；为空时后端抛 `BizException` |
| `keyword` | 否 | 当前接口不依赖该字段 |
| `pageNum` | 否 | 当前接口不依赖该字段 |
| `pageSize` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": {
    "id": "EMP-1001",
    "code": "E001",
    "name": "张三",
    "label": "E001 张三"
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 否 | 匹配不到员工时返回 `null` |
| `data.id` | 是 | 员工 ID，字符串类型 |
| `data.code` | 否 | 员工工号 |
| `data.name` | 否 | 员工姓名 |
| `data.label` | 否 | 前端直接展示文案 |

## 规则说明

- 用于成员单选已有值的回显展示
- `id` 不能为空，缺失时后端抛 `BizException`
- 当前实现按 `corpid + id` 查询员工；查不到时返回 `null`
- 返回结果统一转成成员选择候选对象结构
