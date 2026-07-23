# product-product 接口文档

## 文档说明
- 模块：`xbb-erp-module-product`
- 领域：`product-product`
- 统一返回：`ResultVO`
- 非脚本接口公共入参：`corpid`、`userId`
- 当前最小模型：一条 `SPU` 对应一条 `SKU`
- 文档中的 JSON 为示例报文，字段名与接口 DTO/VO 保持一致
- 复杂字段、状态字段通过 `xxxComment` 字段补充说明

## 1. 商品聚合-新增
- 请求地址：`POST /erp/v1/product/create`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "spuCode": "SPU-001",
  "spuName": "测试商品",
  "categoryId": 1001,
  "brandId": 2001,
  "productType": "NORMAL",
  "productTypeComment": "商品类型，示例值仅作报文演示",
  "enableSpec": 0,
  "enableSpecComment": "0-不启用规格，1-启用规格",
  "description": "测试商品描述",
  "imageUrl": "https://example.com/product.png",
  "spuEnableStatus": 1,
  "spuEnableStatusComment": "0-禁用，1-启用",
  "skuCode": "SKU-001",
  "skuName": "测试商品-默认规格",
  "mnemonicCode": "CS",
  "mainBarcode": "690000000001",
  "canPurchase": 1,
  "canPurchaseComment": "0-否，1-是",
  "canSale": 1,
  "canSaleComment": "0-否，1-是",
  "canInventory": 1,
  "canInventoryComment": "0-否，1-是",
  "canProduce": 0,
  "canProduceComment": "0-否，1-是",
  "skuEnableStatus": 1,
  "skuEnableStatusComment": "0-禁用，1-启用",
  "listingStatus": 1,
  "listingStatusComment": "0-下架，1-上架"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": 4001
}
```

## 2. 商品聚合-修改
- 请求地址：`POST /erp/v1/product/update`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "spuId": 4001,
  "skuId": 5001,
  "spuCode": "SPU-001",
  "spuName": "测试商品-更新",
  "categoryId": 1001,
  "brandId": 2001,
  "productType": "NORMAL",
  "productTypeComment": "商品类型，示例值仅作报文演示",
  "enableSpec": 0,
  "enableSpecComment": "0-不启用规格，1-启用规格",
  "description": "更新后的商品描述",
  "imageUrl": "https://example.com/product-new.png",
  "spuEnableStatus": 1,
  "spuEnableStatusComment": "0-禁用，1-启用",
  "skuCode": "SKU-001",
  "skuName": "测试商品-默认规格",
  "mnemonicCode": "CS",
  "mainBarcode": "690000000001",
  "canPurchase": 1,
  "canPurchaseComment": "0-否，1-是",
  "canSale": 1,
  "canSaleComment": "0-否，1-是",
  "canInventory": 1,
  "canInventoryComment": "0-否，1-是",
  "canProduce": 0,
  "canProduceComment": "0-否，1-是",
  "skuEnableStatus": 1,
  "skuEnableStatusComment": "0-禁用，1-启用",
  "listingStatus": 1,
  "listingStatusComment": "0-下架，1-上架"
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

## 3. 商品聚合-删除
- 请求地址：`POST /erp/v1/product/remove`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "spuId": 4001,
  "skuId": 5001,
  "skuIdComment": "传 skuId 时优先删除 SKU，再按规则删除对应 SPU"
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

## 4. 商品聚合-详情
- 请求地址：`GET /erp/v1/product/detail`
- 请求 JSON
```json
{
  "corpid": "10001",
  "spuId": 4001,
  "skuId": 5001
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "spuId": 4001,
    "skuId": 5001,
    "spuCode": "SPU-001",
    "spuName": "测试商品",
    "categoryId": 1001,
    "brandId": 2001,
    "productType": "NORMAL",
    "productTypeComment": "商品类型，示例值仅作报文演示",
    "enableSpec": 0,
    "enableSpecComment": "0-不启用规格，1-启用规格",
    "description": "测试商品描述",
    "imageUrl": "https://example.com/product.png",
    "spuEnableStatus": 1,
    "spuEnableStatusComment": "0-禁用，1-启用",
    "skuCode": "SKU-001",
    "skuName": "测试商品-默认规格",
    "mnemonicCode": "CS",
    "mainBarcode": "690000000001",
    "canPurchase": 1,
    "canPurchaseComment": "0-否，1-是",
    "canSale": 1,
    "canSaleComment": "0-否，1-是",
    "canInventory": 1,
    "canInventoryComment": "0-否，1-是",
    "canProduce": 0,
    "canProduceComment": "0-否，1-是",
    "skuEnableStatus": 1,
    "skuEnableStatusComment": "0-禁用，1-启用",
    "listingStatus": 1,
    "listingStatusComment": "0-下架，1-上架"
  }
}
```

## 5. 商品SPU列表
- 请求地址：`GET /erp/v1/product/spu/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "spuCode": "SPU",
  "spuName": "测试商品",
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
      "spuId": 4001,
      "spuCode": "SPU-001",
      "spuName": "测试商品",
      "productType": "NORMAL",
      "productTypeComment": "商品类型，示例值仅作报文演示",
      "enableStatus": 1,
      "enableStatusComment": "0-禁用，1-启用"
    }
  ]
}
```

## 6. 商品SKU列表
- 请求地址：`GET /erp/v1/product/sku/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "skuCode": "SKU",
  "skuName": "默认规格",
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
      "skuId": 5001,
      "skuCode": "SKU-001",
      "skuName": "测试商品-默认规格",
      "mainBarcode": "690000000001",
      "enableStatus": 1,
      "enableStatusComment": "0-禁用，1-启用",
      "listingStatus": 1,
      "listingStatusComment": "0-下架，1-上架"
    }
  ]
}
```

## 7. 商品SPU+SKU列表
- 请求地址：`GET /erp/v1/product/spu-sku/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "spuCode": "SPU",
  "spuName": "测试商品",
  "skuCode": "SKU",
  "skuName": "默认规格",
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
      "spuId": 4001,
      "spuCode": "SPU-001",
      "spuName": "测试商品",
      "skuId": 5001,
      "skuCode": "SKU-001",
      "skuName": "测试商品-默认规格",
      "mainBarcode": "690000000001",
      "productType": "NORMAL",
      "productTypeComment": "商品类型，示例值仅作报文演示"
    }
  ]
}
```