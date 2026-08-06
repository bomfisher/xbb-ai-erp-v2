package xbb.ai.erp.module.supplier.application.assembler;

import xbb.ai.erp.module.supplier.admin.dto.SupplierAddressItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBankAccountItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierContactItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftMetaDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierInvoiceProfileItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierMainDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveExtDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSectionStateDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftMetaVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveExtVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSectionStateVO;
import xbb.ai.erp.module.supplier.application.pojo.SupplierDraftMetaPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveContextPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveDraftPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveExtPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSectionStatePojo;
import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;
import xbb.ai.erp.module.supplier.domain.model.SupplierBankAccount;
import xbb.ai.erp.module.supplier.domain.model.SupplierContact;
import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;

import java.util.List;

public final class SupplierAdminAssembler {

    private SupplierAdminAssembler() {
    }

    public static SupplierSaveItemVO buildEmptySaveItemVO() {
        return new SupplierSaveItemVO();
    }

    public static SupplierSaveContextPojo toDraftContext(SupplierDraftSaveDTO dto) {
        SupplierSaveContextPojo context = new SupplierSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getExt()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(0);
        return context;
    }

    public static SupplierSaveContextPojo toSubmitContext(SupplierSubmitSaveDTO dto) {
        SupplierSaveContextPojo context = new SupplierSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getExt()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(1);
        return context;
    }

    public static SupplierSaveDraftPojo toDraftPojo(SupplierDraftSaveDTO dto) {
        SupplierSaveDraftPojo draft = new SupplierSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setDraftCode(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftTitle());
        draft.setUpdatedTime(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getUpdatedTime());
        draft.setMain(dto.getMain());
        draft.setExt(toSaveExtPojo(dto.getExt()));
        draft.setSectionState(toSectionStatePojo(dto.getSectionState()));
        return draft;
    }

    public static SupplierDraftListItemVO toDraftListItemVO(SupplierSaveDraftPojo pojo) {
        SupplierDraftListItemVO vo = new SupplierDraftListItemVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        if (pojo.getMain() != null) {
            vo.setSupplierCode(pojo.getMain().getSupplierCode());
            vo.setSupplierName(pojo.getMain().getSupplierName());
        }
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }

    public static SupplierDraftDetailVO toDraftDetailVO(SupplierSaveDraftPojo pojo) {
        SupplierDraftDetailVO vo = new SupplierDraftDetailVO();
        if (pojo == null) {
            return vo;
        }
        if (pojo.getMain() != null) {
            vo.setMain(pojo.getMain());
        }
        if (pojo.getExt() != null) {
            vo.setExt(toSaveExtVO(pojo.getExt()));
        }
        if (pojo.getSectionState() != null) {
            vo.setSectionState(toSectionStateVO(pojo.getSectionState()));
        }
        vo.setDraftMeta(toDraftMetaVO(pojo));
        return vo;
    }

    public static Supplier toSupplier(SupplierSaveDTO dto) {
        Supplier supplier = new Supplier();
        SupplierMainDTO main = dto.getMain();
        if (main != null) {
            supplier.setId(main.getId());
            supplier.setSupplierCode(main.getSupplierCode());
            supplier.setSupplierName(main.getSupplierName());
            supplier.setSupplierShortName(main.getSupplierShortName());
            supplier.setSupplierCategory(main.getSupplierCategory());
            supplier.setMainBusinessCategory(main.getMainBusinessCategory());
            supplier.setOwnerPurchaserId(main.getOwnerPurchaserId());
            supplier.setOwnerPurchaserNameSnapshot(main.getOwnerPurchaserNameSnapshot());
            supplier.setBizStatus(main.getBizStatus());
            supplier.setRefStatus(main.getRefStatus());
            supplier.setDefaultContactId(main.getDefaultContactId());
            supplier.setDefaultAddressId(main.getDefaultAddressId());
            supplier.setDefaultBankAccountId(main.getDefaultBankAccountId());
            supplier.setDefaultInvoiceProfileId(main.getDefaultInvoiceProfileId());
            supplier.setRemark(main.getRemark());
            supplier.setVersion(main.getVersion());
            supplier.setDel(main.getDel());
            supplier.setAddTime(main.getAddTime());
            supplier.setUpdateTime(main.getUpdateTime());
        }
        supplier.setCorpid(dto.getCorpid());
        supplier.setCreatorId(dto.getUserId());
        supplier.setModifyId(dto.getUserId());
        return supplier;
    }

    public static SupplierContact toSupplierContact(String corpid, Long supplierId, SupplierContactItemDTO item) {
        SupplierContact contact = new SupplierContact();
        contact.setId(item.getId());
        contact.setCorpid(corpid);
        contact.setSupplierId(supplierId);
        contact.setContactName(item.getContactName());
        contact.setMobile(item.getMobile());
        contact.setPhone(item.getPhone());
        contact.setEmail(item.getEmail());
        contact.setPositionName(item.getPositionName());
        contact.setDefaultFlag(item.getDefaultFlag());
        contact.setBizStatus(item.getBizStatus());
        contact.setRemark(item.getRemark());
        contact.setVersion(item.getVersion());
        return contact;
    }

    public static SupplierAddress toSupplierAddress(String corpid, Long supplierId, SupplierAddressItemDTO item) {
        SupplierAddress address = new SupplierAddress();
        address.setId(item.getId());
        address.setCorpid(corpid);
        address.setSupplierId(supplierId);
        address.setAddressType(item.getAddressType());
        address.setReceiverName(item.getReceiverName());
        address.setReceiverMobile(item.getReceiverMobile());
        address.setProvinceCode(item.getProvinceCode());
        address.setCityCode(item.getCityCode());
        address.setDistrictCode(item.getDistrictCode());
        address.setDetailAddress(item.getDetailAddress());
        address.setPostalCode(item.getPostalCode());
        address.setDefaultFlag(item.getDefaultFlag());
        address.setBizStatus(item.getBizStatus());
        address.setVersion(item.getVersion());
        return address;
    }

    public static SupplierBankAccount toSupplierBankAccount(String corpid, Long supplierId, SupplierBankAccountItemDTO item) {
        SupplierBankAccount bankAccount = new SupplierBankAccount();
        bankAccount.setId(item.getId());
        bankAccount.setCorpid(corpid);
        bankAccount.setSupplierId(supplierId);
        bankAccount.setAccountName(item.getAccountName());
        bankAccount.setBankName(item.getBankName());
        bankAccount.setAccountNo(item.getAccountNo());
        bankAccount.setAccountUsage(item.getAccountUsage());
        bankAccount.setDefaultFlag(item.getDefaultFlag());
        bankAccount.setBizStatus(item.getBizStatus());
        bankAccount.setRemark(item.getRemark());
        bankAccount.setVersion(item.getVersion());
        return bankAccount;
    }

    public static SupplierInvoiceProfile toSupplierInvoiceProfile(String corpid, Long supplierId, SupplierInvoiceProfileItemDTO item) {
        SupplierInvoiceProfile profile = new SupplierInvoiceProfile();
        profile.setId(item.getId());
        profile.setCorpid(corpid);
        profile.setSupplierId(supplierId);
        profile.setInvoiceTitle(item.getInvoiceTitle());
        profile.setTaxNo(item.getTaxNo());
        profile.setAddressPhone(item.getAddressPhone());
        profile.setBankName(item.getBankName());
        profile.setBankAccountNo(item.getBankAccountNo());
        profile.setDefaultFlag(item.getDefaultFlag());
        profile.setBizStatus(item.getBizStatus());
        profile.setRemark(item.getRemark());
        profile.setVersion(item.getVersion());
        return profile;
    }

    public static SupplierListItemVO toListItemVO(Supplier supplier) {
        SupplierListItemVO vo = new SupplierListItemVO();
        vo.setId(supplier.getId());
        vo.setSupplierCode(supplier.getSupplierCode());
        vo.setSupplierName(supplier.getSupplierName());
        vo.setSupplierShortName(supplier.getSupplierShortName());
        vo.setSupplierCategory(supplier.getSupplierCategory());
        vo.setMainBusinessCategory(supplier.getMainBusinessCategory());
        vo.setOwnerPurchaserNameSnapshot(supplier.getOwnerPurchaserNameSnapshot());
        vo.setBizStatus(supplier.getBizStatus());
        vo.setRefStatus(supplier.getRefStatus());
        vo.setAddTime(supplier.getAddTime());
        vo.setUpdateTime(supplier.getUpdateTime());
        return vo;
    }

    public static SupplierSaveItemVO toSaveItemVO(
        Supplier supplier,
        List<SupplierContact> contacts,
        List<SupplierAddress> addresses,
        List<SupplierBankAccount> bankAccounts,
        List<SupplierInvoiceProfile> invoiceProfiles
    ) {
        SupplierSaveItemVO vo = buildEmptySaveItemVO();
        if (supplier != null) {
            SupplierMainDTO main = new SupplierMainDTO();
            main.setId(supplier.getId());
            main.setCorpid(supplier.getCorpid());
            main.setSupplierCode(supplier.getSupplierCode());
            main.setSupplierName(supplier.getSupplierName());
            main.setSupplierShortName(supplier.getSupplierShortName());
            main.setSupplierCategory(supplier.getSupplierCategory());
            main.setMainBusinessCategory(supplier.getMainBusinessCategory());
            main.setOwnerPurchaserId(supplier.getOwnerPurchaserId());
            main.setOwnerPurchaserNameSnapshot(supplier.getOwnerPurchaserNameSnapshot());
            main.setBizStatus(supplier.getBizStatus());
            main.setRefStatus(supplier.getRefStatus());
            main.setDefaultContactId(supplier.getDefaultContactId());
            main.setDefaultAddressId(supplier.getDefaultAddressId());
            main.setDefaultBankAccountId(supplier.getDefaultBankAccountId());
            main.setDefaultInvoiceProfileId(supplier.getDefaultInvoiceProfileId());
            main.setRemark(supplier.getRemark());
            main.setCreatorId(supplier.getCreatorId());
            main.setModifyId(supplier.getModifyId());
            main.setVersion(supplier.getVersion());
            main.setDel(supplier.getDel());
            main.setAddTime(supplier.getAddTime());
            main.setUpdateTime(supplier.getUpdateTime());
            vo.setMain(main);
        }
        if (contacts != null) {
            vo.setContacts(contacts.stream().map(contact -> {
                SupplierContactItemDTO item = new SupplierContactItemDTO();
                item.setId(contact.getId());
                item.setContactName(contact.getContactName());
                item.setMobile(contact.getMobile());
                item.setPhone(contact.getPhone());
                item.setEmail(contact.getEmail());
                item.setPositionName(contact.getPositionName());
                item.setDefaultFlag(contact.getDefaultFlag());
                item.setBizStatus(contact.getBizStatus());
                item.setRemark(contact.getRemark());
                item.setVersion(contact.getVersion());
                return item;
            }).toList());
        }
        if (addresses != null) {
            vo.setAddresses(addresses.stream().map(address -> {
                SupplierAddressItemDTO item = new SupplierAddressItemDTO();
                item.setId(address.getId());
                item.setAddressType(address.getAddressType());
                item.setReceiverName(address.getReceiverName());
                item.setReceiverMobile(address.getReceiverMobile());
                item.setProvinceCode(address.getProvinceCode());
                item.setCityCode(address.getCityCode());
                item.setDistrictCode(address.getDistrictCode());
                item.setDetailAddress(address.getDetailAddress());
                item.setPostalCode(address.getPostalCode());
                item.setDefaultFlag(address.getDefaultFlag());
                item.setBizStatus(address.getBizStatus());
                item.setVersion(address.getVersion());
                return item;
            }).toList());
        }
        if (bankAccounts != null) {
            vo.setBankAccounts(bankAccounts.stream().map(bankAccount -> {
                SupplierBankAccountItemDTO item = new SupplierBankAccountItemDTO();
                item.setId(bankAccount.getId());
                item.setAccountName(bankAccount.getAccountName());
                item.setBankName(bankAccount.getBankName());
                item.setAccountNo(bankAccount.getAccountNo());
                item.setAccountUsage(bankAccount.getAccountUsage());
                item.setDefaultFlag(bankAccount.getDefaultFlag());
                item.setBizStatus(bankAccount.getBizStatus());
                item.setRemark(bankAccount.getRemark());
                item.setVersion(bankAccount.getVersion());
                return item;
            }).toList());
        }
        if (invoiceProfiles != null) {
            vo.setInvoiceProfiles(invoiceProfiles.stream().map(profile -> {
                SupplierInvoiceProfileItemDTO item = new SupplierInvoiceProfileItemDTO();
                item.setId(profile.getId());
                item.setInvoiceTitle(profile.getInvoiceTitle());
                item.setTaxNo(profile.getTaxNo());
                item.setAddressPhone(profile.getAddressPhone());
                item.setBankName(profile.getBankName());
                item.setBankAccountNo(profile.getBankAccountNo());
                item.setDefaultFlag(profile.getDefaultFlag());
                item.setBizStatus(profile.getBizStatus());
                item.setRemark(profile.getRemark());
                item.setVersion(profile.getVersion());
                return item;
            }).toList());
        }
        vo.setSectionState(buildSectionState(vo));
        return vo;
    }

    public static SupplierDetailVO toDetailVO(SupplierSaveItemVO saveItemVO) {
        SupplierDetailVO detailVO = new SupplierDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    private static SupplierSaveExtPojo toSaveExtPojo(SupplierSaveExtDTO dto) {
        SupplierSaveExtPojo pojo = new SupplierSaveExtPojo();
        if (dto == null) {
            return pojo;
        }
        pojo.setContacts(dto.getContacts());
        pojo.setAddresses(dto.getAddresses());
        pojo.setBankAccounts(dto.getBankAccounts());
        pojo.setInvoiceProfiles(dto.getInvoiceProfiles());
        return pojo;
    }

    private static SupplierSectionStatePojo toSectionStatePojo(SupplierSectionStateDTO dto) {
        SupplierSectionStatePojo pojo = new SupplierSectionStatePojo();
        if (dto == null) {
            return pojo;
        }
        pojo.setContacts(dto.getContacts());
        pojo.setAddresses(dto.getAddresses());
        pojo.setBankAccounts(dto.getBankAccounts());
        pojo.setInvoiceProfiles(dto.getInvoiceProfiles());
        return pojo;
    }

    private static SupplierDraftMetaPojo toDraftMetaPojo(SupplierDraftMetaDTO dto) {
        SupplierDraftMetaPojo pojo = new SupplierDraftMetaPojo();
        if (dto == null) {
            return pojo;
        }
        pojo.setDraftCode(dto.getDraftCode());
        pojo.setDraftTitle(dto.getDraftTitle());
        pojo.setUpdatedTime(dto.getUpdatedTime());
        return pojo;
    }

    private static SupplierSectionStateVO buildSectionState(SupplierSaveItemVO vo) {
        SupplierSectionStateVO sectionState = defaultSectionState();
        sectionState.setContacts(vo.getContacts().isEmpty() ? 0 : 1);
        sectionState.setAddresses(vo.getAddresses().isEmpty() ? 0 : 1);
        sectionState.setBankAccounts(vo.getBankAccounts().isEmpty() ? 0 : 1);
        sectionState.setInvoiceProfiles(vo.getInvoiceProfiles().isEmpty() ? 0 : 1);
        return sectionState;
    }

    private static SupplierSectionStateVO defaultSectionState() {
        SupplierSectionStateVO sectionState = new SupplierSectionStateVO();
        sectionState.setContacts(0);
        sectionState.setAddresses(0);
        sectionState.setBankAccounts(0);
        sectionState.setInvoiceProfiles(0);
        return sectionState;
    }

    private static SupplierSaveExtVO toSaveExtVO(SupplierSaveExtPojo pojo) {
        SupplierSaveExtVO vo = new SupplierSaveExtVO();
        vo.setContacts(pojo.getContacts());
        vo.setAddresses(pojo.getAddresses());
        vo.setBankAccounts(pojo.getBankAccounts());
        vo.setInvoiceProfiles(pojo.getInvoiceProfiles());
        return vo;
    }

    private static SupplierSectionStateVO toSectionStateVO(SupplierSectionStatePojo pojo) {
        SupplierSectionStateVO vo = defaultSectionState();
        vo.setContacts(pojo.getContacts());
        vo.setAddresses(pojo.getAddresses());
        vo.setBankAccounts(pojo.getBankAccounts());
        vo.setInvoiceProfiles(pojo.getInvoiceProfiles());
        return vo;
    }

    private static SupplierDraftMetaVO toDraftMetaVO(SupplierSaveDraftPojo pojo) {
        SupplierDraftMetaVO vo = new SupplierDraftMetaVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }
}
