## 领域计算规则
1、获得领域前缀：接口所在的module项目的后缀，例如xbb-erp-module-sales，也就是sales
2、获得领域后缀：接口url移除项目url固定前缀后的第一级。例如/erp/v1/order/info  /erp/v1是固定前缀 移除后 获得 user
3、拼接领域前后缀 用 `-`连接。例如 sales-order