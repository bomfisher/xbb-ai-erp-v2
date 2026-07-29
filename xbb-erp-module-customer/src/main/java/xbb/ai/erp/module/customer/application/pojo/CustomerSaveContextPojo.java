package xbb.ai.erp.module.customer.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;

@Data
public class CustomerSaveContextPojo {
    private String corpid;
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtPojo ext = new CustomerSaveExtPojo();
    private CustomerSectionStatePojo sectionState = new CustomerSectionStatePojo();
    private CustomerDraftMetaPojo draftMeta = new CustomerDraftMetaPojo();
    private Integer submitMode;
}
