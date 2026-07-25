package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.pojo.FilterField;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.customer.application.provider.CustomerListMetaProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CustomerListMetaProviderTest {

    @Test
    void should_build_customer_filter_and_default_top_button() {
        ListMetaProvider provider = new CustomerListMetaProvider();
        ListCommonQueryDTO dto = new ListCommonQueryDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setBusinessCode(BusinessCodeEnum.CUSTOMER.getCode());

        List<FilterField> filterList = provider.buildFilterMeta(dto);
        List<FieldEntity> headerList = provider.buildHeaderMeta(dto);
        ListMetaBundlePojo topBundle = provider.buildTopButtonMeta(dto);
        ListMetaBundlePojo bottomBundle = provider.buildBottomButtonMeta(dto);

        assertEquals(BusinessCodeEnum.CUSTOMER.getCode(), provider.businessCode());
        assertFalse(filterList.isEmpty());
        assertFalse(headerList.isEmpty());
        assertEquals("main.customerCode", filterList.get(0).getAttr());
        assertEquals("main.customerCode", headerList.get(0).getAttr());
        assertEquals("新增", topBundle.getTopButtonList().get(0).getButtonName());
        assertEquals("导出", bottomBundle.getBottomButtonList().get(0).getButtonName());
    }
}
