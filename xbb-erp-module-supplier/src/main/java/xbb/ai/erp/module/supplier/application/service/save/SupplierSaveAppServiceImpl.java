package xbb.ai.erp.module.supplier.application.service.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierAddressItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBankAccountItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierContactItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierInvoiceProfileItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.supplier.application.assembler.SupplierAdminAssembler;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveContextPojo;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveExtPojo;
import xbb.ai.erp.module.supplier.application.port.SupplierDraftRepository;
import xbb.ai.erp.module.supplier.application.validator.SupplierSaveBusinessValidator;
import xbb.ai.erp.module.supplier.application.validator.SupplierSaveCommonValidator;
import xbb.ai.erp.module.supplier.application.validator.SupplierSaveProtocolValidator;
import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;
import xbb.ai.erp.module.supplier.domain.model.SupplierBankAccount;
import xbb.ai.erp.module.supplier.domain.model.SupplierContact;
import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;
import xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class SupplierSaveAppServiceImpl implements SupplierSaveAppService {

    private final SupplierRepository supplierRepository;
    private final SupplierContactRepository supplierContactRepository;
    private final SupplierAddressRepository supplierAddressRepository;
    private final SupplierBankAccountRepository supplierBankAccountRepository;
    private final SupplierInvoiceProfileRepository supplierInvoiceProfileRepository;
    private final SupplierDraftRepository supplierDraftRepository;
    private final SupplierSaveProtocolValidator protocolValidator;
    private final SupplierSaveCommonValidator commonValidator;
    private final SupplierSaveBusinessValidator businessValidator;
    private final SupplierOwnerPurchaserResolver ownerPurchaserResolver;

    public SupplierSaveAppServiceImpl(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository
    ) {
        this(
            supplierRepository,
            supplierContactRepository,
            supplierAddressRepository,
            supplierBankAccountRepository,
            supplierInvoiceProfileRepository,
            null,
            null
        );
    }

    public SupplierSaveAppServiceImpl(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository,
        SupplierDraftRepository supplierDraftRepository
    ) {
        this(
            supplierRepository,
            supplierContactRepository,
            supplierAddressRepository,
            supplierBankAccountRepository,
            supplierInvoiceProfileRepository,
            supplierDraftRepository,
            null
        );
    }

    @Autowired
    public SupplierSaveAppServiceImpl(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository,
        SupplierDraftRepository supplierDraftRepository,
        SupplierOwnerPurchaserResolver ownerPurchaserResolver
    ) {
        this.supplierRepository = supplierRepository;
        this.supplierContactRepository = supplierContactRepository;
        this.supplierAddressRepository = supplierAddressRepository;
        this.supplierBankAccountRepository = supplierBankAccountRepository;
        this.supplierInvoiceProfileRepository = supplierInvoiceProfileRepository;
        this.supplierDraftRepository = supplierDraftRepository;
        this.protocolValidator = new SupplierSaveProtocolValidator();
        this.commonValidator = new SupplierSaveCommonValidator();
        this.businessValidator = new SupplierSaveBusinessValidator(supplierRepository);
        this.ownerPurchaserResolver = ownerPurchaserResolver;
    }

    @Override
    public BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto) {
        SupplierSaveContextPojo context = SupplierAdminAssembler.toSubmitContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForSubmit(context);
        businessValidator.validateForSubmit(context);

        SupplierSaveExtPojo filteredExt = filterClosedSections(context);
        SupplierSaveDTO saveDTO = new SupplierSaveDTO();
        saveDTO.setCorpid(dto.getCorpid());
        saveDTO.setUserId(dto.getUserId());
        saveDTO.setMain(dto.getMain());
        saveDTO.setContacts(filteredExt.getContacts());
        saveDTO.setAddresses(filteredExt.getAddresses());
        saveDTO.setBankAccounts(filteredExt.getBankAccounts());
        saveDTO.setInvoiceProfiles(filteredExt.getInvoiceProfiles());
        save(saveDTO);

        if (supplierDraftRepository != null && dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
            supplierDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    @Override
    public Long save(SupplierSaveDTO dto) {
        validateDefaultUniqueness(dto.getContacts(), SupplierContactItemDTO::getDefaultFlag, "联系人默认项只能有一个");
        validateDefaultUniqueness(dto.getAddresses(), SupplierAddressItemDTO::getDefaultFlag, "地址默认项只能有一个");
        validateDefaultUniqueness(dto.getBankAccounts(), SupplierBankAccountItemDTO::getDefaultFlag, "银行账户默认项只能有一个");
        validateDefaultUniqueness(dto.getInvoiceProfiles(), SupplierInvoiceProfileItemDTO::getDefaultFlag, "开票信息默认项只能有一个");

        Supplier supplier = SupplierAdminAssembler.toSupplier(dto);
        refreshOwnerPurchaserSnapshot(dto.getCorpid(), supplier);
        applySupplierDefaults(supplier);
        if (supplier.getId() == null) {
            supplierRepository.insert(supplier);
        } else {
            supplierRepository.update(supplier);
        }

        Long supplierId = supplier.getId();
        syncContacts(dto.getCorpid(), supplierId, dto.getUserId(), dto.getContacts());
        syncAddresses(dto.getCorpid(), supplierId, dto.getUserId(), dto.getAddresses());
        syncBankAccounts(dto.getCorpid(), supplierId, dto.getUserId(), dto.getBankAccounts());
        syncInvoiceProfiles(dto.getCorpid(), supplierId, dto.getUserId(), dto.getInvoiceProfiles());
        return supplierId;
    }

    private SupplierSaveExtPojo filterClosedSections(SupplierSaveContextPojo context) {
        SupplierSaveExtPojo filtered = new SupplierSaveExtPojo();
        if (context == null || context.getExt() == null) {
            return filtered;
        }
        if (context.getSectionState() == null) {
            context.setSectionState(new xbb.ai.erp.module.supplier.application.pojo.SupplierSectionStatePojo());
        }
        filtered.setContacts(isOpen(context.getSectionState().getContacts(), context.getExt().getContacts()) ? context.getExt().getContacts() : List.of());
        filtered.setAddresses(isOpen(context.getSectionState().getAddresses(), context.getExt().getAddresses()) ? context.getExt().getAddresses() : List.of());
        filtered.setBankAccounts(isOpen(context.getSectionState().getBankAccounts(), context.getExt().getBankAccounts()) ? context.getExt().getBankAccounts() : List.of());
        filtered.setInvoiceProfiles(isOpen(context.getSectionState().getInvoiceProfiles(), context.getExt().getInvoiceProfiles()) ? context.getExt().getInvoiceProfiles() : List.of());
        return filtered;
    }

    private boolean isOpen(Integer value, List<?> rows) {
        if (value != null) {
            return Integer.valueOf(1).equals(value);
        }
        return rows != null && !rows.isEmpty();
    }

    private void refreshOwnerPurchaserSnapshot(String corpid, Supplier supplier) {
        if (supplier.getOwnerPurchaserId() == null || supplier.getOwnerPurchaserId().isBlank()) {
            supplier.setOwnerPurchaserNameSnapshot(null);
            return;
        }
        if (ownerPurchaserResolver == null) {
            return;
        }
        supplier.setOwnerPurchaserNameSnapshot(ownerPurchaserResolver.resolveName(corpid, supplier.getOwnerPurchaserId()));
    }

    private void applySupplierDefaults(Supplier supplier) {
        long now = System.currentTimeMillis();
        if (supplier.getSupplierCategory() == null || supplier.getSupplierCategory().isBlank()) {
            supplier.setSupplierCategory("A");
        }
        if (supplier.getBizStatus() == null || supplier.getBizStatus().isBlank()) {
            supplier.setBizStatus("1");
        }
        if (supplier.getRefStatus() == null || supplier.getRefStatus().isBlank()) {
            supplier.setRefStatus("0");
        }
        if (supplier.getVersion() == null) {
            supplier.setVersion(0);
        }
        if (supplier.getDel() == null) {
            supplier.setDel(0);
        }
        if (supplier.getAddTime() == null) {
            supplier.setAddTime(now);
        }
        supplier.setUpdateTime(now);
    }

    private void applyContactDefaults(SupplierContact contact, String userId) {
        long now = System.currentTimeMillis();
        if (contact.getBizStatus() == null || contact.getBizStatus().isBlank()) {
            contact.setBizStatus("1");
        }
        if (contact.getVersion() == null) {
            contact.setVersion(0);
        }
        if (contact.getDel() == null) {
            contact.setDel(0);
        }
        if (contact.getAddTime() == null) {
            contact.setAddTime(now);
        }
        contact.setUpdateTime(now);
        if (contact.getCreatorId() == null || contact.getCreatorId().isBlank()) {
            contact.setCreatorId(userId);
        }
        if (contact.getModifyId() == null || contact.getModifyId().isBlank()) {
            contact.setModifyId(userId);
        }
    }

    private void applyAddressDefaults(SupplierAddress address, String userId) {
        long now = System.currentTimeMillis();
        if (address.getBizStatus() == null || address.getBizStatus().isBlank()) {
            address.setBizStatus("1");
        }
        if (address.getVersion() == null) {
            address.setVersion(0);
        }
        if (address.getDel() == null) {
            address.setDel(0);
        }
        if (address.getAddTime() == null) {
            address.setAddTime(now);
        }
        address.setUpdateTime(now);
        if (address.getCreatorId() == null || address.getCreatorId().isBlank()) {
            address.setCreatorId(userId);
        }
        if (address.getModifyId() == null || address.getModifyId().isBlank()) {
            address.setModifyId(userId);
        }
    }

    private void applyBankAccountDefaults(SupplierBankAccount bankAccount, String userId) {
        long now = System.currentTimeMillis();
        if (bankAccount.getBizStatus() == null || bankAccount.getBizStatus().isBlank()) {
            bankAccount.setBizStatus("1");
        }
        if (bankAccount.getVersion() == null) {
            bankAccount.setVersion(0);
        }
        if (bankAccount.getDel() == null) {
            bankAccount.setDel(0);
        }
        if (bankAccount.getAddTime() == null) {
            bankAccount.setAddTime(now);
        }
        bankAccount.setUpdateTime(now);
        if (bankAccount.getCreatorId() == null || bankAccount.getCreatorId().isBlank()) {
            bankAccount.setCreatorId(userId);
        }
        if (bankAccount.getModifyId() == null || bankAccount.getModifyId().isBlank()) {
            bankAccount.setModifyId(userId);
        }
    }

    private void applyInvoiceProfileDefaults(SupplierInvoiceProfile invoiceProfile, String userId) {
        long now = System.currentTimeMillis();
        if (invoiceProfile.getBizStatus() == null || invoiceProfile.getBizStatus().isBlank()) {
            invoiceProfile.setBizStatus("1");
        }
        if (invoiceProfile.getVersion() == null) {
            invoiceProfile.setVersion(0);
        }
        if (invoiceProfile.getDel() == null) {
            invoiceProfile.setDel(0);
        }
        if (invoiceProfile.getAddTime() == null) {
            invoiceProfile.setAddTime(now);
        }
        invoiceProfile.setUpdateTime(now);
        if (invoiceProfile.getCreatorId() == null || invoiceProfile.getCreatorId().isBlank()) {
            invoiceProfile.setCreatorId(userId);
        }
        if (invoiceProfile.getModifyId() == null || invoiceProfile.getModifyId().isBlank()) {
            invoiceProfile.setModifyId(userId);
        }
    }

    private <T> void validateDefaultUniqueness(List<T> list, Function<T, Integer> getter, String message) {
        long count = list == null ? 0 : list.stream().filter(item -> Integer.valueOf(1).equals(getter.apply(item))).count();
        if (count > 1) {
            throw new BizException(message);
        }
    }

    private void syncContacts(String corpid, Long supplierId, String userId, List<SupplierContactItemDTO> items) {
        if (items == null) {
            return;
        }
        for (SupplierContactItemDTO item : items) {
            SupplierContact contact = SupplierAdminAssembler.toSupplierContact(corpid, supplierId, item);
            if (contact.getId() == null) {
                applyContactDefaults(contact, userId);
                supplierContactRepository.insert(contact);
            } else {
                supplierContactRepository.update(contact);
            }
        }
    }

    private void syncAddresses(String corpid, Long supplierId, String userId, List<SupplierAddressItemDTO> items) {
        if (items == null) {
            return;
        }
        for (SupplierAddressItemDTO item : items) {
            SupplierAddress address = SupplierAdminAssembler.toSupplierAddress(corpid, supplierId, item);
            if (address.getId() == null) {
                applyAddressDefaults(address, userId);
                supplierAddressRepository.insert(address);
            } else {
                supplierAddressRepository.update(address);
            }
        }
    }

    private void syncBankAccounts(String corpid, Long supplierId, String userId, List<SupplierBankAccountItemDTO> items) {
        if (items == null) {
            return;
        }
        for (SupplierBankAccountItemDTO item : items) {
            SupplierBankAccount bankAccount = SupplierAdminAssembler.toSupplierBankAccount(corpid, supplierId, item);
            if (bankAccount.getId() == null) {
                applyBankAccountDefaults(bankAccount, userId);
                supplierBankAccountRepository.insert(bankAccount);
            } else {
                supplierBankAccountRepository.update(bankAccount);
            }
        }
    }

    private void syncInvoiceProfiles(String corpid, Long supplierId, String userId, List<SupplierInvoiceProfileItemDTO> items) {
        if (items == null) {
            return;
        }
        for (SupplierInvoiceProfileItemDTO item : items) {
            SupplierInvoiceProfile profile = SupplierAdminAssembler.toSupplierInvoiceProfile(corpid, supplierId, item);
            if (profile.getId() == null) {
                applyInvoiceProfileDefaults(profile, userId);
                supplierInvoiceProfileRepository.insert(profile);
            } else {
                supplierInvoiceProfileRepository.update(profile);
            }
        }
    }
}
