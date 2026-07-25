package xbb.ai.erp.module.product.application.assembler;

import xbb.ai.erp.module.product.admin.dto.WarehouseMainDTO;
import xbb.ai.erp.module.product.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.product.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.product.domain.model.Warehouse;

public final class WarehouseAdminAssembler {

    private WarehouseAdminAssembler() {
    }

    public static WarehouseSaveItemVO buildEmptySaveItemVO() {
        WarehouseSaveItemVO saveItemVO = new WarehouseSaveItemVO();
        saveItemVO.setMain(new WarehouseMainDTO());
        return saveItemVO;
    }

    public static Warehouse toWarehouse(WarehouseSaveDTO dto) {
        Warehouse warehouse = new Warehouse();
        WarehouseMainDTO main = dto.getMain();
        if (main != null) {
            warehouse.setId(main.getId());
            warehouse.setCorpid(main.getCorpid());
            warehouse.setBizOrgId(main.getBizOrgId());
            warehouse.setWarehouseCode(main.getWarehouseCode());
            warehouse.setWarehouseName(main.getWarehouseName());
            warehouse.setWarehouseType(main.getWarehouseType());
            warehouse.setEnableStatus(main.getEnableStatus());
            warehouse.setAddress(main.getAddress());
            warehouse.setManagerId(main.getManagerId());
            warehouse.setBizStatus(main.getBizStatus());
            warehouse.setCreatorId(main.getCreatorId());
            warehouse.setModifyId(main.getModifyId());
            warehouse.setDeleted(main.getDeleted());
            warehouse.setAddTime(main.getAddTime());
            warehouse.setUpdateTime(main.getUpdateTime());
        }
        warehouse.setCorpid(dto.getCorpid());
        return warehouse;
    }

    public static WarehouseListItemVO toListItemVO(Warehouse warehouse) {
        WarehouseListItemVO vo = new WarehouseListItemVO();
        vo.setId(warehouse.getId());
        vo.setBizOrgId(warehouse.getBizOrgId());
        vo.setWarehouseCode(warehouse.getWarehouseCode());
        vo.setWarehouseName(warehouse.getWarehouseName());
        vo.setWarehouseType(warehouse.getWarehouseType());
        vo.setEnableStatus(warehouse.getEnableStatus());
        vo.setAddress(warehouse.getAddress());
        vo.setManagerId(warehouse.getManagerId());
        vo.setBizStatus(warehouse.getBizStatus());
        vo.setAddTime(warehouse.getAddTime());
        vo.setUpdateTime(warehouse.getUpdateTime());
        return vo;
    }

    public static WarehouseSaveItemVO toSaveItemVO(Warehouse warehouse) {
        WarehouseSaveItemVO vo = buildEmptySaveItemVO();
        if (warehouse == null) {
            return vo;
        }
        WarehouseMainDTO main = new WarehouseMainDTO();
        main.setId(warehouse.getId());
        main.setCorpid(warehouse.getCorpid());
        main.setBizOrgId(warehouse.getBizOrgId());
        main.setWarehouseCode(warehouse.getWarehouseCode());
        main.setWarehouseName(warehouse.getWarehouseName());
        main.setWarehouseType(warehouse.getWarehouseType());
        main.setEnableStatus(warehouse.getEnableStatus());
        main.setAddress(warehouse.getAddress());
        main.setManagerId(warehouse.getManagerId());
        main.setBizStatus(warehouse.getBizStatus());
        main.setCreatorId(warehouse.getCreatorId());
        main.setModifyId(warehouse.getModifyId());
        main.setDeleted(warehouse.getDeleted());
        main.setAddTime(warehouse.getAddTime());
        main.setUpdateTime(warehouse.getUpdateTime());
        vo.setMain(main);
        return vo;
    }

    public static WarehouseDetailVO toDetailVO(WarehouseSaveItemVO saveItemVO) {
        WarehouseDetailVO detailVO = new WarehouseDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
