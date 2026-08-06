# 成员单选弹窗搜索

## 文档信息
- 领域：`org`
- 控制器：`MemberSelectAdminController#dialogSearch`
- 请求方式：`POST /erp/v1/org/memberSelect/dialogSearch`
- 聚合文档引用：`docs/kn/field-business-select-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "keyword": "张",
  "pageNum": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID，为空时后端抛 `BizException` |
| `keyword` | 否 | 成员姓名或工号关键字 |
| `pageNum` | 否 | 页码，小于 `1` 时按 `1` 处理 |
| `pageSize` | 否 | 每页条数，小于 `1` 时按 `20` 处理 |
| `id` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": {
    "list": [
      {
        "id": "EMP-1001",
        "code": "E001",
        "name": "张三",
        "label": "E001 张三"
      }
    ],
    "pageHelper": {
      "page": 1,
      "count": 1,
      "hasLeft": false,
      "hasRight": false
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 当前页成员候选列表 |
| `data.list[].id` | 是 | 员工 ID，字符串类型 |
| `data.list[].code` | 否 | 员工工号 |
| `data.list[].name` | 否 | 员工姓名 |
| `data.list[].label` | 否 | 前端直接展示文案 |
| `data.pageHelper` | 是 | 分页信息 |
| `data.pageHelper.page` | 是 | 当前页码 |
| `data.pageHelper.count` | 是 | 总页数 |
| `data.pageHelper.hasLeft` | 是 | 是否存在上一页 |
| `data.pageHelper.hasRight` | 是 | 是否存在下一页 |

## 规则说明

- 只返回“启用且在职”的员工候选
- 用于成员单选弹窗内的列表搜索
- 当前实现先查出全部符合条件员工，再按页码做内存分页切片
- 后端默认页码为 `1`、默认每页条数为 `20`
