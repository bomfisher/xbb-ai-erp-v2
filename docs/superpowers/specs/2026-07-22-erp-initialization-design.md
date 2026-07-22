# ERP 系统初始化设计文档

## 1. 目标与范围

本次初始化目标是在当前仓库中建立符合 DDD 工程约束的 ERP 后端最小可运行骨架，仅初始化以下模块：

- `xbb-erp-base-common`
- `xbb-erp-base-web`
- `xbb-erp-base-security`
- `xbb-erp-base-tenant`
- `xbb-erp-base-persistence`
- `xbb-erp-base-cache`
- `xbb-erp-base-idgen`
- `xbb-erp-base-log`
- `xbb-erp-base-test`
- `xbb-erp-app-admin`
- `xbb-erp-app-mobile`
- `xbb-erp-app-job`

本次不初始化以下模块：

- `xbb-erp-module-*`
- `xbb-erp-process-*`
- `xbb-erp-ext-*`

本次初始化粒度为“最小可运行骨架”，即：

- 建立父工程与多模块结构
- 补齐每个 `xbb-erp-base-*`、`xbb-erp-app-*` 模块的最小可运行代码骨架
- 保证 `xbb-erp-app-admin`、`xbb-erp-app-mobile`、`xbb-erp-app-job` 三个应用均可独立启动
- 接入 MySQL、Redis、MyBatis-Plus、Logback 等启动必需基础设施
- 本版本不接入 Flyway

## 2. 设计原则

本次初始化遵循以下原则：

- 严格遵循 `docs/base/后端项目框架设计原则.md` 中定义的 DDD 分层与模块边界
- 公共技术能力下沉到 `xbb-erp-base-*`，启动装配职责保留在 `xbb-erp-app-*`
- 仅实现当前启动所需的最小骨架，不提前引入业务模块、流程模块、行业扩展模块
- 真实接线 MySQL 与 Redis，确保应用不是“空壳启动”
- 不引入 Flyway，数据库迁移能力延后到后续阶段接入
- 模块边界优先于短期省事，避免将来再拆分底座职责

## 3. 顶层工程结构

### 3.1 父工程定位

仓库根 `pom.xml` 调整为聚合父工程，统一承担以下职责：

- 聚合全部 12 个初始化模块
- 管理依赖版本与插件版本
- 统一 Java 版本为 `JDK 21`
- 统一编码、测试与构建配置

建议父工程逻辑命名为：

- `artifactId`: `xbb-erp-parent`

仓库目录名可以保持现状，不要求强行改动物理目录名。

### 3.2 初始化模块清单

本次父工程下只纳入以下 12 个模块：

#### 基础能力模块

- `xbb-erp-base-common`
- `xbb-erp-base-web`
- `xbb-erp-base-security`
- `xbb-erp-base-tenant`
- `xbb-erp-base-persistence`
- `xbb-erp-base-cache`
- `xbb-erp-base-idgen`
- `xbb-erp-base-log`
- `xbb-erp-base-test`

#### 启动装配模块

- `xbb-erp-app-admin`
- `xbb-erp-app-mobile`
- `xbb-erp-app-job`

## 4. 包根与命名约束

### 4.1 包根名

全项目统一包根名为：

- `xbb.ai.erp`

### 4.2 命名约束

- 直接对接数据库的实体类使用 `Entity` 后缀
- 直接对接数据库的字段不使用布尔值，统一使用 `Integer`
- 枚举类统一使用 `Enum` 结尾
- 前端入参对象使用 `DTO` 后缀
- 接口出参对象使用 `VO` 后缀
- 其余中转对象使用 `Pojo` 后缀

### 4.3 BaseDTO 约束

`xbb-erp-base-common` 中提供 `BaseDTO`，用于承载非脚本类接口的公共入参字段。

`BaseDTO` 至少包含：

- `corpid`
- `userId`

约束如下：

- 所有非脚本类接口的入参 DTO 必须继承 `BaseDTO`
- 脚本类接口入参不强制继承 `BaseDTO`

### 4.4 接口 URL 规范

所有 HTTP 接口统一遵循以下 URL 结构：

- 第一级固定为 `erp`
- 第二级暂时固定为 `v1`
- 第三级表示接口所在领域
- 第四级及以后表示具体功能，长度不固定

统一结构如下：

- `/erp/v1/{domain}/{feature-path}`

示例：

- `/erp/v1/user/info`

约束如下：

- Controller 路由设计必须遵守该结构
- 第三级必须表达清晰领域语义，不使用含糊命名
- 第四级及以后按功能语义展开，不按技术分层命名
- 后续版本升级时，仅第二级版本段发生变化，其余结构保持一致演进

## 5. 模块职责与依赖边界

### 5.1 base 模块总体边界

`xbb-erp-base-*` 负责技术底座与跨应用公共能力，不承载业务规则，不承载具体业务域逻辑。

### 5.2 app 模块总体边界

`xbb-erp-app-*` 仅负责启动装配、入口暴露与少量通道级配置，不沉淀业务规则。

### 5.3 模块依赖方向

依赖方向遵循以下规则：

- `app-*` 依赖所需的 `base-*`
- `base-*` 之间仅允许少量单向底层依赖
- `base-*` 不允许反向依赖 `app-*`
- `app-admin`、`app-mobile`、`app-job` 之间不互相依赖

建议最小依赖边界如下：

- `xbb-erp-base-common`：最底层，不依赖其他业务性基础模块
- `xbb-erp-base-web`：依赖 `xbb-erp-base-common`
- `xbb-erp-base-security`：依赖 `xbb-erp-base-common`，必要时依赖 `xbb-erp-base-web`
- `xbb-erp-base-tenant`：依赖 `xbb-erp-base-common`
- `xbb-erp-base-persistence`：依赖 `xbb-erp-base-common`
- `xbb-erp-base-cache`：依赖 `xbb-erp-base-common`
- `xbb-erp-base-idgen`：依赖 `xbb-erp-base-common`
- `xbb-erp-base-log`：依赖 `xbb-erp-base-common`
- `xbb-erp-base-test`：依赖公共测试所需的基础模块
- `xbb-erp-app-*`：依启动需要组合依赖上述基础模块

## 6. 12 个模块的最小可运行骨架

### 6.1 `xbb-erp-base-common`

功能定位：

- 最底层公共基础能力模块

责任范围：

- 统一返回对象
- 基础异常体系
- 错误码基类
- `BaseDTO`
- 通用常量与最小公共工具

边界：

- 不放 Spring Boot 自动配置
- 不放业务规则
- 不放具体持久化、缓存、Web、安全实现

### 6.2 `xbb-erp-base-web`

功能定位：

- Web 协议层公共能力模块

责任范围：

- 全局异常处理
- 统一返回包装
- 基础 MVC 配置
- 通用参数绑定与请求处理基础能力

边界：

- 不承载具体业务控制器
- 不承担业务规则

落地要求：

- 以可复用自动配置形式提供给 `app-*`

### 6.3 `xbb-erp-base-security`

功能定位：

- 安全基础能力模块

责任范围：

- 最小安全配置
- 基础放行策略
- 用户上下文占位能力

边界：

- 本期不实现复杂鉴权流程
- 不耦合具体业务权限模型

### 6.4 `xbb-erp-base-tenant`

功能定位：

- 租户上下文基础模块

责任范围：

- `TenantContext`
- 基础租户解析与透传骨架

边界：

- 本期不实现复杂租户隔离规则
- 不直接承载业务侧租户校验逻辑

### 6.5 `xbb-erp-base-persistence`

功能定位：

- 持久化基础模块

责任范围：

- 数据源装配
- MyBatis-Plus 基础配置
- Mapper 扫描配置
- 审计字段自动填充骨架

边界：

- 本版不接 Flyway
- 不承载具体业务仓储实现
- 不承载业务 Mapper

### 6.6 `xbb-erp-base-cache`

功能定位：

- 缓存基础模块

责任范围：

- Redis 基础装配
- `RedisTemplate` 等公共 Bean
- 缓存 Key 规范骨架
- 最小缓存访问能力

边界：

- 不提前实现复杂缓存场景
- 不承载业务缓存策略

### 6.7 `xbb-erp-base-idgen`

功能定位：

- 基础 ID 生成模块

责任范围：

- ID 生成接口
- 最小默认实现

边界：

- 不承载具体业务单号规则
- 不替代后续 `xbb-erp-module-code-rule`

### 6.8 `xbb-erp-base-log`

功能定位：

- 统一日志基础模块

责任范围：

- `logback` 统一配置
- 日志上下文基础能力
- 应用统一日志输出规范

边界：

- 不承载业务审计规则
- 不由各 `app-*` 单独维护独立日志策略

环境约束：

- 本地环境打印 `DEBUG`
- 其余环境打印 `INFO`

### 6.9 `xbb-erp-base-test`

功能定位：

- 公共测试底座模块

责任范围：

- JUnit 5 测试基础能力
- Testcontainers 公共基座
- 公共测试辅助类

边界：

- 不放业务测试用例
- 不承担业务测试编排

### 6.10 `xbb-erp-app-admin`

功能定位：

- PC 管理后台启动装配入口

责任范围：

- `AdminApplication` 启动类
- 独立应用配置
- 健康检查入口
- 基础模块装配

边界：

- 不沉淀业务规则
- 不取代后续业务模块

### 6.11 `xbb-erp-app-mobile`

功能定位：

- 移动端/BFF 启动装配入口

责任范围：

- `MobileApplication` 启动类
- 独立应用配置
- 健康检查入口
- 基础模块装配

边界：

- 本期仅搭建装配层
- 不复制独立业务模型

### 6.12 `xbb-erp-app-job`

功能定位：

- 任务与后台执行启动装配入口

责任范围：

- `JobApplication` 启动类
- 独立应用配置
- 健康检查入口
- 基础模块装配

边界：

- 本期不强行接入具体定时任务
- 仅保证任务应用具备独立启动能力

## 7. 启动配置与环境分层

### 7.1 总体要求

三个应用均按真实基础设施接线启动：

- MySQL
- Redis
- MyBatis-Plus
- Logback

不采用“纯空壳模式”启动。

### 7.2 配置分层建议

每个 `app-*` 模块建议提供如下配置分层：

- `application.yml`：公共默认配置
- `application-local.yml`：本地开发配置
- `application-dev.yml`：开发环境占位配置
- `application-test.yml`：测试环境占位配置
- `application-prod.yml`：生产环境占位配置

### 7.3 环境行为约束

- `local`：连接本地 MySQL 与 Redis，并输出 `DEBUG` 级别日志
- 其余环境：保留真实接线配置占位，并统一输出 `INFO` 级别日志

### 7.4 Flyway 约束

本版不接 Flyway，意味着：

- 启动时不依赖数据库迁移脚本
- 数据源与持久化配置以“可装配、可连通、可启动”为目标
- 后续数据库迁移能力在独立阶段接入

## 8. 健康检查与可启动验收

### 8.1 启动成功判定

应用启动成功的判定口径不是仅进程拉起，而是以下基础能力装配完成：

- Spring 上下文完成启动
- Web 容器完成初始化
- 数据源完成装配
- Redis 完成装配
- MyBatis-Plus 基础配置完成装配

### 8.2 健康检查要求

每个应用提供统一探活能力，优先采用：

- `/actuator/health`

若初始化过程中需要极简探活接口，可作为补充，但优先使用标准健康检查能力。

### 8.3 验收标准

本次初始化验收包括两层：

#### 构建层

- 父工程能成功执行 `mvn test` 或 `mvn package`

#### 运行层

- `xbb-erp-app-admin` 可独立启动成功
- `xbb-erp-app-mobile` 可独立启动成功
- `xbb-erp-app-job` 可独立启动成功

## 9. 错误处理设计

统一异常处理放在 `xbb-erp-base-web` 中，目标如下：

- 统一收敛参数异常、业务异常、系统异常
- 统一输出结构
- 避免各应用散落重复 `try-catch`

本阶段只建立异常体系骨架：

- 基础异常类
- 错误码基类
- 统一异常处理器
- 统一响应包装

本阶段不提前设计完整业务错误码字典。

## 10. 测试策略

### 10.1 总体目标

本次测试目标是验证“可构建、可启动、可扩展”的工程基线，而不是一次性铺满完整业务测试。

### 10.2 测试要求

- 根工程至少能够跑通基础测试链路
- 三个 `app-*` 至少各有一个启动测试
- 启动测试用于验证 Spring 上下文可拉起
- `xbb-erp-base-test` 提供 JUnit 5 + Testcontainers 公共基座

### 10.3 边界约束

- 因本版不接 Flyway，测试不以迁移脚本为前提
- 不要求当前阶段一次铺满所有集成测试
- 优先确保测试骨架与应用启动验证可用

## 11. `项目module导航.md` 更新方案

`docs/base/项目module导航.md` 应补充本次初始化的 12 个模块导航，并统一使用以下记录维度：

- 模块名
- 功能定位
- 责任范围
- 不负责什么 / 边界
- 当前依赖与被谁依赖

文档按两组组织：

- `xbb-erp-base-*`
- `xbb-erp-app-*`

记录重点如下：

- `base-*`：强调提供何种公共技术能力，以及明确禁止承载何种业务语义
- `app-*`：强调装配入口定位，以及不沉淀业务规则的边界

文档中应明确记载当前阶段约束：

- 本期仅初始化 `base-*` 与 `app-*`
- 暂不初始化 `module-* / process-* / ext-*`
- 本版不接 Flyway
- 非脚本类接口入参 DTO 统一继承 `BaseDTO(corpid, userId)`

## 12. 实施边界总结

本次初始化最终边界如下：

- 仅初始化 9 个 `xbb-erp-base-*` 模块与 3 个 `xbb-erp-app-*` 模块
- 包根统一为 `xbb.ai.erp`
- 采用最小可运行骨架，不扩展到业务模块层
- 三个 `app-*` 均需可独立启动
- 接线 MySQL、Redis、MyBatis-Plus、Logback
- `logback` 统一由 `xbb-erp-base-log` 提供，本地 `DEBUG`，其他环境 `INFO`
- 所有非脚本类接口入参 DTO 必须继承 `BaseDTO`，其中包含 `corpid`、`userId`
- 本版不接 Flyway
- `项目module导航.md` 同步记录模块功能、职责边界与依赖关系

## 13. 下一步

设计确认后，下一步进入实施计划阶段，产出具体初始化顺序、文件清单、模块依赖清单与验证步骤，然后再执行代码落地。