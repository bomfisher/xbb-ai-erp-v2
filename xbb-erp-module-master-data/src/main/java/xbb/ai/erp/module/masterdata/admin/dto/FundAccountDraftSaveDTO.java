package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FundAccountDraftSaveDTO extends FundAccountSaveDTO {
    private FundAccountDraftMetaDTO draftMeta = new FundAccountDraftMetaDTO();
}
