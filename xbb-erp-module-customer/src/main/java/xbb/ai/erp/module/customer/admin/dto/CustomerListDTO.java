package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.ListBaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerListDTO extends ListBaseDTO {
    private String keyword;
    private String customerCode;
    private String customerName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String bizStatus;
    private String refStatus;
}
