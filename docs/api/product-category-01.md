# product-category 接口文档

## 文档说明
- 模块：`xbb-erp-module-product`
- 领域：`product-category`
- 当前文档覆盖：`category` / `brand` / `unit` 三组普通 CRUD facade
- 统一返回：`ResultVO.success(...)`
- 非脚本接口公共入参：`corpid`、`userId`
- 当前普通 CRUD 协议统一采用 `list / addItem / updateItem / save / detail / delete`
- 当前普通 CRUD 协议统一采用 `POST + @RequestBody`

## 1. 商品分类-新增骨架
- 请求地址：`POST /erp/v1/product/category/addItem`
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
    "headList": [],
    "data": {
      "main": {
        "id": null,
        "corpid": null,
        "categoryCode": null,
        "categoryName": null,
        "parentId": null,
        "categoryLevel": null,
        "sortNo": null,
        "enableStatus": null
      }
    }
  }
}
```

## 2. 商品分类-编辑骨架
- 请求地址：`POST /erp/v1/product/category/updateItem`
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
  "success": true,
  "data": {
    "headList": [],
    "data": {
      "main": {
        "id": 1001,
        "corpid": "10001",
        "categoryCode": "CAT-001",
        "categoryName": "原材料",
        "parentId": 0,
        "categoryLevel": 1,
        "sortNo": 10,
        "enableStatus": 1
      }
    }
  }
}
```

## 3. 商品分类-保存
- 请求地址：`POST /erp/v1/product/category/save`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "main": {
    "id": 1001,
    "categoryCode": "CAT-001",
    "categoryName": "原材料",
    "parentId": 0,
    "categoryLevel": 1,
    "sortNo": 10,
    "enableStatus": 1
  }
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 1001
}
```

## 4. 商品分类-删除
- 请求地址：`POST /erp/v1/product/category/delete`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "idList": [1001]
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

## 5. 商品分类-详情
- 请求地址：`POST /erp/v1/product/category/detail`
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
  "success": true,
  "data": {
    "headList": [],
    "mainData": {
      "main": {
        "id": 1001,
        "corpid": "10001",
        "categoryCode": "CAT-001",
        "categoryName": "原材料",
        "parentId": 0,
        "categoryLevel": 1,
        "sortNo": 10,
        "enableStatus": 1
      }
    }
  }
}
```

## 6. 商品分类-列表
- 请求地址：`POST /erp/v1/product/category/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "categoryCode": "CAT",
  "categoryName": "材料"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [],
    "list": [
      {
        "id": 1001,
        "categoryCode": "CAT-001",
        "categoryName": "原材料",
        "parentId": 0,
        "categoryLevel": 1,
        "sortNo": 10,
        "enableStatus": 1
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

## 7. 商品品牌-新增骨架
- 请求地址：`POST /erp/v1/product/brand/addItem`
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
    "headList": [],
    "data": {
      "main": {
        "id": null,
        "corpid": null,
        "brandCode": null,
        "brandName": null,
        "sortNo": null,
        "enableStatus": null
      }
    }
  }
}
```

## 8. 商品品牌-编辑骨架
- 请求地址：`POST /erp/v1/product/brand/updateItem`
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
  "success": true,
  "data": {
    "headList": [],
    "data": {
      "main": {
        "id": 2001,
        "corpid": "10001",
        "brandCode": "BR-001",
        "brandName": "默认品牌",
        "sortNo": 10,
        "enableStatus": 1
      }
    }
  }
}
```

## 9. 商品品牌-保存
- 请求地址：`POST /erp/v1/product/brand/save`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "main": {
    "id": 2001,
    "brandCode": "BR-001",
    "brandName": "默认品牌",
    "sortNo": 10,
    "enableStatus": 1
  }
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 2001
}
```

## 10. 商品品牌-删除
- 请求地址：`POST /erp/v1/product/brand/delete`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "idList": [2001]
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

## 11. 商品品牌-详情
- 请求地址：`POST /erp/v1/product/brand/detail`
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
  "success": true,
  "data": {
    "headList": [],
    "mainData": {
      "main": {
        "id": 2001,
        "corpid": "10001",
        "brandCode": "BR-001",
        "brandName": "默认品牌",
        "sortNo": 10,
        "enableStatus": 1
      }
    }
  }
}
```

## 12. 商品品牌-列表
- 请求地址：`POST /erp/v1/product/brand/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "brandCode": "BR",
  "brandName": "默认"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [],
    "list": [
      {
        "id": 2001,
        "brandCode": "BR-001",
        "brandName": "默认品牌",
        "sortNo": 10,
        "enableStatus": 1
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

## 13. 商品单位-新增骨架
- 请求地址：`POST /erp/v1/product/unit/addItem`
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
    "headList": [],
    "data": {
      "main": {
        "id": null,
        "corpid": null,
        "unitCode": null,
        "unitName": null,
        "precisionNum": null,
        "enableStatus": null
      }
    }
  }
}
```

## 14. 商品单位-编辑骨架
- 请求地址：`POST /erp/v1/product/unit/updateItem`
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
  "success": true,
  "data": {
    "headList": [],
    "data": {
      "main": {
        "id": 3001,
        "corpid": "10001",
        "unitCode": "UNIT-001",
        "unitName": "个",
        "precisionNum": 2,
        "enableStatus": 1
      }
    }
  }
}
```

## 15. 商品单位-保存
- 请求地址：`POST /erp/v1/product/unit/save`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "main": {
    "id": 3001,
    "unitCode": "UNIT-001",
    "unitName": "个",
    "precisionNum": 2,
    "enableStatus": 1
  }
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": 3001
}
```

## 16. 商品单位-删除
- 请求地址：`POST /erp/v1/product/unit/delete`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "idList": [3001]
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

## 17. 商品单位-详情
- 请求地址：`POST /erp/v1/product/unit/detail`
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
  "success": true,
  "data": {
    "headList": [],
    "mainData": {
      "main": {
        "id": 3001,
        "corpid": "10001",
        "unitCode": "UNIT-001",
        "unitName": "个",
        "precisionNum": 2,
        "enableStatus": 1
      }
    }
  }
}
```

## 18. 商品单位-列表
- 请求地址：`POST /erp/v1/product/unit/list`
- 请求 JSON
```json
{
  "corpid": "10001",
  "userId": "EMP0001",
  "offset": 0,
  "pageSize": 10,
  "unitCode": "UNIT",
  "unitName": "个"
}
```
- 响应 JSON
```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [],
    "list": [
      {
        "id": 3001,
        "unitCode": "UNIT-001",
        "unitName": "个",
        "precisionNum": 2,
        "enableStatus": 1
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

## 规则说明
- `list` 当前返回静态 `headList` + 分页列表。
- `addItem` 当前返回带 `headList` 的保存页空骨架。
- `updateItem` 当前按 `id` 真实回填单条主档保存页数据。
- `save` 当前统一使用 `main` 对象入参；`main.id` 为空时新增，不为空时更新。
- `detail` 当前返回 `headList + mainData` 结构。
- `delete` 当前统一使用 `BatchBaseDTO.idList` 批量删除；删除成功返回 `null`。
