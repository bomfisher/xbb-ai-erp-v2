package xbb.ai.erp.base.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseDTOTest {

    @Test
    void should_store_corpid_and_user_id() {
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("123");

        assertEquals("corp-001", dto.getCorpid());
        assertEquals("123", dto.getUserId());
    }
}
