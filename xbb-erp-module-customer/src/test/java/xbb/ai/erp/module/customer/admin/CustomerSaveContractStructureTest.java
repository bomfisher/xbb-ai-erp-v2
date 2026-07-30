package xbb.ai.erp.module.customer.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftMetaDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveExtDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSectionStateDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftMetaVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveExtVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSectionStateVO;
import xbb.ai.erp.module.customer.application.pojo.CustomerDraftMetaPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerSaveContractStructureTest {

    @Test
    void should_declare_new_customer_save_contract_types() {
        assertNotNull(new CustomerDraftSaveDTO());
        assertNotNull(new CustomerSubmitSaveDTO());
        assertNotNull(new CustomerDraftListDTO());
        assertNotNull(new CustomerDraftLoadDTO());
        assertNotNull(new CustomerDraftDetailVO());
        assertNotNull(new CustomerDraftListItemVO());
        assertNotNull(new CustomerSaveItemVO());
        assertNotNull(new CustomerSaveExtPojo());
        assertNotNull(new CustomerDraftMetaPojo());
        assertNotNull(new CustomerSaveDraftPojo());
        assertNotNull(new CustomerSaveContextPojo());
    }

    @Test
    void should_use_main_ext_section_state_draft_meta_shell() throws Exception {
        assertEquals("main", field(CustomerDraftSaveDTO.class, "main").getName());
        assertEquals("ext", field(CustomerDraftSaveDTO.class, "ext").getName());
        assertEquals(CustomerSaveExtDTO.class, field(CustomerDraftSaveDTO.class, "ext").getType());
        assertEquals("sectionState", field(CustomerDraftSaveDTO.class, "sectionState").getName());
        assertEquals(CustomerSectionStateDTO.class, field(CustomerDraftSaveDTO.class, "sectionState").getType());
        assertEquals("draftMeta", field(CustomerDraftSaveDTO.class, "draftMeta").getName());
        assertEquals(CustomerDraftMetaDTO.class, field(CustomerDraftSaveDTO.class, "draftMeta").getType());
        assertEquals("main", field(CustomerSubmitSaveDTO.class, "main").getName());
        assertEquals("ext", field(CustomerSubmitSaveDTO.class, "ext").getName());
        assertEquals(CustomerSaveExtDTO.class, field(CustomerSubmitSaveDTO.class, "ext").getType());
        assertEquals("sectionState", field(CustomerSubmitSaveDTO.class, "sectionState").getName());
        assertEquals(CustomerSectionStateDTO.class, field(CustomerSubmitSaveDTO.class, "sectionState").getType());
        assertEquals("draftMeta", field(CustomerSubmitSaveDTO.class, "draftMeta").getName());
        assertEquals(CustomerDraftMetaDTO.class, field(CustomerSubmitSaveDTO.class, "draftMeta").getType());
        assertEquals("sectionState", field(CustomerDraftDetailVO.class, "sectionState").getName());
        assertEquals(CustomerSectionStateVO.class, field(CustomerDraftDetailVO.class, "sectionState").getType());
        assertEquals(CustomerSaveExtVO.class, field(CustomerDraftDetailVO.class, "ext").getType());
        assertEquals(CustomerDraftMetaVO.class, field(CustomerDraftDetailVO.class, "draftMeta").getType());
        assertEquals("sectionState", field(CustomerSaveItemVO.class, "sectionState").getName());
        assertEquals(CustomerSectionStateVO.class, field(CustomerSaveItemVO.class, "sectionState").getType());
        assertEquals("sectionState", field(CustomerSaveDraftPojo.class, "sectionState").getName());
        assertEquals("sectionState", field(CustomerSaveContextPojo.class, "sectionState").getName());
        assertEquals("draftCode", field(CustomerDraftLoadDTO.class, "draftCode").getName());
    }

    @Test
    void should_declare_ext_and_draft_summary_fields() throws Exception {
        assertEquals("contacts", field(CustomerSaveExtPojo.class, "contacts").getName());
        assertEquals("addresses", field(CustomerSaveExtPojo.class, "addresses").getName());
        assertEquals("bankAccounts", field(CustomerSaveExtPojo.class, "bankAccounts").getName());
        assertEquals("invoiceProfiles", field(CustomerSaveExtPojo.class, "invoiceProfiles").getName());
        assertEquals("draftCode", field(CustomerDraftMetaPojo.class, "draftCode").getName());
        assertEquals("draftTitle", field(CustomerDraftMetaPojo.class, "draftTitle").getName());
        assertEquals("updatedTime", field(CustomerDraftMetaPojo.class, "updatedTime").getName());
        assertEquals("draftCode", field(CustomerDraftListItemVO.class, "draftCode").getName());
        assertEquals("draftTitle", field(CustomerDraftListItemVO.class, "draftTitle").getName());
        assertEquals("customerName", field(CustomerDraftListItemVO.class, "customerName").getName());
        assertEquals("customerCode", field(CustomerDraftListItemVO.class, "customerCode").getName());
        assertEquals("updatedTime", field(CustomerDraftListItemVO.class, "updatedTime").getName());
    }

    private Field field(Class<?> type, String name) throws NoSuchFieldException {
        return type.getDeclaredField(name);
    }
}
