# DEMO_SUB 草稿列表

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#draftList`
- 请求方式：`POST /erp/v1/demo-sub/draftList`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 规则说明
- 仅从当前租户的草稿缓存读取，按最近更新时间倒序最多返回 10 条，不查询正式业务表。
