# 订单开票状态字段

执行事实源为 `xbb-erp-app-admin/src/main/resources/db/migration/V28__add_order_invoice_status.sql`。

销售订单 `sales_order` 与采购订单 `purchase_order` 均新增 `invoice_status`：

| 值 | 含义 |
| --- | --- |
| `0` | 未开票 |
| `1` | 部分开票 |
| `2` | 全部开票 |

销售订单按照来源订单行的非作废销售发票数量实时汇总，销售发票保存、删除、作废和红冲后同步更新。采购订单当前在新增时初始化为未开票，待采购发票模块接入后再按采购发票数量驱动状态变化。
