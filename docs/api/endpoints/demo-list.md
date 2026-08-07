# DEMO 列表

- 方法：`POST`
- 路径：`/erp/v1/demo/list`
- 请求：`ListBaseDTO`，支持 `keyword`、白名单 `conditions` 和分页；不定义业务专属列表 DTO。
- 响应：`ResultVO<ListBaseVO<DemoListItemVO>>`。
- 说明：字段、筛选、顶部新增和行编辑动作由 `DEMO` 列表元数据 Provider 提供。
