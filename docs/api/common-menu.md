# 菜单模块接口

## `/erp/v1/menu/list`

- 请求方式：`POST`
- 入参：`MenuListDTO`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "surface": "PC_ADMIN"
}
```

### 参数说明

- `corpid`：企业 ID
- `userId`：员工 ID
- `surface`：菜单端类型，`PC_ADMIN` 表示 web 后台菜单，`MOBILE` 表示移动端菜单

### 响应

```json
{
  "data": {
    "list": [{
      "menuCode": "PURCHASE_CENTER",
      "menuName": "采购业务",
      "routePath": null,
      "componentPath": null,
      "children": [{
        "menuCode": "PURCHASE_REQUISITION",
        "menuName": "采购申请",
        "routePath": "/purchase/requisitions",
        "componentPath": "purchase/requisition/create",
        "children": []
      }]
    }]
  }
}
```

### 规则说明

- 只返回 `enableStatus = 1` 的菜单
- 只返回 `surface` 匹配的菜单
- 如果父节点没有任何带 `routePath` 的子节点，父节点丢弃
- 如果 `routePath` 节点没有完整父链路，丢弃
- 返回结果为菜单树，节点仅保留 `menuCode`、`menuName`、`routePath`、`componentPath`、`children`
