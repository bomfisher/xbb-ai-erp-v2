# 保存全局自动审核配置

## 文档信息

- 领域：`system`
- 控制器：`BusinessConfigAdminController#saveGlobal`
- 请求方式：`POST /erp/v1/system/businessConfig/saveGlobal`
- 聚合文档引用：`docs/kn/system-config-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "autoApprovalBusinessCodes": ["PURCHASE_ORDER", "SALES_ORDER", "RECEIPT"]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户编码。 |
| `userId` | 是 | 当前操作人。 |
| `autoApprovalBusinessCodes` | 否 | 自动审核的业务单据编码数组；空数组表示全部必须人工审核。 |

## 响应示例

```json
{
  "success": true,
  "data": {
    "categoryCode": "GLOBAL",
    "categoryName": "全局配置",
    "documents": []
  }
}
```

## 规则说明

- 仅支持全局配置目录返回的十四种单据编码，未知编码返回业务错误。
- 勾选的单据保存为 `AUTO`，未勾选的单据保存为 `REQUIRED`。
- 保存过程在同一事务中完成，并按单据精确失效配置缓存。
