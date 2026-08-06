package xbb.ai.erp.module.supplier.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;

public interface SupplierSaveAppService {
    BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto);

    Long save(SupplierSaveDTO dto);
}
