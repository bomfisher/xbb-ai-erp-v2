# product-category 接口文档

## 文档说明
- 模块：`xbb-erp-module-product`
- 领域：`product-category`
- 统一返回：`ResultVO`
- 非脚本接口公共入参：`corpid`、`userId`
- 文档中的 JSON 为示例报文，字段名与接口 DTO/VO 保持一致
- 复杂字段、状态字段通过 `xxxComment` 字段补充说明

## 1. 商品分类-新增
- 请求地址：`POST /erp/v1/product/category/create`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "categoryCode": "CAT-001",
  "categoryName": "原材料",
  "parentId": 0,
  "categoryLevel": 1,
  "sortNo": 10,
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": 1001
}
```

## 2. 商品分类-修改
- 请求地址：`POST /erp/v1/product/category/update`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 1001,
  "categoryCode": "CAT-001",
  "categoryName": "原材料-更新",
  "parentId": 0,
  "categoryLevel": 1,
  "sortNo": 20,
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

## 3. 商品分类-删除
- 请求地址：`POST /erp/v1/product/category/remove`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 1001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

## 4. 商品分类-详情
- 请求地址：`GET /erp/v1/product/category/detail`
- 请求 JSON
```json
{
  "corpid": "10001",
  "id": 1001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1001,
    "categoryCode": "CAT-001",
    "categoryName": "原材料",
    "parentId": 0,
    "categoryLevel": 1,
    "sortNo": 10,
    "enableStatus": 1,
    "enableStatusComment": "0-禁用，1-启用"
  }
}
```

## 5. 商品分类-列表
- 请求地址：`GET /erp/v1/product/category/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "categoryCode": "CAT",
  "categoryName": "材料",
  "offsetComment": "分页起始行，非必填",
  "pageSizeComment": "分页大小，非必填"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1001,
      "categoryCode": "CAT-001",
      "categoryName": "原材料",
      "parentId": 0,
      "categoryLevel": 1,
      "sortNo": 10,
      "enableStatus": 1,
      "enableStatusComment": "0-禁用，1-启用"
    }
  ]
}
```

## 6. 商品品牌-新增
- 请求地址：`POST /erp/v1/product/brand/create`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "brandCode": "BR-001",
  "brandName": "默认品牌",
  "sortNo": 10,
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": 2001
}
```

## 7. 商品品牌-修改
- 请求地址：`POST /erp/v1/product/brand/update`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 2001,
  "brandCode": "BR-001",
  "brandName": "默认品牌-更新",
  "sortNo": 20,
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

## 8. 商品品牌-删除
- 请求地址：`POST /erp/v1/product/brand/remove`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 2001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

## 9. 商品品牌-详情
- 请求地址：`GET /erp/v1/product/brand/detail`
- 请求 JSON
```json
{
  "corpid": "10001",
  "id": 2001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 2001,
    "brandCode": "BR-001",
    "brandName": "默认品牌",
    "sortNo": 10,
    "enableStatus": 1,
    "enableStatusComment": "0-禁用，1-启用"
  }
}
```

## 10. 商品品牌-列表
- 请求地址：`GET /erp/v1/product/brand/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "brandCode": "BR",
  "brandName": "默认",
  "offsetComment": "分页起始行，非必填",
  "pageSizeComment": "分页大小，非必填"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 2001,
      "brandCode": "BR-001",
      "brandName": "默认品牌",
      "sortNo": 10,
      "enableStatus": 1,
      "enableStatusComment": "0-禁用，1-启用"
    }
  ]
}
```

## 11. 商品单位-新增
- 请求地址：`POST /erp/v1/product/unit/create`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "unitCode": "UNIT-001",
  "unitName": "个",
  "precisionNum": 2,
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": 3001
}
```

## 12. 商品单位-修改
- 请求地址：`POST /erp/v1/product/unit/update`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 3001,
  "unitCode": "UNIT-001",
  "unitName": "件",
  "precisionNum": 2,
  "enableStatus": 1,
  "enableStatusComment": "0-禁用，1-启用"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

## 13. 商品单位-删除
- 请求地址：`POST /erp/v1/product/unit/remove`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "id": 3001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": true
}
```

## 14. 商品单位-详情
- 请求地址：`GET /erp/v1/product/unit/detail`
- 请求 JSON
```json
{
  "corpid": "10001",
  "id": 3001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 3001,
    "unitCode": "UNIT-001",
    "unitName": "个",
    "precisionNum": 2,
    "enableStatus": 1,
    "enableStatusComment": "0-禁用，1-启用"
  }
}
```

## 15. 商品单位-列表
- 请求地址：`GET /erp/v1/product/unit/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "unitCode": "UNIT",
  "unitName": "个",
  "offsetComment": "分页起始行，非必填",
  "pageSizeComment": "分页大小，非必填"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 3001,
      "unitCode": "UNIT-001",
      "unitName": "个",
      "precisionNum": 2,
      "enableStatus": 1,
      "enableStatusComment": "0-禁用，1-启用"
    }
  ]
}
```