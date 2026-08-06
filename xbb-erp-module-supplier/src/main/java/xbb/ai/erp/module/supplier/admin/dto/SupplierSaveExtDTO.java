package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SupplierSaveExtDTO {
    private List<SupplierContactItemDTO> contacts = new ArrayList<>();
    private List<SupplierAddressItemDTO> addresses = new ArrayList<>();
    private List<SupplierBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<SupplierInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
