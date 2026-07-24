package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.module.BusinessTypeEnum;
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
        dto.setBusinessCode(BusinessTypeEnum.CUSTOMER.getCode());

        ListMetaBundlePojo bundle = provider.buildBaseMeta(dto);
        List<FilterField> filterList = bundle.getFilterList();

        assertEquals(BusinessTypeEnum.CUSTOMER.getCode(), provider.businessCode());
        assertFalse(filterList.isEmpty());
        assertEquals("main.customerCode", filterList.get(0).getAttr());
        assertEquals("新增", bundle.getTopButtonList().get(0).getButtonName());
    }
}
