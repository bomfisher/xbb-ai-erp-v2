---
description: 用户提供了完整的module初始化资料时执行
---


## 适用场景
当用户提供了完整的module初始化资料，并且要求就行module初始化时执行


## 执行流程
- module项目包的前缀是xbb-erp-module，和用户确认当前要创建的module包的业务名，例如销售管理，用户提供了sales，那项目包名是xbb-erp-module-sales
- 包名查重，重复返回第一步
- 完成项目初始化创建，仅生成package结构，不创建任何文件，详见 `.claude/commands/init-module/module-demo.md`
- 持久层代码优先走仓库内 YAML 生成器 `xbb-erp-codegen`，不要再手工逐个创建 PO、Mapper、RepositoryImpl、Mapper XML
- 先准备模块规格 YAML；已有规格优先复用，没有则按数据库设计资料补一份。规格字段至少包含：`moduleCode`、`moduleName`、`packageBase`、`pathStrategy`、`aggregate.aggregateName`、`aggregate.tableName`、`aggregate.fields`、`generate`。可参考 `xbb-erp-codegen/src/main/resources/examples/supplier-vendor.yaml` 或 `xbb-erp-codegen/src/main/resources/examples/customer-module.yaml`。
- 先执行 dry-run 检查路径是否正确：`mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run <spec.yaml>'`
- dry-run 确认无误后再执行生成：`mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate <spec.yaml> .'`
- `pathStrategy` 当前默认使用 `ddd-mybatis-plus`，生成结果应落到当前模块的 `admin`、`application`、`domain`、`infrastructure/persistence` 以及 `src/main/resources/mapper/<moduleCode>`
- 如果一个业务模块包含主表和多个子表，优先在同一个 YAML 中通过 `generate.children` 维护主子实体关系；只有当结构差异过大时才拆多个 YAML
- 根据提供的数据库设计资料，补齐或校正生成器尚未覆盖的部分，再继续生成领域对象与仓储实现
- 按 `docs/harness/数据库迁移规范.md` 生成 Flyway 迁移；`docs/sql` 仅保留可读设计说明。
- 生成领域对象时，至少实现单个 `insert`、批量 `insertBatch`、`removeById`、`removeBatchByIds`、`update`、`findById`、`findByCondition`、`count`
- `insertBatch` 必须使用 SQL 批量插入，禁止 `for` 循环逐条插入
- `removeById` 与 `removeBatchByIds` 都是逻辑删除
- `findByCondition` 和 `count` 在 `mapper.xml` 中共用同一个筛选条件片段；以上有 `corpid` 字段的表都需要传 `corpid`
- `findByCondition` 需要对接分页参 `offset` 与 `pageSize`；如果 `offset` 不存在，仅有 `pageSize`，那就只取 `pageSize` 条数据
- `findByCondition` 需要对接 `groupByStr` 与 `orderByStr`
- 生成结果需要符合 `docs/harness/工程规则唯一事实源.md`：直连数据库的对象实体后缀必须是 `PO`；直连数据库字段禁止使用布尔值，改用 `Integer`；枚举类必须以 `Enum` 结尾；接口入参统一使用 DTO，非脚本接口 DTO 继承 `BaseDTO`；`userId` 是字符串。
- 创建完新 module 后，回写到 `docs/base/项目业务module导航.md`。
- 完成后至少执行模块级编译验证；优先验证生成器模块和目标业务模块

## 执行检查清单
- 第一步：确认业务名、模块目录名、`moduleCode`、`packageBase` 一致
- 第二步：确认目标模块目录不存在重名冲突
- 第三步：仅按 `.claude/commands/init-module/module-demo.md` 创建 package 结构，不预先手写业务文件
- 第四步：准备或补齐 YAML 规格，优先复用已有规格文件
- 第五步：先跑 `dry-run`，核对 `admin`、`application`、`domain`、`infrastructure/persistence`、`mapper xml` 的最终落位
- 第六步：执行 `generate`，生成持久层与基础骨架
- 第七步：根据数据库设计文档补齐生成器未覆盖的 SQL、领域对象、仓储实现与接口出入参
- 第八步：生成 Flyway 迁移、回写 `docs/sql` 设计说明与 `docs/base/项目业务module导航.md`
- 第九步：执行模块级编译验证，并记录无关失败项，不顺手修 unrelated 问题

## 交付物清单
- 目标业务模块目录，例如 `xbb-erp-module-<moduleCode>`
- 模块规格 YAML，优先放在生成器示例或业务模块可追溯的位置
- 持久层代码：`PO`、`Mapper`、`RepositoryImpl`、`Mapper XML`
- 管理端基础骨架：`admin/dto`、`admin/vo`、`controller`、`application/service`
- Flyway 迁移：`xbb-erp-app-admin/src/main/resources/db/migration/V<version>__init_<moduleCode>_module.sql`
- 导航文档回写：`docs/base/项目业务module导航.md`
- 编译验证结果，至少覆盖 `xbb-erp-codegen` 与目标业务模块
## 边界规则

[//]: # (修改的接口查询接口文档时，根据接口所在module的名称后缀，例如xbb-erp-module-sales，当前接口领域就是sales)
