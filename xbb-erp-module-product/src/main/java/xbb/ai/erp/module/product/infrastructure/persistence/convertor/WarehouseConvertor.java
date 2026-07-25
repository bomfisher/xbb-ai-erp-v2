package xbb.ai.erp.module.product.infrastructure.persistence.convertor;

import xbb.ai.erp.module.product.domain.model.Warehouse;
import xbb.ai.erp.module.product.infrastructure.persistence.po.WarehousePO;

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
        po.setBizOrgId(warehouse.getBizOrgId());
        po.setWarehouseCode(warehouse.getWarehouseCode());
        po.setWarehouseName(warehouse.getWarehouseName());
        po.setWarehouseType(warehouse.getWarehouseType());
        po.setEnableStatus(warehouse.getEnableStatus());
        po.setAddress(warehouse.getAddress());
        po.setManagerId(warehouse.getManagerId());
        po.setBizStatus(warehouse.getBizStatus());
        po.setCreatorId(warehouse.getCreatorId());
        po.setModifyId(warehouse.getModifyId());
        po.setDel(warehouse.getDeleted());
        po.setAddTime(warehouse.getAddTime());
        po.setUpdateTime(warehouse.getUpdateTime());
        return po;
    }

    public static Warehouse toDomain(WarehousePO po) {
        if (po == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(po.getId());
        warehouse.setCorpid(po.getCorpid());
        warehouse.setBizOrgId(po.getBizOrgId());
        warehouse.setWarehouseCode(po.getWarehouseCode());
        warehouse.setWarehouseName(po.getWarehouseName());
        warehouse.setWarehouseType(po.getWarehouseType());
        warehouse.setEnableStatus(po.getEnableStatus());
        warehouse.setAddress(po.getAddress());
        warehouse.setManagerId(po.getManagerId());
        warehouse.setBizStatus(po.getBizStatus());
        warehouse.setCreatorId(po.getCreatorId());
        warehouse.setModifyId(po.getModifyId());
        warehouse.setDeleted(po.getDel());
        warehouse.setAddTime(po.getAddTime());
        warehouse.setUpdateTime(po.getUpdateTime());
        return warehouse;
    }
}
