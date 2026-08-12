# 客户新增表单初始化

## 文档信息
- 领域：`customer`
- 控制器：`CustomerAdminController#addItem`
- 请求方式：`POST /erp/v1/masterData/customer/addItem`
- 聚合文档引用：`docs/kn/customer-m.md`

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
{ "code": 0, "success": true, "data": { "headList": [{ "attr": "contacts", "attrName": "联系人", "fieldType": "49", "subField": [{ "attr": "name", "fieldType": "1" }, { "attr": "mobile", "fieldType": "1" }, { "attr": "defaultFlag", "fieldType": "19" }] }], "data": { "main": null, "contacts": [{ "name": null, "mobile": null, "defaultFlag": null }] } } }
```

## 响应参数说明

| 字段 | 是否必返 | 备注 |
| --- | --- | --- |
| `headList` | 是 | CREATE 场景的字段元数据 |
| `headList[].attr=contacts` | 是 | 真正的联系人子表字段，值与 `data.contacts` 对齐 |
| `data.contacts` | 是 | 联系人数组；默认返回一行空白行供用户填写 |

## 规则说明

- 联系人使用 `SUB_ITEM(49)` 子表协议，名称和手机号分别保存为同一行的 `name`、`mobile` 属性。
- `main.defaultContactId` 不再作为子表字段下发；正式保存后由选中的联系人行回写。
