package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SkuSupplierRelationAdminControllerStructureTest {

    @Test
    void should_declare_sku_supplier_relation_endpoints() throws Exception {
        Class<?> controllerClass = Class.forName("xbb.ai.erp.module.product.admin.SkuSupplierRelationAdminController");
        Class<?> listDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationListDTO");
        Class<?> queryDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationQueryDTO");
        Class<?> skuOptionsDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSkuOptionsDTO");
        Class<?> saveDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO");
        Class<?> idBaseDtoClass = Class.forName("xbb.ai.erp.base.common.dto.IdBaseDTO");

        assertNotNull(controllerClass.getMethod("list", listDtoClass));
        assertNotNull(controllerClass.getMethod("addItem", queryDtoClass));
        assertNotNull(controllerClass.getMethod("updateItem", idBaseDtoClass));
        assertNotNull(controllerClass.getMethod("save", saveDtoClass));
        assertNotNull(controllerClass.getMethod("setDefault", idBaseDtoClass));
        assertNotNull(controllerClass.getMethod("enable", idBaseDtoClass));
        assertNotNull(controllerClass.getMethod("disable", idBaseDtoClass));
        assertNotNull(controllerClass.getMethod("delete", idBaseDtoClass));
        assertNotNull(controllerClass.getMethod("history", idBaseDtoClass));
        assertNotNull(controllerClass.getMethod("skuOptionsBySpu", skuOptionsDtoClass));
    }

    @Test
    void should_depend_on_relation_admin_app_service() throws Exception {
        Class<?> controllerClass = Class.forName("xbb.ai.erp.module.product.admin.SkuSupplierRelationAdminController");
        Field field = controllerClass.getDeclaredField("skuSupplierRelationAdminAppService");

        assertEquals(
            Class.forName("xbb.ai.erp.module.product.application.service.SkuSupplierRelationAdminAppService"),
            field.getType()
        );
    }

    @Test
    void should_define_relation_dto_and_vo_contract() throws Exception {
        Class<?> saveDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO");
        Class<?> saveItemVoClass = Class.forName("xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSaveItemVO");
        Class<?> listItemVoClass = Class.forName("xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO");
        Class<?> historyItemVoClass = Class.forName("xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO");

        assertEquals(BaseDTO.class, saveDtoClass.getSuperclass());
        assertEquals(Long.class, saveDtoClass.getDeclaredField("skuId").getType());
        assertEquals(Long.class, saveDtoClass.getDeclaredField("supplierId").getType());
        assertEquals(Integer.class, saveDtoClass.getDeclaredField("defaultFlag").getType());
        assertEquals(Integer.class, saveDtoClass.getDeclaredField("enableStatus").getType());
        assertEquals(List.class, saveItemVoClass.getDeclaredField("skuOptions").getType());
        assertEquals(List.class, saveItemVoClass.getDeclaredField("supplierOptions").getType());
        assertEquals(String.class, listItemVoClass.getDeclaredField("skuCode").getType());
        assertEquals(String.class, listItemVoClass.getDeclaredField("supplierName").getType());
        assertEquals(String.class, historyItemVoClass.getDeclaredField("operateType").getType());
    }

    @Test
    void should_make_relation_controller_return_expected_wrappers() throws Exception {
        Class<?> controllerClass = Class.forName("xbb.ai.erp.module.product.admin.SkuSupplierRelationAdminController");
        Class<?> listDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationListDTO");
        Class<?> queryDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationQueryDTO");
        Class<?> saveDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSaveDTO");
        Class<?> skuOptionsDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationSkuOptionsDTO");
        Class<?> idBaseDtoClass = Class.forName("xbb.ai.erp.base.common.dto.IdBaseDTO");
        Class<?> listBaseVoClass = Class.forName("xbb.ai.erp.base.common.vo.ListBaseVO");
        Class<?> baseVoClass = Class.forName("xbb.ai.erp.base.common.vo.BaseVO");
        Class<?> mainDtoClass = Class.forName("xbb.ai.erp.module.product.admin.dto.SkuSupplierRelationMainDTO");
        Class<?> listItemVoClass = Class.forName("xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationListItemVO");
        Class<?> skuOptionVoClass = Class.forName("xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationSkuOptionVO");
        Class<?> historyItemVoClass = Class.forName("xbb.ai.erp.module.product.admin.vo.SkuSupplierRelationHistoryItemVO");

        Method list = controllerClass.getMethod("list", listDtoClass);
        Method addItem = controllerClass.getMethod("addItem", queryDtoClass);
        Method updateItem = controllerClass.getMethod("updateItem", idBaseDtoClass);
        Method save = controllerClass.getMethod("save", saveDtoClass);
        Method setDefault = controllerClass.getMethod("setDefault", idBaseDtoClass);
        Method enable = controllerClass.getMethod("enable", idBaseDtoClass);
        Method disable = controllerClass.getMethod("disable", idBaseDtoClass);
        Method delete = controllerClass.getMethod("delete", idBaseDtoClass);
        Method history = controllerClass.getMethod("history", idBaseDtoClass);
        Method skuOptionsBySpu = controllerClass.getMethod("skuOptionsBySpu", skuOptionsDtoClass);

        assertResultDataType(list.getGenericReturnType(), listBaseVoClass, listItemVoClass);
        assertResultDataType(addItem.getGenericReturnType(), Class.forName("xbb.ai.erp.base.common.vo.SaveItemVO"), mainDtoClass);
        assertResultDataType(updateItem.getGenericReturnType(), Class.forName("xbb.ai.erp.base.common.vo.SaveItemVO"), mainDtoClass);
        assertResultDataType(save.getGenericReturnType(), Long.class);
        assertResultDataType(setDefault.getGenericReturnType(), baseVoClass);
        assertResultDataType(enable.getGenericReturnType(), baseVoClass);
        assertResultDataType(disable.getGenericReturnType(), baseVoClass);
        assertResultDataType(delete.getGenericReturnType(), baseVoClass);
        assertResultListDataType(history.getGenericReturnType(), historyItemVoClass);
        assertResultListDataType(skuOptionsBySpu.getGenericReturnType(), skuOptionVoClass);
    }

    private void assertResultDataType(Type genericReturnType, Class<?> expectedType) {
        ParameterizedType resultType = (ParameterizedType) genericReturnType;
        assertEquals(expectedType, resultType.getActualTypeArguments()[0]);
    }

    private void assertResultDataType(Type genericReturnType, Class<?> expectedRawType, Class<?> expectedItemType) {
        ParameterizedType resultType = (ParameterizedType) genericReturnType;
        ParameterizedType dataType = (ParameterizedType) resultType.getActualTypeArguments()[0];
        assertEquals(expectedRawType, dataType.getRawType());
        assertEquals(expectedItemType, dataType.getActualTypeArguments()[0]);
    }

    private void assertResultListDataType(Type genericReturnType, Class<?> expectedItemType) {
        ParameterizedType resultType = (ParameterizedType) genericReturnType;
        ParameterizedType dataType = (ParameterizedType) resultType.getActualTypeArguments()[0];
        assertEquals(List.class, dataType.getRawType());
        assertEquals(expectedItemType, dataType.getActualTypeArguments()[0]);
    }
}
