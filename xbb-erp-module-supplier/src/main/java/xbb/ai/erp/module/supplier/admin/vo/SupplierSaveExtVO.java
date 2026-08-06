package xbb.ai.erp.module.supplier.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.supplier.admin.dto.SupplierAddressItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBankAccountItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierContactItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierInvoiceProfileItemDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class SupplierSaveExtVO {
    private List<SupplierContactItemDTO> contacts = new ArrayList<>();
    private List<SupplierAddressItemDTO> addresses = new ArrayList<>();
    private List<SupplierBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<SupplierInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
