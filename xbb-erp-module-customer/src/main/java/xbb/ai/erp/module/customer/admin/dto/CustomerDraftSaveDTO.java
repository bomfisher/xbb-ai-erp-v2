package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDraftSaveDTO extends BaseDTO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtDTO ext = new CustomerSaveExtDTO();
    private CustomerSectionStateDTO sectionState = new CustomerSectionStateDTO();
    private CustomerDraftMetaDTO draftMeta = new CustomerDraftMetaDTO();
}
