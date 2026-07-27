# 客户模块接口

## `/erp/v1/customer/list`

- 请求方式：`POST`
- 入参：`CustomerListDTO`
- 用途：返回客户列表数据与分页信息；列表表头已抽离到公共接口 `/erp/v1/common/list/header`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "pageNum": 1,
  "pageSize": 20,
  "keyword": "杭州", // 关键字，同时匹配客户编码、客户名称
  "customerCode": "CUST-001", // 客户编码，模糊筛选
  "customerName": "杭州", // 客户名称，模糊筛选
  "customerCategory": "RETAIL", // 客户分类
  "regionCode": "330100", // 区域编码
  "ownerSalesId": "sales-001", // 归属销售员工Id
  "bizStatus": "ENABLED", // ENABLED: 启用, DISABLED: 停用
  "refStatus": "0" // 0: 未引用, 1: 已引用
}
```

### 响应示例

```json
{
  "headList": null,
  "list": [
    {
      "id": 1,
      "customerCode": "CUST-001", // 客户编码
      "customerName": "杭州客户", // 客户名称
      "defaultContactName": "张三", // 默认联系人姓名
      "defaultContactMobile": "13800000000", // 默认联系人手机号
      "bizStatus": "ENABLED" // ENABLED: 启用, DISABLED: 停用
    }
  ],
  "pageHelper": {
    "page": 1,
    "count": 1, // 总页数
    "hasLeft": false,
    "hasRight": false
  }
}
```

## `/erp/v1/customer/delete`

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 删除范围：客户主档、联系人、地址、银行账户、开票信息
- 删除限制：
  - 客户不存在时直接报错：`客户不存在`
  - `refStatus = "1"` 视为已引用，直接报错：`客户已被引用，不能删除`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "ids": [1, 2]
}
```

### 响应

```json
null
```
