# org-org 接口文档

## 文档说明
- 模块：`xbb-erp-module-org`
- 领域：`org-org`
- 当前文档维护 12 个接口，扩展接口见 `docs/api/org-org-02.md`
- 统一返回：`ResultVO.success(...)`
- 列表、树查询、权限详情等接口按当前代码返回真实结构，基础动作类接口返回 `BaseVO`

---

## 接口名称

组织员工列表查询

## 请求方式

`POST /erp/v1/org/employee/list`

## 请求示例

```json
{
  "corpid": "corp-001",
  "pageNum": 1,
  "pageSize": 20,
  "keyword": "张三",
  "departmentId": 10,
  "employmentStatus": "ACTIVE",
  "userStatus": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `pageNum` | 否 | 页码 |
| `pageSize` | 否 | 每页条数 |
| `keyword` | 否 | 员工 ID、员工编码、员工姓名、邮箱关键字 |
| `departmentId` | 否 | 部门 ID；用于组织架构页左树选中后筛选右侧员工 |
| `employmentStatus` | 否 | 任职状态 |
| `userStatus` | 否 | 员工状态 |

### 参数补充说明

| 字段 | 可选值 | 说明 |
| --- | --- | --- |
| `employmentStatus` | `ACTIVE` | 在职 |
| `employmentStatus` | `RESIGNED` | 离职 |
| `userStatus` | `1` | 启用 |
| `userStatus` | `0` | 停用 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "id": "EMP-1001",
        "userCode": "E001",
        "userName": "张三",
        "email": "zhangsan@example.com",
        "jobNo": "JOB-001",
        "mainDepartmentId": 10,
        "mainDepartmentName": "总部",
        "employmentStatus": "ACTIVE",
        "userStatus": 1,
        "roleIdList": [101, 102],
        "roleNameList": ["超级管理员", "采购员"]
      }
    ],
    "pageHelper": {
      "pageNum": 1,
      "pageCount": 1
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 员工列表 |
| `data.list[].id` | 是 | 员工 `userId` |
| `data.list[].userCode` | 否 | 员工编码 |
| `data.list[].userName` | 否 | 员工姓名 |
| `data.list[].email` | 否 | 邮箱 |
| `data.list[].jobNo` | 否 | 工号 |
| `data.list[].mainDepartmentId` | 否 | 主部门 ID |
| `data.list[].mainDepartmentName` | 否 | 主部门名称 |
| `data.list[].employmentStatus` | 否 | 任职状态 |
| `data.list[].userStatus` | 否 | 员工状态 |
| `data.list[].roleIdList` | 否 | 员工已绑定角色 ID 列表 |
| `data.list[].roleNameList` | 否 | 员工已绑定角色中文名列表 |
| `data.pageHelper.pageNum` | 是 | 当前页码 |
| `data.pageHelper.pageCount` | 是 | 总页数 |

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 对外员工标识统一返回 `userId`。
- 当前 controller 入参为 `EmployeeListDTO`。

---

## 接口名称

组织员工保存

## 请求方式

`POST /erp/v1/org/employee/save`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": "EMP-1001",
  "userId": "EMP-1001",
  "accountId": 2001,
  "userCode": "E001",
  "userName": "张三",
  "email": "zhangsan@example.com",
  "jobNo": "JOB-001",
  "mainDepartmentId": 10,
  "userStatus": 1,
  "employmentStatus": "ACTIVE",
  "entryTime": 1753891200000,
  "resignedTime": null,
  "remark": "备注",
  "departmentIdList": [10, 11],
  "roleIdList": [101, 102]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 否 | 员工 `userId`；为空时表示新建 |
| `userId` | 否 | 员工 `userId`；更新时通常与 `id` 一致 |
| `accountId` | 否 | 账号 ID |
| `userCode` | 否 | 员工编码 |
| `userName` | 否 | 员工姓名 |
| `email` | 否 | 邮箱 |
| `jobNo` | 否 | 工号 |
| `mainDepartmentId` | 是 | 主部门 ID |
| `userStatus` | 否 | 员工状态 |
| `employmentStatus` | 否 | 任职状态 |
| `entryTime` | 否 | 入职时间戳 |
| `resignedTime` | 否 | 离职时间戳 |
| `remark` | 否 | 备注 |
| `departmentIdList` | 否 | 部门 ID 列表 |
| `roleIdList` | 否 | 角色 ID 列表 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "employeeId": "EMP-1001",
    "departmentIdList": [10, 11],
    "roleIdList": [101, 102]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.employeeId` | 是 | 保存后的员工 `userId` |
| `data.departmentIdList` | 是 | 最终部门 ID 列表 |
| `data.roleIdList` | 是 | 最终角色 ID 列表 |

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 对外员工交互标识统一使用 `userId`。
- 当前 controller 入参为 `EmployeeSaveDTO`。
- 角色编辑可继续复用本接口，只更新 `roleIdList` 并保留员工原有基础信息与部门关系。
- 若本次新增分配的角色中包含停用角色，返回 `角色已停用，不能分配`。
- 员工编辑自己时，若尝试移除自己最后一个 `roleType=SYSTEM` 角色，返回 `不能删除自己的超级管理员角色`。
- 若某员工被移除后公司将不存在任何 `roleType=SYSTEM` 角色员工，返回 `一个公司至少需要保留一个超级管理员`。

---

## 接口名称

组织员工详情查询

## 请求方式

`POST /erp/v1/org/employee/detail`

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
  "data": {
    "mainData": {
      "id": "EMP-1001",
      "userCode": "E001",
      "userName": "张三",
      "email": "zhangsan@example.com",
      "jobNo": "JOB-001",
      "mainDepartmentId": 10,
      "mainDepartmentName": "总部",
      "employmentStatus": "ACTIVE",
      "userStatus": 1,
      "roleIdList": [101, 102],
      "roleNameList": ["超级管理员", "采购员"]
    },
    "departmentIdList": [10, 11],
    "roleIdList": [101, 102],
    "roleList": [
      {
        "id": 101,
        "roleName": "超级管理员",
        "roleType": "SYSTEM",
        "roleStatus": 1
      },
      {
        "id": 102,
        "roleName": "采购员",
        "roleType": "CUSTOM",
        "roleStatus": 1
      }
    ]
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.mainData` | 是 | 员工主数据，与员工列表单项结构一致 |
| `data.departmentIdList` | 是 | 员工当前全部部门 ID 列表 |
| `data.roleIdList` | 是 | 员工当前角色 ID 列表 |
| `data.roleList` | 是 | 员工当前角色明细列表 |
| `data.roleList[].id` | 是 | 角色 ID |
| `data.roleList[].roleName` | 否 | 角色名称 |
| `data.roleList[].roleType` | 否 | 角色类型 |
| `data.roleList[].roleStatus` | 否 | 角色状态 |

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前 controller 入参为 `EmployeeIdDTO`。
- 详情接口用于组织架构页员工角色编辑回填。

---

## 接口名称

组织员工转离职

## 请求方式

`POST /erp/v1/org/employee/resign`

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

组织员工删除

## 请求方式

`POST /erp/v1/org/employee/delete`

## 请求示例

```json
{
  "corpid": "corp-001",
  "idList": ["EMP-1001", "EMP-1002"]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `idList` | 是 | 员工 `userId` 列表 |

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
- 当前 controller 入参为 `EmployeeBatchDTO`。

---

## 接口名称

组织部门树查询

## 请求方式

`POST /erp/v1/org/department/tree`

## 请求示例

```json
{
  "corpid": "corp-001",
  "departmentStatus": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `departmentStatus` | 否 | 部门状态 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "treeList": [
      {
        "id": 10,
        "departmentCode": "HQ",
        "departmentName": "总部",
        "parentId": 0,
        "departmentLevel": 1,
        "departmentStatus": 1,
        "children": [
          {
            "id": 11,
            "departmentCode": "RD",
            "departmentName": "研发部",
            "parentId": 10,
            "departmentLevel": 2,
            "departmentStatus": 1,
            "children": []
          }
        ]
      }
    ],
    "flatList": [
      {
        "id": 10,
        "departmentCode": "HQ",
        "departmentName": "总部",
        "parentId": 0,
        "departmentLevel": 1,
        "departmentStatus": 1,
        "children": []
      },
      {
        "id": 11,
        "departmentCode": "RD",
        "departmentName": "研发部",
        "parentId": 10,
        "departmentLevel": 2,
        "departmentStatus": 1,
        "children": []
      }
    ]
  }
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 当前 controller 入参为 `DepartmentTreeDTO`。

---

## 接口名称

组织部门保存

## 请求方式

`POST /erp/v1/org/department/save`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 10,
  "departmentCode": "RD",
  "departmentName": "研发部",
  "parentId": 1,
  "enableStatus": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 否 | 部门 ID；为空时表示新建 |
| `departmentCode` | 否 | 部门编码 |
| `departmentName` | 否 | 部门名称 |
| `parentId` | 否 | 父部门 ID |
| `enableStatus` | 否 | 当前 DTO 字段名 |

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
- 当前 controller 入参为 `DepartmentSaveDTO`。

---

## 接口名称

组织部门调整

## 请求方式

`POST /erp/v1/org/department/move`

## 请求示例

```json
{
  "corpid": "corp-001",
  "id": 10,
  "targetParentId": 2
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 当前部门 ID |
| `targetParentId` | 是 | 目标父部门 ID |

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
- 当前 controller 入参为 `DepartmentMoveDTO`。

---

## 接口名称

组织角色列表查询

## 请求方式

`POST /erp/v1/org/role/list`

## 请求示例

```json
{
  "corpid": "corp-001",
  "pageNum": 1,
  "pageSize": 20,
  "keyword": "管理员",
  "roleStatus": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `pageNum` | 否 | 页码 |
| `pageSize` | 否 | 每页条数 |
| `keyword` | 否 | 角色名称关键字 |
| `roleStatus` | 否 | 角色状态 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "id": 9,
        "roleName": "管理员",
        "roleType": "SYSTEM",
        "roleStatus": 1
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
- 角色权限页左侧角色列表支持按状态筛选，并可直接执行启用、停用维护。

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
- 当前 controller 入参为 `IdBaseDTO`。

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
- 当前 controller 入参为 `IdBaseDTO`。

---

## 接口名称

组织权限列表查询

## 请求方式

`POST /erp/v1/org/permission/list`

## 请求示例

```json
{
  "keyword": "员工",
  "permissionType": "MENU",
  "permissionStatus": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 否 | 当前 DTO 仍带该字段，但权限表查询不按 `corpid` 过滤 |
| `keyword` | 否 | 权限编码或名称关键字 |
| `permissionType` | 否 | 权限类型 |
| `permissionStatus` | 否 | 权限状态 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "list": [
      {
        "id": 201,
        "permissionCode": "EMP_VIEW",
        "permissionName": "员工查看",
        "permissionType": "MENU",
        "menuAlias": "employee:view",
        "actionCode": "view",
        "dataScopeFlag": 1,
        "permissionStatus": 1
      }
    ]
  }
}
```

## 规则说明

- 统一走 `ResultVO.success(...)`。
- 权限来源于公共表 `sys_permission`，当前查询不按企业维度过滤。
- 当前 controller 入参为 `PermissionListDTO`。
