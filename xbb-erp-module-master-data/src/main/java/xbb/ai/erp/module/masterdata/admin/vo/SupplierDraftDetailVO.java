package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierMainDTO;

@Data
public class SupplierDraftDetailVO {
    private String draftCode;
    private SupplierMainDTO main;
}
