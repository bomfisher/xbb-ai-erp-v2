package xbb.ai.erp.base.common.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseVOTest {

    @Test
    void should_expose_default_ok_flag() {
        BaseVO vo = new BaseVO();
        assertEquals(1, vo.getOk());
    }
}
