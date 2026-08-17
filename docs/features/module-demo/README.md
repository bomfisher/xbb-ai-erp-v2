# module-demo 功能交付

## 适用范围

本目录记录 `xbb-erp-module-demo` 的需求规格、代码生成输入和交付边界；完整接口事实以 `docs/api/endpoints/` 为准，数据库执行事实以 Flyway 迁移为准。

## 业务边界

- `demo` 是顶级业务表，对外业务编码为 `DEMO`。
- `demo_item` 是新建 `demo` 时同步维护的一对多子档。
- `demo_sub` 是由 `demo` 创建关联的下游业务表，基础代码只提供领域和持久化骨架，不创建独立 HTTP 入口。
- 页面字段按需求文档提供名称、员工、部门、下拉、多选、数字、金额、日期、时间、附件、图片和地址字段元数据。

## 规格入口

- ROOT：`specs/demo.yaml`
- CHILD：`specs/demo-item.yaml`、`specs/demo-sub.yaml`
- 字段元数据：`specs/field-metadata.json`

## 字段设计器

- `index.html` 支持非分组和分组管理两种模式；分组模式会输出 `formSections`，非分组模式仅输出字段与列表动作。
- 启用分组后，所有字段默认归入“基本信息”；可新增、重命名、上移或下移分组，并通过字段卡片的“所属分组”下拉框快速迁移字段。
- 切换到非分组模式不会删除内存中的分组映射，切回分组管理可恢复之前的分组、字段归属和排序。

## 验收入口

- 模块校验：`python3 .agents/skills/business-module-delivery/scripts/verify_module_delivery.py xbb-erp-module-demo Demo --field-metadata docs/features/module-demo/specs/field-metadata.json --child DemoItem --child DemoSub`
- 构建测试：`mvn -pl xbb-erp-module-demo -am test`
