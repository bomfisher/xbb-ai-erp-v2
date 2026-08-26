package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;

@Data
public class ReceiptSaveItemVO {
    private ReceiptMainDTO main;

    private List<ReceiptPaymentDTO> payments;

    private List<ReceiptWriteOffItemDTO> writeOffs;
}
