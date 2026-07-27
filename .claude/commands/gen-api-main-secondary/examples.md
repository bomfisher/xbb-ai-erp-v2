## 结构化输入示例

- 输入：`subjectLabel=客户，scope=list,detail，docMode=both，requirements=优先增量更新`
- 期望行为：先定位 `docs/kn/customer-m.md`，再同步 `docs/kn/customer-list-s.md`

## 自然语言输入示例

- 输入：`帮我补客户列表和详情的主文档与次文档，已有文档尽量不要重写`
- 期望行为：抽取主题、范围与更新偏好；缺字段时反问

## 冲突示例

- 输入：只给“采购”，但仓库中出现多个可能主文档
- 期望行为：输出候选与来源，暂停并询问用户
