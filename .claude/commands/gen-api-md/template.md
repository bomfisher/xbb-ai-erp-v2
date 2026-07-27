# API 接口文档模板

> 本模板仅用于 `docs/api` 下的 API 接口文档，不用于主文档、次文档或业务知识文档。

## 接口名称

菜单列表查询

## 请求方式

`POST /erp/v1/menu/list`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "surface": "PC_ADMIN"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `surface` | 是 | 菜单端类型，支持不同端侧菜单查询 |

### 参数补充说明

| 字段 | 可选值 | 说明 |
| --- | --- | --- |
| `surface` | `PC_ADMIN` | Web 后台菜单 |
| `surface` | `MOBILE` | 移动端菜单 |

## 响应示例

```json
{
  "data": {
    "list": [
      {
        "menuCode": "PURCHASE_CENTER",
        "menuName": "采购业务",
        "routePath": null,
        "componentPath": null,
        "children": [
          {
            "menuCode": "PURCHASE_REQUISITION",
            "menuName": "采购申请",
            "routePath": "/purchase/requisitions",
            "componentPath": "purchase/requisition/create",
            "children": []
          }
        ]
      }
    ]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 响应数据主体 |
| `data.list` | 是 | 菜单树列表 |
| `data.list[].menuCode` | 是 | 菜单编码 |
| `data.list[].menuName` | 是 | 菜单名称 |
| `data.list[].routePath` | 否 | 前端路由路径；目录节点可为空 |
| `data.list[].componentPath` | 否 | 前端组件路径；目录节点可为空 |
| `data.list[].children` | 是 | 子菜单列表，无子节点时返回空数组 |

### 响应字段补充说明

| 字段 | 说明 |
| --- | --- |
| `data.list` | 顶层节点与子节点统一为树形结构，可递归解析 |
| `routePath` | 通常只有实际可访问菜单节点才会有值 |
| `componentPath` | 用于前端加载对应页面组件 |
| `children` | 即使没有子节点，也建议保持数组结构返回 |

## 规则说明

- 只返回 `enableStatus = 1` 的菜单。
- 只返回与 `surface` 参数匹配的菜单。
- 如果父节点下不存在任何带 `routePath` 的有效子节点，则该父节点不返回。
- 如果某个带 `routePath` 的菜单节点缺少完整父级链路，则该节点不返回。
- 返回结果为菜单树结构。
- 节点仅保留以下字段：`menuCode`、`menuName`、`routePath`、`componentPath`、`children`。

---

# API 接口文档编写示例

> 以下模板可直接复制，用于后续任意接口文档编写。

## 接口名称

菜单列表查询（示例）

## 请求方式

`POST /erp/v1/example/demo`

## 请求示例

```json
{
  "示例字段1": "示例值",
  "示例字段2": "示例值"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `字段名` | 是/否 | 字段含义 |
| `字段名` | 是/否 | 字段含义 |

### 参数补充说明（可选）

> 当字段存在枚举值、业务状态、类型映射、复杂说明时，可补充下表。

| 字段 | 可选值 | 说明 |
| --- | --- | --- |
| `字段名` | `值1` | 对应说明 |
| `字段名` | `值2` | 对应说明 |

## 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `code` | 是 | 响应状态码 |
| `message` | 是 | 响应说明 |
| `data` | 是/否 | 响应数据主体 |
| `data.xxx` | 是/否 | 具体业务字段说明 |

### 响应字段补充说明（可选）

| 字段 | 说明 |
| --- | --- |
| `字段名` | 对复杂结构、枚举值、嵌套列表等进行补充说明 |

## 规则说明

- 根据接口实际业务补充返回、校验、过滤、排序、权限等规则。
- 如有特殊逻辑，按规则逐条列出。

