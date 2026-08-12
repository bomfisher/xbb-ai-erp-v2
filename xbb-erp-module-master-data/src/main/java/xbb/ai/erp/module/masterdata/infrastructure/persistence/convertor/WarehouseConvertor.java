package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.Warehouse;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.WarehousePO;

public final class WarehouseConvertor {

    private WarehouseConvertor() {
    }

    public static WarehousePO toPO(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        WarehousePO po = new WarehousePO();
        po.setId(warehouse.getId());
        po.setCorpid(warehouse.getCorpid());
        po.setWarehouseCode(warehouse.getWarehouseCode());
        po.setWarehouseName(warehouse.getWarehouseName());
        po.setAddress(warehouse.getAddress());
        po.setOwnerId(warehouse.getOwnerId());
        po.setEnabled(warehouse.getEnabled());
        po.setRemark(warehouse.getRemark());
        po.setCreatorId(warehouse.getCreatorId());
        po.setModifyId(warehouse.getModifyId());
        return po;
    }

    public static Warehouse toDomain(WarehousePO po) {
        if (po == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(po.getId());
        warehouse.setCorpid(po.getCorpid());
        warehouse.setWarehouseCode(po.getWarehouseCode());
        warehouse.setWarehouseName(po.getWarehouseName());
        warehouse.setAddress(po.getAddress());
        warehouse.setOwnerId(po.getOwnerId());
        warehouse.setEnabled(po.getEnabled());
        warehouse.setRemark(po.getRemark());
        warehouse.setCreatorId(po.getCreatorId());
        warehouse.setModifyId(po.getModifyId());
        return warehouse;
    }
}
