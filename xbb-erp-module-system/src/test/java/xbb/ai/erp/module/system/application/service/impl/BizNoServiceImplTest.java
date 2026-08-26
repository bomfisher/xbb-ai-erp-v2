package xbb.ai.erp.module.system.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.admin.dto.BizNoNextDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleSaveDTO;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.model.BizNoSerialModeEnum;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;

class BizNoServiceImplTest {

    private final BizNoRuleRepository ruleRepository = Mockito.mock(BizNoRuleRepository.class);
    private final BizNoGenerator bizNoGenerator = Mockito.mock(BizNoGenerator.class);
    private final BizNoServiceImpl service = new BizNoServiceImpl(ruleRepository, bizNoGenerator);

    @Test
    void should_save_tenant_scoped_rule() {
        BizNoRuleSaveDTO dto = new BizNoRuleSaveDTO();
        dto.setCorpid("corp-a");
        dto.setBusinessCode("PRODUCT_SPU");
        dto.setPrefix("SPU");
        dto.setRuleType("MASTER_DATA");

        service.saveRule(dto);

        verify(ruleRepository).save(new BizNoRule("corp-a", "PRODUCT_SPU", "SPU", BizNoRuleTypeEnum.MASTER_DATA));
    }

    @Test
    void should_save_all_number_rule_settings_for_current_company() {
        BizNoRuleSaveDTO dto = new BizNoRuleSaveDTO();
        dto.setCorpid("corp-a");
        dto.setBusinessCode("CUSTOMER");
        dto.setPrefix("CUS");
        dto.setIncludeDate(1);
        dto.setSuffixLength(8);
        dto.setSerialMode("DAILY");

        service.saveRule(dto);

        verify(ruleRepository).save(new BizNoRule("corp-a", "CUSTOMER", "CUS", 1, 8,
            BizNoSerialModeEnum.DAILY, BizNoRuleTypeEnum.DOCUMENT));
    }

    @Test
    void should_mark_default_rule_as_not_overridden_in_business_tree() {
        when(ruleRepository.findAvailable("corp-a"))
            .thenReturn(List.of(new BizNoRule("0", "CUSTOMER", "CUS", BizNoRuleTypeEnum.MASTER_DATA)));

        var tree = service.businessTree("corp-a");

        assertEquals(1, tree.size());
        assertEquals("基础资料", tree.getFirst().getBusinessName());
        assertEquals("CUSTOMER", tree.getFirst().getChildren().getFirst().getBusinessCode());
        assertEquals("客户", tree.getFirst().getChildren().getFirst().getBusinessName());
        assertEquals(0, tree.getFirst().getChildren().getFirst().getOverridden());
    }

    @Test
    void should_fall_back_to_business_code_when_rule_is_not_in_business_code_enum() {
        when(ruleRepository.findAvailable("corp-a"))
            .thenReturn(List.of(new BizNoRule("0", "LEGACY_DOCUMENT", "LEG", BizNoRuleTypeEnum.DOCUMENT)));

        var tree = service.businessTree("corp-a");

        assertEquals("LEGACY_DOCUMENT", tree.getFirst().getChildren().getFirst().getBusinessName());
    }

    @Test
    void should_reject_unknown_rule_type() {
        BizNoRuleSaveDTO dto = new BizNoRuleSaveDTO();
        dto.setCorpid("corp-a");
        dto.setBusinessCode("PRODUCT_SPU");
        dto.setPrefix("SPU");
        dto.setRuleType("UNKNOWN");

        assertThrows(BizException.class, () -> service.saveRule(dto));
    }

    @Test
    void should_delegate_next_number_by_corp_and_business_code() {
        BizNoNextDTO dto = new BizNoNextDTO();
        dto.setCorpid("corp-a");
        dto.setBusinessCode("PURCHASE_ORDER");
        when(bizNoGenerator.next("corp-a", "PURCHASE_ORDER")).thenReturn("PO-20260813-00001");

        assertEquals("PO-20260813-00001", service.next(dto).getCode());
    }
}
