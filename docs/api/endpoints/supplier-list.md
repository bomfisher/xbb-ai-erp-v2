# 供应商列表查询

## 文档信息
- 领域：`supplier-list`
- 控制器：`SupplierAdminController#list`
- 请求方式：`POST /erp/v1/supplier/list`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "keyword": "杭州",
  "supplierCode": "SUP-001",
  "supplierName": "杭州供应商",
  "pageNum": 1,
  "pageSize": 20,
  "conditions": [
    {
      "attr": "bizStatus",
      "symbol": "EQ",
      "value": "1"
    }
  ]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `keyword` | 否 | 关键字，当前按供应商编码、供应商名称模糊查询 |
| `supplierCode` | 否 | 供应商编码 |
| `supplierName` | 否 | 供应商名称 |
| `supplierShortName` | 否 | 供应商简称 |
| `supplierCategory` | 否 | 供应商分类 |
| `mainBusinessCategory` | 否 | 主营类目 |
| `ownerPurchaserId` | 否 | 采购负责人 ID |
| `bizStatus` | 否 | 业务状态 |
| `refStatus` | 否 | 引用状态 |
| `pageNum` | 否 | 页码 |
| `pageSize` | 否 | 每页条数 |
| `conditions` | 否 | 动态筛选条件列表，对接公共列表筛选元数据 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "headList": null,
    "list": [
      {
        "id": 1,
        "supplierCode": "SUP-001",
        "supplierName": "杭州供应商",
        "supplierShortName": "杭供",
        "supplierCategory": "A",
        "mainBusinessCategory": "steel",
        "ownerPurchaserNameSnapshot": "张三",
        "bizStatus": "1",
        "refStatus": "0",
        "addTime": 1721606400000,
        "updateTime": 1721606400000
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
| `data.list` | 是 | 列表数据 |
| `data.list[].id` | 是 | 供应商主键 |
| `data.list[].supplierCode` | 否 | 供应商编码 |
| `data.list[].supplierName` | 否 | 供应商名称 |
| `data.pageHelper` | 是 | 分页信息 |

## 规则说明

- 当前列表查询由 `SupplierListDTO` 直接承接筛选字段，同时兼容 `conditions` 动态条件列表
- 默认页码为 `1`；当 `pageSize` 未传或小于 `1` 时，按当前结果总数兜底计算分页
- 当前返回 `headList=[]`，列表字段元数据不由本接口承载
