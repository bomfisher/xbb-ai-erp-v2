package xbb.ai.erp.module.system.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.admin.dto.BizNoNextDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleSaveDTO;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
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
