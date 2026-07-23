# 菜单模块接口

## `/erp/v1/menu/list`

- 请求方式：`POST`
- 入参：`MenuDTO`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "surface": "PC_ADMIN" //PC_ADMIN web后台菜单 MOBILE 移动端菜单
}
```

### 响应

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "data": {
    "list": [{
      "menuCode": "PURCHASE_CENTER",
      "menuName": "采购业务",
      "children": [{
        "menuCode": "PURCHASE_REQUISITION",
        "menuName": "采购申请",
        "routePath": "/purchase/requisitions",//菜单路由
        "componentPath": "purchase/requisition/create"//进入菜单默认打开 create:创建页; query: 查询页; list: 列表页; detail: 详情页
      }]
    }]
  }
}
```
