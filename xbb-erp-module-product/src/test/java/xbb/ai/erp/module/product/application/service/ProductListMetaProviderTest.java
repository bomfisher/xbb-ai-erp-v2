package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.product.application.provider.ProductListMetaProvider;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductListMetaProviderTest {

    @Test
    void should_build_product_list_meta() {
        ListMetaProvider provider = new ProductListMetaProvider();
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(BusinessCodeEnum.PRODUCT.getCode());

        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        ListMetaBundlePojo topBundle = provider.buildTopButtonMeta(dto);
        ListMetaBundlePojo bottomBundle = provider.buildBottomButtonMeta(dto);
        ListMetaBundlePojo rowActionBundle = provider.buildRowActionMeta(dto);
        Map<String, xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo> conditionMetaMap = provider.buildFilterConditionMeta(dto);

        assertEquals(BusinessCodeEnum.PRODUCT.getCode(), provider.businessCode());
        assertTrue(provider.buildFilterMeta(dto).isEmpty());
        assertTrue(conditionMetaMap.isEmpty());
        assertEquals(List.of("main.spuCode", "main.spuName", "main.productType", "main.spuEnableStatus"),
            headerList.stream().map(FieldEntity::getAttr).toList());
        assertEquals("新增", topBundle.getTopButtonList().get(0).getButtonName());
        assertEquals("导出", bottomBundle.getBottomButtonList().get(0).getButtonName());
        assertEquals("EDIT", rowActionBundle.getRowActionList().get(0).getActionCode());
        assertEquals("PRIMARY", rowActionBundle.getRowActionList().get(0).getShowMode());
    }
}
