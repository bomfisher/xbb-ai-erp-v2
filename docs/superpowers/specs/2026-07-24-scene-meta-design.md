# xbb-erp-scene-meta 设计说明

## 1. 背景

当前客户模块已经开始用 `CustomerFieldFactory` 管理列表、新建、编辑场景的字段定义，但该实现仍然位于 `xbb-erp-module-customer` 内部，并同时混合了两类职责：

- 跨模块都应统一遵守的字段场景协议
- 客户档案自身独有的字段目录、子表语义与展示规则

如果后续采购、商品、供应商等模块各自继续在本模块内复制一套 `FieldFactory`，字段能力会逐步走偏：

- 场景接口可能不一致
- 字段元数据结构可能不一致
- 列表表头、新建、编辑、详情的组织方式可能不一致
- 应用层组装逻辑可能在各模块重复扩散

因此需要抽出一层独立能力，统一字段场景的协议与基础装配方式，同时保留各业务对象对自身字段语义的控制权。

## 2. 目标

本设计希望建立一个新的独立模块 `xbb-erp-scene-meta`，作为 `base` 与具体业务模块之间的中间层，承接以下能力：

- 统一列表、创建、编辑、详情四种场景的字段协议
- 统一字段元数据结构
- 统一字段元数据到 `FieldEntity` 的装配方式
- 统一业务模块提供字段定义的顶层接口
- 为后续客户、采购、商品、供应商等模块提供同一套接入范式

## 3. 非目标

第一版明确不做以下事情：

- 不做动态字段平台
- 不做数据库配置化字段中心
- 不做全局字段注册中心
- 不在公共层抽象客户联系人、客户地址、采购明细行等业务概念
- 不在公共层预设主表/子表结构
- 不统一所有模块的字段枚举内容
- 不一次性迁移多个业务模块

第一版只验证“统一协议 + 单业务对象实现”的最小闭环。

## 4. 命名与模块定位

### 4.1 模块命名

新模块命名为：

- `xbb-erp-scene-meta`

命名原因：

- 该层表达的是字段场景与元数据协议，而不是某个具体业务模块
- 它不适合继续挂在 `module-common` 这种含义不清的名字下
- 它又高于单个业务模块，适合作为跨模块复用的场景元数据层

### 4.2 模块定位

`xbb-erp-scene-meta` 的职责不是承载业务字段内容，而是定义：

- 字段场景如何表达
- 业务对象如何对外提供字段定义
- 字段元数据如何转成现有前端协议对象

一句话说：

`scene-meta` 统一能力模型，业务模块负责业务语义。

## 5. 总体分层

未来字段场景能力分为三层：

### 5.1 协议层：`xbb-erp-scene-meta`

负责：

- 场景枚举
- 通用字段元数据
- 顶层 provider 接口
- 规则接口
- 通用 assembler
- 可选的薄抽象基类

这一层不允许出现客户、采购、商品等业务词汇。

### 5.2 实现层：各业务模块

例如：

- `xbb-erp-module-customer`
- `xbb-erp-module-purchase`

负责：

- 本业务对象的字段目录
- 本业务对象在四个场景下的字段清单
- 本业务对象独有的规则
- 本业务对象对字段标题、必填、可编辑性的局部定制

### 5.3 消费层：应用服务

应用服务只依赖 `scene-meta` 协议，不依赖其他业务模块的字段实现细节。

应用服务只表达：

- 我需要列表字段
- 我需要创建字段
- 我需要编辑字段
- 我需要详情字段

具体字段由所属业务对象的 provider 返回。

## 6. 核心设计

### 6.1 统一场景枚举

第一版统一四个场景：

- `LIST`
- `CREATE`
- `UPDATE`
- `DETAIL`

其中：

- `LIST` 用于列表表头
- `CREATE` 用于新建页面字段
- `UPDATE` 用于编辑页面字段
- `DETAIL` 用于详情页面字段

第一版不继续拆分更多场景，避免一开始过度细化。

### 6.2 通用字段元数据

`CustomerFieldMeta` 需要去业务化，上移为公共元数据对象，例如：

- `SceneFieldMeta`

建议至少保留以下属性：

- `attr`
- `attrName`
- `fieldType`
- `required`
- `editable`

这些属性只表达字段展示与交互协议，不表达任何客户/采购领域含义。

### 6.3 顶层 Provider 协议

第一版推荐用 `Provider`，不用 `Factory`。

原因：

- 这里表达的是按场景提供字段定义
- 它更像稳定提供能力，不是临时造对象
- 后续一个模块下会有多个业务对象，各自一个 provider，更符合语义

推荐公共接口形态：

```java
public interface SceneFieldProvider {

    String sceneKey();

    List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);
}
```

含义：

- `sceneKey()` 标识当前 provider 面向哪个业务对象
- `getFields(...)` 返回该业务对象在某场景下的字段定义

### 6.4 sceneKey 规范

第一版约定采用：

- `模块.业务对象`

例如：

- `customer.archive`
- `customer.contract`
- `purchase.request`
- `purchase.order`

这样可以保证：

- 语义清晰
- 不依赖类名
- 不依赖 URL
- 便于后续做跨端约定或动态查找

第一版不使用：

- Java 全限定类名
- 中文名称
- URL 路径
- 数字编码

## 7. 类职责边界

### 7.1 应迁入 `xbb-erp-scene-meta` 的内容

第一版建议迁入以下内容的去业务化版本：

- `SceneTypeEnum`
- `SceneFieldMeta`
- `SceneFieldProvider`
- `SceneFieldRule`
- `SceneFieldAssembler`
- `AbstractSceneFieldProvider`（可选）

其中：

- `SceneFieldAssembler` 负责将 `SceneFieldMeta` 装配为 `FieldEntity`
- `SceneFieldRule` 负责在同一个业务对象内部对字段列表做扩展或修正
- `AbstractSceneFieldProvider` 只允许提供公共流程，不允许写业务判断

### 7.2 必须留在业务模块内的内容

以下内容保留在各自业务模块：

- 业务对象字段枚举
- 场景字段选择逻辑
- 业务对象独有字段别名
- 业务对象独有子表字段
- 业务对象独有扩展规则

以客户档案为例，以下内容应继续保留在 `xbb-erp-module-customer`：

- `CustomerFieldEnum`
- 客户档案列表别名规则，例如“默认联系人”“默认开票抬头”
- 联系人、地址、银行账户、开票信息等字段语义

原因是这些内容一旦脱离客户领域就不成立，不应进入公共层。

## 8. 一个 module 下有多个业务对象时的组织方式

未来 `xbb-erp-module-customer` 不会只有一个业务对象。

因此边界不能是：

- 一个 module 一个大一统 FieldFactory

正确边界应是：

- 一个业务对象一个 provider
- module 只是承载这些 provider 的目录边界

例如未来可以存在：

- `CustomerArchiveSceneFieldProvider`
- `CustomerContractSceneFieldProvider`
- `CustomerCreditSceneFieldProvider`

因此第一版不建议继续保留 `CustomerFieldFactory` 这类“整个模块唯一工厂”的表达。

更合适的组织方式是：

- 一个业务对象一套字段目录
- 一个业务对象一个 provider
- 一个业务对象一组私有规则

## 9. 目录建议

### 9.1 `xbb-erp-scene-meta`

建议目录：

- `scene/SceneTypeEnum`
- `field/SceneFieldMeta`
- `field/SceneFieldProvider`
- `field/SceneFieldRule`
- `field/SceneFieldAssembler`
- `field/AbstractSceneFieldProvider`

### 9.2 `xbb-erp-module-customer`

建议不要把未来所有 provider 平铺在同一个 `field` 包下。

第一版可以按业务对象分目录，例如：

- `...customer.scene.archive/CustomerArchiveFieldEnum`
- `...customer.scene.archive/CustomerArchiveSceneFieldProvider`
- `...customer.scene.archive/CustomerArchive...Rule`

后续再新增客户合同、授信等对象时，也各自建立独立 scene 目录。

## 10. 第一版实现建议

第一版只迁移“客户档案”这一类业务对象，作为统一协议样板。

### 10.1 第一版落地内容

- 新增模块：`xbb-erp-scene-meta`
- 下沉协议层类与 assembler
- 将当前客户档案字段能力改造成：
  - `CustomerArchiveSceneFieldProvider`
  - `sceneKey = customer.archive`
- 将 `CustomerAdminAppServiceImpl` 改为仅按四个场景消费公共协议

### 10.2 第一版不引入 Registry

第一版不做全局 provider 注册中心。

应用服务直接注入所属业务对象的 provider 即可。

原因：

- 当前重点是跑通边界
- 当前不存在必须按 `sceneKey` 动态查找 provider 的刚性需求
- 提前引入 registry 会先把查找机制复杂化，而不是把协议稳定下来

当未来出现统一“字段中心”接口或动态查找场景时，再补 registry。

## 11. 对现有客户实现的改造方向

当前客户模块中的：

- `CustomerFieldMeta`
- `CustomerFieldRule`
- `CustomerFieldAssembler`

应迁移为去业务化的公共版本。

当前：

- `DefaultCustomerFieldFactory`

不适合作为最终命名。

建议未来重构为：

- `CustomerArchiveSceneFieldProvider`

原因：

- 它不是全局默认实现
- 它只服务于客户档案这一类业务对象
- 后续客户模块还会有其他业务对象，不应继续把所有字段场景挂在一个 `CustomerFieldFactory` 下

## 12. 判断是否应该进入 `scene-meta` 的标准

以后遇到一个字段相关类时，判断是否进入 `scene-meta`，只看一条：

> 这个类脱离 customer / purchase / supplier 语境后，还成立吗？

如果成立，适合进 `scene-meta`。

如果不成立，必须留在业务模块。

例如：

- `SceneFieldMeta`：脱离业务语境仍成立，进入 `scene-meta`
- `CustomerFieldEnum`：脱离客户语境不成立，留在 `customer`
- `CustomerArchiveSceneFieldProvider`：脱离客户档案语境不成立，留在 `customer`

## 13. 迁移顺序

建议迁移顺序如下：

1. 新建 `xbb-erp-scene-meta` 模块
2. 建立 `SceneTypeEnum`、`SceneFieldMeta`、`SceneFieldProvider`、`SceneFieldRule`、`SceneFieldAssembler`
3. 将客户模块中的公共能力去业务化并迁入新模块
4. 将客户档案改造成 `CustomerArchiveSceneFieldProvider`
5. 将 `CustomerAdminAppServiceImpl` 改成按场景从 provider 获取字段
6. 为列表、创建、编辑、详情四个场景补足测试
7. 客户档案样板稳定后，再复制协议模式到采购等其他模块

## 14. 结论

第一版的关键不是一次性抽象所有单据，而是建立一条任何模块都不能偏离的统一协议线。

`xbb-erp-scene-meta` 负责统一字段场景模型，业务模块负责保留自身业务语义。客户档案作为第一个样板完成迁移后，后续采购、商品、供应商等模块可以按同一方式接入，而不会继续在各模块内各长一套字段场景体系。
