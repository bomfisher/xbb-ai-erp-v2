# 主数据模块

- `ProductSpu`：产品 SPU 主档，提供列表、表单、草稿与正式提交接口。
- `ProductSku`：产品 SKU 持久化聚合，仅提供领域与基础设施持久化能力，不暴露 HTTP 接口。
- 数据库迁移：`V6__create_product_spu_and_sku.sql`。

