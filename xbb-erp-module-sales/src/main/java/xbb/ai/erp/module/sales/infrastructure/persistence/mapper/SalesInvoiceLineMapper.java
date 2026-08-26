package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoiceLinePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesInvoiceLineMapper {
    int insert(SalesInvoiceLinePO po);

    int insertBatch(@Param("list") List<SalesInvoiceLinePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SalesInvoiceLinePO po);

    SalesInvoiceLinePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SalesInvoiceLinePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
