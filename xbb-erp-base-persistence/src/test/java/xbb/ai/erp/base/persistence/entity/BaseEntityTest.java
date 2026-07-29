package xbb.ai.erp.base.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseEntityTest {

    @Test
    void should_use_integer_deleted_flag() {
        BaseEntity entity = new BaseEntity();
        entity.setDeleted(0);

        assertEquals(0, entity.getDeleted());
    }
}
