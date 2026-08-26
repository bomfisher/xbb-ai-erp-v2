package xbb.ai.erp.module.settlement.admin.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptWriteOffDTO extends BaseDTO {
    private Long receiptId;
    private List<ReceiptWriteOffItemDTO> items;
}
