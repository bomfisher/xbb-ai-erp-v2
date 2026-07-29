package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerDraftRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerDraftServiceTest {

    @Test
    void should_save_and_load_draft_by_code() {
        InMemoryCustomerDraftRepository draftRepository = new InMemoryCustomerDraftRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            draftRepository
        );

        CustomerDraftSaveDTO dto = new CustomerDraftSaveDTO();
        dto.setCorpid("corp-001");
        dto.getMain().setCustomerCode("CUST-001");
        dto.getMain().setCustomerName("杭州客户");
        dto.getDraftMeta().setDraftTitle("草稿A");
        dto.getDraftMeta().setUpdatedTime(100L);

        service.saveDraft(dto);
        String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

        CustomerDraftLoadDTO loadDTO = new CustomerDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(draftCode);
        CustomerDraftDetailVO loaded = service.loadDraft(loadDTO);

        assertNotNull(draftCode);
        assertEquals("草稿A", loaded.getDraftMeta().getDraftTitle());
        assertEquals("CUST-001", loaded.getMain().getCustomerCode());
    }

    @Test
    void should_keep_section_state_consistent_after_saving_and_loading_draft() throws Exception {
        InMemoryCustomerDraftRepository draftRepository = new InMemoryCustomerDraftRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            draftRepository
        );

        CustomerDraftSaveDTO dto = new CustomerDraftSaveDTO();
        dto.setCorpid("corp-001");
        dto.getMain().setCustomerCode("CUST-001");
        dto.getMain().setCustomerName("杭州客户");
        dto.getDraftMeta().setDraftTitle("草稿A");
        dto.getDraftMeta().setUpdatedTime(100L);
        dto.getExt().setContacts(List.of());
        writeField(readField(dto, "sectionState"), "contacts", 1);
        writeField(readField(dto, "sectionState"), "addresses", 0);
        writeField(readField(dto, "sectionState"), "bankAccounts", 0);
        writeField(readField(dto, "sectionState"), "invoiceProfiles", 0);

        service.saveDraft(dto);
        String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

        CustomerDraftLoadDTO loadDTO = new CustomerDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(draftCode);
        CustomerDraftDetailVO loaded = service.loadDraft(loadDTO);

        Object loadedSectionState = readField(loaded, "sectionState");
        assertEquals(1, readField(loadedSectionState, "contacts"));
        assertEquals(0, readField(loadedSectionState, "addresses"));
        assertEquals(0, readField(loadedSectionState, "bankAccounts"));
        assertEquals(0, readField(loadedSectionState, "invoiceProfiles"));
    }

    @Test
    void should_return_latest_drafts_first_and_apply_limit() {
        InMemoryCustomerDraftRepository draftRepository = new InMemoryCustomerDraftRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            draftRepository
        );
        draftRepository.seed(draft("corp-001", "draft-1", "草稿1", "CUST-001", "客户1", 100L));
        draftRepository.seed(draft("corp-001", "draft-2", "草稿2", "CUST-002", "客户2", 300L));
        draftRepository.seed(draft("corp-001", "draft-3", "草稿3", "CUST-003", "客户3", 200L));
        draftRepository.seed(draft("corp-002", "draft-4", "草稿4", "CUST-004", "客户4", 400L));

        CustomerDraftListDTO dto = new CustomerDraftListDTO();
        dto.setCorpid("corp-001");
        List<CustomerDraftListItemVO> drafts = service.draftList(dto);

        assertEquals(3, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
        assertEquals("draft-3", drafts.get(1).getDraftCode());
        assertEquals("draft-1", drafts.get(2).getDraftCode());
    }

    private CustomerSaveDraftPojo draft(
        String corpid,
        String draftCode,
        String draftTitle,
        String customerCode,
        String customerName,
        Long updatedTime
    ) {
        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode(customerCode);
        main.setCustomerName(customerName);

        CustomerSaveDraftPojo draft = new CustomerSaveDraftPojo();
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
