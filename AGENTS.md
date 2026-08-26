# Agent 项目规则

## 必读上下文
- 开始任务前阅读 `docs/harness/工程规则唯一事实源.md`、`docs/harness/README.md`、`docs/harness/文档地图.md`，再按地图加载目标模块代码和相关 API 文档；文档分类、事实源与新增文档落位遵循 `docs/harness/后端文档规范.md`。
- 项目结构导航：`docs/base/项目业务module导航.md`；接口导航：`docs/kn/总目录.md`；前端仓库：`/Users/bomfish/xbb-ai-test-codegen/xbb-ai-erp-v2-front`。
- 技术栈：JDK 21、Spring Boot 3.3.2、Maven、MyBatis-Plus、MySQL、Redis、JUnit 5；模块开发遵循 DDD。
- 所有 `infrastructure.persistence.po` 下的 `*PO` 必须继承 `xbb.ai.erp.base.persistence.entity.BaseEntity`，不得重复声明 `id`、`del/deleted`、`addTime`、`updateTime` 基础字段。
- 采购保存入库的 `NOT NULL` 字段初始化遵循 `docs/guide/采购模块保存入库初始化规范.md`，新增只补空值，显式值优先。

## Agent 约束
- 对话和代码注释使用中文；代码修改前主动询问是否创建 worktree。
- 交付完整 ERP 功能或跨越需求规格、DDD 模块、代码生成、数据库迁移、接口文档和验证多个层次时，必须先使用 `.agents/skills/erp-feature-delivery/SKILL.md` 做总流程编排，再按需使用 `business-module-delivery` 与 `ddd-business-module-development`。
- 完成开发后运行 `scripts/harness-verify.sh` 和相关测试；接口契约变化时执行 `.claude/commands/multi-player/SKILL.md`。
- 不覆盖用户已有改动，不创建提交或分支，除非用户明确要求。
- 所有新增或修改的 Java、XML 文件必须按本模块既有风格格式化；禁止压缩为单行、在一行中堆叠多个语句或保留不可读的长 SQL/XML。没有格式化器时必须在交付前人工整理缩进、换行和导入顺序。
- 禁用 SKILL `superpowers`。
