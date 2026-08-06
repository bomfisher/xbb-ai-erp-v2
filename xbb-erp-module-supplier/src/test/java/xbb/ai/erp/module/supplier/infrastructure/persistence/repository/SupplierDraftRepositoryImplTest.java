package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.supplier.admin.dto.SupplierAddressItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierContactItemDTO;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveDraftPojo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplierDraftRepositoryImplTest {

    @Test
    void should_isolate_loaded_draft_from_store_mutation() {
        SupplierDraftRepositoryImpl repository = new SupplierDraftRepositoryImpl();
        SupplierSaveDraftPojo draft = createDraft();

        String draftCode = repository.saveDraft(draft);
        SupplierSaveDraftPojo loaded = repository.loadDraft("corp-001", draftCode);
        loaded.getMain().setSupplierName("被篡改名称");
        loaded.getExt().getContacts().get(0).setContactName("被篡改联系人");
        loaded.getSectionState().setContacts(0);

        SupplierSaveDraftPojo reloaded = repository.loadDraft("corp-001", draftCode);
        assertEquals("杭州供应商", reloaded.getMain().getSupplierName());
        assertEquals("张三", reloaded.getExt().getContacts().get(0).getContactName());
        assertEquals(1, reloaded.getSectionState().getContacts());
    }

    @Test
    void should_isolate_list_draft_result_from_store_mutation() {
        SupplierDraftRepositoryImpl repository = new SupplierDraftRepositoryImpl();
        SupplierSaveDraftPojo draft = createDraft();

        repository.saveDraft(draft);
        SupplierSaveDraftPojo listed = repository.listDrafts("corp-001", 10).get(0);
        listed.getMain().setSupplierCode("MUTATED");
        listed.getExt().getAddresses().get(0).setDetailAddress("被篡改地址");
        listed.getSectionState().setAddresses(0);

        SupplierSaveDraftPojo relisted = repository.listDrafts("corp-001", 10).get(0);
        assertEquals("SUP-001", relisted.getMain().getSupplierCode());
        assertEquals("浙江省杭州市", relisted.getExt().getAddresses().get(0).getDetailAddress());
        assertEquals(1, relisted.getSectionState().getAddresses());
    }

    private SupplierSaveDraftPojo createDraft() {
        SupplierSaveDraftPojo draft = new SupplierSaveDraftPojo();
        draft.setCorpid("corp-001");
        draft.setDraftTitle("供应商草稿");
        draft.setUpdatedTime(100L);
        draft.getMain().setSupplierCode("SUP-001");
        draft.getMain().setSupplierName("杭州供应商");

        SupplierContactItemDTO contact = new SupplierContactItemDTO();
        contact.setContactName("张三");
        draft.getExt().setContacts(List.of(contact));

        SupplierAddressItemDTO address = new SupplierAddressItemDTO();
        address.setDetailAddress("浙江省杭州市");
        draft.getExt().setAddresses(List.of(address));

        draft.getSectionState().setContacts(1);
        draft.getSectionState().setAddresses(1);
        draft.getSectionState().setBankAccounts(0);
        draft.getSectionState().setInvoiceProfiles(0);
        return draft;
    }
}
