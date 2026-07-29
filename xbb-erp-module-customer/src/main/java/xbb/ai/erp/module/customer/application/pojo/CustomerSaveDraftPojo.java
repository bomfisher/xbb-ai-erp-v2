package xbb.ai.erp.module.customer.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;

@Data
public class CustomerSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtPojo ext = new CustomerSaveExtPojo();
    private CustomerSectionStatePojo sectionState = new CustomerSectionStatePojo();
    private Long updatedTime;
}
