---
description: 根据用户输入生成或更新业务聚合文档与功能聚合文档
---

## 适用场景
- 用户希望维护某个业务主题的聚合文档，例如客户、供应商、采购
- 用户希望维护某类功能的聚合文档，例如保存、列表、详情、草稿
- 用户希望按统一规则维护 `docs/kn` 下的业务聚合文档、功能聚合文档与它们对 API 原子文档的引用关系

## 目标
- 维护 `docs/kn` 下的业务聚合文档与功能聚合文档
- 业务聚合文档按业务组织多个接口摘要
- 功能聚合文档按功能组织多个接口摘要
- `docs/api/endpoints` 下的 API 原子文档是接口完整事实源
- 聚合文档只维护摘要、差异点、章节组织与跳转关系，不复制完整请求/响应/字段正文
- 不确定信息统一输出待确认项

## 输入入口
- 先读取 `input-contract.md`
- 再读取 `decision-rules.md`
- 按需套用 `main-template.md` 与 `secondary-template.md`
- 参考 `examples.md` 组织输出形式

## 暂停并反问条件
- 缺少关键字段
- 候选业务聚合文档不唯一
- 候选功能聚合文档不唯一
- 代码事实、API 原子文档与现有聚合文档冲突且无法判断

## 执行流程
- 第一步：读取 `input-contract.md`，把用户输入归一化为统一模型
- 第二步：扫描 `docs/kn`、`docs/api/endpoints`、`docs/superpowers/specs` 与代码事实
- 第三步：根据 `decision-rules.md` 决定业务聚合文档、功能聚合文档与更新模式
- 第四步：先处理业务聚合文档，再处理功能聚合文档
- 第五步：按统一格式输出结果摘要与待确认项

## 扫描范围
- `docs/kn/*.md`
- `docs/api/endpoints/*.md`
- `docs/superpowers/specs/*.md`
- 相关 controller、DTO、VO、URL、业务枚举

## 输出模板
- 新建文档：列出新建的业务聚合文档与功能聚合文档
- 更新文档：列出被增量更新的聚合文档
- 更新章节：列出业务章节、功能章节与新增跳转项
- 事实来源：说明本次主要依据的 API 原子文档、现有聚合文档与代码来源
- 待确认：统一使用 `待确认：事项；候选：A / B；来源：...` 格式输出

## 使用顺序
1. 阅读 `input-contract.md`
2. 阅读 `decision-rules.md`
3. 按需套用 `main-template.md` 与 `secondary-template.md`
4. 参考 `examples.md` 输出结果
