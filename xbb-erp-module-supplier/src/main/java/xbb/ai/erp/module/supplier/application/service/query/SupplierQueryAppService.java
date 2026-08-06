package xbb.ai.erp.module.supplier.application.service.query;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;

import java.util.List;

public interface SupplierQueryAppService {
    ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto);

    SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto);

    SupplierDetailVO detail(IdBaseDTO dto);

    List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto);

    ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto);

    SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto);
}
