package xbb.ai.erp.module.product.application.service;

import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationListDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationQueryDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSkuOptionsDTO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSkuOptionVO;

import java.util.List;

public interface SkuSupplierRelationAdminAppService {

    ListBaseVO<SkuSupplierRelationListItemVO> list(SkuSupplierRelationListDTO dto);

    SaveItemVO<SkuSupplierRelationMainDTO> addItem(SkuSupplierRelationQueryDTO dto);

    SaveItemVO<SkuSupplierRelationMainDTO> updateItem(IdBaseDTO dto);

    Long save(SkuSupplierRelationSaveDTO dto);

    BaseVO setDefault(IdBaseDTO dto);

    BaseVO enable(IdBaseDTO dto);

    BaseVO disable(IdBaseDTO dto);

    BaseVO delete(IdBaseDTO dto);

    List<SkuSupplierRelationHistoryItemVO> history(IdBaseDTO dto);

    List<SkuSupplierRelationSkuOptionVO> skuOptionsBySpu(SkuSupplierRelationSkuOptionsDTO dto);
}
