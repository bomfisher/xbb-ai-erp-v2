## 领域计算规则
1. 领域前缀：取接口所在 module 项目名的业务后缀，例如 `xbb-erp-module-customer` -> `customer`
2. 动作标识：优先取 `/erp/v1/` 之后接口最后一级路径的稳定动作语义，例如 `/erp/v1/customer/saveAndSubmit` -> `save-and-submit`
3. 如果最后一级路径不足以区分接口，再结合请求方式、Controller 方法名或用户显式输入补充动作标识
4. 文件名：`<领域前缀>-<动作标识>.md`
5. 输出目录：`docs/api/endpoints/`
6. 一个接口只对应一个目标文件；禁止在一个文件内继续累积第二个接口正文
7. 如果领域前缀或动作标识无法稳定判断，必须输出待确认项并说明来源
