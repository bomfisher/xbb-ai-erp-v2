package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerAddressPO;

public final class CustomerAddressConvertor {

    private CustomerAddressConvertor() {
    }

    public static CustomerAddressPO toPO(CustomerAddress customerAddress) {
        if (customerAddress == null) {
            return null;
        }
        CustomerAddressPO po = new CustomerAddressPO();
        po.setId(customerAddress.getId());
        po.setCorpid(customerAddress.getCorpid());
        po.setCustomerId(customerAddress.getCustomerId());
        po.setAddressType(customerAddress.getAddressType());
        po.setReceiverName(customerAddress.getReceiverName());
        po.setReceiverMobile(customerAddress.getReceiverMobile());
        po.setProvinceCode(customerAddress.getProvinceCode());
        po.setCityCode(customerAddress.getCityCode());
        po.setDistrictCode(customerAddress.getDistrictCode());
        po.setDetailAddress(customerAddress.getDetailAddress());
        po.setPostalCode(customerAddress.getPostalCode());
        po.setDefaultFlag(customerAddress.getDefaultFlag());
        po.setBizStatus(customerAddress.getBizStatus());
        po.setCreatorId(customerAddress.getCreatorId());
        po.setModifyId(customerAddress.getModifyId());
        po.setVersion(customerAddress.getVersion());
        po.setDel(customerAddress.getDel());
        po.setAddTime(customerAddress.getAddTime());
        po.setUpdateTime(customerAddress.getUpdateTime());
        return po;
    }

    public static CustomerAddress toDomain(CustomerAddressPO po) {
        if (po == null) {
            return null;
        }
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setId(po.getId());
        customerAddress.setCorpid(po.getCorpid());
        customerAddress.setCustomerId(po.getCustomerId());
        customerAddress.setAddressType(po.getAddressType());
        customerAddress.setReceiverName(po.getReceiverName());
        customerAddress.setReceiverMobile(po.getReceiverMobile());
        customerAddress.setProvinceCode(po.getProvinceCode());
        customerAddress.setCityCode(po.getCityCode());
        customerAddress.setDistrictCode(po.getDistrictCode());
        customerAddress.setDetailAddress(po.getDetailAddress());
        customerAddress.setPostalCode(po.getPostalCode());
        customerAddress.setDefaultFlag(po.getDefaultFlag());
        customerAddress.setBizStatus(po.getBizStatus());
        customerAddress.setCreatorId(po.getCreatorId());
        customerAddress.setModifyId(po.getModifyId());
        customerAddress.setVersion(po.getVersion());
        customerAddress.setDel(po.getDel());
        customerAddress.setAddTime(po.getAddTime());
        customerAddress.setUpdateTime(po.getUpdateTime());
        return customerAddress;
    }
}
