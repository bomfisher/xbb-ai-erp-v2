package xbb.ai.erp.base.common.support;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdminParamValidatorTest {

    @Test
    void should_require_corpid() {
        BaseDTO dto = new BaseDTO();

        BizException exception = assertThrows(BizException.class, () -> AdminParamValidator.requireCorpid(dto));

        assertEquals("公司不能为空", exception.getMessage());
    }

    @Test
    void should_require_id_for_id_query() {
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> AdminParamValidator.validateIdQuery(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    @Test
    void should_require_non_empty_id_list_for_batch_delete() {
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> AdminParamValidator.validateBatchDelete(dto));

        assertEquals("idList不能为空", exception.getMessage());
    }

    @Test
    void should_pass_when_batch_delete_parameters_complete() {
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L));

        assertDoesNotThrow(() -> AdminParamValidator.validateBatchDelete(dto));
    }
}
