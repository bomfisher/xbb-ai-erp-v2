# 仓库新增表单初始化

## 文档信息

- 领域：`master-data`
- 控制器：`WarehouseAdminController#addItem`
- 请求方式：`POST /erp/v1/masterData/warehouse/addItem`
- 聚合文档引用：`docs/kn/warehouse-m.md`

## 请求示例

```json
{ "corpid": "demo-corp", "userId": "115014265324309213" }
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 当前租户标识 |
| `userId` | 是 | 当前操作人标识 |

## 响应示例

```json
{ "code": 0, "success": true, "data": { "headList": [{ "attr": "main.warehouseCode", "attrName": "仓库编码", "fieldType": "21" }], "data": { "main": { "warehouseCode": "WH-00001" } } } }
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `headList` | 是 | CREATE 场景的字段元数据 |
| `headList[].attr=main.warehouseCode` | 是 | 仓库编码字段，类型为 `SERIAL_NO(21)` |
| `data.main.warehouseCode` | 是 | 当前租户 `WAREHOUSE` 业务编号规则生成的仓库编码 |

## 规则说明

- 服务端调用 `BizNoGenerator#next(corpid, BusinessCodeEnum.WAREHOUSE.getCode())` 生成编码，不接受前端传入或覆盖。
- 仓库属于主数据，编号规则为配置前缀加自增后缀，例如前缀为 `WH` 时生成 `WH-00001`。
- 规则按 `corpid + businessCode` 隔离；租户未配置时使用默认租户规则。
