package xbb.ai.erp.module.supplier.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.application.service.query.SupplierQueryAppServiceImpl;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierAddressRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierContactRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.application.service.support.InMemorySupplierRepository;
import xbb.ai.erp.module.supplier.domain.model.Supplier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SupplierBusinessSelectQueryServiceTest {

    @Test
    void should_support_supplier_business_select_queries() {
        InMemorySupplierRepository supplierRepository = new InMemorySupplierRepository();
        supplierRepository.insert(buildSupplier(2001L, "demo-corp", "SUP-001", "杭州供应商"));
        supplierRepository.insert(buildSupplier(2002L, "demo-corp", "SUP-002", "宁波供应商"));
        SupplierQueryAppServiceImpl service = new SupplierQueryAppServiceImpl(
            supplierRepository,
            new InMemorySupplierContactRepository(),
            new InMemorySupplierAddressRepository(),
            new InMemorySupplierBankAccountRepository(),
            new InMemorySupplierInvoiceProfileRepository()
        );

        SupplierBusinessSelectQueryDTO queryDTO = new SupplierBusinessSelectQueryDTO();
        queryDTO.setCorpid("demo-corp");
        queryDTO.setKeyword("宁波");
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(20);

        List<SupplierBusinessSelectOptionVO> quickSearch = service.businessSelectQuickSearch(queryDTO);
        ListBaseVO<SupplierBusinessSelectOptionVO> dialogSearch = service.businessSelectDialogSearch(queryDTO);

        SupplierBusinessSelectQueryDTO byIdDTO = new SupplierBusinessSelectQueryDTO();
        byIdDTO.setCorpid("demo-corp");
        byIdDTO.setId(2001L);
        SupplierBusinessSelectOptionVO byId = service.businessSelectGetById(byIdDTO);

        assertEquals(1, quickSearch.size());
        assertEquals(2002L, quickSearch.get(0).getId());
        assertEquals(1, dialogSearch.getList().size());
        assertEquals("SUP-001 杭州供应商", byId.getLabel());
        assertNotNull(dialogSearch.getPageHelper());
    }

    private Supplier buildSupplier(Long id, String corpid, String code, String name) {
        Supplier supplier = new Supplier();
        supplier.setId(id);
        supplier.setCorpid(corpid);
        supplier.setSupplierCode(code);
        supplier.setSupplierName(name);
        return supplier;
    }
}
