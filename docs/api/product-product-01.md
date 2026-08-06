# product-product 接口文档

## 文档说明
- 模块：`xbb-erp-module-product`
- 领域：`product-product`
- 当前文档按 `ProductAdminController` 维护。
- 当前共 9 个接口，未达到单文档 20 个接口上限。
- 统一返回：`ResultVO.success(...)`
- 当前商品聚合读取模型：`SPU + SKU[]`
- 当前商品草稿链路已接入真实 Redis 持久化，文档按当前代码真实行为维护。

---

## 接口名称

商品列表查询

## 请求方式

`POST /erp/v1/product/list`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "keyword": "测试商品",
  "page": 1,
  "pageSize": 20
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `keyword` | 否 | 商品关键字 |
| `page` | 否 | 页码 |
| `pageSize` | 否 | 每页条数 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [],
    "list": [
      {
        "spuId": 101,
        "spuCode": "SPU-101",
        "spuName": "测试商品",
        "productType": "NORMAL",
        "spuEnableStatus": 1
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
| `code` | 是 | 响应码 |
| `message` | 是 | 响应说明 |
| `success` | 是 | 是否成功 |
| `data` | 是 | 响应主体 |
| `data.headList` | 是 | 表头定义，当前返回商品保存页字段头 |
| `data.list` | 是 | 商品列表 |
| `data.list[].spuId` | 是 | 商品主档 ID |
| `data.list[].spuCode` | 否 | 商品主档编码 |
| `data.list[].spuName` | 否 | 商品主档名称 |
| `data.list[].productType` | 否 | 商品类型 |
| `data.list[].spuEnableStatus` | 否 | 主档启用状态 |
| `data.pageHelper` | 是 | 分页信息 |
| `data.pageHelper.page` | 是 | 当前页 |
| `data.pageHelper.count` | 是 | 统计值 |
| `data.pageHelper.hasLeft` | 是 | 是否有上一页 |
| `data.pageHelper.hasRight` | 是 | 是否有下一页 |

## 规则说明

- 当前由 `ProductQueryAppServiceImpl` 提供查询能力。
- 当前支持 `keyword`、`page`、`pageSize` 入参。
- 返回列表按 `SPU` 维度聚合。

---

## 接口名称

商品明细新增行骨架

## 请求方式

`POST /erp/v1/product/addItem`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [
      {
        "attr": "main.spuCode",
        "attrName": "商品编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1,
        "itemList": []
      },
      {
        "attr": "main.enableSpec",
        "attrName": "启用规格",
        "fieldType": "19",
        "required": 1,
        "editable": 1,
        "itemList": [
          { "value": "1", "text": "启用" },
          { "value": "0", "text": "不启用" }
        ]
      },
      {
        "attr": "skus.skuCode",
        "attrName": "SKU编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1,
        "itemList": []
      }
    ],
    "data": {
      "main": {
        "spuId": null,
        "spuCode": null,
        "spuName": null,
        "categoryId": null,
        "brandId": null,
        "productType": null,
        "enableSpec": 0,
        "description": null,
        "imageUrl": null,
        "spuEnableStatus": 1
      },
      "skus": []
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.headList` | 是 | 表头定义，当前返回商品保存页字段头，包含 `required`、`editable`、`fieldType`、`itemList` 等元数据 |
| `data.data` | 是 | 商品保存页骨架 |
| `data.data.main` | 是 | 商品主档骨架 |
| `data.data.main.enableSpec` | 是 | 是否启用规格，默认 `0`，表示默认单规格模式 |
| `data.data.main.spuEnableStatus` | 是 | 主档启用状态，默认 `1` |
| `data.data.skus` | 是 | SKU 行列表骨架，当前默认空列表 |

## 规则说明

- 当前返回 `SaveItemVO<ProductSaveItemVO>`，保存页骨架位于 `data.data`。
- `headList` 会返回商品保存页必填字段定义，前端据此渲染必填标记与控件类型。
- `main.enableSpec` 当前使用开关字段类型 `19`，并返回“启用/不启用”选项。
- 默认 `enableSpec = 0`，前端按单规格模式初始化。

---

## 接口名称

商品明细编辑行骨架

## 请求方式

`POST /erp/v1/product/updateItem`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "id": 1001
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `id` | 是 | 行 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "headList": [
      {
        "attr": "main.spuCode",
        "attrName": "商品编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1,
        "itemList": []
      },
      {
        "attr": "main.enableSpec",
        "attrName": "启用规格",
        "fieldType": "19",
        "required": 1,
        "editable": 1,
        "itemList": [
          { "value": "1", "text": "启用" },
          { "value": "0", "text": "不启用" }
        ]
      },
      {
        "attr": "skus.skuCode",
        "attrName": "SKU编码",
        "fieldType": "input",
        "required": 1,
        "editable": 1,
        "itemList": []
      }
    ],
    "data": {
      "main": {
        "spuId": 1001,
        "spuCode": "SPU-1001",
        "spuName": "测试商品",
        "categoryId": null,
        "brandId": null,
        "productType": "NORMAL",
        "enableSpec": 0,
        "description": null,
        "imageUrl": null,
        "spuEnableStatus": 1
      },
      "skus": [
        {
          "skuId": 2001,
          "skuCode": "SKU-2001",
          "skuName": "默认规格",
          "specSignature": "SPEC-001",
          "specSnapshot": "SPEC-001"
        }
      ]
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.headList` | 是 | 表头定义，当前返回商品保存页字段头，包含 `required`、`editable`、`fieldType`、`itemList` 等元数据 |
| `data.data` | 是 | 商品保存页数据 |
| `data.data.main` | 是 | 商品主档数据 |
| `data.data.main.enableSpec` | 是 | 是否启用规格，`0` 为单规格，`1` 为多规格 |
| `data.data.skus` | 是 | SKU 行列表 |

## 规则说明

- 当前返回 `SaveItemVO<ProductSaveItemVO>`，保存页数据位于 `data.data`。
- 当前会按 `id` 真实回填商品主档与 SKU 列表，并附带商品保存页字段头 `headList`。
- `headList` 会返回商品保存页必填字段定义，前端据此渲染必填标记与控件类型。
- `main.enableSpec` 当前使用开关字段类型 `19`，并返回“启用/不启用”选项。

---

## 接口名称

商品草稿保存

## 请求方式

`POST /erp/v1/product/saveDraft`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "draftMeta": {
    "draftId": 1,
    "draftCode": "DRAFT-001",
    "draftName": "测试草稿"
  },
  "main": {
    "spuId": 101,
    "spuCode": "SPU-101",
    "spuName": "测试商品",
    "categoryId": 10,
    "brandId": 20,
    "productType": "NORMAL",
    "enableSpec": 1,
    "description": "测试描述",
    "imageUrl": "https://example.com/p.png",
    "spuEnableStatus": 1
  },
  "skus": [
    {
      "skuId": 1001,
      "skuCode": "SKU-1001",
      "skuName": "红色款",
      "specSignature": "color:red",
      "specSnapshot": "{\"color\":\"red\"}",
      "mnemonicCode": "HSK",
      "mainBarcode": "690000000001",
      "canPurchase": 1,
      "canSale": 1,
      "canInventory": 1,
      "canProduce": 0,
      "skuEnableStatus": 1,
      "listingStatus": 1
    }
  ]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `draftMeta` | 否 | 草稿元信息 |
| `draftMeta.draftId` | 否 | 草稿 ID |
| `draftMeta.draftCode` | 否 | 草稿编码 |
| `draftMeta.draftName` | 否 | 草稿名称 |
| `main` | 否 | 商品主档 |
| `main.spuId` | 否 | 商品主档 ID |
| `main.spuCode` | 否 | 商品主档编码 |
| `main.spuName` | 否 | 商品主档名称 |
| `main.categoryId` | 否 | 分类 ID |
| `main.brandId` | 否 | 品牌 ID |
| `main.productType` | 否 | 商品类型 |
| `main.enableSpec` | 否 | 是否启用规格，`0/1` |
| `main.description` | 否 | 商品描述 |
| `main.imageUrl` | 否 | 图片地址 |
| `main.spuEnableStatus` | 否 | 主档启用状态 |
| `skus` | 否 | SKU 列表 |
| `skus[].skuId` | 否 | SKU ID |
| `skus[].skuCode` | 否 | SKU 编码 |
| `skus[].skuName` | 否 | SKU 名称 |
| `skus[].specSignature` | 否 | 规格签名 |
| `skus[].specSnapshot` | 否 | 规格快照 |
| `skus[].mnemonicCode` | 否 | 助记码 |
| `skus[].mainBarcode` | 否 | 主条码 |
| `skus[].canPurchase` | 否 | 是否可采购，`0/1` |
| `skus[].canSale` | 否 | 是否可销售，`0/1` |
| `skus[].canInventory` | 否 | 是否可库存，`0/1` |
| `skus[].canProduce` | 否 | 是否可生产，`0/1` |
| `skus[].skuEnableStatus` | 否 | SKU 启用状态 |
| `skus[].listingStatus` | 否 | 上下架状态 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "draftId": null
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.draftId` | 否 | 草稿 ID |

## 规则说明

- 当前已接入真实草稿持久化，保存对象为 `SPU + SKU[]` 聚合。
- 提交前会校验：`main` 不能为空、`skus` 不能为空。
- 当 `enableSpec = 0` 时，仅允许 1 条 SKU。
- 草稿存储于 Redis，按企业最多保留最近 10 条，TTL 为 7 天。
- 返回 `draftId` 用于后续 `loadDraft` 加载。

---

## 接口名称

商品保存并提交

## 请求方式

`POST /erp/v1/product/saveAndSubmit`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "draftMeta": {
    "draftId": 1,
    "draftCode": "DRAFT-001",
    "draftName": "测试草稿"
  },
  "main": {
    "spuId": 101,
    "spuCode": "SPU-101",
    "spuName": "测试商品",
    "categoryId": 10,
    "brandId": 20,
    "productType": "NORMAL",
    "enableSpec": 1,
    "description": "测试描述",
    "imageUrl": "https://example.com/p.png",
    "spuEnableStatus": 1
  },
  "skus": [
    {
      "skuId": 1001,
      "skuCode": "SKU-1001",
      "skuName": "红色款",
      "specSignature": "color:red",
      "specSnapshot": "{\"color\":\"red\"}",
      "mnemonicCode": "HSK",
      "mainBarcode": "690000000001",
      "canPurchase": 1,
      "canSale": 1,
      "canInventory": 1,
      "canProduce": 0,
      "skuEnableStatus": 1,
      "listingStatus": 1
    }
  ]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `draftMeta` | 否 | 草稿元信息 |
| `main` | 否 | 商品主档 |
| `skus` | 否 | SKU 列表 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "ok": 1
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.ok` | 是 | 成功标记，固定初始化为 `1` |

## 规则说明

- 当前已接入真实保存链路，提交时按 `SPU + SKU[]` 聚合执行保存。
- 提交前会校验：`main` 不能为空、`skus` 不能为空。
- 当 `enableSpec = 0` 时，仅允许 1 条 SKU。
- 同一商品下 `skuCode` 不允许重复。
- 同一商品下 `specSignature` 不允许重复。
- 保存时会按 `spuId` 同步 SKU：缺失旧 SKU 删除，带 `skuId` 的行更新，无 `skuId` 的行新增。
- 当请求携带 `draftMeta.draftCode` 时，保存成功后会清理对应草稿。

---

## 接口名称

商品草稿列表查询

## 请求方式

`POST /erp/v1/product/draftList`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "keyword": "测试"
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `keyword` | 否 | 草稿关键字 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": []
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data` | 是 | 草稿列表 |
| `data[].draftId` | 否 | 草稿 ID |
| `data[].draftCode` | 否 | 草稿编码 |
| `data[].draftName` | 否 | 草稿名称 |

## 规则说明

- 当前已返回企业最近草稿列表，最多 10 条。
- 若传入 `keyword`，按 `draftCode`、`draftName`、`spuCode`、`spuName` 任一包含即命中。
- `corpid` 为空时会抛业务异常。

---

## 接口名称

商品草稿加载

## 请求方式

`POST /erp/v1/product/loadDraft`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "draftId": 1
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `draftId` | 是 | 草稿 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "draftMeta": {
      "draftId": 1,
      "draftCode": "DRAFT-001",
      "draftName": "测试草稿"
    },
    "detail": {
      "mainData": {
        "main": {
          "spuId": 101,
          "spuCode": "SPU-101",
          "spuName": "测试商品",
          "categoryId": 10,
          "brandId": 20,
          "productType": "NORMAL",
          "enableSpec": 1,
          "description": "测试描述",
          "imageUrl": "https://example.com/p.png",
          "spuEnableStatus": 1
        },
        "skus": [
          {
            "skuId": 1001,
            "skuCode": "SKU-1001",
            "skuName": "红色款",
            "specSignature": "color:red",
            "specSnapshot": "{\"color\":\"red\"}"
          }
        ]
      }
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.draftMeta` | 否 | 草稿元信息 |
| `data.draftMeta.draftId` | 否 | 草稿 ID |
| `data.draftMeta.draftCode` | 否 | 草稿编码 |
| `data.draftMeta.draftName` | 否 | 草稿名称 |
| `data.detail` | 否 | 草稿对应商品明细 |
| `data.detail.mainData` | 否 | 商品保存页数据 |
| `data.detail.mainData.main` | 否 | 商品主档 |
| `data.detail.mainData.skus` | 否 | SKU 列表 |

## 规则说明

- 当前按 `draftId` 真实加载草稿详情，并还原到 `data.detail.mainData.main + data.detail.mainData.skus`。
- `corpid` 为空或 `draftId` 为空时会抛业务异常。

---

## 接口名称

商品明细查询

## 请求方式

`POST /erp/v1/product/detail`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "id": 101
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `id` | 是 | 商品主档 ID |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "mainData": {
      "main": {
        "spuId": 101,
        "spuCode": "SPU-101",
        "spuName": "测试商品",
        "categoryId": null,
        "brandId": null,
        "productType": "NORMAL",
        "enableSpec": 1,
        "description": null,
        "imageUrl": null,
        "spuEnableStatus": 1
      },
      "skus": [
        {
          "skuId": 1001,
          "skuCode": "SKU-1",
          "skuName": "红色款",
          "specSignature": "color:red",
          "specSnapshot": "{\"color\":\"red\"}"
        },
        {
          "skuId": 1002,
          "skuCode": "SKU-2",
          "skuName": "蓝色款",
          "specSignature": "color:blue",
          "specSnapshot": "{\"color\":\"blue\"}"
        }
      ]
    }
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.mainData` | 是 | 商品保存页数据 |
| `data.mainData.main` | 否 | 商品主档 |
| `data.mainData.main.spuId` | 否 | 商品主档 ID |
| `data.mainData.main.spuCode` | 否 | 商品主档编码 |
| `data.mainData.main.spuName` | 否 | 商品主档名称 |
| `data.mainData.main.categoryId` | 否 | 分类 ID |
| `data.mainData.main.brandId` | 否 | 品牌 ID |
| `data.mainData.main.productType` | 否 | 商品类型 |
| `data.mainData.main.enableSpec` | 否 | 是否启用规格 |
| `data.mainData.main.description` | 否 | 商品描述 |
| `data.mainData.main.imageUrl` | 否 | 图片地址 |
| `data.mainData.main.spuEnableStatus` | 否 | 主档启用状态 |
| `data.mainData.skus` | 是 | SKU 列表 |
| `data.mainData.skus[].skuId` | 否 | SKU ID |
| `data.mainData.skus[].skuCode` | 否 | SKU 编码 |
| `data.mainData.skus[].skuName` | 否 | SKU 名称 |
| `data.mainData.skus[].specSignature` | 否 | 规格签名 |
| `data.mainData.skus[].specSnapshot` | 否 | 规格快照 |

## 规则说明

- 当前按 `ProductDetailVO.mainData` 返回商品明细。
- 明细读取由 `ProductQueryAppServiceImpl` 聚合主档与多条 SKU。

---

## 接口名称

商品删除

## 请求方式

`POST /erp/v1/product/delete`

## 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "EMP-001",
  "idList": [101]
}
```

## 参数说明

| 字段 | 是否必填 | 备注 |
| --- | --- | --- |
| `corpid` | 是 | 企业 ID |
| `userId` | 是 | 员工 ID |
| `idList` | 是 | 商品主档 ID 列表 |

## 响应示例

```json
{
  "code": 0,
  "message": "success",
  "success": true,
  "data": {
    "ok": 1
  }
}
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `data.ok` | 是 | 成功标记，固定初始化为 `1` |

## 规则说明

- 当前先按 `spuId` 删除关联 SKU，再删除 SPU。
- 当 `idList` 为空时，当前实现会抛出业务异常。
