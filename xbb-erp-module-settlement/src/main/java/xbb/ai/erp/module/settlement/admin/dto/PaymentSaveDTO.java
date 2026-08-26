package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentSaveDTO extends BaseDTO {
    private PaymentMainDTO main;
    private java.util.List<PaymentInfoDTO> paymentInfos;
    private java.util.List<PaymentWriteOffItemDTO> writeOffs;
}
