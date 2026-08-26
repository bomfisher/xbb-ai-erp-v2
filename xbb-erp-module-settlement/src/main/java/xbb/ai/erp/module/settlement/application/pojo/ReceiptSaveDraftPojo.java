package xbb.ai.erp.module.settlement.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;
import java.util.List;

@Data
public class ReceiptSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private ReceiptMainDTO main;
    private List<ReceiptPaymentDTO> payments;
    private List<ReceiptWriteOffItemDTO> writeOffs;
    private Long updatedTime;
}
