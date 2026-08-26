package xbb.ai.erp.module.settlement.admin.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptWriteOffSaveDTO extends BaseDTO {
    private Long id;
    private String writeoffNo;
    private Long customerId;
    private Long receiptId;
    private Long receivableId;
    private Long writeoffDate;
    private BigDecimal amount;
    private String remark;
    private List<ReceiptWriteOffAllocationDTO> allocations;
}
