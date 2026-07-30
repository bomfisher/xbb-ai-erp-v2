package xbb.ai.erp.module.customer.application.service.draft;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerDraftRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerDraftAppServiceTest {

    @Test
    void should_save_and_load_draft() {
        InMemoryCustomerDraftRepository draftRepository = new InMemoryCustomerDraftRepository();
        CustomerDraftAppService service = new CustomerDraftAppServiceImpl(draftRepository);

        CustomerDraftSaveDTO saveDTO = new CustomerDraftSaveDTO();
        saveDTO.setCorpid("corp-001");
        saveDTO.getMain().setCustomerCode("CUST-001");
        saveDTO.getMain().setCustomerName("杭州客户");
        saveDTO.getDraftMeta().setDraftTitle("草稿A");
        saveDTO.getDraftMeta().setUpdatedTime(100L);
        String draftCode = service.saveDraft(saveDTO).getDraftCode();

        CustomerDraftLoadDTO loadDTO = new CustomerDraftLoadDTO();
        loadDTO.setCorpid("corp-001");
        loadDTO.setDraftCode(draftCode);
        CustomerDraftDetailVO detail = service.loadDraft(loadDTO);

        assertEquals("草稿A", detail.getDraftMeta().getDraftTitle());
        assertEquals("CUST-001", detail.getMain().getCustomerCode());
    }
}
