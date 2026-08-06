# 公共列表行内动作

## 文档信息
- 领域：`common-row-action`
- 控制器：`ListCommonController#rowAction`
- 请求方式：`POST /erp/v1/common/list/rowAction`
- 聚合文档引用：`docs/kn/common-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "businessCode": "CUSTOMER"
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
        "actionCode": "EDIT",
        "actionName": "编辑",
        "sort": 10,
        "showMode": "PRIMARY",
        "confirmType": "NONE"
      }
    ]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 行内动作列表 |
| `data.list[].actionCode` | 是 | 动作编码 |
| `data.list[].actionName` | 是 | 动作名称 |
| `data.list[].sort` | 是 | 排序值 |
| `data.list[].showMode` | 是 | 展示模式 |
| `data.list[].confirmType` | 是 | 二次确认类型 |

## 规则说明

- 入参使用 `ListCommonQueryDTO`
- 出参使用 `ResultVO<ListRowActionVO>`
- 结果仅包含行内动作元数据
- `businessCode` 当前已覆盖 `CUSTOMER`、`SUPPLIER` 等已注册业务编码；若业务未注册会抛出 `BizException`
- 客户列表当前仅落地 `EDIT` 行动作，不提前暴露其他动作
