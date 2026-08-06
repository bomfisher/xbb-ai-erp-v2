# 公共列表顶部按钮

## 文档信息
- 领域：`common-top-button`
- 控制器：`ListCommonController#topButton`
- 请求方式：`POST /erp/v1/common/list/topButton`
- 聚合文档引用：`docs/kn/common-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "SUPPLIER"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `businessCode` | 是 | 业务编码 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "buttonCode": "ADD",
        "buttonName": "新增",
        "sort": 10,
        "actionCode": "ADD"
      }
    ]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 顶部按钮列表 |
| `data.list[].buttonCode` | 是 | 按钮编码 |
| `data.list[].buttonName` | 是 | 按钮名称 |
| `data.list[].sort` | 是 | 排序值 |
| `data.list[].actionCode` | 是 | 动作编码 |

## 规则说明

- 入参使用 `ListCommonQueryDTO`
- 响应使用 `ResultVO<ListTopButtonVO>`
- 结果仅包含顶部按钮元数据
