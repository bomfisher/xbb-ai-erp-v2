# DEMO 编辑初始化

- 方法：`POST`
- 路径：`/erp/v1/demo/updateItem`
- 请求：`IdBaseDTO`，需要 `corpid` 与主档 `id`。
- 响应：`ResultVO<SaveItemVO<DemoSaveItemVO>>`，返回 `UPDATE` 场景字段元数据及主档、`demo_item` 子档回填。
