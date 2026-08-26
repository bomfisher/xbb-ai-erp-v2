package xbb.ai.erp.module.purchase.infrastructure.persistence.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoiceLinePO;

@Mapper
public interface PurchaseInvoiceLineMapper {
    int insertBatch(@Param("list") List<PurchaseInvoiceLinePO> lines);
    int removeByInvoiceId(@Param("corpid") String corpid, @Param("invoiceId") Long invoiceId);
    List<PurchaseInvoiceLinePO> findByInvoiceId(@Param("corpid") String corpid, @Param("invoiceId") Long invoiceId);
}
