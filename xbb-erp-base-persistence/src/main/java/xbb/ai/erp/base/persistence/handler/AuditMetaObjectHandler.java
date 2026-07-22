package xbb.ai.erp.base.persistence.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        long now = System.currentTimeMillis();
        if (getFieldValByName("addTime", metaObject) == null) {
            setFieldValByName("addTime", now, metaObject);
        }
        setFieldValByName("updateTime", now, metaObject);
        if (getFieldValByName("del", metaObject) == null) {
            setFieldValByName("del", 0, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
    }
}
