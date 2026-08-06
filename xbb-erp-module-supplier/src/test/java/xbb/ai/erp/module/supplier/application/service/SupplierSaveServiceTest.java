package xbb.ai.erp.module.supplier.application.service;

import org.junit.jupiter.api.Test;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.supplier.application.service.delete.SupplierDeleteAppService;
import xbb.ai.erp.module.supplier.application.service.delete.SupplierDeleteAppServiceImpl;
import xbb.ai.erp.module.supplier.application.service.query.SupplierQueryAppService;
import xbb.ai.erp.module.supplier.application.service.query.SupplierQueryAppServiceImpl;
import xbb.ai.erp.module.supplier.application.service.save.SupplierSaveAppService;
import xbb.ai.erp.module.supplier.application.service.save.SupplierSaveAppServiceImpl;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.ArrayList;
import xbb.ai.erp.module.supplier.admin.dto.SupplierContactItemDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierMainDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.application.service.impl.SupplierAdminAppServiceImpl;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierAddressRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierContactRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierSaveServiceTest {

    @Test
    void should_insert_supplier_and_contact_in_one_save() {
        InMemorySupplierRepository supplierRepository = new InMemorySupplierRepository();
        InMemorySupplierContactRepository contactRepository = new InMemorySupplierContactRepository();
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            supplierRepository,
            contactRepository,
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository()
        );

        SupplierMainDTO main = new SupplierMainDTO();
        main.setSupplierCode("SUP-001");
        main.setSupplierName("杭州供应商");
        main.setSupplierCategory("A");
        main.setBizStatus("1");

        SupplierContactItemDTO contact = new SupplierContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(1);

        SupplierSaveDTO dto = new SupplierSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        dto.setContacts(List.of(contact));

        Long supplierId = service.save(dto);

        assertEquals(1, supplierRepository.all().size());
        assertEquals(supplierId, contactRepository.all().get(0).getSupplierId());
    }

    @Test
    void should_refresh_owner_purchaser_name_snapshot_on_save() {
        InMemorySupplierRepository supplierRepository = new InMemorySupplierRepository();
        InMemorySupplierContactRepository contactRepository = new InMemorySupplierContactRepository();
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            supplierRepository,
            contactRepository,
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository(),
            null,
            (corpid, ownerPurchaserId) -> "张三"
        );

        SupplierMainDTO main = new SupplierMainDTO();
        main.setSupplierCode("SUP-002");
        main.setSupplierName("宁波供应商");
        main.setOwnerPurchaserId("EMP-1001");

        SupplierSaveDTO dto = new SupplierSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        dto.setContacts(List.of());

        service.save(dto);

        assertEquals("EMP-1001", supplierRepository.all().get(0).getOwnerPurchaserId());
        assertEquals("张三", supplierRepository.all().get(0).getOwnerPurchaserNameSnapshot());
    }

    @Test
    void should_build_supplier_add_item_head_list_with_user_and_comb_fields() {
        SupplierAdminAppServiceImpl service = SupplierAdminAppServiceImpl.forTesting(
            new InMemorySupplierRepository(),
            new InMemorySupplierContactRepository(),
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository()
        );
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");

        SaveItemVO<SupplierSaveItemVO> addItem = service.addItem(dto);
        FieldEntity ownerPurchaserField = addItem.getHeadList().stream().filter(item -> "main.ownerPurchaserId".equals(item.getAttr())).findFirst().orElseThrow();
        FieldEntity supplierCategoryField = addItem.getHeadList().stream().filter(item -> "main.supplierCategory".equals(item.getAttr())).findFirst().orElseThrow();

        assertEquals(String.valueOf(FieldTypeEnum.USER.getType()), ownerPurchaserField.getFieldType());
        assertEquals("/erp/v1/org/memberSelect/quickSearch", ownerPurchaserField.getBusinessSelectConfig().getQuickSearchUrl());
        assertEquals(String.valueOf(FieldTypeEnum.COMB.getType()), supplierCategoryField.getFieldType());
        assertTrue(supplierCategoryField.getItemList() != null && !supplierCategoryField.getItemList().isEmpty());
    }

    @Test
    void should_use_batch_remove_for_supplier_delete() {
        TrackingSupplierRepository supplierRepository = new TrackingSupplierRepository();
        SupplierDeleteAppServiceImpl service = new SupplierDeleteAppServiceImpl(supplierRepository);
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L, 2L, 3L));

        service.delete(dto);

        assertEquals(0, supplierRepository.removeByIdCalls);
        assertEquals(List.of(1L, 2L, 3L), supplierRepository.lastBatchIds);
        assertEquals("corp-001", supplierRepository.lastBatchCorpid);
    }

    @Test
    void should_register_supplier_sub_services_as_spring_services() {
        assertTrue(SupplierQueryAppServiceImpl.class.isAnnotationPresent(Service.class));
        assertTrue(SupplierSaveAppServiceImpl.class.isAnnotationPresent(Service.class));
        assertTrue(SupplierDeleteAppServiceImpl.class.isAnnotationPresent(Service.class));
    }

    @Test
    void should_wire_admin_and_sub_services_in_spring_context() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.registerBean(SupplierAdminAppServiceImpl.class);
            context.registerBean(SupplierRepository.class, InMemorySupplierRepository::new);
            context.registerBean(xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository.class, InMemorySupplierContactRepository::new);
            context.registerBean(xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository.class, InMemorySupplierAddressRepository::new);
            context.registerBean(xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository.class, InMemorySupplierBankAccountRepository::new);
            context.registerBean(xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository.class, InMemorySupplierInvoiceProfileRepository::new);
            context.registerBean(SupplierQueryAppService.class, () -> new SupplierQueryAppServiceImpl(
                context.getBean(SupplierRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository.class)
            ));
            context.registerBean(SupplierSaveAppService.class, () -> new SupplierSaveAppServiceImpl(
                context.getBean(SupplierRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository.class),
                context.getBean(xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository.class)
            ));
            context.registerBean(xbb.ai.erp.module.supplier.application.port.SupplierDraftRepository.class, xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierDraftRepository::new);
            context.registerBean(xbb.ai.erp.module.supplier.application.service.draft.SupplierDraftAppService.class, () -> new xbb.ai.erp.module.supplier.application.service.draft.SupplierDraftAppServiceImpl(
                context.getBean(xbb.ai.erp.module.supplier.application.port.SupplierDraftRepository.class)
            ));
            context.registerBean(SupplierDeleteAppService.class, () -> new SupplierDeleteAppServiceImpl(context.getBean(SupplierRepository.class)));
            context.refresh();

            SupplierAdminAppServiceImpl admin = context.getBean(SupplierAdminAppServiceImpl.class);
            assertNotNull(admin);
        }
    }

    private static class TrackingSupplierRepository extends InMemorySupplierRepository {
        private int removeByIdCalls;
        private String lastBatchCorpid;
        private List<Long> lastBatchIds = new ArrayList<>();

        @Override
        public void removeById(String corpid, Long id) {
            removeByIdCalls++;
            super.removeById(corpid, id);
        }

        @Override
        public void removeBatchByIds(String corpid, List<Long> ids) {
            lastBatchCorpid = corpid;
            lastBatchIds = new ArrayList<>(ids);
            super.removeBatchByIds(corpid, ids);
        }
    }

}
