package xbb.ai.erp.base.common.enums;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AuditStatusEnumTest {
    @Test
    void shouldAllowApprovedAndNoNeedApprovedForDownstream() {
        assertTrue(AuditStatusEnum.allowsDownstream(AuditStatusEnum.APPROVED.getCode()));
        assertTrue(AuditStatusEnum.allowsDownstream(AuditStatusEnum.NO_NEED_APPROVED.getCode()));
        assertFalse(AuditStatusEnum.allowsDownstream(AuditStatusEnum.PENDING.getCode()));
        assertFalse(AuditStatusEnum.allowsDownstream(null));
    }
}
