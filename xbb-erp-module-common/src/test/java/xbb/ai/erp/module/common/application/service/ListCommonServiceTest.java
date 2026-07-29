package xbb.ai.erp.module.common.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;
import xbb.ai.erp.module.common.admin.pojo.ListRowActionItemPojo;
import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;
import xbb.ai.erp.module.common.application.service.impl.ListCommonServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class ListCommonServiceTest {

    @Test
    void should_dispatch_to_provider_by_business_code() {
        ListMetaProvider provider = new StubListMetaProvider();
        ListMetaRegistry registry = new ListMetaRegistry(List.of(provider));
        ListCommonServiceImpl service = new ListCommonServiceImpl(registry);
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setBusinessCode("CUSTOMER");
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");

        ListFilterVO filterVO = service.filter(dto);
        ListHeaderVO headerVO = service.header(dto);
        ListTopButtonVO topButtonVO = service.topButton(dto);
        ListBottomButtonVO bottomButtonVO = service.bottomButton(dto);

        assertEquals("customerCode", filterVO.getList().get(0).getAttr());
        assertEquals("TEXT", filterVO.getList().get(0).getFieldType());
        assertEquals(List.of("EQ", "NE", "CONTAINS"), filterVO.getList().get(0).getSupportedSymbols());
        assertEquals("客户编码", filterVO.getList().get(0).getAttrName());
        assertEquals("main.customerCode", headerVO.getList().get(0).getAttr());
        assertEquals("新增", topButtonVO.getList().get(0).getButtonName());
        assertEquals("导出", bottomButtonVO.getList().get(0).getButtonName());
    }

    @Test
    void should_define_independent_provider_methods_for_three_endpoints() {
        try {
            ListMetaProvider.class.getMethod("buildFilterMeta", ListCommonQueryDTO.class);
            ListMetaProvider.class.getMethod("buildHeaderMeta", ListCommonQueryDTO.class);
            ListMetaProvider.class.getMethod("buildTopButtonMeta", ListCommonQueryDTO.class);
            ListMetaProvider.class.getMethod("buildBottomButtonMeta", ListCommonQueryDTO.class);
            ListMetaProvider.class.getMethod("buildRowActionMeta", ListCommonQueryDTO.class);
        } catch (NoSuchMethodException exception) {
            fail(exception);
        }
    }

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
    void should_fail_when_business_code_is_not_registered() {
        ListCommonServiceImpl service = new ListCommonServiceImpl(new ListMetaRegistry(List.of()));
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setBusinessCode("UNKNOWN");

        BizException exception = assertThrows(BizException.class, () -> service.filter(dto));
        assertEquals("未找到业务编码对应的列表元数据提供者: UNKNOWN", exception.getMessage());
    }

    @Test
    void should_define_list_filter_condition_contract() {
        ListFilterCondition condition = new ListFilterCondition();
        condition.setAttr("createTime");
        condition.setFieldType("DATE");
        condition.setSymbol("BETWEEN");
        condition.setValue(List.of("2026-01-01 00:00:00", "2026-01-31 23:59:59"));

        assertEquals("createTime", condition.getAttr());
        assertEquals("DATE", condition.getFieldType());
        assertEquals("BETWEEN", condition.getSymbol());
        assertEquals(List.of("2026-01-01 00:00:00", "2026-01-31 23:59:59"), condition.getValue());
    }

    private static final class StubListMetaProvider implements ListMetaProvider {

        @Override
        public String businessCode() {
            return "CUSTOMER";
        }

        @Override
        public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
            FilterField filterField = new FilterField();
            filterField.setAttr("customerCode");
            filterField.setAttrName("客户编码");
            filterField.setFieldType("TEXT");
            filterField.setSupportedSymbols(List.of("EQ", "NE", "CONTAINS"));
            FieldItem item = new FieldItem();
            item.setValue("enabled");
            item.setText("启用");
            filterField.setItemList(List.of(item));
            return List.of(filterField);
        }

        @Override
        public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
            FieldEntity field = new FieldEntity();
            field.setAttr("main.customerCode");
            field.setAttrName("客户编码");
            field.setFieldType("1");
            return List.of(field);
        }

        @Override
        public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
            ListButtonItemPojo topButton = new ListButtonItemPojo();
            topButton.setButtonCode("ADD");
            topButton.setButtonName("新增");
            topButton.setSort(10);
            topButton.setActionCode("ADD");

            ListMetaBundlePojo bundle = new ListMetaBundlePojo();
            bundle.setTopButtonList(List.of(topButton));
            return bundle;
        }

        @Override
        public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
            ListButtonItemPojo bottomButton = new ListButtonItemPojo();
            bottomButton.setButtonCode("EXPORT");
            bottomButton.setButtonName("导出");
            bottomButton.setSort(20);
            bottomButton.setActionCode("EXPORT");

            ListMetaBundlePojo bundle = new ListMetaBundlePojo();
            bundle.setBottomButtonList(List.of(bottomButton));
            return bundle;
        }

        @Override
        public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
            ListRowActionItemPojo rowAction = new ListRowActionItemPojo();
            rowAction.setActionCode("EDIT");
            rowAction.setActionName("编辑");
            rowAction.setSort(10);
            rowAction.setShowMode("PRIMARY");

            ListMetaBundlePojo bundle = new ListMetaBundlePojo();
            bundle.setRowActionList(List.of(rowAction));
            return bundle;
        }
    }
}
