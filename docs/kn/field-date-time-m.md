# DATE/TIME 字段公共组件指导

## 适用范围

本指导约束后端 `xbb.ai.erp.base.common.filed.FieldTypeEnum` 的 `DATE`、`TIME` 字段，以及后台前端公共动态表单组件的渲染协议。

## 字段协议

| 字段类型 | 枚举值 | 业务语义 | 前端组件 | 展示格式 | 提交值 |
| --- | ---: | --- | --- | --- | --- |
| `DATE` | `6` | 年月日 | Element Plus `ElDatePicker`，`type="date"` | `YYYY-MM-DD` | Unix 毫秒时间戳 |
| `TIME` | `7` | 日期时间，精确到秒 | Element Plus `ElDatePicker`，`type="datetime"` | `YYYY-MM-DD HH:mm:ss` | Unix 毫秒时间戳 |

后端字段使用 `Long` 承载时间戳，数据库字段通常使用 `bigint`。前端不得把 `DATE`、`TIME` 当作普通文本输入，也不得把展示字符串直接提交给保存接口。

## 后端实现

1. 字段元数据通过 `FieldEntity.fieldType` 返回 `String.valueOf(FieldTypeEnum.DATE.getType())` 或 `String.valueOf(FieldTypeEnum.TIME.getType())`。
2. 采购订单交货日期使用 `main.deliveryDate`，类型为 `FieldTypeEnum.DATE`，可选时 `required=0`。
3. 保存 DTO、领域模型和持久化对象保持时间戳类型一致；空值保持 `null`，不能用空字符串代替。
4. 通用字段校验允许 `DATE`、`TIME` 的时间戳字符串通过协议，业务模块仍需按自身场景声明必填规则。

## 前端实现

公共表单入口为 `apps/admin-web/src/components/form/DynamicFormField.vue`，字段类型映射集中在 `formSchema.ts`：

- `6` 或 `date` 渲染日期选择器。
- `7`、`time` 或 `datetime` 渲染日期时间选择器。
- 统一设置 `value-format="x"`，确保回填和提交均使用 Unix 毫秒时间戳。
- `TIME` 必须显示并选择到秒，不使用仅到分钟的时间组件。
- `editable != 1` 时控件禁用；字段标签、必填标记和属性路径继续由后端元数据驱动。

Element Plus 通过后台应用的 resolver 按需装载，组件不在应用入口全量注册。

## 采购订单验收

- 新建和编辑采购订单的 `main.deliveryDate` 均显示日期选择器。
- 选择日期后提交值为毫秒时间戳，可被后端 `Long deliveryDate` 接收。
- 后端返回已有时间戳时，编辑表单可以正确回填日期。
- 其他业务字段声明 `FieldTypeEnum.TIME` 时，无需新增页面分支即可获得秒级日期时间选择能力。

## 验证

```bash
cd /Users/bomfish/xbb-ai-erp-v2-front
pnpm --filter @xbb-erp/admin-web test -- src/components/form/formSchema.spec.ts
pnpm --filter @xbb-erp/admin-web typecheck

cd /Users/bomfish/xbb-ai-erp-v2
scripts/harness-verify.sh
```
