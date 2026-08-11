# DEMO_SUB 选择回填

## 文档信息
- 领域：`demo-sub`
- 请求方式：`POST /erp/v1/demo-sub/selectionFill`

## 请求示例
```json
{"corpid":"企业ID","userId":"员工ID","fieldAttr":"main.dataId","referenceId":1}
```

## 响应示例
```json
{"success":true,"data":{"referenceId":1,"patch":{"main.parentName":"DEMO名称"}}}
```

## 规则说明
- 仅允许 `main.dataId` 作为 DEMO 选择来源字段。
- 服务端按 `corpid` 查询有效 DEMO，返回当前 `DEMO_SUB` 表单允许写入的字段补丁。
- `parentName` 会随 `DEMO_SUB` 正式保存持久化；前端提交的值仍需通过业务校验。
- `fieldAttr` 是当前业务模块的回填分派键；后续新增上游业务时在同一接口增加已启用字段的规则，禁止新增按上游单据区分的 HTTP 接口。
