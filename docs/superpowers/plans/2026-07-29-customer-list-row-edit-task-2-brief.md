# Task 2 Brief

## 任务定位
为客户列表首版下发 `EDIT` 行内动作元数据，作为前端通用操作列接入客户编辑链路的最小业务接入点。这个任务建立在 Task 1 已经打通公共 `/erp/v1/common/list/rowAction` 协议之上。

## Global Constraints
- 项目架构按 DDD 领域驱动设计落位。
- 对话永远在中文语境下，注释使用中文。
- 所有主动捕获的报错、业务的主动抛错，都使用 `BizException`。
- 非脚本接口入参 DTO 统一继承 `BaseDTO`，接口参数返回统一使用 `ResultVO.success()` 包装。
- `userId` 员工 Id 是字符串 id。
- getter / setter 用 Lombok 管理。
- 不做前端本地硬编码权限判断；动作集合由后端元数据下发。
- 行内操作列放在表格最后一列，并固定在右侧。
- 行内动作统一采用“主动作直出，其余进入更多”的形态。
- 客户编辑复用现有新建侧开抽屉，统一演进为 `create / edit` 双模式。
- 本次只落地客户 `EDIT`，不提前接入删除、停用、提交等其他动作。

## Files
- Modify: `xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/service/CustomerListMetaProviderTest.java`
- Test: `xbb-erp-module-customer/src/test/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProviderStructureTest.java`

## Interfaces
- Consumes: `ListMetaProvider#buildRowActionMeta(ListCommonQueryDTO dto)` from Task 1.
- Produces:
  - `CustomerListMetaProvider#buildRowActionMeta(ListCommonQueryDTO dto): ListMetaBundlePojo`
  - `ListMetaBundlePojo#getRowActionList(): List<ListRowActionItemPojo>` returning customer `EDIT` action

## Required Test Additions
```java
@Test
void should_build_customer_row_action_meta() {
    CustomerListMetaProvider provider = new CustomerListMetaProvider(new DefaultCustomerFieldFactory(List.of()));
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");
    dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

    ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);

    assertEquals(1, rowActionBundle.getRowActionList().size());
    assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
    assertEquals("编辑", rowActionBundle.getRowActionList().get(0).getActionName());
    assertEquals("PRIMARY", rowActionBundle.getRowActionList().get(0).getShowMode());
}
```

## Required Commands
- Failing test: `mvn -pl xbb-erp-module-customer -Dtest=CustomerListMetaProviderTest,CustomerListMetaProviderStructureTest test`
- Passing test: `mvn -pl xbb-erp-module-customer -am -Dsurefire.failIfNoSpecifiedTests=false -Dtest=CustomerListMetaProviderTest,CustomerListMetaProviderStructureTest test`

## Required Implementation Snippet
```java
@Override
public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
    ListMetaBundlePojo bundle = new ListMetaBundlePojo();
    bundle.setRowActionList(List.of(buildRowAction("EDIT", "编辑", 10, "PRIMARY", "NONE")));
    return bundle;
}

private ListRowActionItemPojo buildRowAction(String actionCode, String actionName, Integer sort, String showMode, String confirmType) {
    ListRowActionItemPojo item = new ListRowActionItemPojo();
    item.setActionCode(actionCode);
    item.setActionName(actionName);
    item.setSort(sort);
    item.setShowMode(showMode);
    item.setConfirmType(confirmType);
    return item;
}
```

## Additional Execution Rules
- 必须先写失败测试，再实现最小代码，再回归目标测试。
- 只改客户列表元数据 provider 相关文件，不要修改 `CustomerAdminAppServiceImpl.java` 与 `CustomerSaveServiceTest.java`。
- 用户明确不允许本次创建 git commit；你必须完成实现和测试，但不要提交。
- 完成后把完整结果写入同目录报告文件 `2026-07-29-customer-list-row-edit-task-2-report.md`。
- 报告必须包含：状态（DONE / DONE_WITH_CONCERNS / NEEDS_CONTEXT / BLOCKED）、修改文件列表、运行过的命令、关键测试输出摘要、自检结论、任何 concerns。
