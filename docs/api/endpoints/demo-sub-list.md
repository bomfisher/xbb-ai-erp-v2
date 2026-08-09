# DEMO_SUB 列表

## 文档信息
- 领域：`demo-sub`
- 控制器：`DemoSubAdminController#list`
- 请求方式：`POST /erp/v1/demo-sub/list`
- 响应类型：`ResultVO<ListBaseVO<DemoSubListItemVO>>`
- 聚合文档引用：`docs/kn/demo-sub-m.md`

## 请求示例
```json
{"corpid":"企业ID","userId":"员工ID","pageNum":1,"pageSize":20,"conditions":[]}
```

## 响应示例
```json
{"success":true,"data":{"list":[],"pageHelper":{"page":1,"count":0}}}
```

## 规则说明
- 筛选字段和操作符由 `DEMO_SUB` 公共列表元数据下发，服务端只接受白名单映射。
- `dataId` 筛选项为关联 `DEMO` 的业务选择器，公共筛选元数据下发当前租户的 `businessSelectConfig`；先选择关联数据，再按 `data_id` 白名单筛选。
- `userId` 与 `departmentId` 筛选项分别下发成员、部门的选择配置；前端先选择稳定 ID，再按白名单列筛选。
