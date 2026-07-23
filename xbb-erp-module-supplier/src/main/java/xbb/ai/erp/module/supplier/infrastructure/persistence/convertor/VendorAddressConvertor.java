package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.VendorAddress;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorAddressPO;

public final class VendorAddressConvertor {

    private VendorAddressConvertor() {
    }

    public static VendorAddressPO toPO(VendorAddress vendorAddress) {
        if (vendorAddress == null) {
            return null;
        }
        VendorAddressPO po = new VendorAddressPO();
        po.setId(vendorAddress.getId());
        po.setCorpid(vendorAddress.getCorpid());
        po.setVendorId(vendorAddress.getVendorId());
        po.setAddressType(vendorAddress.getAddressType());
        po.setReceiverName(vendorAddress.getReceiverName());
        po.setReceiverMobile(vendorAddress.getReceiverMobile());
        po.setProvinceCode(vendorAddress.getProvinceCode());
        po.setCityCode(vendorAddress.getCityCode());
        po.setDistrictCode(vendorAddress.getDistrictCode());
        po.setDetailAddress(vendorAddress.getDetailAddress());
        po.setPostalCode(vendorAddress.getPostalCode());
        po.setDefaultFlag(vendorAddress.getDefaultFlag());
        po.setBizStatus(vendorAddress.getBizStatus());
        po.setCreatorId(vendorAddress.getCreatorId());
        po.setModifyId(vendorAddress.getModifyId());
        po.setDeleted(vendorAddress.getDeleted());
        po.setAddTime(vendorAddress.getAddTime());
        po.setUpdateTime(vendorAddress.getUpdateTime());
        po.setVersion(vendorAddress.getVersion());
        return po;
    }

    public static VendorAddress toDomain(VendorAddressPO po) {
        if (po == null) {
            return null;
        }
        VendorAddress vendorAddress = new VendorAddress();
        vendorAddress.setId(po.getId());
        vendorAddress.setCorpid(po.getCorpid());
        vendorAddress.setVendorId(po.getVendorId());
        vendorAddress.setAddressType(po.getAddressType());
        vendorAddress.setReceiverName(po.getReceiverName());
        vendorAddress.setReceiverMobile(po.getReceiverMobile());
        vendorAddress.setProvinceCode(po.getProvinceCode());
        vendorAddress.setCityCode(po.getCityCode());
        vendorAddress.setDistrictCode(po.getDistrictCode());
        vendorAddress.setDetailAddress(po.getDetailAddress());
        vendorAddress.setPostalCode(po.getPostalCode());
        vendorAddress.setDefaultFlag(po.getDefaultFlag());
        vendorAddress.setBizStatus(po.getBizStatus());
        vendorAddress.setCreatorId(po.getCreatorId());
        vendorAddress.setModifyId(po.getModifyId());
        vendorAddress.setDeleted(po.getDeleted());
        vendorAddress.setAddTime(po.getAddTime());
        vendorAddress.setUpdateTime(po.getUpdateTime());
        vendorAddress.setVersion(po.getVersion());
        return vendorAddress;
    }
}
