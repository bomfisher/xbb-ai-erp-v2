# customer-m.md

## 列表

### `/erp/v1/customer/list`

- 请求方式：`POST`
- 入参：`CustomerListDTO`
- 用途：返回客户列表数据与分页信息

#### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "pageNum": 1,
  "pageSize": 20
}
```

#### 响应示例

```json
{
  "list": [
    {
      "id": 1,
      "customerCode": "CUST-001",
      "customerName": "杭州客户"
    }
  ],
  "pageHelper": {
    "page": 1,
    "count": 1,
    "hasLeft": false,
    "hasRight": false
  }
}
```

#### 参数说明

- `corpid`：企业 ID
- `userId`：员工 ID
- `pageNum`：页码
- `pageSize`：分页大小

#### 规则说明

- 仅在主文档维护完整请求与响应正文
- 同类接口的横向归类交由次文档维护
