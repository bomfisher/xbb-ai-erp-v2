## 文档作用
当前文档用于快速定位项目内业务 `module` 的职责与代码落位，便于开发人员或 agent 在业务域内查找模块边界。

## 当前初始化范围
本期已初始化以下业务模块：

- `xbb-erp-module-customer`

当前仅完成 `customer` 模块最小范围骨架与持久化落位，覆盖：

- `customer`
- `customer_contact`
- `customer_address`

当前未纳入本次初始化范围：

- `xbb-erp-process-*`
- `xbb-erp-ext-*`
- 其余 `xbb-erp-module-*`

## 导航

### xbb-erp-module-*

#### xbb-erp-module-customer
- 功能定位：客户管理业务模块
- 责任范围：客户主数据、客户联系人、客户地址的领域模型、仓储接口、持久化映射与基础 CRUD 能力
- 当前表范围：`customer`、`customer_contact`、`customer_address`
- 当前代码落位：`domain/model`、`domain/repository`、`infrastructure/persistence/po`、`infrastructure/persistence/mapper`、`infrastructure/persistence/repository`
- 不负责什么：当前不承载附件、跨模块协同、聚合编排、应用层接口与前端交互适配
- 当前依赖与被谁依赖：依赖 `xbb-erp-base-common`、`xbb-erp-base-persistence`，供后续 `app` 层或其他业务编排按模块边界接入
