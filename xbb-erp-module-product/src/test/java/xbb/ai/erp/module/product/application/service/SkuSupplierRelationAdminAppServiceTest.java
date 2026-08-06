package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationListDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationQueryDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO;
import xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSkuOptionsDTO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO;
import xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSkuOptionVO;
import xbb.ai.erp.module.product.application.service.impl.SkuSupplierRelationAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.FakeProductSkuRepository;
import xbb.ai.erp.module.product.application.service.support.FakeSupplierRepository;
import xbb.ai.erp.module.product.application.service.support.InMemoryProductSkuSupplierRelationRepository;
import xbb.ai.erp.module.product.domain.model.ProductSku;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelation;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSkuOption;
import xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationSupplierOption;
import xbb.ai.erp.module.supplier.domain.model.Supplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkuSupplierRelationAdminAppServiceTest {

    @Test
    void should_return_relation_list_by_spu_context() {
        InMemoryProductSkuSupplierRelationRepository relationRepository = relationRepository();
        seedRelationData(relationRepository);
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(
            relationRepository,
            new FakeProductSkuRepository(List.of(buildSku(1001L, 101L))),
            new FakeSupplierRepository(List.of(buildSupplier(2001L)))
        );
        SkuSupplierRelationListDTO dto = new SkuSupplierRelationListDTO();
        dto.setCorpid("demo-corp");
        dto.setView("sku");
        dto.setSpuId(101L);
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<?> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals(1, result.getList().size());
        assertNotNull(result.getPageHelper());
    }

    @Test
    void should_build_add_item_with_context_options() {
        InMemoryProductSkuSupplierRelationRepository relationRepository = relationRepository();
        seedRelationData(relationRepository);
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(relationRepository, null, null);
        SkuSupplierRelationQueryDTO dto = new SkuSupplierRelationQueryDTO();
        dto.setCorpid("demo-corp");
        dto.setSpuId(101L);
        dto.setSupplierId(2001L);

        SaveItemVO<SkuSupplierRelationMainDTO> result = service.addItem(dto);

        assertEquals(1001L, result.getData().getSkuId());
        assertEquals(2001L, result.getData().getSupplierId());
        assertEquals(2, result.getHeadList().size());
        assertEquals("main.skuId", result.getHeadList().get(0).getAttr());
        assertEquals("8", result.getHeadList().get(0).getFieldType());
        assertEquals(1, result.getHeadList().get(0).getItemList().size());
        assertEquals("main.supplierId", result.getHeadList().get(1).getAttr());
        assertEquals("16", result.getHeadList().get(1).getFieldType());
        assertNotNull(result.getHeadList().get(1).getBusinessSelectConfig());
        assertEquals("supplier", result.getHeadList().get(1).getBusinessSelectConfig().getBusinessType());
        assertEquals(Map.of("corpid", "demo-corp"), result.getHeadList().get(1).getBusinessSelectConfig().getRequestPayload());
    }

    @Test
    void should_save_relation_and_write_history() {
        InMemoryProductSkuSupplierRelationRepository relationRepository = relationRepository();
        seedRelationData(relationRepository);
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(
            relationRepository,
            new FakeProductSkuRepository(List.of(buildSku(1001L, 101L))),
            new FakeSupplierRepository(List.of(buildSupplier(2001L)))
        );
        SkuSupplierRelationSaveDTO dto = new SkuSupplierRelationSaveDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("u-1");
        dto.setSkuId(1001L);
        dto.setSupplierId(2001L);
        dto.setPurchasePrice(new BigDecimal("12.80"));
        dto.setDefaultFlag(1);
        dto.setEnableStatus(1);

        Long relationId = service.save(dto);

        assertNotNull(relationId);
        assertEquals(2, relationRepository.allRelations().size());
        assertEquals(1, relationRepository.allHistory().size());
    }

    @Test
    void should_set_default_and_clear_previous_default() {
        InMemoryProductSkuSupplierRelationRepository relationRepository = relationRepository();
        seedRelationData(relationRepository);
        ProductSkuSupplierRelation second = new ProductSkuSupplierRelation();
        second.setId(2L);
        second.setCorpid("demo-corp");
        second.setSkuId(1001L);
        second.setSupplierId(2002L);
        second.setDefaultFlag(0);
        second.setEnableStatus(1);
        relationRepository.seedRelation(second);
        relationRepository.seedSupplierOption(optionSupplier(2002L, "SUP-002", "宁波供应商"));
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(relationRepository, null, null);
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("u-2");
        dto.setId(2L);

        BaseVO result = service.setDefault(dto);

        assertEquals(1, result.getOk());
        assertEquals(0, relationRepository.findById("demo-corp", 1L).getDefaultFlag());
        assertEquals(1, relationRepository.findById("demo-corp", 2L).getDefaultFlag());
    }

    @Test
    void should_return_history_and_sku_options() {
        InMemoryProductSkuSupplierRelationRepository relationRepository = relationRepository();
        seedRelationData(relationRepository);
        ProductSkuSupplierRelation relation = relationRepository.findById("demo-corp", 1L);
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(relationRepository, null, null);
        relationRepository.insertHistory(history(relation, "u-1", "UPDATE_RELATION"));
        IdBaseDTO historyDto = new IdBaseDTO();
        historyDto.setCorpid("demo-corp");
        historyDto.setId(1L);
        SkuSupplierRelationSkuOptionsDTO optionDto = new SkuSupplierRelationSkuOptionsDTO();
        optionDto.setCorpid("demo-corp");
        optionDto.setSpuId(101L);

        List<?> historyList = service.history(historyDto);
        List<SkuSupplierRelationSkuOptionVO> optionList = service.skuOptionsBySpu(optionDto);

        assertEquals(1, historyList.size());
        assertEquals(1, optionList.size());
        assertEquals(1001L, optionList.get(0).getSkuId());
    }

    @Test
    void should_build_update_item_with_current_relation_options() {
        InMemoryProductSkuSupplierRelationRepository relationRepository = relationRepository();
        seedRelationData(relationRepository);
        relationRepository.seedSkuOption(optionSku(1002L, 101L, "SKU-002", "蓝色款", "{\"color\":\"blue\"}"));
        relationRepository.seedSupplierOption(optionSupplier(2002L, "SUP-002", "宁波供应商"));
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(
            relationRepository,
            new FakeProductSkuRepository(List.of(buildSku(1001L, 101L), buildSku(1002L, 101L))),
            null
        );
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("demo-corp");
        dto.setId(1L);

        SaveItemVO<SkuSupplierRelationMainDTO> result = service.updateItem(dto);

        assertEquals(1001L, result.getData().getSkuId());
        assertEquals(2001L, result.getData().getSupplierId());
        assertEquals(2, result.getHeadList().size());
        assertEquals("main.skuId", result.getHeadList().get(0).getAttr());
        assertEquals(2, result.getHeadList().get(0).getItemList().size());
        assertEquals("main.supplierId", result.getHeadList().get(1).getAttr());
        assertNotNull(result.getHeadList().get(1).getBusinessSelectConfig());
        assertEquals("supplier", result.getHeadList().get(1).getBusinessSelectConfig().getBusinessType());
        assertEquals(Map.of("corpid", "demo-corp"), result.getHeadList().get(1).getBusinessSelectConfig().getRequestPayload());
    }

    @Test
    void should_reject_missing_sku_on_save() {
        SkuSupplierRelationAdminAppServiceImpl service = SkuSupplierRelationAdminAppServiceImpl.forTesting(null, null, null);
        SkuSupplierRelationSaveDTO dto = new SkuSupplierRelationSaveDTO();
        dto.setCorpid("demo-corp");
        dto.setSupplierId(2001L);

        BizException exception = assertThrows(BizException.class, () -> service.save(dto));

        assertEquals("skuId不能为空", exception.getMessage());
    }

    private InMemoryProductSkuSupplierRelationRepository relationRepository() {
        return new InMemoryProductSkuSupplierRelationRepository();
    }

    private void seedRelationData(InMemoryProductSkuSupplierRelationRepository relationRepository) {
        relationRepository.seedSkuOption(optionSku(1001L, 101L, "SKU-001", "红色款", "{\"color\":\"red\"}"));
        relationRepository.seedSupplierOption(optionSupplier(2001L, "SUP-001", "杭州供应商"));
        ProductSkuSupplierRelation relation = new ProductSkuSupplierRelation();
        relation.setId(1L);
        relation.setCorpid("demo-corp");
        relation.setSkuId(1001L);
        relation.setSupplierId(2001L);
        relation.setPurchasePrice(new BigDecimal("10.50"));
        relation.setDefaultFlag(1);
        relation.setEnableStatus(1);
        relation.setUpdateTime(1L);
        relationRepository.seedRelation(relation);
    }

    private ProductSku buildSku(Long skuId, Long spuId) {
        ProductSku sku = new ProductSku();
        sku.setId(skuId);
        sku.setCorpid("demo-corp");
        sku.setSpuId(spuId);
        sku.setSkuCode("SKU-001");
        sku.setSkuName("红色款");
        return sku;
    }

    private Supplier buildSupplier(Long supplierId) {
        Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        supplier.setCorpid("demo-corp");
        supplier.setSupplierCode("SUP-001");
        supplier.setSupplierName("杭州供应商");
        return supplier;
    }

    private ProductSkuSupplierRelationSkuOption optionSku(Long skuId, Long spuId, String skuCode, String skuName, String specSnapshot) {
        ProductSkuSupplierRelationSkuOption option = new ProductSkuSupplierRelationSkuOption();
        option.setSkuId(skuId);
        option.setSpuId(spuId);
        option.setSkuCode(skuCode);
        option.setSkuName(skuName);
        option.setSpecSnapshot(specSnapshot);
        return option;
    }

    private ProductSkuSupplierRelationSupplierOption optionSupplier(Long supplierId, String supplierCode, String supplierName) {
        ProductSkuSupplierRelationSupplierOption option = new ProductSkuSupplierRelationSupplierOption();
        option.setSupplierId(supplierId);
        option.setSupplierCode(supplierCode);
        option.setSupplierName(supplierName);
        return option;
    }

    private xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory history(ProductSkuSupplierRelation relation, String userId, String operateType) {
        xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory history = new xbb.ai.erp.module.product.domain.model.ProductSkuSupplierRelationHistory();
        history.setCorpid(relation.getCorpid());
        history.setRelationId(relation.getId());
        history.setOperatorId(userId);
        history.setOperateType(operateType);
        history.setRemark("test");
        return history;
    }
}
