# 列表展示值渲染

`xbb-erp-module-common` 的 `ListValueRenderer` 根据当前业务 LIST 表头渲染列表行展示值。业务列表在领域行转换为 ListItemVO 后调用该服务；不得在 Query AppService 中逐行或按字段手工查询名称。

## 表头约定

- 普通 `FieldEntity` 的 `attr` 同时表示原始值读取和展示值写入属性。
- 仅列表表头可使用 `ListRenderFieldEntity.renderValueAttr`：`attr` 为展示属性，`renderValueAttr` 为原始码值属性。它不进入表单、保存和筛选领域模型。
- 静态选项字段从 `itemList` 渲染；`DATE(6)`、`TIME(7)` 使用毫秒时间戳，时区固定为 `Asia/Shanghai`。
- 引用字段以 `fieldType + businessSelectConfig.businessCode` 定位 Provider；首期已注册 `ORG_MEMBER`、`ORG_DEPARTMENT` 和 `DEMO`。

## 批量规则

渲染器先扫描当前页的全部字段和全部行，按引用键汇总、去重原始值，再对每个引用键调用一次批量查询 Provider，最后回写展示值。`USER` 与 `USER_MULTI` 归一到同一个 `ORG_MEMBER` 查询，`DEPT` 与 `DEPT_MULTI` 同理；禁止按字段或按行查询。

Provider 由数据所属模块实现 `ListReferenceValueProvider` 并注册 Spring Bean；`module-common` 不直接依赖 org、demo 或其他业务模块。
