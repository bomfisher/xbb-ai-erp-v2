# create-s

## 客户
- 接口：`POST /erp/v1/customer/addItem`
- 用途：客户新增表单元数据初始化
- 差异点：返回 `headList + data + sectionState` 的新建抽屉结构
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-add-item.md`

## 供应商
- 接口：`POST /erp/v1/supplier/addItem`
- 用途：供应商新增表单初始化
- 差异点：返回主档、子档与 `sectionState` 的完整抽屉初始化结构
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-add-item.md`
