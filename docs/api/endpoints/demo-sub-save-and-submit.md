# DEMO_SUB 保存并提交

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#saveAndSubmit`
- 请求方式：`POST /erp/v1/demo-sub/saveAndSubmit`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 请求示例
```json
{"corpid":"企业ID","userId":"员工ID","main":{"dataId":1,"parentName":"DEMO名称","name":"示例名称"}}
```

## 响应示例
```json
{"success":true,"data":{}}
```

## 规则说明
- 按协议校验、通用校验和业务校验顺序保存；`main`、`dataId` 和 `name` 必须提供，有 `id` 时更新，否则新增。
- 正式保存全部成功后，才删除请求 `draftMeta.draftCode` 指定的同租户草稿。
