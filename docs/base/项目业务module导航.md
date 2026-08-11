## 文档作用
当前文档是用于快速定位项目 module 作用的导航文档，用于开发人员或 agent 快速定位业务代码位置。

## 当前初始化范围
非脚本类接口入参 DTO 统一继承 `BaseDTO`，并包含：

- `corpid`
- `userId`

接口 URL 统一采用：

- `/erp/v1/{domain}/{feature-path}`

## 导航

### xbb-erp-base-*

#### xbb-erp-base-common
- 功能定位：最底层公共基础能力模块
- 责任范围：统一返回对象、基础异常、错误码基类、`BaseDTO`、通用常量
- 不负责什么：不承载业务规则，不承载 Web / 缓存 / 持久化实现
- 当前依赖与被谁依赖：作为其他 `base` / `app` 模块的通用依赖

#### xbb-erp-base-web
- 功能定位：Web 协议层公共能力模块
- 责任范围：统一异常处理、统一返回包装、基础 MVC 配置
- 不负责什么：不承载业务 Controller 与业务规则
- 当前依赖与被谁依赖：依赖 `xbb-erp-base-common`，被三个 `app` 入口复用

#### xbb-erp-base-security
- 当前状态：已从当前版本初始化结果中移除
- 原因：为保证当前最小可运行骨架先通过真实编译与启动验证，暂不接入安全链能力
- 后续边界：后续如恢复，需要独立补齐安全自动装配顺序、放行策略与测试验证

#### xbb-erp-base-tenant
- 功能定位：租户上下文基础模块
- 责任范围：`TenantContext`、基础租户解析透传骨架
- 不负责什么：不承载业务租户隔离规则
- 当前依赖与被谁依赖：依赖 `xbb-erp-base-common`，被三个 `app` 入口复用

#### xbb-erp-base-persistence
- 功能定位：持久化基础模块
- 责任范围：数据源、MyBatis-Plus、审计字段填充骨架
- 不负责什么：不承载业务仓储与业务 Mapper，不接 `Flyway`
- 当前依赖与被谁依赖：依赖 `xbb-erp-base-common`，被三个 `app` 入口复用

#### xbb-erp-base-cache
- 功能定位：缓存基础模块
- 责任范围：Redis 装配、`RedisTemplate`、缓存 Key 规范骨架
- 不负责什么：不承载业务缓存策略
- 当前依赖与被谁依赖：依赖 `xbb-erp-base-common`，被三个 `app` 入口复用

#### xbb-erp-base-idgen
- 功能定位：基础 ID 生成模块
- 责任范围：ID 生成接口与默认实现
- 不负责什么：不承载业务单号规则
- 当前依赖与被谁依赖：被三个 `app` 入口复用，后续供业务模块扩展接入

#### xbb-erp-base-log
- 功能定位：统一日志基础模块
- 责任范围：`logback` 配置、日志上下文基础能力
- 不负责什么：不承载业务审计规则
- 当前依赖与被谁依赖：被三个 `app` 入口复用；本地环境输出 `DEBUG`，其余环境输出 `INFO`

#### xbb-erp-base-test
- 功能定位：公共测试底座模块
- 责任范围：`JUnit 5 + Testcontainers` 公共基座
- 不负责什么：不承载业务测试用例
- 当前依赖与被谁依赖：供后续 `base` / `module` / `app` 测试复用

### xbb-erp-module-*

#### xbb-erp-module-demo
- 功能定位：DEMO 主从业务基础模块
- 责任范围：`demo` 主表、`demo_item` 子档、`demo_sub` 下游关联的 DDD 领域与持久化骨架，主表提供列表、表单初始化、保存提交和草稿接口骨架
- 当前表范围：`demo`、`demo_item`、`demo_sub`
- 当前代码落位：`admin`、`application`、`domain`、`infrastructure/persistence`、`src/main/resources/mapper/demo`
- 不负责什么：不承载 OSS、地址高德、附件上传服务和下游业务完整流程

#### xbb-erp-module-demo_sub
- 功能定位：DEMO 下游业务独立模块
- 责任范围：复用 `demo_sub` 表，提供 `DEMO_SUB` 列表、动态表单初始化、正式保存和草稿接口协议。
- 当前表范围：`demo_sub`
- 当前代码落位：`admin`、`application`、`domain`、`infrastructure/persistence`、`src/main/resources/mapper/demo_sub`
- 不负责什么：不创建或维护 `demo` 主档；草稿存储实现待后续接入。



### xbb-erp-app-*

#### xbb-erp-app-admin
- 功能定位：PC 管理后台启动装配入口
- 责任范围：`AdminApplication`、配置装配、健康检查、示例接口
- 不负责什么：不沉淀业务规则
- 当前依赖与被谁依赖：依赖所需 `base` 模块，不被其他 `app` 依赖

#### xbb-erp-app-mobile
- 功能定位：移动端 / BFF 启动装配入口
- 责任范围：`MobileApplication`、配置装配、健康检查、示例接口
- 不负责什么：不复制独立业务模型
- 当前依赖与被谁依赖：依赖所需 `base` 模块，不被其他 `app` 依赖

#### xbb-erp-app-job
- 功能定位：任务与后台执行启动装配入口
- 责任范围：`JobApplication`、配置装配、健康检查
- 不负责什么：本期不接入具体定时任务
- 当前依赖与被谁依赖：依赖所需 `base` 模块，不被其他 `app` 依赖
