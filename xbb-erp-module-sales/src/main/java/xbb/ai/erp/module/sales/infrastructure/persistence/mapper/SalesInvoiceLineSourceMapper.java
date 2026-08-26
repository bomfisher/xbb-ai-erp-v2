package xbb.ai.erp.module.sales.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoiceLineSourcePO;

@Mapper
public interface SalesInvoiceLineSourceMapper {
    int insertBatch(@Param("list") List<SalesInvoiceLineSourcePO> sources);

    int removeByInvoiceLineIds(@Param("corpid") String corpid, @Param("invoiceLineIds") List<Long> invoiceLineIds);

    List<SalesInvoiceLineSourcePO> findByInvoiceLineIds(@Param("corpid") String corpid,
                                                         @Param("invoiceLineIds") List<Long> invoiceLineIds);

    List<SalesInvoiceLineSourcePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    BigDecimal sumPostedQuantity(@Param("corpid") String corpid,
                                 @Param("sourceType") String sourceType,
                                 @Param("sourceLineId") Long sourceLineId,
                                 @Param("excludedInvoiceId") Long excludedInvoiceId);

    BigDecimal sumPostedQuantityBySalesOrderItem(@Param("corpid") String corpid,
                                                 @Param("salesOrderItemId") Long salesOrderItemId,
                                                 @Param("excludedInvoiceId") Long excludedInvoiceId);
}
