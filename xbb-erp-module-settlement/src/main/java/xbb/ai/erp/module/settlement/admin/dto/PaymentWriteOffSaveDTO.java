package xbb.ai.erp.module.settlement.admin.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentWriteOffSaveDTO extends BaseDTO {
    private Long supplierId;
    private String writeoffNo;
    private Long writeoffDate;
    private String remark;
    private List<PaymentWriteOffAllocationDTO> allocations;
}
