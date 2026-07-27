---
description: 当agent完成代码开发任务并且通过验证，新增了接口或者修改接口导致接口出入参、url发生变化，新增接口文档或者修改接口文档
---


## 适用场景
当 agent 完成代码开发任务并且通过验证，新增了接口或者修改接口导致接口出入参、url 发生变化。
在 `docs/api` 目录下创建 API 接口文档，或找到已有的 API 接口文档修改已有接口信息。

## 执行流程
- 计算接口领域，计算方式参考 `.claude/commands/gen-api-md/cal-domain.md`，将计算结果与用户确认
- 只维护 `docs/api` 下的 API 接口文档
- 如果没有已存在的接口文档，则新建
- 如果接口文档中没有维护过当前接口，则新增
- 如果已有文档已有接口，根据当前代码，检查更新当前接口
- 参考 `.claude/commands/gen-api-md/template.md` 生成 API 接口文档
- 如果接口文档内接口数量已经 >= 20 则新增一份接口文档
- 如果用户要求的是主文档、次文档或业务知识文档，应转交 `gen-api-main-secondary`

## 规范限制
参考 `.claude/commands/gen-api-md/template.md`
- 本 skill 只负责 `docs/api` 下的 API 接口文档
- 不负责主文档、次文档或业务知识文档