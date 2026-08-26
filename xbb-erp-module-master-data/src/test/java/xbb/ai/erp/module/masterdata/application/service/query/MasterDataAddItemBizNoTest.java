package xbb.ai.erp.module.masterdata.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.masterdata.application.field.CustomerFieldFactory;
import xbb.ai.erp.module.masterdata.application.field.ProductSpuFieldFactory;
import xbb.ai.erp.module.masterdata.application.field.SupplierFieldFactory;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerRepository;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSpuRepository;
import xbb.ai.erp.module.masterdata.domain.repository.SupplierRepository;

class MasterDataAddItemBizNoTest {

    @Test
    void shouldGenerateCustomerCodeForNewItem() {
        BizNoGenerator generator = generator("CUSTOMER", "CU-00001");
        CustomerFieldFactory fieldFactory = Mockito.mock(CustomerFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        CustomerQueryAppServiceImpl service = new CustomerQueryAppServiceImpl(
            Mockito.mock(CustomerRepository.class), Mockito.mock(CustomerContactRepository.class), fieldFactory,
            null, null, generator);

        assertEquals("CU-00001", service.addItem(baseDTO()).getData().getMain().getCustomerCode());
    }

    @Test
    void shouldGenerateProductSpuCodeForNewItem() {
        BizNoGenerator generator = generator("PRODUCT_SPU", "SPU-00001");
        ProductSpuFieldFactory fieldFactory = Mockito.mock(ProductSpuFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        ProductSpuQueryAppServiceImpl service = new ProductSpuQueryAppServiceImpl(
            Mockito.mock(ProductSpuRepository.class), fieldFactory, null, null, generator);

        assertEquals("SPU-00001", service.addItem(baseDTO()).getData().getMain().getSpuCode());
    }

    @Test
    void shouldGenerateSupplierCodeForNewItem() {
        BizNoGenerator generator = generator("SUPPLIER", "SU-00001");
        SupplierFieldFactory fieldFactory = Mockito.mock(SupplierFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        SupplierQueryAppServiceImpl service = new SupplierQueryAppServiceImpl(
            Mockito.mock(SupplierRepository.class), fieldFactory, null, null, generator);

        assertEquals("SU-00001", service.addItem(baseDTO()).getData().getMain().getSupplierCode());
    }

    private BizNoGenerator generator(String businessCode, String code) {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", businessCode)).thenReturn(code);
        return generator;
    }

    private BaseDTO baseDTO() {
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");
        return dto;
    }
}
