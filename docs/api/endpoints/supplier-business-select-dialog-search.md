# 供应商业务选择弹窗搜索

## 文档信息
- 领域：`supplier-business-select-dialog-search`
- 控制器：`SupplierAdminController#businessSelectDialogSearch`
- 请求方式：`POST /erp/v1/supplier/businessSelect/dialogSearch`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "demo-corp",
  "keyword": "宁波",
  "pageNum": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `keyword` | 否 | 供应商编码或名称关键字 |
| `pageNum` | 否 | 页码，小于 1 时按 1 处理 |
| `pageSize` | 否 | 每页条数，小于 1 时按 20 处理 |
| `id` | 否 | 可选单条定位条件 |

## 响应示例

```json
{
  "code": "1",
  "message": "",
  "success": true,
  "data": {
    "list": [
      {
        "id": 2002,
        "code": "SUP-002",
        "name": "宁波供应商",
        "label": "SUP-002 宁波供应商"
      }
    ],
    "pageHelper": {
      "page": 1,
      "count": 1,
      "hasLeft": false,
      "hasRight": false
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.list` | 是 | 当前页候选列表 |
| `data.list[].id` | 是 | 供应商 ID |
| `data.list[].code` | 是 | 供应商编码 |
| `data.list[].name` | 是 | 供应商名称 |
| `data.list[].label` | 是 | 前端直接展示文案 |
| `data.pageHelper` | 是 | 分页信息 |
| `data.pageHelper.page` | 是 | 当前页码 |
| `data.pageHelper.count` | 是 | 总页数 |
| `data.pageHelper.hasLeft` | 是 | 是否存在上一页 |
| `data.pageHelper.hasRight` | 是 | 是否存在下一页 |

## 规则说明

- 用于业务选择弹窗内的列表搜索
- 返回分页结构，前端可基于 `pageHelper` 扩展翻页交互
- `pageNum` 默认值为 1，`pageSize` 默认值为 20
- 当前实现先查出符合条件的供应商，再按页码做内存分页切片
