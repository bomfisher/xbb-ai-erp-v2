# 客户列表查询

## 文档信息
- 领域：`customer-list`
- 控制器：`CustomerAdminController#list`
- 请求方式：`POST /erp/v1/customer/list`
- 聚合文档引用：`docs/kn/customer-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "pageNum": 1,
  "pageSize": 20,
  "keyword": "杭州",
  "conditions": [
    {
      "attr": "customerName",
      "fieldType": "TEXT",
      "symbol": "CONTAINS",
      "value": ["客户"]
    },
    {
      "attr": "bizStatus",
      "fieldType": "ENUM",
      "symbol": "IN",
      "value": ["1", "0"]
    }
  ]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `pageNum` | 是 | 页码，从 1 开始 |
| `pageSize` | 是 | 每页条数 |
| `keyword` | 否 | 顶部关键字搜索，当前匹配客户编码与客户名称 |
| `conditions` | 否 | 高级动态筛选条件列表 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "headList": null,
    "list": [
      {
        "id": 1,
        "customerCode": "CUST-001",
        "customerName": "杭州客户",
        "customerCategory": "A",
        "ownerSalesId": "emp-001",
        "defaultContactName": "张三",
        "defaultContactMobile": "13800000000",
        "defaultAddressSummary": "浙江杭州西湖区文三路 1 号",
        "defaultInvoiceTitle": "杭州客户有限公司",
        "bizStatus": "1"
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
| `data.list` | 是 | 客户列表数据 |
| `data.pageHelper` | 是 | 分页信息 |

## 规则说明

- 客户列表接口已切换为 `keyword + conditions` 新协议，不再接收旧的平铺筛选字段
- `keyword` 与 `conditions` 同时存在时，按 `keyword AND conditions...` 组合查询
- 动态筛选字段会先在服务层按白名单校验，再映射为安全列名进入 SQL
