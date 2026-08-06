---
description: 当代码新增接口或接口契约变化时，维护 docs/api/endpoints 下的 API 原子接口文档
---

## 适用场景
- agent 完成代码开发任务并且通过验证，新增了接口
- agent 完成代码开发任务并且通过验证，接口 URL、请求方式、入参 DTO、出参 VO 发生变化
- 用户明确要求补充或修订某个接口的 API 文档

## 目标
- 只维护 `docs/api/endpoints` 下的 API 原子接口文档
- 一个接口一份文档
- 每份文档只维护一个接口的完整事实
- 输出本次新建或更新的 API 文档路径，供上层 skill 继续同步聚合文档与双入口导航
- 如果用户要求维护业务文档、功能文档或总导航文档，应转交 `gen-api-main-secondary` 或 `multi-player`

## 执行流程
- 先读取 `.claude/commands/gen-api-md/cal-domain.md`，确定领域前缀、动作标识、目标文件名与落盘目录
- 再读取 `.claude/commands/gen-api-md/template.md`，按单接口模板维护目标文档
- 只依据当前代码事实、用户显式输入与已有 API 原子文档更新当前接口
- 如果目标文档不存在则新建；如果已存在则仅更新该单一接口对应文档
- 输出结果摘要：新建/更新的 API 文档路径、主要事实来源、待确认项

## 规范限制
- 本 skill 只负责 `docs/api/endpoints` 下的 API 原子接口文档
- 本 skill 不负责 `docs/kn` 下的业务聚合文档、功能聚合文档或双入口导航文档
- 禁止在一个文件内继续累积第二个接口正文
- 不确定的接口归属、URL、DTO、VO 信息必须输出待确认项并带来源
- 文档内容不得凭猜测补齐，必须基于代码事实或用户显式输入

## 使用顺序
1. 阅读 `.claude/commands/gen-api-md/cal-domain.md`
2. 阅读 `.claude/commands/gen-api-md/template.md`
3. 定位或创建 `docs/api/endpoints/<domain>-<action>.md`
4. 维护当前这一个接口的完整事实
5. 输出本次更新的 API 文档路径与待确认项
