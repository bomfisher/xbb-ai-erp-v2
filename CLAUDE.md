# Agent 项目规则

## 必读上下文
- 开始任务前阅读 `docs/harness/工程规则唯一事实源.md`、`docs/harness/README.md`、目标模块代码和相关 API 文档。
- 项目结构导航：`docs/base/项目业务module导航.md`；接口导航：`docs/kn/总目录.md`；前端仓库：`/Users/bomfish/xbb-ai-erp-v2-front`。
- 技术栈：JDK 21、Spring Boot 3.3.2、Maven、MyBatis-Plus、MySQL、Redis、JUnit 5；模块开发遵循 DDD。

## Agent 约束
- 对话和代码注释使用中文；代码修改前主动询问是否创建 worktree。
- 完成开发后运行 `scripts/harness-verify.sh` 和相关测试；接口契约变化时执行 `.claude/commands/multi-player/SKILL.md`。
- 不覆盖用户已有改动，不创建提交或分支，除非用户明确要求。
