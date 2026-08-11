# DEMO 编辑初始化

- 方法：`POST`
- 路径：`/erp/v1/demo/updateItem`
- 请求：`IdBaseDTO`，需要 `corpid` 与主档 `id`。
- 响应：`ResultVO<SaveItemVO<DemoSaveItemVO>>`，返回 `UPDATE` 场景字段元数据及主档、`demo_item` 子档回填。
- `data.items` 按 `corpid + dataId` 查询并按 `id asc` 返回，行结构为 `{ id, name }`；`headList.items.subField` 与新建接口保持一致。
- `data.items2` 回填名称以 `-2` 结尾的子档；其余子档回填至 `data.items`。
