package xbb.ai.erp.module.customer.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;

public interface CustomerAdminAppService {
    ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto);

    SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(CustomerSaveDTO dto);

    CustomerDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
