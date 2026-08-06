package xbb.ai.erp.module.supplier.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierMainDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveDraftPojo;
import xbb.ai.erp.module.supplier.application.service.impl.SupplierAdminAppServiceImpl;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierAddressRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierContactRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierDraftRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierRepository;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierDraftServiceTest {

    @Test
    void should_save_and_load_draft_by_code() {
        InMemorySupplierDraftRepository draftRepository = new InMemorySupplierDraftRepository();
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            new InMemorySupplierRepository(),
            new InMemorySupplierContactRepository(),
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository(),
            draftRepository
        );

        SupplierDraftSaveDTO dto = new SupplierDraftSaveDTO();
        dto.setCorpid("corp-001");
        dto.getMain().setSupplierCode("SUP-001");
        dto.getMain().setSupplierName("杭州供应商");
        dto.getDraftMeta().setDraftTitle("草稿A");
        dto.getDraftMeta().setUpdatedTime(100L);

        service.saveDraft(dto);
        String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

        SupplierDraftLoadDTO loadDTO = new SupplierDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(draftCode);
        SupplierDraftDetailVO loaded = service.loadDraft(loadDTO);

        assertNotNull(draftCode);
        assertEquals("草稿A", loaded.getDraftMeta().getDraftTitle());
        assertEquals("SUP-001", loaded.getMain().getSupplierCode());
    }

    @Test
    void should_keep_section_state_consistent_after_saving_and_loading_draft() throws Exception {
        InMemorySupplierDraftRepository draftRepository = new InMemorySupplierDraftRepository();
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            new InMemorySupplierRepository(),
            new InMemorySupplierContactRepository(),
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository(),
            draftRepository
        );

        SupplierDraftSaveDTO dto = new SupplierDraftSaveDTO();
        dto.setCorpid("corp-001");
        dto.getMain().setSupplierCode("SUP-001");
        dto.getMain().setSupplierName("杭州供应商");
        dto.getDraftMeta().setDraftTitle("草稿A");
        dto.getDraftMeta().setUpdatedTime(100L);
        dto.getExt().setContacts(List.of());
        writeField(readField(dto, "sectionState"), "contacts", 1);
        writeField(readField(dto, "sectionState"), "addresses", 0);
        writeField(readField(dto, "sectionState"), "bankAccounts", 0);
        writeField(readField(dto, "sectionState"), "invoiceProfiles", 0);

        service.saveDraft(dto);
        String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

        SupplierDraftLoadDTO loadDTO = new SupplierDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(draftCode);
        SupplierDraftDetailVO loaded = service.loadDraft(loadDTO);

        Object loadedSectionState = readField(loaded, "sectionState");
        assertEquals(1, readField(loadedSectionState, "contacts"));
        assertEquals(0, readField(loadedSectionState, "addresses"));
        assertEquals(0, readField(loadedSectionState, "bankAccounts"));
        assertEquals(0, readField(loadedSectionState, "invoiceProfiles"));
    }

    @Test
    void should_return_latest_drafts_first_and_apply_limit() {
        InMemorySupplierDraftRepository draftRepository = new InMemorySupplierDraftRepository();
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            new InMemorySupplierRepository(),
            new InMemorySupplierContactRepository(),
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository(),
            draftRepository
        );
        draftRepository.seed(draft("corp-001", "draft-1", "草稿1", "SUP-001", "供应商1", 100L));
        draftRepository.seed(draft("corp-001", "draft-2", "草稿2", "SUP-002", "供应商2", 300L));
        draftRepository.seed(draft("corp-001", "draft-3", "草稿3", "SUP-003", "供应商3", 200L));
        draftRepository.seed(draft("corp-002", "draft-4", "草稿4", "SUP-004", "供应商4", 400L));

        SupplierDraftListDTO dto = new SupplierDraftListDTO();
        dto.setCorpid("corp-001");
        List<SupplierDraftListItemVO> drafts = service.draftList(dto);

        assertEquals(3, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
        assertEquals("draft-3", drafts.get(1).getDraftCode());
        assertEquals("draft-1", drafts.get(2).getDraftCode());
    }

    @Test
    void should_remove_draft_after_submit_success() {
        InMemorySupplierDraftRepository draftRepository = new InMemorySupplierDraftRepository();
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            new InMemorySupplierRepository(),
            new InMemorySupplierContactRepository(),
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository(),
            draftRepository
        );

        SupplierMainDTO main = new SupplierMainDTO();
        main.setSupplierCode("SUP-001");
        main.setSupplierName("杭州供应商");

        SupplierDraftSaveDTO draftDTO = new SupplierDraftSaveDTO();
        draftDTO.setCorpid("corp-001");
        draftDTO.setUserId("user-001");
        draftDTO.setMain(main);
        draftDTO.getDraftMeta().setDraftTitle("供应商草稿");
        service.saveDraft(draftDTO);

        String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

        SupplierSubmitSaveDTO submitDTO = new SupplierSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setUserId("user-001");
        submitDTO.setMain(main);
        submitDTO.getDraftMeta().setDraftCode(draftCode);

        service.saveAndSubmit(submitDTO);

        assertTrue(draftRepository.listDrafts("corp-001", 10).isEmpty());
    }

    private SupplierSaveDraftPojo draft(
        String corpid,
        String draftCode,
        String draftTitle,
        String supplierCode,
        String supplierName,
        Long updatedTime
    ) {
        SupplierMainDTO main = new SupplierMainDTO();
        main.setSupplierCode(supplierCode);
        main.setSupplierName(supplierName);

        SupplierSaveDraftPojo draft = new SupplierSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftCode(draftCode);
        draft.setDraftTitle(draftTitle);
        draft.setMain(main);
        draft.setUpdatedTime(updatedTime);
        return draft;
    }

    private Object readField(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private void writeField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
