---
description: 用户提供了业务完整字段时执行基础接口初始化
---


## 适用场景
用户提供了业务完整字段时执行基础接口初始化

## 期望
生成以下接口
1、列表接口 /list
2、新建页(获取新建字段和默认值) /addItem
3、编辑页(获取编辑字段和历史值) /updateItem
4、保存(新建和编辑的保存接口) /save
5、详情 /detail
6、删除 /delete


## 执行流程
- 先拆解当前业务的字段，用于列表、新建、编辑、详情4个场景
- 参考`xbb/ai/erp/base/common/filed/FieldDemoEnum.java` 完成字段枚举创建，根据实际业务决定是否初始化必填字段和不可编辑字段
- 如果出现新的未定义的字段类型，尤其是复杂字段，例如选择产品、多规格，优先和用户确认，定义新的字段类型 
- 列表接口入参优先使用 `xbb/ai/erp/base/common/dto/ListBaseDTO.java` 回参VO用`xbb/ai/erp/base/common/vo/ListBaseVO.java` list的泛型业务自行定义
- 新建页接口优先使用 `xbb/ai/erp/base/common/dto/BaseDTO.java` 回参VO用`xbb/ai/erp/base/common/vo/SaveItemVO.java` data的泛型业务自行定义
- 编辑页接口优先使用 `xbb/ai/erp/base/common/dto/IdBaseDTO.java` 回参VO用`xbb/ai/erp/base/common/vo/SaveItemVO.java` data的泛型业务自行定义
- 删除接口优先使用 `xbb/ai/erp/base/common/dto/BatchBaseDTO.java`
- 保存接口DTO根据当前业务自定义，放在业务所属包下

## 其他规则
新增接口或者修改接口的入参或回参，维护自身模块在 `docs/api` 的接口文档，文档维护领域名称参考 `.claude/commands/init-crud-ctrl/cal-domain.md`

## 边界约定
只实现CRUD，但是根据DDD模型实现