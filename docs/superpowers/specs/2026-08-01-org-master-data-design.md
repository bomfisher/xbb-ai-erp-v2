# 组织主数据层设计文档

## 1. 文档目标

本文档定义 `xbb-erp-module-org` 第一阶段的组织主数据层设计，覆盖员工、离职员工、部门、角色、权限字典读取、角色授权、菜单级数据权限等能力，作为后续实现与评审依据。

本文档目标是先把组织权限相关的主数据事实、管理端接口与核心关系设计稳定下来，为下一阶段的权限装载、缓存与 `facade` 输出提供基础。

## 2. 本期范围

### 2.1 本期纳入范围

本期纳入以下能力：

- 员工管理
- 离职员工独立列表
- 部门管理
- 角色管理
- 平台统一权限字典只读查询
- 角色权限授权
- 角色菜单级数据权限配置
- 员工与部门关系维护
- 员工与角色关系维护

### 2.2 本期不纳入范围

本期不包含以下能力：

- 账号管理
- 登录认证协议接入
- 权限快照装载
- 跨模块 `facade` 输出
- 字段级权限
- 审批组织规则
- 菜单主数据维护

## 3. 模块定位与边界

### 3.1 模块定位

本期能力统一落位于 `xbb-erp-module-org`。

模块职责聚焦在组织主数据与授权事实维护，不负责认证本身，也不负责菜单主数据维护。

### 3.2 模块职责

本模块负责：

- 维护员工、部门、角色主数据
- 维护员工与部门、员工与角色的关系事实
- 读取平台统一权限字典
- 维护角色与权限点授权关系
- 维护角色在菜单权限维度的数据权限配置

### 3.3 模块不负责

本模块不负责：

- 登录账号体系
- Token、会话、认证链路
- 菜单主数据本体维护
- 其他业务模块的查询注入与权限判定执行

## 4. 核心设计口径

### 4.1 权限字典口径

权限字典采用平台统一主数据模型：

- `sys_permission` 为平台统一权限字典
- 租户不维护自己的权限点主数据
- 租户内角色通过关系表引用公共权限点

该设计保证权限编码、菜单映射、动作映射口径统一，便于后续跨模块校验与权限装载。

### 4.2 组织归属口径

员工与部门关系采用：

- `1` 个主部门
- `N` 个兼职部门

其中：

- 员工主表保存 `main_department_id`
- 关系表保存主部门与兼职部门事实
- 主表与关系表中的主部门记录必须保持一致

### 4.3 角色挂载口径

员工保存时直接维护角色挂载：

- 一个员工可挂多个角色
- 员工主档保存时同步覆盖员工角色关系
- 停用角色不允许继续分配给员工

### 4.4 数据权限口径

角色菜单级数据权限范围固定为四档：

- `SELF`
- `DEPT`
- `DEPT_AND_CHILD`
- `ALL`

数据权限按 `角色 + 菜单权限` 维度配置，不直接对员工配置。

### 4.5 部门删除口径

部门不做物理删除，仅允许停用。

关键约束：

- 存在在职员工以该部门作为主部门时禁止停用
- 禁止把部门挂到自己或自己的下级节点之下
- 存在启用中的下级部门时父部门不得直接停用

## 5. 模块结构设计

建议沿用项目现有 DDD 风格，在 `xbb-erp-module-org` 中采用如下结构：

```text
xbb-erp-module-org
└── src/main/java/xbb/ai/erp/module/org
    ├── admin
    │   ├── dto
    │   ├── vo
    │   ├── DepartmentAdminController.java
    │   ├── EmployeeAdminController.java
    │   ├── PermissionAdminController.java
    │   ├── ResignedEmployeeAdminController.java
    │   └── RoleAdminController.java
    ├── application
    │   ├── assembler
    │   ├── pojo
    │   └── service
    ├── domain
    │   ├── enums
    │   ├── model
    │   └── repository
    └── infrastructure
        └── persistence
            ├── convertor
            ├── mapper
            ├── po
            └── repository
```

分层职责如下：

- `admin`：接收请求、参数绑定、返回 `ResultVO.success()`
- `application`：用例编排、事务边界、批量聚合查询
- `domain`：组织主数据与授权规则语义
- `infrastructure`：PO、Mapper、仓储实现、复杂查询落地

## 6. 核心对象设计

### 6.1 Employee

职责：

- 承接租户内员工主体
- 维护姓名、工号、手机号、邮箱、主部门、任职状态、启停状态
- 维护员工与部门、角色的关系事实基础

关键规则：

- 一个员工必须且仅有一个主部门
- 员工可以有多个兼职部门
- 员工可以挂多个角色
- 离职员工不物理删除，使用状态承接

### 6.2 Department

职责：

- 承接租户组织树
- 维护父子关系、祖先路径、层级、负责人、排序、启停状态
- 为后续部门筛选和数据权限提供组织树基础

关键规则：

- 采用 `parent_id + ancestor_path + department_level` 模型
- 部门迁移时必须刷新当前节点及全部下级节点路径信息
- 部门停用不等于删除

### 6.3 Role

职责：

- 承接租户内角色主体
- 作为权限授权的唯一正式载体

关键规则：

- 角色本身不保存权限字符串
- 角色可授权多个权限点
- 停用角色不再允许挂载给员工

### 6.4 Permission

职责：

- 承接平台统一权限字典
- 维护菜单权限和操作权限定义
- 标记菜单权限是否支持数据权限配置

关键规则：

- `permission_type` 区分 `MENU`、`ACTION`
- 菜单权限通过 `menu_alias` 对应菜单
- 操作权限通过 `action_code` 标记动作能力
- 仅 `MENU` 且 `data_scope_flag=1` 时允许配置数据权限

### 6.5 EmployeeDepartmentRelation

职责：

- 承接员工与部门多对多关系
- 区分主部门与兼职部门

关键规则：

- 一个员工只能存在一条主部门关系
- 主部门关系必须与员工主表 `main_department_id` 一致

### 6.6 EmployeeRoleRelation

职责：

- 承接员工与角色多对多关系
- 为角色授权结果落到员工提供事实基础

### 6.7 RolePermissionRelation

职责：

- 承接角色到权限点的正式授权关系
- 同时覆盖菜单权限与操作权限

### 6.8 RolePermissionDataScope

职责：

- 承接角色在某菜单权限上的数据权限配置

关键规则：

- 数据权限只允许 `SELF`、`DEPT`、`DEPT_AND_CHILD`、`ALL`
- 同一 `role_id + permission_id` 只允许一条有效配置
- 未授权菜单权限不得单独保存数据权限

## 7. 表设计建议

### 7.1 sys_user

建议字段：

- `id`
- `corpid`
- `user_code`
- `user_name`
- `mobile`
- `email`
- `main_department_id`
- `employment_status`
- `enable_status`
- `entry_time`
- `resigned_time`
- `remark`
- 审计字段

说明：

- `employment_status`、`enable_status` 使用 `Integer`
- 本期不包含账号字段

建议索引：

- `uk_corpid_user_code`
- `idx_corpid_main_department_id`
- `idx_corpid_employment_status`

### 7.2 sys_department

建议字段：

- `id`
- `corpid`
- `department_name`
- `department_code`
- `parent_id`
- `ancestor_path`
- `department_level`
- `leader_user_id`
- `sort_num`
- `enable_status`
- `remark`
- 审计字段

建议索引：

- `uk_corpid_department_code`
- `idx_corpid_parent_id`
- `idx_corpid_ancestor_path`

### 7.3 sys_role

建议字段：

- `id`
- `corpid`
- `role_name`
- `role_code`
- `enable_status`
- `remark`
- 审计字段

建议索引：

- `uk_corpid_role_code`
- `idx_corpid_enable_status`

### 7.4 sys_permission

建议字段：

- `id`
- `permission_code`
- `permission_name`
- `permission_type`
- `menu_alias`
- `action_code`
- `data_scope_flag`
- `enable_status`
- `sort_num`
- `remark`
- 审计字段

说明：

- 为平台统一字典表，不带 `corpid`

建议索引：

- `uk_permission_code`
- `idx_menu_alias`
- `idx_permission_type`

### 7.5 sys_user_department_rel

建议字段：

- `id`
- `corpid`
- `user_id`
- `department_id`
- `is_main_department`
- 审计字段

建议索引：

- `uk_corpid_user_department`
- 唯一约束保证每个员工仅一条主部门关系

### 7.6 sys_user_role_rel

建议字段：

- `id`
- `corpid`
- `user_id`
- `role_id`
- 审计字段

建议索引：

- `uk_corpid_user_role`

### 7.7 sys_role_permission_rel

建议字段：

- `id`
- `corpid`
- `role_id`
- `permission_id`
- 审计字段

建议索引：

- `uk_corpid_role_permission`

### 7.8 sys_role_permission_data_scope

建议字段：

- `id`
- `corpid`
- `role_id`
- `permission_id`
- `data_scope_type`
- 审计字段

建议索引：

- `uk_corpid_role_permission_scope`

## 8. 枚举设计建议

建议在 `domain/enums` 中定义：

- `EmploymentStatusEnum`
- `EnableStatusEnum`
- `PermissionTypeEnum`
- `DataScopeTypeEnum`
- `DepartmentRelationTypeEnum` 或以 `is_main_department` 整型表达主兼职关系

枚举命名统一以 `Enum` 结尾。

## 9. 管理端接口设计

### 9.1 员工管理

建议接口：

- `POST /erp/v1/org/employee/list`
- `POST /erp/v1/org/employee/detail`
- `POST /erp/v1/org/employee/save`
- `POST /erp/v1/org/employee/enable`
- `POST /erp/v1/org/employee/disable`
- `POST /erp/v1/org/employee/resign`

职责说明：

- 员工保存时同步维护部门关系与角色关系
- 在职员工列表默认只返回在职人员

### 9.2 离职员工

建议接口：

- `POST /erp/v1/org/resigned-employee/list`
- `POST /erp/v1/org/resigned-employee/detail`

职责说明：

- 离职员工与在职员工列表拆开
- 保留历史部门与角色关系用于展示

### 9.3 部门管理

建议接口：

- `POST /erp/v1/org/department/tree`
- `POST /erp/v1/org/department/detail`
- `POST /erp/v1/org/department/save`
- `POST /erp/v1/org/department/enable`
- `POST /erp/v1/org/department/disable`
- `POST /erp/v1/org/department/move`

### 9.4 角色管理

建议接口：

- `POST /erp/v1/org/role/list`
- `POST /erp/v1/org/role/detail`
- `POST /erp/v1/org/role/save`
- `POST /erp/v1/org/role/enable`
- `POST /erp/v1/org/role/disable`
- `POST /erp/v1/org/role/permissionDetail`
- `POST /erp/v1/org/role/savePermission`

### 9.5 权限字典

建议接口：

- `POST /erp/v1/org/permission/list`
- `POST /erp/v1/org/permission/detail`

职责说明：

- 仅支持读取与筛选
- 不提供租户后台编辑能力

## 10. DTO 与 VO 设计建议

### 10.1 通用规则

- 非脚本接口入参 DTO 统一继承 `BaseDTO`
- 分页列表 DTO 继承项目现有分页基类
- 成功返回统一使用 `ResultVO.success()`
- 无返回主体使用 `BaseVO`

### 10.2 员工保存 DTO

建议字段：

- `id`
- `userName`
- `userCode`
- `mobile`
- `email`
- `mainDepartmentId`
- `partTimeDepartmentIds`
- `roleIds`
- `employmentStatus`
- `enableStatus`
- `entryTime`
- `remark`

### 10.3 员工列表 DTO

建议支持筛选：

- 关键词
- 主部门
- 任意归属部门
- 角色
- 启停状态
- 入职时间范围

### 10.4 离职员工列表 DTO

建议支持筛选：

- 关键词
- 离职前主部门
- 离职时间范围

### 10.5 部门保存 DTO

建议字段：

- `id`
- `departmentName`
- `departmentCode`
- `parentId`
- `leaderUserId`
- `sortNum`
- `enableStatus`
- `remark`

### 10.6 角色保存 DTO

建议字段：

- `id`
- `roleName`
- `roleCode`
- `enableStatus`
- `remark`

### 10.7 角色授权保存 DTO

建议字段：

- `roleId`
- `permissionIds`
- `menuDataScopes`

其中 `menuDataScopes` 每项建议含：

- `permissionId`
- `dataScopeType`

## 11. 关键业务流设计

### 11.1 员工保存

建议在一个事务内完成：

- 保存 `sys_user`
- 覆盖维护 `sys_user_department_rel`
- 覆盖维护 `sys_user_role_rel`
- 同步回写 `sys_user.main_department_id`

关键校验：

- 主部门不能为空
- 主部门必须包含在部门关系中
- 角色不得重复挂载
- 同一部门不得重复挂载
- 已停用角色不得分配

### 11.2 员工离职

建议处理：

- 更新 `employment_status=RESIGNED`
- 写入 `resigned_time`
- 保留角色与部门历史关系

离职后约束：

- 不继续配置角色和部门
- 不在员工主列表中展示
- 在离职员工独立列表中展示

### 11.3 部门保存与迁移

新增或编辑部门时维护：

- 基础信息
- 父子关系
- 层级信息
- 路径信息

部门迁移时：

1. 校验新父节点合法性
2. 计算新 `ancestor_path`
3. 计算新 `department_level`
4. 批量更新当前部门及全部下级节点

### 11.4 角色授权保存

建议在一个事务内完成：

- 覆盖保存 `sys_role_permission_rel`
- 覆盖保存 `sys_role_permission_data_scope`

关键校验：

- 权限点必须存在且启用
- 数据权限只能配置在已授权菜单权限上
- 非 `MENU` 权限不允许配置数据权限
- `data_scope_flag=0` 的菜单权限不允许配置数据权限

## 12. 查询设计

### 12.1 统一查询原则

遵循当前项目统一动态筛选口径：

- 固定条件走 `paramMap`
- 动态条件走 `conditions[]`
- Service 层维护白名单字段
- 禁止前端字段名直接进入 SQL

### 12.2 员工列表

建议支持：

- 员工编码
- 员工姓名
- 手机号
- 主部门
- 任意归属部门
- 角色
- 启停状态
- 入职时间范围

实现约束：

- 批量回查角色与部门信息
- 避免循环逐个查询数据库

### 12.3 离职员工列表

与主员工列表分开实现，固定条件为：

- `employment_status = RESIGNED`

同时支持：

- 离职时间范围
- 离职前主部门
- 角色摘要展示

### 12.4 角色详情

建议一次性返回：

- 角色基础信息
- 已授权权限列表
- 支持数据权限的菜单权限列表
- 当前数据权限配置

### 12.5 部门树查询

建议同时支持：

- 树形结果
- 平铺节点列表

以满足树展示、下拉选择、回填等场景。

## 13. 校验与异常处理

### 13.1 总体原则

- 所有主动业务错误统一抛 `BizException`
- Controller 不吞异常
- 统一异常层包装标准返回

### 13.2 典型业务校验

员工相关：

- 主部门不能为空
- 主部门必须有效
- 离职员工不允许继续配置角色与部门
- 停用员工规则按后续认证接入再扩展

部门相关：

- 禁止挂到自身下级
- 禁止停用仍被在职员工作为主部门的部门
- 禁止直接停用仍存在启用下级部门的节点

角色相关：

- 停用角色不允许分配给员工
- 角色编码在租户内唯一

授权相关：

- 未授权菜单不得保存数据权限
- 不支持数据权限的菜单不得保存数据权限
- 操作权限不得配置菜单级数据权限

## 14. 缓存预留点

本期不把缓存作为主实现重点，但需要为下一期预留缓存失效触发点。

建议预留以下触发场景：

- 员工角色变更
- 员工部门变更
- 角色权限变更
- 角色数据权限变更
- 部门树结构变更

## 15. 测试建议

### 15.1 单元测试重点

- 主部门唯一性与关系一致性
- 部门路径与层级计算
- 角色授权数据合法性校验
- 离职员工规则校验

### 15.2 集成测试重点

- 员工保存同步维护部门与角色关系
- 部门迁移后树结构正确
- 角色授权保存后权限关系与数据权限关系一致
- 在职员工与离职员工列表隔离正确

## 16. 实施结论

本期组织主数据层建议以 `xbb-erp-module-org` 为唯一模块落位，采用现有项目一致的 DDD 分层结构，先把员工、部门、角色、平台统一权限字典读取、角色授权与菜单级数据权限配置能力稳定下来。

本期关键结论如下：

1. 不做账号管理，先聚焦员工主体。
2. 员工采用 `1` 个主部门 + 多个兼职部门模型。
3. 员工保存时直接维护角色挂载。
4. 权限字典为平台统一主数据，租户角色引用公共权限。
5. 角色授权同时覆盖权限点配置与菜单级数据权限配置。
6. 数据权限范围固定为 `SELF`、`DEPT`、`DEPT_AND_CHILD`、`ALL`。
7. 部门不做物理删除，仅允许停用并受组织约束校验。
8. 离职员工通过独立列表承接，不与在职员工混查。

下一阶段再在此基础上补齐权限装载、缓存、`facade` 与跨模块接入。