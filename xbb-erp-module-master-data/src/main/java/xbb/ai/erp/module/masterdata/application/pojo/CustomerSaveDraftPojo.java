package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerContactDTO;

import java.util.List;

@Data
public class CustomerSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private CustomerMainDTO main;
    private List<CustomerContactDTO> contacts;
    private Long updatedTime;
}
