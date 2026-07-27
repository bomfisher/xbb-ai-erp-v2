---
description: 执行多项项目文档维护任务
---


## 适用场景
根据用户给定的接口文件、接口方法，项目路径，业务范围等，进行当前项目内的业务文档、接口文档维护

## 执行流程
- 先执行 `.claude/commands/gen-api-main-secondary/SKILL.md`，维护 `docs/kn` 下的主文档、次文档或业务知识文档
- 再执行 `.claude/commands/gen-api-md/SKILL.md`，维护 `docs/api` 下的 API 接口文档
- 最后在 `docs/kn` 的知识文档中更新维护 `docs/api` 接口文档的路径

## 边界限制
- 只修改维护指定范围内的内容，禁止越界
- 无法确定的和用户确认，禁止想象