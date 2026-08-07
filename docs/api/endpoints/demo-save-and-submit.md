# DEMO 保存并提交

- 方法：`POST`
- 路径：`/erp/v1/demo/saveAndSubmit`
- 请求：`DemoSubmitSaveDTO`，包含主档和可选 `items` 子档。
- 响应：`ResultVO<BaseVO>`。
- 事务：主档成功后同步 `demo_item`，编辑时按当前子档集合重建明细。
