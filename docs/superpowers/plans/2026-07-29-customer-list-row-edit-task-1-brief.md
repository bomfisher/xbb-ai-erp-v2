# Task 1 Brief

## 任务定位
为通用列表元数据体系新增 `rowAction` 出口，供客户列表首版行内 `EDIT` 动作接入使用。这是后续前端通用操作列和客户编辑抽屉编排的后端基础。

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
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/pojo/ListRowActionItemPojo.java`
- Create: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/vo/ListRowActionVO.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/pojo/ListMetaBundlePojo.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/provider/ListMetaProvider.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/ListCommonService.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/application/service/impl/ListCommonServiceImpl.java`
- Modify: `xbb-erp-module-common/src/main/java/xbb/ai/erp/module/common/admin/ListCommonController.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/admin/ListCommonControllerStructureTest.java`
- Test: `xbb-erp-module-common/src/test/java/xbb/ai/erp/module/common/application/service/ListCommonServiceTest.java`

## Interfaces
- Consumes: `ListMetaProvider#buildTopButtonMeta(ListCommonQueryDTO)`、`ListMetaProvider#buildBottomButtonMeta(ListCommonQueryDTO)` 现有元数据装配模式。
- Produces:
  - `ListMetaProvider#buildRowActionMeta(ListCommonQueryDTO dto): ListMetaBundlePojo`
  - `ListCommonService#rowAction(ListCommonQueryDTO dto): ListRowActionVO`
  - `ListRowActionVO#getList(): List<ListRowActionItemPojo>`
  - `ListMetaBundlePojo#setRowActionList(List<ListRowActionItemPojo>)`

## Required Test Additions
```java
@Test
void should_dispatch_row_action_meta_by_business_code() {
    ListMetaProvider provider = new StubListMetaProvider();
    ListMetaRegistry registry = new ListMetaRegistry(List.of(provider));
    ListCommonServiceImpl service = new ListCommonServiceImpl(registry);
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setBusinessCode("CUSTOMER");
    dto.setCorpid("corp-001");
    dto.setUserId("user-001");

    ListRowActionVO rowActionVO = service.rowAction(dto);

    assertEquals("EDIT", rowActionVO.getList().get(0).getActionCode());
    assertEquals("编辑", rowActionVO.getList().get(0).getActionName());
    assertEquals("PRIMARY", rowActionVO.getList().get(0).getShowMode());
}

@Test
void should_define_row_action_endpoint_on_common_controller() throws Exception {
    Method rowAction = ListCommonController.class.getMethod("rowAction", ListCommonQueryDTO.class);
    assertNotNull(rowAction);
}
```

## Required Commands
- Failing test: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`
- Passing test: `mvn -pl xbb-erp-module-common -Dtest=ListCommonServiceTest,ListCommonControllerStructureTest test`

## Required Implementation Snippets
```java
@Data
public class ListRowActionItemPojo {
    private String actionCode;
    private String actionName;
    private Integer sort;
    private String showMode;
    private String confirmType;
}
```

```java
@Data
public class ListRowActionVO {
    private List<ListRowActionItemPojo> list;
}
```

```java
public interface ListMetaProvider {
    String businessCode();
    List<FilterField> buildFilterMeta(ListCommonQueryDTO dto);
    List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto);
    ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto);
    ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto);
    ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto);

    default void applyPackageExtension(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
    }

    default void applyPermissionTrim(ListMetaBundlePojo bundle, ListMetaContextPojo context) {
    }
}
```

```java
@Data
public class ListMetaBundlePojo {
    private List<FilterField> filterList;
    private List<ListButtonItemPojo> topButtonList;
    private List<ListButtonItemPojo> bottomButtonList;
    private List<ListRowActionItemPojo> rowActionList;
}
```

```java
@PostMapping("/rowAction")
public ResultVO<ListRowActionVO> rowAction(@RequestBody ListCommonQueryDTO dto) {
    return ResultVO.success(listCommonService.rowAction(dto));
}
```

```java
@Override
public ListRowActionVO rowAction(ListCommonQueryDTO dto) {
    ListMetaProvider provider = registry.get(dto.getBusinessCode());
    ListMetaBundlePojo bundle = provider.buildRowActionMeta(dto);
    ListRowActionVO vo = new ListRowActionVO();
    vo.setList(bundle.getRowActionList());
    return vo;
}
```

```java
@Override
public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
    return new ListMetaBundlePojo();
}
```

## Additional Execution Rules
- 必须先写失败测试，再实现最小代码，再回归目标测试。
- 不要创建 git commit；用户明确不允许本次提交。
- 完成后把完整结果写入同目录报告文件 `2026-07-29-customer-list-row-edit-task-1-report.md`。
- 报告必须包含：状态（DONE / DONE_WITH_CONCERNS / NEEDS_CONTEXT / BLOCKED）、修改文件列表、运行过的命令、关键测试输出摘要、自检结论、任何 concerns。
