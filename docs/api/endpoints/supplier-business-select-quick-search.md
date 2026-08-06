# 供应商业务选择快捷搜索

## 文档信息
- 领域：`supplier-business-select-quick-search`
- 控制器：`SupplierAdminController#businessSelectQuickSearch`
- 请求方式：`POST /erp/v1/supplier/businessSelect/quickSearch`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "keyword": "杭州"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `keyword` | 否 | 供应商编码或名称关键字 |
| `id` | 否 | 可选单条定位条件 |
| `pageNum` | 否 | 当前接口不依赖该字段 |
| `pageSize` | 否 | 当前接口不依赖该字段 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": [
    {
      "id": 2001,
      "code": "SUP-001",
      "name": "杭州供应商",
      "label": "SUP-001 杭州供应商"
    }
  ]
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 业务选择候选列表 |
| `data[].id` | 是 | 供应商 ID |
| `data[].code` | 是 | 供应商编码 |
| `data[].name` | 是 | 供应商名称 |
| `data[].label` | 是 | 前端直接展示文案 |

## 规则说明

- 用于业务选择输入框的快捷搜索下拉候选
- 返回轻量列表，不返回分页结构
- 关键字同时匹配供应商编码与名称
- 如果传入 `id`，查询链路会一并参与过滤
