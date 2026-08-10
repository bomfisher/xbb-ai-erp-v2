# DEMO 新建初始化

- 方法：`POST`
- 路径：`/erp/v1/demo/addItem`
- 请求：`BaseDTO`。
- 响应：`ResultVO<SaveItemVO<DemoSaveItemVO>>`，返回 `CREATE` 场景字段元数据和空主档/子档数据。`COMB`、`COMB_MULTI`、`CHECKBOX`、`RADIO_BTN` 使用 `itemList(value,text)` 返回选项；选择数据字段的 `businessSelectConfig` 仅返回目标 `businessCode`，前端按编码注册表解析选择接口。
