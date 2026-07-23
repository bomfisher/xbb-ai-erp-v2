package xbb.ai.erp.module.supplier.application.assembler;

import xbb.ai.erp.module.supplier.admin.dto.VendorMainDTO;
import xbb.ai.erp.module.supplier.admin.dto.VendorSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.VendorDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorSaveItemVO;
import xbb.ai.erp.module.supplier.domain.model.Vendor;

public final class VendorAdminAssembler {

    private VendorAdminAssembler() {
    }

    public static VendorSaveItemVO buildEmptySaveItemVO() {
        return new VendorSaveItemVO();
    }

    public static Vendor toVendor(VendorSaveDTO dto) {
        Vendor vendor = new Vendor();
        VendorMainDTO main = dto.getMain();
        if (main != null) {
            vendor.setId(main.getId());
            vendor.setCorpid(main.getCorpid());
            vendor.setVendorCode(main.getVendorCode());
            vendor.setVendorName(main.getVendorName());
            vendor.setVendorShortName(main.getVendorShortName());
            vendor.setVendorCategory(main.getVendorCategory());
            vendor.setMainBusinessCategory(main.getMainBusinessCategory());
            vendor.setOwnerPurchaserId(main.getOwnerPurchaserId());
            vendor.setOwnerPurchaserNameSnapshot(main.getOwnerPurchaserNameSnapshot());
            vendor.setBizStatus(main.getBizStatus());
            vendor.setRefStatus(main.getRefStatus());
            vendor.setDefaultContactId(main.getDefaultContactId());
            vendor.setDefaultAddressId(main.getDefaultAddressId());
            vendor.setDefaultBankAccountId(main.getDefaultBankAccountId());
            vendor.setDefaultInvoiceProfileId(main.getDefaultInvoiceProfileId());
            vendor.setRemark(main.getRemark());
            vendor.setCreatorId(main.getCreatorId());
            vendor.setModifyId(main.getModifyId());
            vendor.setVersion(main.getVersion());
            vendor.setDeleted(main.getDeleted());
            vendor.setAddTime(main.getAddTime());
            vendor.setUpdateTime(main.getUpdateTime());
        }
        vendor.setCorpid(dto.getCorpid());
        return vendor;
    }

    public static VendorListItemVO toListItemVO(Vendor vendor) {
        VendorListItemVO vo = new VendorListItemVO();
        vo.setId(vendor.getId());
        vo.setVendorCode(vendor.getVendorCode());
        vo.setVendorName(vendor.getVendorName());
        vo.setVendorShortName(vendor.getVendorShortName());
        vo.setVendorCategory(vendor.getVendorCategory());
        vo.setMainBusinessCategory(vendor.getMainBusinessCategory());
        vo.setOwnerPurchaserNameSnapshot(vendor.getOwnerPurchaserNameSnapshot());
        vo.setBizStatus(vendor.getBizStatus());
        vo.setRefStatus(vendor.getRefStatus());
        vo.setAddTime(vendor.getAddTime());
        vo.setUpdateTime(vendor.getUpdateTime());
        return vo;
    }

    public static VendorSaveItemVO toSaveItemVO(Vendor vendor) {
        VendorSaveItemVO vo = new VendorSaveItemVO();
        if (vendor == null) {
            return vo;
        }
        VendorMainDTO main = new VendorMainDTO();
        main.setId(vendor.getId());
        main.setCorpid(vendor.getCorpid());
        main.setVendorCode(vendor.getVendorCode());
        main.setVendorName(vendor.getVendorName());
        main.setVendorShortName(vendor.getVendorShortName());
        main.setVendorCategory(vendor.getVendorCategory());
        main.setMainBusinessCategory(vendor.getMainBusinessCategory());
        main.setOwnerPurchaserId(vendor.getOwnerPurchaserId());
        main.setOwnerPurchaserNameSnapshot(vendor.getOwnerPurchaserNameSnapshot());
        main.setBizStatus(vendor.getBizStatus());
        main.setRefStatus(vendor.getRefStatus());
        main.setDefaultContactId(vendor.getDefaultContactId());
        main.setDefaultAddressId(vendor.getDefaultAddressId());
        main.setDefaultBankAccountId(vendor.getDefaultBankAccountId());
        main.setDefaultInvoiceProfileId(vendor.getDefaultInvoiceProfileId());
        main.setRemark(vendor.getRemark());
        main.setCreatorId(vendor.getCreatorId());
        main.setModifyId(vendor.getModifyId());
        main.setVersion(vendor.getVersion());
        main.setDeleted(vendor.getDeleted());
        main.setAddTime(vendor.getAddTime());
        main.setUpdateTime(vendor.getUpdateTime());
        vo.setMain(main);
        return vo;
    }

    public static VendorDetailVO toDetailVO(VendorSaveItemVO saveItemVO) {
        VendorDetailVO detailVO = new VendorDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
