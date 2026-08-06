# product-warehouse 接口文档

## 文档说明
- 模块：`xbb-erp-module-product`
- 领域：`product-warehouse`
- 统一返回：所有接口均使用 `ResultVO.success(...)` 包装
- 非脚本接口公共入参：`corpid`、`userId`
- 文档中的 JSON 为示例报文，字段名与接口 DTO/VO 保持一致
- 复杂字段、状态字段通过 `xxxComment` 字段补充说明

## 1. 仓库列表
- 请求地址：`POST /erp/v1/product/warehouse/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 1,
  "bizOrgId": 10,
  "warehouseCode": "WH-001",
  "warehouseName": "杭州仓",
  "warehouseType": "FINISHED",
  "warehouseTypeComment": "仓库类型，示例值：RAW-原料仓，FINISHED-成品仓，TRANSIT-中转仓",
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用",
  "address": "杭州市余杭区仓前街道XX路1号",
  "managerId": "EMP0002",
  "bizStatus": "ENABLED",
  "bizStatusComment": "业务状态，示例值：ENABLED-正常，DISABLED-停用",
  "pageNum": 1,
  "pageSize": 10,
  "offset": 0,
  "groupByStr": "warehouse_type",
  "orderByStr": "update_time desc",
  "pageNumComment": "页码，从1开始；传 pageNum + pageSize 时服务会计算 offset",
  "pageSizeComment": "分页大小",
  "offsetComment": "分页起始行，通常由服务内部计算，手工传入时需与 pageNum/pageSize 保持一致",
  "groupByStrComment": "可选分组字段，当前仅建议传数据库字段名组合",
  "orderByStrComment": "可选排序字段，示例：update_time desc"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [
      {
        "attr": "bizOrgId",
        "attrName": "业务组织",
        "fieldType": "input-number",
        "required": 1,
        "editable": 1
      },
      {
        "attr": "warehouseCode",
        "attrName": "仓库编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1
      }
    ],
    "list": [
      {
        "id": 1,
        "bizOrgId": 10,
        "warehouseCode": "WH-001",
        "warehouseName": "杭州仓",
        "warehouseType": "FINISHED",
        "enableStatus": 1,
        "address": "杭州市余杭区仓前街道XX路1号",
        "managerId": "EMP0002",
        "bizStatus": "ENABLED",
        "addTime": 1721800000000,
        "updateTime": 1721803600000,
        "warehouseTypeComment": "FINISHED-成品仓",
        "enableStatusComment": "1-启用",
        "bizStatusComment": "ENABLED-正常"
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

## 2. 仓库新增表单
- 请求地址：`POST /erp/v1/product/warehouse/addItem`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [
      {
        "attr": "bizOrgId",
        "attrName": "业务组织",
        "fieldType": "input-number",
        "required": 1,
        "editable": 1
      },
      {
        "attr": "warehouseCode",
        "attrName": "仓库编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1
      }
    ],
    "data": {
      "main": {
        "id": null,
        "corpid": null,
        "bizOrgId": null,
        "warehouseCode": null,
        "warehouseName": null,
        "warehouseType": null,
        "enableStatus": null,
        "address": null,
        "managerId": null,
        "bizStatus": null,
        "creatorId": null,
        "modifyId": null,
        "deleted": null,
        "addTime": null,
        "updateTime": null
      }
    }
  }
}
```

## 3. 仓库修改表单
- 请求地址：`POST /erp/v1/product/warehouse/updateItem`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 1
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [
      {
        "attr": "bizOrgId",
        "attrName": "业务组织",
        "fieldType": "input-number",
        "required": 1,
        "editable": 1
      },
      {
        "attr": "warehouseCode",
        "attrName": "仓库编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1
      }
    ],
    "data": {
      "main": {
        "id": 1,
        "corpid": "10001",
        "bizOrgId": 10,
        "warehouseCode": "WH-001",
        "warehouseName": "杭州仓",
        "warehouseType": "FINISHED",
        "warehouseTypeComment": "FINISHED-成品仓",
        "enableStatus": 1,
        "enableStatusComment": "0-禁用，1-启用",
        "address": "杭州市余杭区仓前街道XX路1号",
        "managerId": "EMP0002",
        "bizStatus": "ENABLED",
        "bizStatusComment": "ENABLED-正常，DISABLED-停用",
        "creatorId": "EMP0001",
        "modifyId": "EMP0001",
        "deleted": 0,
        "deletedComment": "0-未删除，1-已删除",
        "addTime": 1721800000000,
        "updateTime": 1721803600000
      }
    }
  }
}
```

## 4. 仓库保存
- 请求地址：`POST /erp/v1/product/warehouse/save`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "main": {
    "id": 1,
    "bizOrgId": 10,
    "warehouseCode": "WH-001",
    "warehouseName": "杭州总仓",
    "warehouseType": "FINISHED",
    "warehouseTypeComment": "RAW-原料仓，FINISHED-成品仓，TRANSIT-中转仓",
    "enableStatus": 1,
    "enableStatusComment": "0-禁用，1-启用",
    "address": "杭州市余杭区仓前街道XX路99号",
    "managerId": "EMP0002",
    "bizStatus": "ENABLED",
    "bizStatusComment": "ENABLED-正常，DISABLED-停用"
  }
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 1
}
```

## 5. 仓库详情
- 请求地址：`POST /erp/v1/product/warehouse/detail`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 1
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [
      {
        "attr": "bizOrgId",
        "attrName": "业务组织",
        "fieldType": "input-number",
        "required": 1,
        "editable": 1
      },
      {
        "attr": "warehouseCode",
        "attrName": "仓库编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1
      }
    ],
    "mainData": {
      "main": {
        "id": 1,
        "corpid": "10001",
        "bizOrgId": 10,
        "warehouseCode": "WH-001",
        "warehouseName": "杭州总仓",
        "warehouseType": "FINISHED",
        "warehouseTypeComment": "FINISHED-成品仓",
        "enableStatus": 1,
        "enableStatusComment": "0-禁用，1-启用",
        "address": "杭州市余杭区仓前街道XX路99号",
        "managerId": "EMP0002",
        "bizStatus": "ENABLED",
        "bizStatusComment": "ENABLED-正常，DISABLED-停用",
        "creatorId": "EMP0001",
        "modifyId": "EMP0003",
        "deleted": 0,
        "deletedComment": "0-未删除，1-已删除",
        "addTime": 1721800000000,
        "updateTime": 1721807200000
      }
    }
  }
}
```

## 6. 仓库删除
- 请求地址：`POST /erp/v1/product/warehouse/delete`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "idList": [
    1,
    2
  ],
  "idListComment": "批量删除的仓库ID列表"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": null
}
```
