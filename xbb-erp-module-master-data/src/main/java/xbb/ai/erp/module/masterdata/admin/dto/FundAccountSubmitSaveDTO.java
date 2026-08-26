package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FundAccountSubmitSaveDTO extends FundAccountSaveDTO {
    private FundAccountDraftMetaDTO draftMeta = new FundAccountDraftMetaDTO();
}
