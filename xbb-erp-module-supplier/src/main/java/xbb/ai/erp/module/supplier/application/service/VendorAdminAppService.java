package xbb.ai.erp.module.supplier.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.VendorListDTO;
import xbb.ai.erp.module.supplier.admin.dto.VendorSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.VendorDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorSaveItemVO;

public interface VendorAdminAppService {
    ListBaseVO<VendorListItemVO> list(VendorListDTO dto);

    SaveItemVO<VendorSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<VendorSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(VendorSaveDTO dto);

    VendorDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
