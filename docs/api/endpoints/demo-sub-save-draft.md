# DEMO_SUB 保存草稿

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#saveDraft`
- 请求方式：`POST /erp/v1/demo-sub/saveDraft`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 规则说明
- 执行协议校验和草稿通用校验后写入 Redis 缓存，返回 `DraftSaveVO.draftCode`。
- 传入 `draftMeta.draftCode` 时覆盖同租户草稿；草稿有效期为 7 天。
