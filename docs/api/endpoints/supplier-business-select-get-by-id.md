# 供应商业务选择按 ID 回显

## 文档信息
- 领域：`supplier-business-select-get-by-id`
- 控制器：`SupplierAdminController#businessSelectGetById`
- 请求方式：`POST /erp/v1/supplier/businessSelect/getById`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "id": 2001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `id` | 是 | 供应商 ID |
| `keyword` | 否 | 当前接口不依赖该字段 |
| `pageNum` | 否 | 当前接口不依赖该字段 |
| `pageSize` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": {
    "id": 2001,
    "code": "SUP-001",
    "name": "杭州供应商",
    "label": "SUP-001 杭州供应商"
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 否 | 匹配不到供应商时返回 `null` |
| `data.id` | 是 | 供应商 ID |
| `data.code` | 是 | 供应商编码 |
| `data.name` | 是 | 供应商名称 |
| `data.label` | 是 | 前端直接展示文案 |

## 规则说明

- 用于表单已有值的回显展示
- `id` 不能为空，缺失时后端抛业务异常
- 如果根据 `corpid + id` 查不到供应商，返回 `null`
- 返回结果统一转成业务选择候选对象结构
