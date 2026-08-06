package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierSaveDTO extends BaseDTO {
    private SupplierMainDTO main;
    private List<SupplierContactItemDTO> contacts = new ArrayList<>();
    private List<SupplierAddressItemDTO> addresses = new ArrayList<>();
    private List<SupplierBankAccountItemDTO> bankAccounts = new ArrayList<>();
    private List<SupplierInvoiceProfileItemDTO> invoiceProfiles = new ArrayList<>();
}
