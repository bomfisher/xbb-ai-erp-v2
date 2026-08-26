# 保存系统配置

`POST /erp/v1/system/businessConfig/save`

请求体为 `BusinessConfigSaveDTO`，继承 `BaseDTO`，包含：

- `businessCode`：要保存的单据业务编码。
- `values`：配置项编码到值的对象。

服务端根据 `BusinessConfigCatalog` 白名单校验单据、配置项和值类型。未知项、未声明选项或错误类型会返回业务错误。成功时返回 `ResultVO<BusinessConfigDocumentVO>`，返回服务端保存后的有效配置。

保存后的租户覆盖值写入 `sys_business_config`；成功提交数据库事务后精确删除 Redis 键 `erp:system:business-config:{corpid}:{businessCode}`，后续查询自动重建缓存。
