

```text
xbb-erp-module-sales
└── src/main/java/com/xxx/is/sales
    ├── controller
    │   ├── admin
    │   │   ├── SalesOrderAdminController.java
    │   │   ├── SalesOutboundAdminController.java
    │   │   └── SalesReturnAdminController.java
    │   ├── mobile
    │   │   ├── SalesTodoMobileController.java
    │   │   └── SalesQueryMobileController.java
    │   └── open
    │       ├── OpenSalesOrderController.java
    │       └── OpenSalesStatusController.java
    ├── app
    │   ├── command
    │   │   ├── CreateSalesOrderCommand.java
    │   │   ├── SubmitSalesOrderCommand.java
    │   │   ├── SalesOutboundCommand.java
    │   │   └── CancelSalesOrderCommand.java
    │   ├── query
    │   │   ├── SalesOrderListQuery.java
    │   │   ├── SalesOrderDetailQuery.java
    │   │   ├── SalesDashboardQuery.java
    │   │   └── SalesOrderQueryService.java
    │   ├── service
    │   │   ├── SalesOrderAppService.java
    │   │   ├── SalesOutboundAppService.java
    │   │   └── SalesReturnAppService.java
    │   ├── dto
    │   │   ├── SalesOrderDTO.java
    │   │   ├── SalesOrderDetailDTO.java
    │   │   ├── SalesOutboundResultDTO.java
    │   │   └── SalesStatisticsDTO.java
    │   ├── assembler
    │   │   ├── SalesOrderCommandAssembler.java
    │   │   ├── SalesOrderDTOAssembler.java
    │   │   └── SalesOrderResponseAssembler.java
    │   └── extension
    │       ├── BeforeSalesOrderCreateProcessor.java
    │       ├── AfterSalesOrderCreateProcessor.java
    │       └── AfterSalesOutboundProcessor.java
    ├── domain
    │   ├── model
    │   │   ├── SalesOrder.java
    │   │   ├── SalesOrderItem.java
    │   │   ├── SalesReturnOrder.java
    │   │   └── SalesOutboundRecord.java
    │   ├── valueobject
    │   │   ├── SalesOrderNo.java
    │   │   ├── CustomerSnapshot.java
    │   │   ├── DeliveryAddress.java
    │   │   ├── MoneyAmount.java
    │   │   └── TaxRate.java
    │   ├── service
    │   │   ├── SalesOrderDomainService.java
    │   │   ├── SalesPricingDomainService.java
    │   │   └── SalesCreditCheckDomainService.java
    │   ├── policy
    │   │   ├── SalesPricingPolicy.java
    │   │   ├── SalesDiscountPolicy.java
    │   │   └── SalesDeliveryMatchPolicy.java
    │   ├── event
    │   │   ├── SalesOrderCreatedEvent.java
    │   │   ├── SalesOrderSubmittedEvent.java
    │   │   └── SalesOrderOutboundEvent.java
    │   ├── repository
    │   │   ├── SalesOrderRepository.java
    │   │   └── SalesReturnOrderRepository.java
    │   └── extension
    │       ├── SalesOrderCreateExtension.java
    │       ├── SalesSubmitValidateExtension.java
    │       └── SalesOutboundExtension.java
    ├── facade
    │   ├── dto
    │   │   ├── CreateSalesOrderFacadeCommand.java
    │   │   ├── SalesOutboundFacadeCommand.java
    │   │   ├── SalesOrderFacadeDTO.java
    │   │   └── SalesOutboundFacadeResult.java
    │   ├── SalesFacade.java
    │   └── impl
    │       └── SalesFacadeImpl.java
    └── infrastructure
        ├── persistence
        │   ├── po
        │   │   ├── SalesOrderPO.java
        │   │   ├── SalesOrderItemPO.java
        │   │   ├── SalesReturnOrderPO.java
        │   │   └── SalesOutboundRecordPO.java
        │   ├── mapper
        │   │   ├── SalesOrderMapper.java
        │   │   ├── SalesOrderItemMapper.java
        │   │   ├── SalesReturnOrderMapper.java
        │   │   └── SalesStatisticsMapper.java
        │   ├── repository
        │   │   ├── SalesOrderRepositoryImpl.java
        │   │   └── SalesReturnOrderRepositoryImpl.java
        │   └── convertor
        │       ├── SalesOrderConvertor.java
        │       └── SalesReturnOrderConvertor.java
        ├── cache
        │   ├── SalesOrderCacheRepository.java
        │   └── SalesPricingCacheService.java
        ├── integration
        │   ├── CustomerRemoteClient.java
        │   ├── ContractRemoteClient.java
        │   └── InvoiceRemoteClient.java
        └── mq
            ├── SalesOrderEventProducer.java
            └── SalesOrderChangeConsumer.java
```

### 细化 package 树说明

下面以 `xbb-erp-module-sales` 为例，对每一级 package 的推荐落位进一步展开，便于后续直接作为研发建包模板使用。

#### 1. `controller`

- 作用：协议接入层，负责接收不同来源的请求。
- 建议子包：`admin`、`mobile`、`open`。
- 建议类名：`SalesOrderAdminController`、`SalesTodoMobileController`、`OpenSalesOrderController`。
- 典型文件示例：
    - `controller/admin/SalesOrderAdminController.java`
    - `controller/mobile/SalesQueryMobileController.java`
    - `controller/open/OpenSalesStatusController.java`

#### 2. `app.command`

- 作用：定义写操作输入模型。
- 建议类名：`CreateSalesOrderCommand`、`SubmitSalesOrderCommand`、`SalesOutboundCommand`。
- 典型文件示例：
    - `app/command/CreateSalesOrderCommand.java`
    - `app/command/SalesOutboundCommand.java`

#### 3. `app.query`

- 作用：定义查询模型与查询服务。
- 建议类名：`SalesOrderListQuery`、`SalesOrderDetailQuery`、`SalesOrderQueryService`。
- 典型文件示例：
    - `app/query/SalesOrderListQuery.java`
    - `app/query/SalesOrderQueryService.java`

#### 4. `app.service`

- 作用：承担用例编排、事务边界、跨模块协作。
- 建议类名：`SalesOrderAppService`、`SalesOutboundAppService`、`SalesReturnAppService`。
- 典型文件示例：
    - `app/service/SalesOrderAppService.java`
    - `app/service/SalesOutboundAppService.java`

#### 5. `app.dto`

- 作用：承载应用层输出对象与聚合展示对象。
- 建议类名：`SalesOrderDTO`、`SalesOrderDetailDTO`、`SalesStatisticsDTO`。
- 典型文件示例：
    - `app/dto/SalesOrderDetailDTO.java`
    - `app/dto/SalesOutboundResultDTO.java`

#### 6. `app.assembler`

- 作用：负责请求对象、命令对象、DTO、响应对象之间的转换。
- 建议类名：`SalesOrderCommandAssembler`、`SalesOrderDTOAssembler`、`SalesOrderResponseAssembler`。
- 典型文件示例：
    - `app/assembler/SalesOrderCommandAssembler.java`
    - `app/assembler/SalesOrderResponseAssembler.java`

#### 7. `app.extension`

- 作用：定义应用层扩展点，承接行业增强或流程后处理。
- 建议类名：`BeforeSalesOrderCreateProcessor`、`AfterSalesOrderCreateProcessor`、`AfterSalesOutboundProcessor`。
- 典型文件示例：
    - `app/extension/BeforeSalesOrderCreateProcessor.java`
    - `app/extension/AfterSalesOutboundProcessor.java`

#### 8. `domain.model`

- 作用：定义聚合根、实体和核心业务对象。
- 建议类名：`SalesOrder`、`SalesOrderItem`、`SalesReturnOrder`、`SalesOutboundRecord`。
- 典型文件示例：
    - `domain/model/SalesOrder.java`
    - `domain/model/SalesOrderItem.java`

#### 9. `domain.valueobject`

- 作用：定义值对象，表达金额、地址、税率、客户快照等语义单元。
- 建议类名：`SalesOrderNo`、`CustomerSnapshot`、`MoneyAmount`、`TaxRate`。
- 典型文件示例：
    - `domain/valueobject/MoneyAmount.java`
    - `domain/valueobject/CustomerSnapshot.java`

#### 10. `domain.service`

- 作用：承载不适合放入单一聚合根的领域规则编排。
- 建议类名：`SalesOrderDomainService`、`SalesPricingDomainService`、`SalesCreditCheckDomainService`。
- 典型文件示例：
    - `domain/service/SalesOrderDomainService.java`
    - `domain/service/SalesPricingDomainService.java`

#### 11. `domain.policy`

- 作用：定义可替换的策略规则，例如定价、折扣、发货匹配。
- 建议类名：`SalesPricingPolicy`、`SalesDiscountPolicy`、`SalesDeliveryMatchPolicy`。
- 典型文件示例：
    - `domain/policy/SalesPricingPolicy.java`
    - `domain/policy/SalesDiscountPolicy.java`

#### 12. `domain.event`

- 作用：定义领域事件，表达关键业务事实。
- 建议类名：`SalesOrderCreatedEvent`、`SalesOrderSubmittedEvent`、`SalesOrderOutboundEvent`。
- 典型文件示例：
    - `domain/event/SalesOrderCreatedEvent.java`
    - `domain/event/SalesOrderOutboundEvent.java`

#### 13. `domain.repository`

- 作用：定义仓储接口，抽象领域对象的装载与保存能力。
- 建议类名：`SalesOrderRepository`、`SalesReturnOrderRepository`。
- 典型文件示例：
    - `domain/repository/SalesOrderRepository.java`
    - `domain/repository/SalesReturnOrderRepository.java`

#### 14. `domain.extension`

- 作用：定义领域规则扩展点，承接行业校验与行业特定规则。
- 建议类名：`SalesOrderCreateExtension`、`SalesSubmitValidateExtension`、`SalesOutboundExtension`。
- 典型文件示例：
    - `domain/extension/SalesSubmitValidateExtension.java`
    - `domain/extension/SalesOutboundExtension.java`

#### 15. `facade`

- 作用：提供本模块对外稳定能力入口。
- 建议类名：`SalesFacade`、`SalesFacadeImpl`。
- 典型文件示例：
    - `facade/SalesFacade.java`
    - `facade/impl/SalesFacadeImpl.java`

#### 16. `facade.dto`

- 作用：定义跨模块调用时使用的输入输出契约对象。
- 建议类名：`CreateSalesOrderFacadeCommand`、`SalesOutboundFacadeCommand`、`SalesOutboundFacadeResult`。
- 典型文件示例：
    - `facade/dto/SalesOutboundFacadeCommand.java`
    - `facade/dto/SalesOutboundFacadeResult.java`

#### 17. `infrastructure.persistence.po`

- 作用：定义数据库持久化对象。
- 建议类名：`SalesOrderPO`、`SalesOrderItemPO`、`SalesReturnOrderPO`。
- 典型文件示例：
    - `infrastructure/persistence/po/SalesOrderPO.java`
    - `infrastructure/persistence/po/SalesOrderItemPO.java`

#### 18. `infrastructure.persistence.mapper`

- 作用：定义 MyBatis Mapper 或等价持久化接口。
- 建议类名：`SalesOrderMapper`、`SalesOrderItemMapper`、`SalesStatisticsMapper`。
- 典型文件示例：
    - `infrastructure/persistence/mapper/SalesOrderMapper.java`
    - `infrastructure/persistence/mapper/SalesStatisticsMapper.java`

#### 19. `infrastructure.persistence.repository`

- 作用：实现领域仓储接口，连接 `domain.repository` 与 `mapper/po`。
- 建议类名：`SalesOrderRepositoryImpl`、`SalesReturnOrderRepositoryImpl`。
- 典型文件示例：
    - `infrastructure/persistence/repository/SalesOrderRepositoryImpl.java`
    - `infrastructure/persistence/repository/SalesReturnOrderRepositoryImpl.java`

#### 20. `infrastructure.persistence.convertor`

- 作用：实现领域对象与持久化对象之间的转换。
- 建议类名：`SalesOrderConvertor`、`SalesReturnOrderConvertor`。
- 典型文件示例：
    - `infrastructure/persistence/convertor/SalesOrderConvertor.java`
    - `infrastructure/persistence/convertor/SalesReturnOrderConvertor.java`

#### 21. `infrastructure.cache`

- 作用：承载缓存访问与缓存领域辅助实现。
- 建议类名：`SalesOrderCacheRepository`、`SalesPricingCacheService`。
- 典型文件示例：
    - `infrastructure/cache/SalesOrderCacheRepository.java`
    - `infrastructure/cache/SalesPricingCacheService.java`

#### 22. `infrastructure.integration`

- 作用：封装外部系统或其他非本模块自有协议的适配实现。
- 建议类名：`CustomerRemoteClient`、`ContractRemoteClient`、`InvoiceRemoteClient`。
- 典型文件示例：
    - `infrastructure/integration/CustomerRemoteClient.java`
    - `infrastructure/integration/InvoiceRemoteClient.java`

#### 23. `infrastructure.mq`

- 作用：封装消息发送与消息消费实现。
- 建议类名：`SalesOrderEventProducer`、`SalesOrderChangeConsumer`。
- 典型文件示例：
    - `infrastructure/mq/SalesOrderEventProducer.java`
    - `infrastructure/mq/SalesOrderChangeConsumer.java`

### 