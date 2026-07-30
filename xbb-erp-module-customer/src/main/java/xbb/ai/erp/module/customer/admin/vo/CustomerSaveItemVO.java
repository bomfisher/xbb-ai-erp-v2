package xbb.ai.erp.module.customer.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerSaveItemVO {
    private CustomerMainDTO main = new CustomerMainDTO();
    private List<CustomerContactItemDTO> contacts = new ArrayList<>();
    private List<CustomerAddressItemDTO> addresses = new ArrayList<>();
    private List<CustomerBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<CustomerInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
    private CustomerSectionStateVO sectionState = new CustomerSectionStateVO();
}
