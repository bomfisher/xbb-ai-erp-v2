package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerSaveExtDTO {
    private List<CustomerContactItemDTO> contacts = new ArrayList<>();
    private List<CustomerAddressItemDTO> addresses = new ArrayList<>();
    private List<CustomerBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<CustomerInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
