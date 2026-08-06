# 客户模块 DDD 收敛与可复用框架化改造设计文档

## 1. 文档信息

| 项目 | 内容 |
|---|---|
| 文档名称 | 客户模块 DDD 收敛与可复用框架化改造设计文档 |
| 所属系统 | 进销存系统 |
| 所属模块 | 客户模块 / 公共列表模块 |
| 关联接口 | `CustomerAdminController` `ListCommonController` |
| 对应实现 | `xbb-erp-module-customer` `xbb-erp-module-common` |
| 版本号 | V1.0 |
| 创建日期 | 2026-07-29 |
| 最后更新日期 | 2026-07-29 |

## 2. 背景与目标

当前客户模块与公共列表模块已经形成“业务接口 + 公共元数据接口”的初步分层，但仍存在以下结构性问题：

- `CustomerAdminController` 下游集中依赖单一 `CustomerAdminAppServiceImpl`，列表、详情、草稿、保存、删除、字段装配、业务校验混在同一个实现中
- `ListCommonController` 只承接列表筛选、表头、按钮、行操作元数据，不承接列表数据查询协议，前后端需要同时维护公共元数据协议与业务列表查询协议
- 写侧业务校验复用了列表过滤 DSL，导致保存逻辑依赖列表元数据定义
- domain repository 暴露 `Map<String, Object>` 条件查询，语义弱，难以作为稳定的业务接缝
- `domain -> application.pojo`、`admin.vo -> application.pojo` 之类的反向依赖已经出现，DDD 分层不稳
- 无返回接口、异常类型、DTO/VO 契约尚未完全统一，不利于后续模块按样板接入

本次设计目标不是一次性把客户模块做成“最完美的 DDD 实现”，而是把客户模块改造成**首个标准接入样板模块**，并同步把公共列表能力升级为**可复用的平台协议骨架**。

本次目标按优先级排序如下：

1. 让后续新业务模块可以更快接入，避免复制 `CustomerAdminAppServiceImpl` 式的大类实现
2. 让 `xbb-erp-module-common` 不再只是“半套列表元数据服务”，而是形成稳定的平台协议入口
3. 在不做大爆炸重构的前提下，收敛客户模块的 DDD 分层边界
4. 保留合理的过渡层，允许新旧结构短期并存，但禁止继续扩大旧结构负债

## 3. 非目标

本次设计明确不追求以下事项：

- 不一次性重命名所有 DTO / VO / Pojo
- 不一次性平台化所有草稿协议与详情协议
- 不一次性重写全部 assembler / validator / repository
- 不要求第一轮就让多个业务模块同时迁移到新协议
- 不把客户业务特有规则强行抽到 common 中

## 4. 设计结论

### 4.1 总体结论

本次改造采用“双轨但分阶段收敛”的方案：

- 先定义平台层与业务层的稳定接缝
- 再把客户模块改造成第一个按新接缝运行的样板模块
- 最后再由第二个业务模块验证抽象是否稳定

### 4.2 架构结论

未来目标结构应从：

```text
Controller -> 巨型 AppService -> Validator / Provider / Repository / Assembler
```

收敛为：

```text
Controller -> Facade -> UseCase -> Domain / Repository / Platform Adapter
```

其中：

- `Facade` 面向 controller，仅负责出口聚合与转发
- `UseCase` 负责单一业务用例的编排
- `Platform Adapter / Provider` 负责列表与表单等平台协议接入
- `Domain` 只承接本业务真正稳定的领域规则与仓储语义

### 4.3 平台化结论

公共层不再定位为“通用列表 controller + 元数据 service”，而是升级为“列表 / 表单平台协议层”，至少覆盖：

- 列表 schema 协议
- 列表查询协议
- 列表分页返回协议
- 表单 schema 协议
- 表单数据加载协议
- 提交 / 草稿的稳定接入点
- `businessCode` 到 provider / use case 的注册与发现机制

### 4.4 客户模块结论

客户模块不再作为“首个做出来的业务模块”继续堆需求，而是收敛成：

- 一个对 controller 稳定的 facade
- 一组按业务能力拆分的 use case
- 一组按平台协议接入的 schema provider / query adapter
- 一组只保留客户特有规则的 domain service / repository

## 5. 目标架构

### 5.1 分层职责

#### admin 层

保留：

- controller
- DTO
- VO

职责：

- 接收入参
- 返回 `ResultVO.success()` 包装结果
- 不承载业务规则
- 不直接编排领域逻辑

#### application.facade 层

职责：

- 作为 controller 唯一依赖入口
- 聚合多个 use case 暴露统一出口
- 不承载复杂业务逻辑
- 不直接包含大段保存 / 查询 / 草稿实现

#### application.usecase 层

职责：

- 一个类只负责一个业务用例
- 编排 repository、domain service、schema provider、adapter
- 明确区分读写链路
- 明确区分 schema 链路与 data 链路

#### application.schema / adapter 层

职责：

- 对接平台协议
- 提供列表 schema、表单 schema
- 提供查询条件协议转换
- 提供表单数据加载与回填转换

#### domain 层

职责：

- 保留客户真正独有的业务规则
- 保留明确语义的 repository interface
- 不依赖 application.pojo
- 不依赖平台层的元数据协议

#### infrastructure 层

职责：

- MyBatis / Redis / mapper / PO 转换 / repository impl
- 允许保留 `Map`、SQL 片段等技术细节
- 但这些细节不能继续上浮到 domain 接口

### 5.2 目标调用链

#### 列表查询链

```text
CustomerAdminController#list
  -> CustomerAdminFacade#list
    -> CustomerListQueryUseCase
      -> CustomerListQueryAdapter
      -> CustomerRepository
      -> CustomerListSchemaProvider（仅在需要列表条件解释时参与）
```

#### 表单 schema 链

```text
CustomerAdminController#addItem / updateItem
  -> CustomerAdminFacade#formSchema / formData
    -> CustomerFormSchemaUseCase
      -> CustomerFormSchemaProvider
      -> CustomerFormDataLoader
```

#### 提交保存链

```text
CustomerAdminController#saveAndSubmit
  -> CustomerAdminFacade#submit
    -> CustomerSubmitUseCase
      -> Protocol Validator
      -> Common Validator
      -> Domain Service / Repository
```

#### 草稿链

```text
CustomerAdminController#saveDraft / draftList / loadDraft
  -> CustomerAdminFacade#draft*
    -> Draft UseCase
      -> Draft Repository
```

## 6. 平台层与业务层边界

### 6.1 放入平台层的内容

以下能力适合沉淀为复用框架：

- `businessCode` 路由
- 列表 schema 返回协议
- 列表查询基础 DTO 与分页 VO
- 表单 schema 返回协议
- 通用字段描述模型
- 通用按钮 / 行操作协议
- 通用查询条件 DSL
- provider / adapter / use case 的注册机制

### 6.2 留在业务模块的内容

以下能力保留在客户模块，不放入 common：

- 客户字段具体定义
- 客户字段值域来源
- 客户编码唯一性
- 默认联系人 / 默认地址 / 默认开票主体规则
- 客户主档与子表同步规则
- 草稿数据结构的业务细节
- 详情页特有附加信息

### 6.3 明确禁止的反向依赖

本次改造必须切断以下依赖方向：

- `domain -> application.pojo`
- `admin.vo -> application.pojo`
- `save validator -> list schema provider`
- `common -> customer-specific semantics`

## 7. 目录与类蓝图

### 7.1 客户模块建议目录

建议把 `xbb-erp-module-customer` 收敛为：

```text
customer/
  admin/
    CustomerAdminController
    dto/
    vo/
  application/
    facade/
      CustomerAdminFacade
      CustomerAdminFacadeImpl
    usecase/
      list/
      detail/
      form/
      draft/
      submit/
      delete/
    schema/
      CustomerListSchemaProvider
      CustomerFormSchemaProvider
      CustomerListQueryAdapter
      CustomerFormDataLoader
  domain/
    model/
    repository/
    service/
    rule/
  infrastructure/
    persistence/
    draft/
    convert/
```

### 7.2 建议拆分的 use case

最少拆分为：

- `CustomerListQueryUseCase`
- `CustomerDetailQueryUseCase`
- `CustomerFormSchemaUseCase`
- `CustomerSaveDraftUseCase`
- `CustomerLoadDraftUseCase`
- `CustomerDraftListUseCase`
- `CustomerSubmitUseCase`
- `CustomerDeleteUseCase`

### 7.3 `CustomerAdminAppServiceImpl` 的演进路线

当前 `CustomerAdminAppServiceImpl` 仍会短期保留，但只作为过渡 facade：

- 第一阶段：保留类名，对外兼容，内部改成委派多个 use case
- 第二阶段：controller 改为依赖 `CustomerAdminFacade`
- 第三阶段：旧的大实现类退出主要业务职责

本次不要求第一轮彻底删除旧类，但禁止继续把主要业务逻辑塞回该类。

### 7.4 `CustomerListMetaProvider` 的演进路线

当前 `CustomerListMetaProvider` 同时承担：

- 列表筛选元数据
- 列表表头元数据
- 列表按钮元数据
- 行操作元数据
- 条件元数据暴露

建议拆分为：

- `CustomerListSchemaProvider`：只负责 schema
- `CustomerListQueryAdapter`：只负责查询条件适配

这样写侧校验不再需要依赖 schema provider。

### 7.5 repository 接口演进

当前 `CustomerRepository#findByCondition(Map<String, Object>)` 可暂时保留，但不再作为长期主语义接口。

建议逐步补充明确语义接口，例如：

- `existsByCustomerCode(String corpid, String customerCode, Long excludeId)`
- `pageQuery(CustomerListQuery query)`
- `countByQuery(CustomerListQuery query)`

其中 `Map`、分页偏移量、SQL 片段校验等技术细节继续留在 infrastructure 内部。

## 8. 协议统一设计

### 8.1 列表协议

建议收敛为统一平台协议：

- `ListQueryDTO extends BusinessBaseDTO`
- `ListPageVO<T>`
- `ListSchemaVO`

其中：

- `ListQueryDTO` 至少包含 `businessCode`、`keyword`、`conditions`、`pageNum`、`pageSize`
- `ListPageVO<T>` 至少包含 `list`、`pageHelper`
- `ListSchemaVO` 至少包含 `filter`、`header`、`topButton`、`bottomButton`、`rowAction`

客户列表不再继续维持“公共元数据协议 + 客户私有列表查询 DTO”两套协议并存的状态。

### 8.2 表单协议

建议沉淀表单相关平台协议：

- `FormSchemaProvider`
- `FormSchemaVO`
- `FormDataLoader<T>`
- `FormLoadVO<T>`
- `FormSubmitDTO<T>`

平台层负责页面字段结构与 schema，业务模块负责值域、默认值与编辑回填。

### 8.3 草稿协议

草稿协议本次不要求完全平台化，但要先满足两点：

- admin VO 不再直接暴露 application.pojo
- 草稿加载与保存用例单独成链，避免继续混在巨型应用服务中

### 8.4 无返回与异常协议

统一要求：

- 无返回业务统一为 `ResultVO<BaseVO>`
- 应用层与基础设施层主动抛错、主动捕获后的包装错误统一使用 `BizException`

## 9. 分阶段整改方案

### 9.1 M1：止血与立接缝

目标：先让客户模块不再继续扩大结构负债。

范围：

- 把 `CustomerAdminAppServiceImpl` 降级为 facade / 委派实现
- 拆出最少 6 条 use case 链
- 切断写侧校验对列表 DSL 的依赖
- 统一无返回接口与异常约束

产出：

- customer controller 不再依赖巨型业务实现类
- 写侧校验通过明确语义的 repository / domain service 完成
- 历史结构负债停止扩张

### 9.2 M2：平台协议成型

目标：形成真正可复制的新业务接入骨架。

范围：

- 统一列表 schema / query / page 协议
- common 升级为平台列表协议入口
- customer 作为第一个按新协议接入的样板模块
- 逐步补齐表单 schema 与提交协议接缝

产出：

- 第二个业务模块接入时不需要复制 `CustomerAdminAppServiceImpl`
- 平台层具备稳定 provider / adapter / registry 机制

### 9.3 M3：样板模块收尾

目标：让 customer 成为标准样板，而不是半迁移状态的历史模块。

范围：

- 收敛详情 / 草稿 / 表单 VO 边界
- 清理明显的跨层依赖
- 补接入说明与样板文档

产出：

- customer 模块具备可复制结构
- common 抽象边界更稳定

## 10. 本次明确做 / 暂不做

### 10.1 本次明确做

- facade 化 `CustomerAdminAppServiceImpl`
- use case 拆分
- 列表协议统一
- common 列表平台协议升级
- 写侧校验与列表 DSL 解耦
- 无返回接口与异常约束统一

### 10.2 本次暂不做

- 全量 DTO / VO / Pojo 重命名
- 全量 assembler 重写
- 草稿协议完全平台化
- 多业务模块并行迁移

## 11. 风险与控制

### 11.1 common 抽象过早

风险：把客户模块特有语义错误固化成平台标准。

控制方式：

- 平台层只定义稳定协议，不吸收客户业务规则
- 只有跨模块稳定存在的能力才进入 common

### 11.2 结构拆分但语义不提升

风险：从一个大类拆成多个小类，但 repository 仍通过 `Map` 暴露弱语义，收益有限。

控制方式：

- 优先替换关键写侧校验与关键查询为明确语义接口
- `Map` 仅限 infrastructure 内部保留

### 11.3 新旧协议并存过久

风险：团队继续沿旧方式开发，导致重构长期悬空。

控制方式：

- M2 完成后明确规定：新业务模块必须走新协议
- 旧结构只允许过渡，不允许继续扩容

### 11.4 facade 形似但不真实

风险：名义上 facade 化，实际仍在 facade 中堆积大量逻辑。

控制方式：

- 规定 facade 只做委派
- use case 才是唯一允许编排逻辑的应用层单元

## 12. 验收标准

本次整改完成后，应满足以下验收口径：

1. 新业务接入时，不需要复制 `CustomerAdminAppServiceImpl` 风格的大类实现
2. 新业务只需补充 `schema provider + query adapter + submit use case + domain rule` 等稳定接缝
3. 写侧校验不再依赖列表元数据或列表查询 DSL
4. customer controller 层只依赖 facade，不直接耦合巨型应用服务实现
5. common 层只保留稳定平台协议，不再泄漏客户特有业务语义
6. 无返回接口统一使用 `BaseVO`，主动业务异常统一使用 `BizException`

## 13. 最终建议

本次重构的核心不是“把客户模块打磨到最漂亮”，而是“先做出一套可复制的业务接入骨架”。

因此建议遵循以下原则：

- 第一轮目标：做出可复制的接入骨架
- 不是第一轮目标：做出最完美的 customer DDD 实现
- customer 是首个样板模块，不是最终平台标准本身
- common 只沉淀稳定协议，不提前吞并业务规则

按此路径推进后，后续第二个业务模块接入时，才有机会真正验证这套平台化与 DDD 收敛方案是否成立。
