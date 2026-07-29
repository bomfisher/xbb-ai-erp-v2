package xbb.ai.erp.module.customer.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerListDTO extends ListBaseDTO {
    private String keyword;
    private List<ListFilterCondition> conditions;
}
