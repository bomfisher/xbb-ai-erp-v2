package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptSaveDTO extends BaseDTO {
    private ReceiptMainDTO main;

    private List<ReceiptPaymentDTO> payments;

    private List<ReceiptWriteOffItemDTO> writeOffs;
}
