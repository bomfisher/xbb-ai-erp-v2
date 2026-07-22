package xbb.ai.erp.base.persistence.entity;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.persistence.handler.AuditMetaObjectHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BaseEntityTest {

    @Test
    void should_fill_add_time_update_time_and_del() {
        BaseEntity entity = new BaseEntity();
        AuditMetaObjectHandler handler = new AuditMetaObjectHandler();
        MetaObject metaObject = SystemMetaObject.forObject(entity);

        handler.insertFill(metaObject);

        assertNotNull(entity.getAddTime());
        assertNotNull(entity.getUpdateTime());
        assertEquals(0, entity.getDel());
    }
}
