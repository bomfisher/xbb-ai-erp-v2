# 审批业务目录

## 适用范围

为审批流程设置页提供可选择的业务对象、支持场景和审批字段白名单。当前由 ERP 审批适配层提供；审批中台拆分后可由中台字段目录服务实现相同契约。

## 接口

`POST /erp/v1/approval/flow/catalog`

请求体继承 `BaseDTO`，传入 `corpid`、`userId`。

## 响应

```json
[
  {
    "businessCode": "SALES_ORDER",
    "businessName": "销售订单",
    "scenes": ["CREATE", "UPDATE"],
    "fields": [
      {
        "attr": "main.totalAmount",
        "name": "订单金额",
        "fieldType": "AMOUNT",
        "operators": ["EQ", "GT", "GE", "LT", "LE", "BETWEEN"]
      }
    ]
  }
]
```

`attr` 是业务表单字段路径，不是数据库列名。前端只能从 `fields` 中选择字段和操作符；服务端后续运行匹配也必须根据同一白名单解析，禁止接收 SQL、任意字段路径或自定义操作符。

首批业务对象为销售订单，开放客户、仓库、下单日期、交货日期、订单金额、对接编号和备注等表头字段。明细行、单据状态和审计字段不在首期审批条件范围内。
