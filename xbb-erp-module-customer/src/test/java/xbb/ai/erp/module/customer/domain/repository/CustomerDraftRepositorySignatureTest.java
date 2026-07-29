package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerDraftRepositorySignatureTest {

    @Test
    void should_declare_customer_draft_repository_methods() throws Exception {
        Method saveDraft = CustomerDraftRepository.class.getMethod("saveDraft", CustomerSaveDraftPojo.class);
        Method listDrafts = CustomerDraftRepository.class.getMethod("listDrafts", String.class, int.class);
        Method loadDraft = CustomerDraftRepository.class.getMethod("loadDraft", String.class, String.class);
        Method removeDraft = CustomerDraftRepository.class.getMethod("removeDraft", String.class, String.class);

        assertNotNull(saveDraft);
        assertNotNull(listDrafts);
        assertNotNull(loadDraft);
        assertNotNull(removeDraft);
    }
}
