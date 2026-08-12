package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerContactDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerSaveItemVO {
    private CustomerMainDTO main;
    private List<CustomerContactDTO> contacts = new ArrayList<>();
}
