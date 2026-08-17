package xbb.ai.erp.module.system.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BizNoCounterMapper {

    int insertIgnore(@Param("corpid") String corpid, @Param("businessCode") String businessCode,
                     @Param("periodKey") String periodKey);

    int reserve(@Param("corpid") String corpid, @Param("businessCode") String businessCode,
                @Param("periodKey") String periodKey, @Param("size") int size);

    Long currentReservedValue();
}
