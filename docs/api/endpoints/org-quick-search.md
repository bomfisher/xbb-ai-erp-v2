# 成员单选快捷搜索

## 文档信息
- 领域：`org`
- 控制器：`MemberSelectAdminController#quickSearch`
- 请求方式：`POST /erp/v1/org/memberSelect/quickSearch`
- 聚合文档引用：`docs/kn/field-business-select-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "keyword": "张"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID，为空时后端抛 `BizException` |
| `keyword` | 否 | 成员姓名或工号关键字 |
| `id` | 否 | 当前接口不依赖该字段 |
| `pageNum` | 否 | 当前接口不依赖该字段 |
| `pageSize` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": [
    {
      "id": "EMP-1001",
      "code": "E001",
      "name": "张三",
      "label": "E001 张三"
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 成员候选列表 |
| `data[].id` | 是 | 员工 ID，字符串类型 |
| `data[].code` | 否 | 员工工号 |
| `data[].name` | 否 | 员工姓名 |
| `data[].label` | 否 | 前端直接展示文案；优先按 `工号 + 空格 + 姓名` 组装 |

## 规则说明

- 只返回“启用且在职”的员工候选
- 用于成员单选输入框的快捷搜索下拉候选
- 返回轻量列表，不返回分页结构
- 后端复用员工仓储查询，状态条件固定为在职 `ACTIVE` 与启用 `1`
