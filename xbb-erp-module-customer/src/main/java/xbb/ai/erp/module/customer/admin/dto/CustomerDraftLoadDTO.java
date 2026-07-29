package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDraftLoadDTO extends BaseDTO {
    private String draftCode;
}
