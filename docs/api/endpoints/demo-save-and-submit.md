# DEMO 保存并提交

- 方法：`POST`
- 路径：`/erp/v1/demo/saveAndSubmit`
- 请求：`DemoSubmitSaveDTO`，包含主档和可选 `items` 子档。
- 响应：`ResultVO<BaseVO>`。
- 事务：主档成功后差量同步 `demo_item`：无 `id` 的子档新增；携带且属于当前 DEMO 的 `id` 更新；数据库已有但请求未提交的子档软删除。
- 请求中的 `items` 为空数组表示清空子档；每个子档至少包含非空 `name`。编辑回显的子档 `id` 必须随保存请求原样提交；保存时忽略客户端审计字段，新增子档主键由数据库自增生成。
- `items2` 的每行 `name` 入库时自动补 `-2` 后缀一次；`items` 的名称不改变。
