package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoicePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesInvoiceMapper {
    int insert(SalesInvoicePO po);

    int insertBatch(@Param("list") List<SalesInvoicePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(SalesInvoicePO po);

    int changeReceivableOpenedAmount(@Param("corpid") String corpid, @Param("id") Long id,
                                     @Param("delta") java.math.BigDecimal delta,
                                     @Param("userId") String userId);

    SalesInvoicePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<SalesInvoicePO> findByIds(@Param("corpid") String corpid, @Param("ids") java.util.Collection<Long> ids);

    List<SalesInvoicePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
