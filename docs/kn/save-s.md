# save-s

## 客户
- 接口：`POST /erp/v1/customer/saveAndSubmit`
- 用途：客户正式保存
- 差异点：支持主档 + 子档正式落库
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-save-and-submit.md`

## 供应商
- 接口：`POST /erp/v1/supplier/save`
- 用途：供应商正式保存
- 差异点：旧保存接口直接返回供应商主键
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-save.md`

## 供应商
- 接口：`POST /erp/v1/supplier/saveAndSubmit`
- 用途：供应商正式保存并提交
- 差异点：提交成功后可清理草稿
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-save-and-submit.md`
