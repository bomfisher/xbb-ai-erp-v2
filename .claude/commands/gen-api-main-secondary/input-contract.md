## 输入协议

### 结构化输入

支持以下字段：

- `subject`
- `subjectLabel`
- `scope`
- `docMode`
- `requirements`

推荐格式示例：

- `subject=customer`
- `subjectLabel=客户`
- `scope=save,draft`
- `docMode=both`
- `requirements=优先增量更新`

### 自然语言输入

- 先解析出主题、范围、输出模式
- 无法稳定解析时停止并反问
- 不允许靠语义猜测补齐缺失关键字段
- 如果用户只表达更新偏好，也要回填到 `requirements`

### 内部统一模型

- `subject`：业务主题标识
- `subjectLabel`：业务主题中文名
- `scope`：`save/list/detail/draft/create/edit/...`
- `docMode`：`business/function/both`
- `requirements`：补充要求

### 缺失字段反问规则

以下字段缺失时必须反问：

- `subject` 与 `subjectLabel` 同时缺失
- `scope` 缺失
- `docMode` 缺失

### `scope` 支持值

- `save`
- `draft`
- `list`
- `create`
- `edit`
- `detail`

### `docMode` 支持值

- `business`
- `function`
- `both`

### 输出目录约束

- 业务聚合文档输出到 `docs/kn/*-m.md`
- 功能聚合文档输出到 `docs/kn/*-s.md`
- `docs/api/endpoints` 只作为 API 接口事实来源扫描
- API 原子文档不在本 skill 中维护
