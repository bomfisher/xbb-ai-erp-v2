# 供应商删除

## 文档信息
- 领域：`supplier-delete`
- 控制器：`SupplierAdminController#delete`
- 请求方式：`POST /erp/v1/supplier/delete`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "idList": [1, 2]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `idList` | 是 | 待删除供应商 ID 列表 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 固定为 `null` |

## 规则说明

- 当前删除调用批量删除链路
- 控制器返回类型为 `ResultVO<Void>`，成功时业务数据体为空
