package xbb.ai.erp.module.org.admin.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.vo.BaseVO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeDetailVO extends BaseVO {
    private EmployeeListItemVO mainData;
    private List<Long> departmentIdList;
    private List<Long> roleIdList;
    private List<RoleListItemVO> roleList;
}
