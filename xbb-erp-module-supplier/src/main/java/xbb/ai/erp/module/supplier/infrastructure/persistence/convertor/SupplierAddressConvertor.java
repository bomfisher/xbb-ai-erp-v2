package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierAddressPO;

public final class SupplierAddressConvertor {

    private SupplierAddressConvertor() {
    }

    public static SupplierAddressPO toPO(SupplierAddress supplierAddress) {
        if (supplierAddress == null) {
            return null;
        }
        SupplierAddressPO po = new SupplierAddressPO();
        po.setId(supplierAddress.getId());
        po.setCorpid(supplierAddress.getCorpid());
        po.setSupplierId(supplierAddress.getSupplierId());
        po.setAddressType(supplierAddress.getAddressType());
        po.setReceiverName(supplierAddress.getReceiverName());
        po.setReceiverMobile(supplierAddress.getReceiverMobile());
        po.setProvinceCode(supplierAddress.getProvinceCode());
        po.setCityCode(supplierAddress.getCityCode());
        po.setDistrictCode(supplierAddress.getDistrictCode());
        po.setDetailAddress(supplierAddress.getDetailAddress());
        po.setPostalCode(supplierAddress.getPostalCode());
        po.setDefaultFlag(supplierAddress.getDefaultFlag());
        po.setBizStatus(supplierAddress.getBizStatus());
        po.setCreatorId(supplierAddress.getCreatorId());
        po.setModifyId(supplierAddress.getModifyId());
        po.setDel(supplierAddress.getDel());
        po.setAddTime(supplierAddress.getAddTime());
        po.setUpdateTime(supplierAddress.getUpdateTime());
        po.setVersion(supplierAddress.getVersion());
        return po;
    }

    public static SupplierAddress toDomain(SupplierAddressPO po) {
        if (po == null) {
            return null;
        }
        SupplierAddress supplierAddress = new SupplierAddress();
        supplierAddress.setId(po.getId());
        supplierAddress.setCorpid(po.getCorpid());
        supplierAddress.setSupplierId(po.getSupplierId());
        supplierAddress.setAddressType(po.getAddressType());
        supplierAddress.setReceiverName(po.getReceiverName());
        supplierAddress.setReceiverMobile(po.getReceiverMobile());
        supplierAddress.setProvinceCode(po.getProvinceCode());
        supplierAddress.setCityCode(po.getCityCode());
        supplierAddress.setDistrictCode(po.getDistrictCode());
        supplierAddress.setDetailAddress(po.getDetailAddress());
        supplierAddress.setPostalCode(po.getPostalCode());
        supplierAddress.setDefaultFlag(po.getDefaultFlag());
        supplierAddress.setBizStatus(po.getBizStatus());
        supplierAddress.setCreatorId(po.getCreatorId());
        supplierAddress.setModifyId(po.getModifyId());
        supplierAddress.setDel(po.getDel());
        supplierAddress.setAddTime(po.getAddTime());
        supplierAddress.setUpdateTime(po.getUpdateTime());
        supplierAddress.setVersion(po.getVersion());
        return supplierAddress;
    }
}
