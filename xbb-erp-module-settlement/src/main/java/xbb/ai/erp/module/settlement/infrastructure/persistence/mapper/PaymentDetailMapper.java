package xbb.ai.erp.module.settlement.infrastructure.persistence.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentDetailPO;

@Mapper
public interface PaymentDetailMapper {

    int insertBatch(@Param("list") List<PaymentDetailPO> details);

    int removeByPaymentId(@Param("corpid") String corpid, @Param("paymentId") Long paymentId);

    List<PaymentDetailPO> findByPaymentId(@Param("corpid") String corpid, @Param("paymentId") Long paymentId);
}
