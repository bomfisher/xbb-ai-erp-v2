package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptPaymentPO;

@Mapper
public interface ReceiptPaymentMapper {

    int insertBatch(@Param("list") List<ReceiptPaymentPO> payments);

    int removeByReceiptId(@Param("corpid") String corpid, @Param("receiptId") Long receiptId);

    List<ReceiptPaymentPO> findByReceiptId(@Param("corpid") String corpid, @Param("receiptId") Long receiptId);
}
