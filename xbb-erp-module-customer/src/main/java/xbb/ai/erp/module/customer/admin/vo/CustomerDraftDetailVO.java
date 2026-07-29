package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.application.pojo.CustomerDraftMetaPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSectionStatePojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;

@Data
public class CustomerDraftDetailVO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtPojo ext = new CustomerSaveExtPojo();
    private CustomerSectionStatePojo sectionState = new CustomerSectionStatePojo();
    private CustomerDraftMetaPojo draftMeta = new CustomerDraftMetaPojo();
}
