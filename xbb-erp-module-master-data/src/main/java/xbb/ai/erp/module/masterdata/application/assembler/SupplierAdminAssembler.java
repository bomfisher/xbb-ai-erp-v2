package xbb.ai.erp.module.masterdata.application.assembler;

import xbb.ai.erp.module.masterdata.admin.dto.SupplierMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.masterdata.domain.model.Supplier;

import java.util.Objects;

public final class SupplierAdminAssembler {

    private SupplierAdminAssembler() {
    }

    public static SupplierSaveItemVO buildEmptySaveItemVO() {
        return new SupplierSaveItemVO();
    }

    public static Supplier toSupplier(SupplierSaveDTO dto) {
        Supplier supplier = new Supplier();
        SupplierMainDTO main = dto.getMain();
        if (main != null) {
            supplier.setId(main.getId());
            supplier.setCorpid(main.getCorpid());
            supplier.setSupplierCode(main.getSupplierCode());
            supplier.setSupplierName(main.getSupplierName());
            supplier.setDefaultContactId(main.getDefaultContactId());
            supplier.setMobile(main.getMobile());
            supplier.setAddress(main.getAddress());
            supplier.setEnabled(main.getEnabled());
            supplier.setRemark(main.getRemark());
            if (Objects.isNull(dto.getMain().getId())) {
                supplier.setCreatorId(dto.getUserId());
            }
            supplier.setModifyId(dto.getUserId());
        }
        supplier.setCorpid(dto.getCorpid());
        return supplier;
    }

    public static SupplierListItemVO toListItemVO(Supplier supplier) {
        SupplierListItemVO vo = new SupplierListItemVO();
        vo.setId(supplier.getId());
        vo.setSupplierCode(supplier.getSupplierCode());
        vo.setSupplierName(supplier.getSupplierName());
        vo.setMobile(supplier.getMobile());
        vo.setAddress(supplier.getAddress());
        vo.setEnabled(supplier.getEnabled());
        vo.setRemark(supplier.getRemark());
        vo.setCreatorId(supplier.getCreatorId());
        vo.setModifyId(supplier.getModifyId());
        return vo;
    }

    public static SupplierSaveItemVO toSaveItemVO(Supplier supplier) {
        SupplierSaveItemVO vo = new SupplierSaveItemVO();
        if (supplier == null) {
            return vo;
        }
        SupplierMainDTO main = new SupplierMainDTO();
        main.setId(supplier.getId());
        main.setCorpid(supplier.getCorpid());
        main.setSupplierCode(supplier.getSupplierCode());
        main.setSupplierName(supplier.getSupplierName());
        main.setDefaultContactId(supplier.getDefaultContactId());
        main.setMobile(supplier.getMobile());
        main.setAddress(supplier.getAddress());
        main.setEnabled(supplier.getEnabled());
        main.setRemark(supplier.getRemark());
        main.setCreatorId(supplier.getCreatorId());
        main.setModifyId(supplier.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static SupplierDetailVO toDetailVO(SupplierSaveItemVO saveItemVO) {
        SupplierDetailVO detailVO = new SupplierDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
