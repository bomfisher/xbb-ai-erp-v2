# draft-s

## 客户
- 接口：`POST /erp/v1/customer/saveDraft`
- 用途：客户草稿保存
- 差异点：保存主档与子档编辑态，并返回草稿编码
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-save-draft.md`

## 客户
- 接口：`POST /erp/v1/customer/draftList`
- 用途：客户草稿列表查询
- 差异点：只返回最近 `10` 条草稿摘要
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-draft-list.md`

## 客户
- 接口：`POST /erp/v1/customer/loadDraft`
- 用途：客户草稿加载
- 差异点：返回完整编辑态数据与 `sectionState`
- 业务文档：`docs/kn/customer-m.md`
- API 文档：`docs/api/endpoints/customer-load-draft.md`

## 供应商
- 接口：`POST /erp/v1/supplier/saveDraft`
- 用途：供应商草稿保存
- 差异点：返回草稿编码，用于草稿续编
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-save-draft.md`

## 供应商
- 接口：`POST /erp/v1/supplier/draftList`
- 用途：供应商草稿列表查询
- 差异点：按更新时间倒序返回最近 `10` 条草稿摘要
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-draft-list.md`

## 供应商
- 接口：`POST /erp/v1/supplier/loadDraft`
- 用途：供应商草稿加载
- 差异点：返回完整编辑态数据与草稿元信息
- 业务文档：`docs/kn/supplier-m.md`
- API 文档：`docs/api/endpoints/supplier-load-draft.md`
