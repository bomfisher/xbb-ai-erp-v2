# 产品 SPU 正式提交

- 领域：`master-data`
- 控制器：`ProductSpuAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/masterData/productSpu/saveAndSubmit`

请求使用 `ProductSpuSubmitSaveDTO`；SPU 编码、名称和分类名称必须填写；响应为 `BaseVO`。首次新建 SPU 成功后，会在同一事务中创建一条默认 SKU：SKU 编码和名称继承 SPU，单位为“件”，启用状态和备注继承 SPU；更新已有 SPU 不会重复创建 SKU。
