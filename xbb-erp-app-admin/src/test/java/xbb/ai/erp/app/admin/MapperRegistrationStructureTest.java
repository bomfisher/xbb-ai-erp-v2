package xbb.ai.erp.app.admin;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapperRegistrationStructureTest {

    @Test
    void should_mark_customer_purchase_product_and_supplier_mappers_with_mapper_annotation() throws Exception {
        assertHasMapperAnnotation("xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerContactMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerAddressMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerBankAccountMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerInvoiceProfileMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseRequestMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseRequestItemMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseOrderMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseOrderItemMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchasePendingTaskMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseSourceRelationMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInboundMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInboundItemMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductBrandMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductCategoryMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductSpuMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductSkuMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductUnitMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.product.infrastructure.persistence.mapper.WarehouseMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierContactMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierAddressMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierBankAccountMapper");
        assertHasMapperAnnotation("xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierInvoiceProfileMapper");
    }

    private void assertHasMapperAnnotation(String className) throws Exception {
        Class<?> mapperClass = Class.forName(className);
        assertNotNull(mapperClass.getAnnotation(Mapper.class), className + " 缺少 @Mapper");
    }
}
