package xbb.ai.erp.module.product.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.WarehouseListDTO;
import xbb.ai.erp.module.product.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.product.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;

public interface WarehouseAdminAppService {

    ListBaseVO<WarehouseListItemVO> list(WarehouseListDTO dto);

    SaveItemVO<WarehouseSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<WarehouseSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(WarehouseSaveDTO dto);

    WarehouseDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
