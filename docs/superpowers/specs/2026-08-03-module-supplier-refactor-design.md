# module-supplier 按 module-customer DDD 风格重构设计

## 背景
当前 `xbb-erp-module-supplier` 已具备基础的 `admin`、`application`、`domain`、`infrastructure/persistence` 四层结构，但整体组织明显比 `xbb-erp-module-customer` 更薄，存在以下问题：

- 模块目录语义为 `supplier`，核心对象却普遍使用 `Vendor*`，领域术语不统一。
- `application` 层未按用例进行清晰拆分，当前以单一 `VendorAdminAppServiceImpl` 承担查询、保存、详情、删除等多类职责。
- 接口层未完全遵循当前项目规范，例如部分接口未统一使用 `ResultVO.success()` 包装返回。
- 供应商模块尚未具备与 `customer` 对齐的草稿、提交、草稿加载、草稿列表等能力组织方式。
- 持久化与数据库仍以 `vendor_*` 为命名中心，不符合本次领域语言统一目标。

本次重构目标不是局部整理，而是将 `xbb-erp-module-supplier` 作为“供应商管理模块”完整收敛到与 `xbb-erp-module-customer` 一致的 DDD 风格与项目规范。

## 目标

### 目标 1：统一领域语言
`xbb-erp-module-supplier` 重构后，模块内以 `Supplier` 作为唯一正式领域术语，不再保留 `Vendor*` 作为业务主命名。

范围包括：

- Java 类名、接口名、方法名、变量名
- DTO / VO / Pojo / Repository / PO / Mapper / Convertor / XML
- Controller、应用服务、领域模型、仓储接口
- 数据库表名、必要字段名、SQL 脚本命名与说明文档

### 目标 2：对齐 module-customer 的 DDD 风格
供应商模块整体结构对齐 `xbb-erp-module-customer`，不仅是层级名称对齐，还包括职责拆分方式对齐。

重构后应具备以下组织特征：

- `admin` 负责后台管理接口入口，Controller 只做协议接入与结果包装。
- `application` 负责用例编排，并按能力拆分子服务，而不是集中在一个实现类中。
- `domain` 承载供应商主档与子档模型、查询对象、仓储接口。
- `infrastructure/persistence` 负责 PO、Mapper、Convertor、RepositoryImpl、XML 等持久化实现。

### 目标 3：对齐 customer 的交互组织方式
供应商模块不只做结构统一，还要引入与 `customer` 对齐的后台交互组织方式，包括：

- 列表
- 新增空白表单项
- 编辑加载表单项
- 保存草稿
- 保存并提交
- 草稿列表
- 草稿加载
- 详情
- 删除

其中“对齐”指的是接口组织方式、应用服务拆分方式、DTO/VO 分层方式、草稿与提交能力链路对齐；不要求机械复制客户特有业务规则。

## 明确不做的事

- 不保留 `Vendor` 与 `Supplier` 的长期兼容双命名层。
- 不使用 `Flyway` 交付数据库迁移。
- 不在运行时对旧 `vendor_*` 表结构做兼容判断。
- 不为了兼容历史调用而保留旧接口返回格式。
- 不机械复制客户模块里只适用于客户业务的校验规则或字段含义。

## 重构后的目标架构

### 1. admin 层
供应商后台入口统一为 `SupplierAdminController`。

接口集合对齐 `customer`：

- `list`
- `addItem`
- `updateItem`
- `saveDraft`
- `saveAndSubmit`
- `draftList`
- `loadDraft`
- `detail`
- `delete`

接口约束：

- 非脚本接口参数使用 DTO，不使用散列参数。
- 入参 DTO 继承 `BaseDTO` 或项目现有基础 DTO 体系中相应基类。
- 返回统一使用 `ResultVO.success()` 包装。
- 无需返回业务数据的接口，使用 `BaseVO` 或 `ResultVO<Void>` 形式与当前项目规范保持一致。

### 2. application 层
保留门面接口 `SupplierAdminAppService`，但实现方式对齐 `customer`，将用例职责拆分为多个子服务，而不是由单个实现类承担全部逻辑。

建议至少补齐以下子结构：

- `application/service/query`
- `application/service/save`
- `application/service/draft`
- `application/service/delete`
- `application/service/impl`
- `application/assembler`
- `application/pojo`
- `application/port`
- `application/validator`

职责原则：

- `impl` 下的管理端门面只负责编排与转发。
- 查询逻辑进入 `query`。
- 保存与提交进入 `save`。
- 草稿读写进入 `draft`。
- 删除逻辑进入 `delete`。
- 表单聚合转换、上下文拼装由 `assembler` 与 `pojo` 承接。
- 与草稿存储等用例级依赖交互的抽象接口可放 `port`。
- 协议校验、通用校验、业务校验按 `customer` 的职责方式进行拆分。

### 3. domain 层
领域模型统一使用 `Supplier*` 命名，例如：

- `Supplier`
- `SupplierContact`
- `SupplierAddress`
- `SupplierBankAccount`
- `SupplierInvoiceProfile`

仓储接口同步统一，例如：

- `SupplierRepository`
- `SupplierContactRepository`
- `SupplierAddressRepository`
- `SupplierBankAccountRepository`
- `SupplierInvoiceProfileRepository`

为避免 application 层直接维护松散 `Map`，应补齐与 `customer` 相同风格的查询与中转 `Pojo`，用于承接查询条件、保存上下文、草稿上下文、子档同步上下文等对象。

### 4. infrastructure/persistence 层
持久化层整体统一为 `Supplier*` 命名，覆盖：

- `po`
- `mapper`
- `convertor`
- `repository`
- `src/main/resources/mapper/supplier`

持久化设计原则：

- `PO` 直接映射数据库表，命名统一带 `PO` 后缀。
- 直接对接数据库的字段不使用布尔类型，继续使用项目约定的 `Integer` 等类型表达状态。
- `RepositoryImpl` 负责领域仓储接口的落地实现。
- Mapper XML 与 Java Mapper 接口、PO 字段命名保持一致。
- 如 `customer` 中已有成熟的条件规整模式，如 `ConditionMapHelper`，供应商模块可按同风格补齐。

## 数据与数据库设计

### 1. 数据库最终态
数据库命名统一从 `vendor_*` 收敛到 `supplier_*`。

最终态要求：

- 主表与各子表表名统一切换到 `supplier_*`
- 与供应商语义强绑定的字段名从 `vendorXxx` / `vendor_xxx` 同步切换到 `supplierXxx` / `supplier_xxx`
- 默认子档 id、关联外键、查询字段、快照字段等同步完成命名调整
- 新代码直接面向 `supplier_*` 最终结构，不保留旧结构兼容逻辑

### 2. 数据迁移交付方式
本次不引入 `Flyway`，但必须交付可手工执行的数据库修改 SQL。

SQL 交付要求：

- 覆盖表名修改
- 覆盖必要字段名修改
- 覆盖索引、约束、关联字段的同步调整
- 保证历史数据可保留到新结构中
- 能够作为独立发布材料由人工执行

### 3. 代码与 SQL 一致性要求
SQL 不是附属材料，而是正式交付物的一部分。代码中的以下内容必须与 SQL 最终结构严格一致：

- 领域模型字段
- PO 字段
- Mapper XML 字段与表名
- 查询条件映射
- 详情、保存、列表等读写链路

## 接口协议设计

### 1. 协议升级策略
本次重构允许整体重做接口协议，不要求兼容旧 `Vendor` 接口的 URL、DTO/VO、返回结构与命名。

因此本次采用“最终态优先”策略：

- 接口命名统一收敛到 `Supplier` 语义
- 返回结构统一收敛到项目当前规范
- DTO / VO 字段名称按最终供应商语义统一
- 不保留旧协议的运行时兼容层

### 2. 接口组织原则
协议设计沿用 `customer` 的组织方式，但业务语义替换为供应商领域。

例如：

- 供应商保存链路支持保存草稿与提交
- 草稿支持列表与加载
- 详情与编辑加载分工清晰
- 子档结构保持供应商主档、联系人、地址、银行账户、开票信息的聚合组织方式

### 3. 结果包装规范
所有接口返回统一采用 `ResultVO.success()` 包装，避免当前 `VendorAdminController` 中直接返回 `Long`、`void` 或未包装对象的情况继续存在。

## 实施顺序

### 阶段 1：术语与结构收口
先完成模块内 `Vendor*` 到 `Supplier*` 的命名统一，并建立与 `customer` 对齐的分层与子包骨架。

阶段目标：

- 代码结构正确
- 领域语言统一
- 不再新增 `Vendor` 命名扩散

### 阶段 2：应用服务能力补齐
在骨架稳定后，补齐 `query / save / draft / delete` 等应用能力，并将当前单实现类职责拆散到对应用例服务。

阶段目标：

- 控制器职责收敛
- application 职责清晰
- 草稿与提交链路具备完整用例分层

### 阶段 3：持久化与数据库 SQL 收口
同步完成 `PO / Mapper / Convertor / RepositoryImpl / XML` 到 `Supplier*` 的统一，并产出数据库修改 SQL。

阶段目标：

- 代码结构与数据库最终态一致
- 所有 `vendor_*` 旧引用被清理
- SQL 可独立执行

### 阶段 4：接口文档与验证
完成接口文档更新、命名残留扫描、编译验证与受影响测试验证。

阶段目标：

- 输出接口文档
- 验证协议、代码、SQL 三者一致
- 确认重构最终态可交付

## 风险与控制

### 风险 1：改名范围大，容易残留旧引用
控制方式：在实现末尾执行全仓范围内 `Vendor`、`vendor_`、旧 Mapper 路径与旧表名残留扫描，逐项清理。

### 风险 2：机械复制 customer 规则导致 supplier 语义失真
控制方式：只对齐组织方式、用例拆分与协议规范，不机械复制客户专属业务规则、字段含义与校验语义。

### 风险 3：数据库 SQL 与代码映射不一致
控制方式：将数据库 SQL 视为正式交付物，与 PO、Mapper、XML、Repository 的最终字段逐项对照核验。

### 风险 4：接口协议整体变更影响对接方
控制方式：本次明确采用新协议最终态，不做兼容层；同步更新接口文档，确保对接方按新接口联调。

## 验证要求
重构完成后的验证至少包括：

- 模块级编译通过
- 受影响测试通过
- `Supplier` 术语统一完成
- `Vendor` 旧命名残留被清理
- `supplier_*` 表结构与代码映射一致
- 接口返回统一使用 `ResultVO.success()`
- 接口文档同步更新

## 交付物
本次重构的正式交付物包括：

- `xbb-erp-module-supplier` 按 `module-customer` 风格完成的 DDD 重构代码
- 统一到 `Supplier*` 术语的接口、应用层、领域层、持久化层代码
- 供应商数据库结构调整 SQL
- 更新后的接口文档

## 结论
本次方案选择的是一次性收口到最终态的重构路线：

- 统一 `Supplier` 作为唯一领域语言
- 完整对齐 `module-customer` 的 DDD 风格
- 引入 `customer` 同级的草稿与提交能力组织方式
- 接口协议整体升级到当前项目规范
- 数据库统一切换到 `supplier_*`，并交付手工执行 SQL

该方案改动面较大，但最终结构最干净，后续维护成本最低，适合作为供应商模块的正式长期形态。