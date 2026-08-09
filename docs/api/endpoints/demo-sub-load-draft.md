# DEMO_SUB 加载草稿

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#loadDraft`
- 请求方式：`POST /erp/v1/demo-sub/loadDraft`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 规则说明
- 仅按当前租户和 `draftCode` 从草稿缓存加载；草稿不存在时返回空草稿详情。
