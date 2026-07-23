package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerInvoiceProfilePO;

public final class CustomerInvoiceProfileConvertor {

    private CustomerInvoiceProfileConvertor() {
    }

    public static CustomerInvoiceProfilePO toPO(CustomerInvoiceProfile invoiceProfile) {
        if (invoiceProfile == null) {
            return null;
        }
        CustomerInvoiceProfilePO po = new CustomerInvoiceProfilePO();
        po.setId(invoiceProfile.getId());
        po.setCorpid(invoiceProfile.getCorpid());
        po.setCustomerId(invoiceProfile.getCustomerId());
        po.setInvoiceTitle(invoiceProfile.getInvoiceTitle());
        po.setTaxNo(invoiceProfile.getTaxNo());
        po.setAddressPhone(invoiceProfile.getAddressPhone());
        po.setBankName(invoiceProfile.getBankName());
        po.setBankAccountNo(invoiceProfile.getBankAccountNo());
        po.setDefaultFlag(invoiceProfile.getDefaultFlag());
        po.setBizStatus(invoiceProfile.getBizStatus());
        po.setRemark(invoiceProfile.getRemark());
        po.setCreatorId(invoiceProfile.getCreatorId());
        po.setModifyId(invoiceProfile.getModifyId());
        po.setVersion(invoiceProfile.getVersion());
        po.setDel(invoiceProfile.getDel());
        po.setAddTime(invoiceProfile.getAddTime());
        po.setUpdateTime(invoiceProfile.getUpdateTime());
        return po;
    }

    public static CustomerInvoiceProfile toDomain(CustomerInvoiceProfilePO po) {
        if (po == null) {
            return null;
        }
        CustomerInvoiceProfile invoiceProfile = new CustomerInvoiceProfile();
        invoiceProfile.setId(po.getId());
        invoiceProfile.setCorpid(po.getCorpid());
        invoiceProfile.setCustomerId(po.getCustomerId());
        invoiceProfile.setInvoiceTitle(po.getInvoiceTitle());
        invoiceProfile.setTaxNo(po.getTaxNo());
        invoiceProfile.setAddressPhone(po.getAddressPhone());
        invoiceProfile.setBankName(po.getBankName());
        invoiceProfile.setBankAccountNo(po.getBankAccountNo());
        invoiceProfile.setDefaultFlag(po.getDefaultFlag());
        invoiceProfile.setBizStatus(po.getBizStatus());
        invoiceProfile.setRemark(po.getRemark());
        invoiceProfile.setCreatorId(po.getCreatorId());
        invoiceProfile.setModifyId(po.getModifyId());
        invoiceProfile.setVersion(po.getVersion());
        invoiceProfile.setDel(po.getDel());
        invoiceProfile.setAddTime(po.getAddTime());
        invoiceProfile.setUpdateTime(po.getUpdateTime());
        return invoiceProfile;
    }
}
