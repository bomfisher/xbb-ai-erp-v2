package xbb.ai.erp.module.supplier.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftSaveVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.supplier.application.port.SupplierDraftRepository;
import xbb.ai.erp.module.supplier.application.service.SupplierAdminAppService;
import xbb.ai.erp.module.supplier.application.service.delete.SupplierDeleteAppService;
import xbb.ai.erp.module.supplier.application.service.draft.SupplierDraftAppService;
import xbb.ai.erp.module.supplier.application.service.draft.SupplierDraftAppServiceImpl;
import xbb.ai.erp.module.supplier.application.service.query.SupplierQueryAppService;
import xbb.ai.erp.module.supplier.application.service.save.SupplierOwnerPurchaserResolver;
import xbb.ai.erp.module.supplier.application.service.save.SupplierSaveAppService;
import xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.List;

@Service
public class SupplierAdminAppServiceImpl implements SupplierAdminAppService {

    private final SupplierQueryAppService supplierQueryAppService;
    private final SupplierSaveAppService supplierSaveAppService;
    private final SupplierDeleteAppService supplierDeleteAppService;
    private final SupplierDraftAppService supplierDraftAppService;

    public SupplierAdminAppServiceImpl(
        SupplierQueryAppService supplierQueryAppService,
        SupplierSaveAppService supplierSaveAppService,
        SupplierDeleteAppService supplierDeleteAppService,
        SupplierDraftAppService supplierDraftAppService
    ) {
        this.supplierQueryAppService = supplierQueryAppService;
        this.supplierSaveAppService = supplierSaveAppService;
        this.supplierDeleteAppService = supplierDeleteAppService;
        this.supplierDraftAppService = supplierDraftAppService;
    }

    public static SupplierAdminAppServiceImpl forTesting(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository
    ) {
        return forTesting(
            supplierRepository,
            supplierContactRepository,
            supplierAddressRepository,
            supplierBankAccountRepository,
            supplierInvoiceProfileRepository,
            null,
            null
        );
    }

    public static SupplierAdminAppServiceImpl forTesting(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository,
        SupplierDraftRepository supplierDraftRepository
    ) {
        return forTesting(
            supplierRepository,
            supplierContactRepository,
            supplierAddressRepository,
            supplierBankAccountRepository,
            supplierInvoiceProfileRepository,
            supplierDraftRepository,
            null
        );
    }

    public static SupplierAdminAppServiceImpl forTesting(
        SupplierRepository supplierRepository,
        SupplierContactRepository supplierContactRepository,
        SupplierAddressRepository supplierAddressRepository,
        SupplierBankAccountRepository supplierBankAccountRepository,
        SupplierInvoiceProfileRepository supplierInvoiceProfileRepository,
        SupplierDraftRepository supplierDraftRepository,
        SupplierOwnerPurchaserResolver ownerPurchaserResolver
    ) {
        SupplierAdminAppServiceImpl service = new SupplierAdminAppServiceImpl(
            new xbb.ai.erp.module.supplier.application.service.query.SupplierQueryAppServiceImpl(
                supplierRepository,
                supplierContactRepository,
                supplierAddressRepository,
                supplierBankAccountRepository,
                supplierInvoiceProfileRepository
            ),
            new xbb.ai.erp.module.supplier.application.service.save.SupplierSaveAppServiceImpl(
                supplierRepository,
                supplierContactRepository,
                supplierAddressRepository,
                supplierBankAccountRepository,
                supplierInvoiceProfileRepository,
                supplierDraftRepository,
                ownerPurchaserResolver
            ),
            new xbb.ai.erp.module.supplier.application.service.delete.SupplierDeleteAppServiceImpl(supplierRepository),
            new SupplierDraftAppServiceImpl(supplierDraftRepository)
        );
        return service;
    }

    @Override
    public ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto) {
        return supplierQueryAppService.list(dto);
    }

    @Override
    public SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto) {
        return supplierQueryAppService.addItem(dto);
    }

    @Override
    public SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto) {
        return supplierQueryAppService.updateItem(dto);
    }

    @Override
    public SupplierDraftSaveVO saveDraft(SupplierDraftSaveDTO dto) {
        return supplierDraftAppService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto) {
        return supplierSaveAppService.saveAndSubmit(dto);
    }

    @Override
    public List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto) {
        return supplierDraftAppService.draftList(dto);
    }

    @Override
    public SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto) {
        return supplierDraftAppService.loadDraft(dto);
    }

    @Override
    public SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto) {
        return supplierQueryAppService.businessSelectGetById(dto);
    }

    @Override
    public List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto) {
        return supplierQueryAppService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
        return supplierQueryAppService.businessSelectDialogSearch(dto);
    }

    @Override
    public Long save(SupplierSaveDTO dto) {
        return supplierSaveAppService.save(dto);
    }

    @Override
    public SupplierDetailVO detail(IdBaseDTO dto) {
        return supplierQueryAppService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        supplierDeleteAppService.delete(dto);
    }
}
