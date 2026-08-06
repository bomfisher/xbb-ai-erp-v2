# GitHub CI 门禁规范

## 当前工作流

`.github/workflows/harness-verify.yml` 在 Pull Request、`main` 和 `master` 推送时运行：

1. 使用 JDK 21 和 Maven 缓存。
2. 执行 `scripts/harness-verify.sh`，检查规则、文档、Skill、迁移文件和模块结构。
3. 执行 `mvn -B test`，验证全仓编译和已有测试。
4. 无论成功或失败都上传 Surefire 报告；当 `CI=true` 时，容器运行时不可用会明确失败，避免 MySQL/Redis 集成测试静默跳过。

工作流只使用代码仓库中的规则，不连接业务数据库、不执行 Flyway 迁移、不读取生产凭据。

## GitHub 仓库设置

工作流文件本身不会自动阻止合并。仓库管理员需要在 `Settings -> Branches -> Branch protection rules` 中：

- 对 `main` 启用 Pull Request 合并；
- 将 `Harness Verify / verify` 设置为 Required status check；
- 要求分支更新到最新目标分支后再合并；
- 按团队需要开启至少一名 Reviewer 的审批。

## 门禁分级

- 当前级别：Harness 静态检查 + Maven 全量测试，适合作为基础阻断门禁。
- 后续级别：增加临时 MySQL/Redis、Flyway `validate/migrate`、API 契约和前后端联调测试。
- 发布级别：增加生产配置检查、数据库备份确认、迁移回滚演练和发布后健康检查。

如果当前存量测试尚未稳定，不应删除测试或降低门禁，而应先在 GitHub 使用非 Required 状态观察失败原因；测试稳定后再改为 Required。
