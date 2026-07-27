# common-list-s.md

## `/erp/v1/common/list/filter`

- 所属业务：`公共列表元数据`
- 请求方式：`POST`
- 用途：按业务编码返回筛选字段元数据
- 差异点：只返回 `FilterField` 列表，不返回表头或按钮信息
- 主文档定位：`docs/kn/common-m.md`
- API 文档定位：`docs/api/common-list.md`

## `/erp/v1/common/list/header`

- 所属业务：`公共列表元数据`
- 请求方式：`POST`
- 用途：按业务编码返回表头字段元数据
- 差异点：只返回 `FieldEntity` 列表，字段包含 `required`、`editable`、`itemList`
- 主文档定位：`docs/kn/common-m.md`
- API 文档定位：`docs/api/common-list.md`

## `/erp/v1/common/list/topButton`

- 所属业务：`公共列表元数据`
- 请求方式：`POST`
- 用途：按业务编码返回顶部按钮元数据
- 差异点：只返回 `ListButtonItemPojo` 列表，用于页面头部操作区
- 主文档定位：`docs/kn/common-m.md`
- API 文档定位：`docs/api/common-list.md`

## `/erp/v1/common/list/bottomButton`

- 所属业务：`公共列表元数据`
- 请求方式：`POST`
- 用途：按业务编码返回底部按钮元数据
- 差异点：只返回 `ListButtonItemPojo` 列表，用于页面底部操作区
- 主文档定位：`docs/kn/common-m.md`
- API 文档定位：`docs/api/common-list.md`
