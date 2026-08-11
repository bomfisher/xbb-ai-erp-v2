# DEMO_SUB 编辑初始化

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#updateItem`
- 请求方式：`POST /erp/v1/demo-sub/updateItem`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 请求示例
```json
{"corpid":"企业ID","userId":"员工ID","id":1}
```

## 响应示例
```json
{"success":true,"data":{"headList":[{"attr":"main.dataId","fieldType":"16","businessSelectConfig":{"businessType":"demo","businessCode":"DEMO","requestPayload":{"corpid":"企业ID","businessCode":"DEMO"},"placeholder":"请选择关联DEMO数据","dialogTitle":"选择关联DEMO数据","multiple":false}},{"attr":"main.parentName","fieldType":"1"}],"data":{"main":{}}}}
```

## 规则说明
- 返回 `UPDATE` 场景字段元数据和指定记录的主档数据。
- `dataId` 仍下发关联 `DEMO` 的 `BUSINESS(16)` 选择配置，编辑态与新建态使用同一租户契约。
- `dataId` 同时下发 `selectionFillConfig.enabled=true`；未启用该配置的选择字段只写入关联 ID，不触发回填接口。
- `userId` 与 `departmentId` 分别使用成员、部门选择配置回显已保存的稳定 ID。
