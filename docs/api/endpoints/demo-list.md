# DEMO 列表

- 方法：`POST`
- 路径：`/erp/v1/demo/list`
- 请求：`ListBaseDTO`，支持 `keyword`、白名单 `conditions` 和分页；不定义业务专属列表 DTO。
- 响应：`ResultVO<ListBaseVO<DemoListItemVO>>`。
- 说明：字段、筛选、顶部新增和行编辑动作由 `DEMO` 列表元数据 Provider 从字段设计统一派生。筛选项的 `fieldType` 返回源字段枚举值（例如 `COMB_MULTI` 为 `9`），用于前端装配控件；`filterFieldType` 返回查询协议（例如 `ENUM_MULTI`），用于 `conditions[].fieldType`。下拉类筛选返回 `itemList(value,text)`；员工和部门筛选的 `businessSelectConfig` 仅返回目标 `businessCode`（分别为 `ORG_MEMBER`、`ORG_DEPARTMENT`），端点和请求上下文由前端注册表维护。
- 列表响应由公共渲染器按 LIST 表头处理：下拉与下拉多选转换为 `itemList` 文案，`date`、`time` 的 13 位时间戳转换为可读字符串，成员与部门字段由组织模块批量查询展示名称。
- 当前页的 `userId`、`creatorId`、`modifyId` 汇总去重后仅执行一次 `ORG_MEMBER` 查询；`depId` 仅执行一次 `ORG_DEPARTMENT` 查询。
