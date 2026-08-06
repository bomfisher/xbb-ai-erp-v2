# org-org 接口文档（二）

## 文档说明
- 模块：`xbb-erp-module-org`
- 领域：`org-org`
- 当前文档维护扩展的 12 个接口
- 统一返回：`ResultVO.success(...)`
- 查询类接口按当前代码返回真实结构，基础动作类接口返回 `BaseVO`

---

## 接口名称

组织员工启用

## 请求方式

`POST /erp/v1/org/employee/enable`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": "EMP-1001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 员工 `userId` |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织员工停用

## 请求方式

`POST /erp/v1/org/employee/disable`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": "EMP-1001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 员工 `userId` |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

离职员工列表查询

## 请求方式

`POST /erp/v1/org/resigned-employee/list`

## 请求示例

```json
{
  "corpid": "corp-001",
  "pageNum": 1,
  "pageSize": 20,
  "keyword": "张三"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `pageNum` | 否 | 页码 |
| `pageSize` | 否 | 每页条数 |
| `keyword` | 否 | 关键字 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "id": "EMP-2001",
        "userCode": "E002",
        "userName": "李四",
        "email": "lisi@example.com",
        "jobNo": "JOB-002",
        "mainDepartmentId": 12,
        "mainDepartmentName": "行政部",
        "employmentStatus": "RESIGNED",
        "userStatus": 0
      }
    ],
    "pageHelper": {
      "pageNum": 1,
      "pageCount": 1
    }
  }
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 服务层固定按 `RESIGNED` 查询。

---

## 接口名称

离职员工详情查询

## 请求方式

`POST /erp/v1/org/resigned-employee/detail`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": "EMP-2001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 员工 `userId` |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织部门详情查询

## 请求方式

`POST /erp/v1/org/department/detail`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 10
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 部门 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织部门启用

## 请求方式

`POST /erp/v1/org/department/enable`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 10
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 部门 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织部门停用

## 请求方式

`POST /erp/v1/org/department/disable`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 10
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 部门 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当部门下存在在职主部门员工时禁止停用。

---

## 接口名称

组织角色详情查询

## 请求方式

`POST /erp/v1/org/role/detail`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 9
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 角色 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织角色启用

## 请求方式

`POST /erp/v1/org/role/enable`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 9
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 角色 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织角色停用

## 请求方式

`POST /erp/v1/org/role/disable`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 9
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 角色 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。

---

## 接口名称

组织角色权限详情查询

## 请求方式

`POST /erp/v1/org/role/permissionDetail`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 9
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 角色 ID |

### 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "roleId": 9,
    "roleName": "管理员",
    "permissionList": [
      {
        "id": 201,
        "parentId": 0,
        "permissionCode": "CUSTOMER",
        "permissionName": "客户",
        "permissionType": "MENU",
        "menuAlias": "customer",
        "actionCode": null,
        "dataScopeFlag": 1,
        "permissionStatus": 1,
        "selected": 1,
        "dataScopeType": "ALL",
        "children": [
          {
            "id": 202,
            "parentId": 201,
            "permissionCode": "CUSTOMER_CREATE",
            "permissionName": "新建",
            "permissionType": "ACTION",
            "menuAlias": null,
            "actionCode": "create",
            "dataScopeFlag": 0,
            "permissionStatus": 1,
            "selected": 1,
            "dataScopeType": null,
            "children": []
          }
        ]
      }
    ]
  }
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- `permissionList` 返回树形菜单节点，父节点为菜单权限，子节点为操作权限。
- 仅 `permissionType=MENU` 且 `dataScopeFlag=1` 的节点返回可编辑的数据权限。
- `dataScopeType` 使用字符串枚举值：`SELF`、`DEPT`、`DEPT_AND_CHILD`、`ALL`。

---

## 接口名称

组织角色权限保存

## 请求方式

`POST /erp/v1/org/role/savePermission`

## 请求示例

```json
{
  "corpid": "corp-001",
  "roleId": 9,
  "permissionList": [
    {
      "menuPermissionId": 201,
      "selected": 1,
      "dataScopeType": "ALL",
      "actionPermissionIdList": [202]
    }
  ]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `roleId` | 是 | 角色 ID |
| `permissionList` | 否 | 菜单权限保存列表 |

### `permissionList` 子项说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `menuPermissionId` | 是 | 菜单权限 ID |
| `selected` | 是 | 是否授权菜单，`1` 是、`0` 否 |
| `dataScopeType` | 否 | 菜单级数据权限范围 |
| `actionPermissionIdList` | 否 | 当前菜单下已选中的操作权限 ID 列表 |

### `dataScopeType` 可选值

| 可选值 | 说明 |
| --- | --- |
| `SELF` | 本人 |
| `DEPT` | 本部门 |
| `DEPT_AND_CHILD` | 本部门及子部门 |
| `ALL` | 全部 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "roleId": 9,
    "permissionList": [
      {
        "menuPermissionId": 201,
        "selected": 1,
        "dataScopeType": "ALL",
        "actionPermissionIdList": [202]
      }
    ]
  }
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 保存时按菜单节点集合覆盖写入角色权限关系与菜单级数据权限。
- 未授权菜单不允许携带 `dataScopeType`。
- 不支持数据权限的菜单不允许保存 `dataScopeType`。

---

## 接口名称

组织权限详情查询

## 请求方式

`POST /erp/v1/org/permission/detail`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 201
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 权限 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {}
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前服务实现返回 `BaseVO`。
