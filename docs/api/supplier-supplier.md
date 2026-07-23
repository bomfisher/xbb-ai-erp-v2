# 供应商模块接口

## `/erp/v1/supplier/list`

- 请求方式：`POST`
- 入参：`VendorListDTO`
- 返回：`ListBaseVO<VendorListItemVO>`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1,
  "vendorCode": "V0001",
  "vendorName": "杭州供应商A",
  "vendorShortName": "供应商A",
  "vendorCategory": "material",
  "mainBusinessCategory": "steel",
  "ownerPurchaserId": "buyer-001",
  "bizStatus": "enabled",
  "refStatus": "0",
  "pageNum": 1,
  "pageSize": 20,
  "offset": 0,
  "groupByStr": "vendor_category",
  "orderByStr": "id desc"
}
```

### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "id": "供应商ID，可选",
  "vendorCode": "供应商编码，可选",
  "vendorName": "供应商名称，可选，支持模糊查询",
  "vendorShortName": "供应商简称，可选",
  "vendorCategory": "供应商分类，可选",
  "mainBusinessCategory": "主营类目，可选",
  "ownerPurchaserId": "负责人采购员ID，可选",
  "bizStatus": "业务状态，可选",
  "refStatus": "引用状态，可选，0=未引用，1=已引用",
  "pageNum": "页码，可选，默认1",
  "pageSize": "每页条数，可选",
  "offset": "起始行，可选",
  "groupByStr": "分组字段，可选，直接透传到 group by",
  "orderByStr": "排序字段，可选，直接透传到 order by"
}
```

### 响应示例

```json
{
  "headList": null,
  "list": [
    {
      "id": 1,
      "vendorCode": "V0001",
      "vendorName": "杭州供应商A",
      "vendorShortName": "供应商A",
      "vendorCategory": "material",
      "mainBusinessCategory": "steel",
      "ownerPurchaserNameSnapshot": "张三",
      "bizStatus": "enabled",
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
```

## `/erp/v1/supplier/addItem`

- 请求方式：`POST`
- 入参：`BaseDTO`
- 返回：`SaveItemVO<VendorSaveItemVO>`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001"
}
```

### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": null
  }
}
```

## `/erp/v1/supplier/updateItem`

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`SaveItemVO<VendorSaveItemVO>`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

### 响应示例

```json
{
  "headList": null,
  "data": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "vendorCode": "V0001",
      "vendorName": "杭州供应商A",
      "vendorShortName": "供应商A",
      "vendorCategory": "material",
      "mainBusinessCategory": "steel",
      "ownerPurchaserId": "buyer-001",
      "ownerPurchaserNameSnapshot": "张三",
      "bizStatus": "enabled",
      "refStatus": "0",
      "defaultContactId": 11,
      "defaultAddressId": 21,
      "defaultBankAccountId": 31,
      "defaultInvoiceProfileId": 41,
      "remark": "核心供应商",
      "creatorId": "u-001",
      "modifyId": "u-002",
      "version": 1,
      "deleted": 0,
      "addTime": 1721606400000,
      "updateTime": 1721692800000
    }
  }
}
```

## `/erp/v1/supplier/save`

- 请求方式：`POST`
- 入参：`VendorSaveDTO`
- 返回：`Long`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "main": {
    "id": 1,
    "corpid": "corp-001",
    "vendorCode": "V0001",
    "vendorName": "杭州供应商A",
    "vendorShortName": "供应商A",
    "vendorCategory": "material",
    "mainBusinessCategory": "steel",
    "ownerPurchaserId": "buyer-001",
    "ownerPurchaserNameSnapshot": "张三",
    "bizStatus": "enabled",
    "refStatus": "0",
    "defaultContactId": 11,
    "defaultAddressId": 21,
    "defaultBankAccountId": 31,
    "defaultInvoiceProfileId": 41,
    "remark": "核心供应商",
    "creatorId": "u-001",
    "modifyId": "u-001",
    "version": 1,
    "deleted": 0,
    "addTime": 1721606400000,
    "updateTime": 1721606400000
  }
}
```

### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "main": {
    "id": "供应商ID，新增时可不传，修改时传已有ID",
    "corpid": "企业ID，可选，最终以后端顶层 corpid 为准",
    "vendorCode": "供应商编码",
    "vendorName": "供应商名称",
    "vendorShortName": "供应商简称",
    "vendorCategory": "供应商分类",
    "mainBusinessCategory": "主营类目",
    "ownerPurchaserId": "负责人采购员ID",
    "ownerPurchaserNameSnapshot": "负责人采购员名称快照",
    "bizStatus": "业务状态",
    "refStatus": "引用状态，0=未引用，1=已引用",
    "defaultContactId": "默认联系人ID",
    "defaultAddressId": "默认地址ID",
    "defaultBankAccountId": "默认银行账户ID",
    "defaultInvoiceProfileId": "默认开票资料ID",
    "remark": "备注",
    "creatorId": "创建人ID",
    "modifyId": "修改人ID",
    "version": "版本号",
    "deleted": "删除标记，0=未删除，1=已删除",
    "addTime": "创建时间戳",
    "updateTime": "更新时间戳"
  }
}
```

### 响应示例

```json
1
```

## `/erp/v1/supplier/detail`

- 请求方式：`POST`
- 入参：`IdBaseDTO`
- 返回：`VendorDetailVO`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "id": 1
}
```

### 响应示例

```json
{
  "mainData": {
    "main": {
      "id": 1,
      "corpid": "corp-001",
      "vendorCode": "V0001",
      "vendorName": "杭州供应商A",
      "vendorShortName": "供应商A",
      "vendorCategory": "material",
      "mainBusinessCategory": "steel",
      "ownerPurchaserId": "buyer-001",
      "ownerPurchaserNameSnapshot": "张三",
      "bizStatus": "enabled",
      "refStatus": "0",
      "defaultContactId": 11,
      "defaultAddressId": 21,
      "defaultBankAccountId": 31,
      "defaultInvoiceProfileId": 41,
      "remark": "核心供应商",
      "creatorId": "u-001",
      "modifyId": "u-002",
      "version": 1,
      "deleted": 0,
      "addTime": 1721606400000,
      "updateTime": 1721692800000
    }
  }
}
```

## `/erp/v1/supplier/delete`

- 请求方式：`POST`
- 入参：`BatchBaseDTO`
- 返回：`void`

### 请求示例

```json
{
  "corpid": "corp-001",
  "userId": "u-001",
  "idList": [1, 2]
}
```

### 入参说明

```json
{
  "corpid": "企业ID，必填",
  "userId": "操作人ID，必填，字符串",
  "idList": "待删除供应商ID列表"
}
```

### 响应

```json
null
```
