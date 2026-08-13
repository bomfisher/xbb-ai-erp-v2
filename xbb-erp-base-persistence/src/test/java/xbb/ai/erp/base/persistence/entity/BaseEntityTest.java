package xbb.ai.erp.base.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseEntityTest {

    @Test
    void should_use_integer_del_flag() {
        BaseEntity entity = new BaseEntity();
        entity.setDel(0);

        assertEquals(0, entity.getDel());
    }
}
