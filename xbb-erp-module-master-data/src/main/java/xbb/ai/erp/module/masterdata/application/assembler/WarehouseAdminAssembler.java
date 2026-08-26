package xbb.ai.erp.module.masterdata.application.assembler;

import xbb.ai.erp.module.masterdata.admin.dto.WarehouseMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.masterdata.domain.model.Warehouse;

import java.util.Objects;

public final class WarehouseAdminAssembler {

    private WarehouseAdminAssembler() {
    }

    public static WarehouseSaveItemVO buildEmptySaveItemVO() {
        return new WarehouseSaveItemVO();
    }

    public static Warehouse toWarehouse(WarehouseSaveDTO dto) {
        Warehouse warehouse = new Warehouse();
        WarehouseMainDTO main = dto.getMain();
        if (main != null) {
            warehouse.setId(main.getId());
            warehouse.setCorpid(main.getCorpid());
            warehouse.setWarehouseCode(main.getWarehouseCode());
            warehouse.setWarehouseName(main.getWarehouseName());
            warehouse.setAddress(main.getAddress());
            warehouse.setOwnerId(main.getOwnerId());
            warehouse.setEnabled(main.getEnabled());
            warehouse.setRemark(main.getRemark());
            if (Objects.isNull(dto.getMain().getId())) {
                warehouse.setCreatorId(dto.getUserId());
            }
            warehouse.setModifyId(dto.getUserId());
        }
        warehouse.setCorpid(dto.getCorpid());
        return warehouse;
    }

    public static WarehouseListItemVO toListItemVO(Warehouse warehouse) {
        WarehouseListItemVO vo = new WarehouseListItemVO();
        vo.setId(warehouse.getId());
        vo.setWarehouseCode(warehouse.getWarehouseCode());
        vo.setWarehouseName(warehouse.getWarehouseName());
        vo.setAddress(warehouse.getAddress());
        vo.setOwnerId(warehouse.getOwnerId());
        vo.setEnabled(Objects.isNull(warehouse.getEnabled()) ? "" : Objects.toString(warehouse.getEnabled()));
        vo.setRemark(warehouse.getRemark());
        vo.setCreatorId(warehouse.getCreatorId());
        vo.setModifyId(warehouse.getModifyId());
        return vo;
    }

    public static WarehouseSaveItemVO toSaveItemVO(Warehouse warehouse) {
        WarehouseSaveItemVO vo = new WarehouseSaveItemVO();
        if (warehouse == null) {
            return vo;
        }
        WarehouseMainDTO main = new WarehouseMainDTO();
        main.setId(warehouse.getId());
        main.setCorpid(warehouse.getCorpid());
        main.setWarehouseCode(warehouse.getWarehouseCode());
        main.setWarehouseName(warehouse.getWarehouseName());
        main.setAddress(warehouse.getAddress());
        main.setOwnerId(warehouse.getOwnerId());
        main.setEnabled(warehouse.getEnabled());
        main.setRemark(warehouse.getRemark());
        main.setCreatorId(warehouse.getCreatorId());
        main.setModifyId(warehouse.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static WarehouseDetailVO toDetailVO(WarehouseSaveItemVO saveItemVO) {
        WarehouseDetailVO detailVO = new WarehouseDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
