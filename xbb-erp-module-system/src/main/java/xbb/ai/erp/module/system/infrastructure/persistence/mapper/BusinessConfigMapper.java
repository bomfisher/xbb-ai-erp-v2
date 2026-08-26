package xbb.ai.erp.module.system.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.system.infrastructure.persistence.po.BusinessConfigPO;

@Mapper
public interface BusinessConfigMapper {

    BusinessConfigPO findByKey(@Param("corpid") String corpid, @Param("businessCode") String businessCode);

    int insert(BusinessConfigPO po);

    int update(BusinessConfigPO po);

    int remove(@Param("corpid") String corpid, @Param("businessCode") String businessCode, @Param("updateTime") long updateTime);
}
