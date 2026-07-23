# 客户模块接口

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
