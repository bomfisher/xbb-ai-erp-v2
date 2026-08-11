# DEMO_SUB 新建初始化

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#addItem`
- 请求方式：`POST /erp/v1/demo-sub/addItem`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 请求示例
```json
{"corpid":"企业ID","userId":"员工ID"}
```

## 响应示例
```json
{"success":true,"data":{"headList":[{"attr":"main.dataId","fieldType":"16","businessSelectConfig":{"businessType":"demo","businessCode":"DEMO","requestPayload":{"corpid":"企业ID","businessCode":"DEMO"},"placeholder":"请选择关联DEMO数据","dialogTitle":"选择关联DEMO数据","multiple":false}},{"attr":"main.parentName","fieldType":"1"}],"data":{"main":{}}}}
```

## 规则说明
- 返回 `CREATE` 场景的 `headList + data`；`dataId` 与 `name` 为必填字段，`userId`、`departmentId` 为可选稳定 ID。
- `dataId` 是关联 `DEMO` 的 `BUSINESS(16)` 字段，必须下发包含当前租户请求参数的 `businessSelectConfig`。
- `dataId` 同时下发 `selectionFillConfig.enabled=true`；前端仅在该开关启用时调用当前业务的 `/selectionFill` 接口。
- `userId` 的 `USER(12)` 字段和 `departmentId` 的 `DEPT(14)` 字段下发组织模块的选择接口及当前租户请求参数。
