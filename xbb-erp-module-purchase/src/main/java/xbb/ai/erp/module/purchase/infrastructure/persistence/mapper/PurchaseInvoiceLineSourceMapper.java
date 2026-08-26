package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoiceLineSourcePO;

@Mapper
public interface PurchaseInvoiceLineSourceMapper {
    int insertBatch(@Param("list") List<PurchaseInvoiceLineSourcePO> sources);
    int removeByInvoiceLineIds(@Param("corpid") String corpid, @Param("lineIds") List<Long> lineIds);
    List<PurchaseInvoiceLineSourcePO> findByInvoiceLineIds(@Param("corpid") String corpid, @Param("lineIds") List<Long> lineIds);

    BigDecimal sumInvoiceQuantityByPurchaseOrderItem(@Param("corpid") String corpid,
                                                      @Param("purchaseOrderItemId") Long purchaseOrderItemId);
}
