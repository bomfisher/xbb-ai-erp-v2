package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.customer.application.pojo.CustomerDraftMetaPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSectionStatePojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerSubmitSaveDTO extends BaseDTO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtPojo ext = new CustomerSaveExtPojo();
    private CustomerSectionStatePojo sectionState = new CustomerSectionStatePojo();
    private CustomerDraftMetaPojo draftMeta = new CustomerDraftMetaPojo();
}
