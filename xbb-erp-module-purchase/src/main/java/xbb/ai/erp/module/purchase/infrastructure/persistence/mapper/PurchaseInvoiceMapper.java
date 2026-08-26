package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoicePO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseInvoiceMapper {
    int insert(PurchaseInvoicePO po);

    int insertBatch(@Param("list") List<PurchaseInvoicePO> poList);

    int removeById(@Param("corpid") String corpid, @Param("id") Long id);

    int removeBatchByIds(@Param("corpid") String corpid, @Param("ids") List<Long> ids);

    int update(PurchaseInvoicePO po);

    int changePayableOpenedAmount(@Param("corpid") String corpid, @Param("id") Long id,
                                  @Param("delta") java.math.BigDecimal delta,
                                  @Param("userId") String userId);

    PurchaseInvoicePO findById(@Param("corpid") String corpid, @Param("id") Long id);

    List<PurchaseInvoicePO> findByCondition(@Param("conditionMap") Map<String, Object> conditionMap);

    Long count(@Param("conditionMap") Map<String, Object> conditionMap);
}
