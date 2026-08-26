# 仓库

## 新建

### `addItem`

- 接口路径：`POST /erp/v1/masterData/warehouse/addItem`
- 用途：仓库新增表单元数据初始化
- 差异点：仓库编码字段使用 `SERIAL_NO(21)`，由当前租户的 `WAREHOUSE` 编号规则按“前缀 + 自增后缀”生成。
- API 文档：`docs/api/endpoints/master-data-warehouse-add-item.md`
