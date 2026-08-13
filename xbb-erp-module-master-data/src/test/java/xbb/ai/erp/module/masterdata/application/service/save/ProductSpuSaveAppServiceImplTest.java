package xbb.ai.erp.module.masterdata.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.application.port.ProductSpuDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveBusinessValidator;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.ProductSpuSaveProtocolValidator;
import xbb.ai.erp.module.masterdata.domain.model.ProductSku;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSpuRepository;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSkuRepository;

class ProductSpuSaveAppServiceImplTest {

    @Test
    void shouldCreateDefaultSkuWhenCreatingProductSpu() {
        ProductSpuRepository productSpuRepository = mock(ProductSpuRepository.class);
        ProductSkuRepository productSkuRepository = mock(ProductSkuRepository.class);
        ProductSpuSaveAppServiceImpl service = service(productSpuRepository, productSkuRepository);
        doAnswer(invocation -> {
            invocation.getArgument(0, xbb.ai.erp.module.masterdata.domain.model.ProductSpu.class).setId(100L);
            return 100L;
        }).when(productSpuRepository).insert(any());

        service.saveAndSubmit(newSubmitDTO(null));

        ArgumentCaptor<ProductSku> captor = ArgumentCaptor.forClass(ProductSku.class);
        verify(productSkuRepository).insert(captor.capture());
        ProductSku sku = captor.getValue();
        assertEquals("corp-001", sku.getCorpid());
        assertEquals(100L, sku.getSpuId());
        assertEquals("SPU-001", sku.getSkuCode());
        assertEquals("测试产品", sku.getSkuName());
        assertEquals("件", sku.getUnitName());
        assertEquals(1, sku.getEnabled());
        assertEquals("备注", sku.getRemark());
        assertEquals("user-001", sku.getCreatorId());
        assertEquals("user-001", sku.getModifyId());
    }

    @Test
    void shouldNotCreateSkuWhenUpdatingProductSpu() {
        ProductSpuRepository productSpuRepository = mock(ProductSpuRepository.class);
        ProductSkuRepository productSkuRepository = mock(ProductSkuRepository.class);
        ProductSpuSaveAppServiceImpl service = service(productSpuRepository, productSkuRepository);

        service.saveAndSubmit(newSubmitDTO(100L));

        verify(productSpuRepository).update(any());
        verify(productSkuRepository, never()).insert(any());
    }

    private ProductSpuSaveAppServiceImpl service(ProductSpuRepository productSpuRepository,
                                                   ProductSkuRepository productSkuRepository) {
        return new ProductSpuSaveAppServiceImpl(
            productSpuRepository,
            productSkuRepository,
            mock(ProductSpuDraftRepository.class),
            new ProductSpuSaveProtocolValidator(),
            new ProductSpuSaveCommonValidator(),
            new ProductSpuSaveBusinessValidator()
        );
    }

    private ProductSpuSubmitSaveDTO newSubmitDTO(Long id) {
        ProductSpuMainDTO main = new ProductSpuMainDTO();
        main.setId(id);
        main.setSpuCode("SPU-001");
        main.setSpuName("测试产品");
        main.setCategoryName("测试分类");
        main.setEnabled(1);
        main.setRemark("备注");
        ProductSpuSubmitSaveDTO dto = new ProductSpuSubmitSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        return dto;
    }
}
