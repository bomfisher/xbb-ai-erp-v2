package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;
import java.util.List;

@Data
public class ReceiptDraftDetailVO {
    private String draftCode;
    private ReceiptMainDTO main;
    private List<ReceiptPaymentDTO> payments;
    private List<ReceiptWriteOffItemDTO> writeOffs;
}
