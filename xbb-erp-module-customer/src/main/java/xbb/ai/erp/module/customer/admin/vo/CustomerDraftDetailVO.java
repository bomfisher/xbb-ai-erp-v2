package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;

@Data
public class CustomerDraftDetailVO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private CustomerSaveExtVO ext = new CustomerSaveExtVO();
    private CustomerSectionStateVO sectionState = new CustomerSectionStateVO();
    private CustomerDraftMetaVO draftMeta = new CustomerDraftMetaVO();
}
