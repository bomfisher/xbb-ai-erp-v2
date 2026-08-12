package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDraftSaveDTO extends CustomerSaveDTO {
    private CustomerDraftMetaDTO draftMeta = new CustomerDraftMetaDTO();
}
