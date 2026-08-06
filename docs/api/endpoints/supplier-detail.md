# 供应商详情查询

## 文档信息
- 领域：`supplier-detail`
- 控制器：`SupplierAdminController#detail`
- 请求方式：`POST /erp/v1/supplier/detail`
- 聚合文档引用：`docs/kn/supplier-m.md`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "user-001",
  "id": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `id` | 是 | 供应商主键 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "mainData": {
      "main": {
        "id": 1,
        "supplierCode": "SUP-001",
        "supplierName": "杭州供应商"
      }
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.mainData` | 是 | 详情主体 |
| `data.mainData.main` | 否 | 供应商主档 |

## 规则说明

- 详情接口当前返回 `mainData`，其内部结构与保存抽屉数据结构对齐
- 详情页主数据来源于保存态聚合结构，而不是单独定义的只读 VO 展平结构
