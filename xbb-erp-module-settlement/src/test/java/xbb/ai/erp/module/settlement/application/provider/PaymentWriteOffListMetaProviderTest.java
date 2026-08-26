package xbb.ai.erp.module.settlement.application.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;

class PaymentWriteOffListMetaProviderTest {

    private final PaymentWriteOffListMetaProvider provider = new PaymentWriteOffListMetaProvider();

    @Test
    void buildTopButtonMetaShouldExposeAddActionForManualWriteoff() {
        var buttons = provider.buildTopButtonMeta(new ListCommonQueryDTO()).getTopButtonList();

        assertFalse(buttons.isEmpty());
        assertEquals("ADD", buttons.getFirst().getButtonCode());
        assertEquals("ADD", buttons.getFirst().getActionCode());
    }
}
