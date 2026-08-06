# Flyway 基线记录

## V1 基线

- 文件：`xbb-erp-app-admin/src/main/resources/db/migration/V1__baseline.sql`
- 生成日期：`2026-08-05`
- 来源：经授权的 MySQL 只读结构导出，源数据库版本为 MySQL `5.7.25`。
- 范围：`34` 张表的 DDL；不包含业务行数据、账号数据或连接凭据。
- 清理：移除了 `DROP TABLE`、会话指令和源库历史 `AUTO_INCREMENT` 值。

## 使用边界

- `V1__baseline.sql` 仅用于创建新的空数据库，假设目标数据库已由部署平台创建。
- 不得修改、重导或覆盖已提交的 `V1`；后续结构变更从 `V2__...sql` 开始。
- 接入已有数据库或启用应用运行时 Flyway 前，仍需确认应用入口、历史环境版本和回滚策略。
