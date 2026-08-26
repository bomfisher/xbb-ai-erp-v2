package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FundAccountListDTO extends BaseDTO {
    private Long id;
    private String accountCode;
    private String accountName;
    private String currency;
    private String bankAccountNo;
    private String accountType;
    private Integer defaultFlag;
    private Integer enabled;
    private String creatorId;
    private String modifyId;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
