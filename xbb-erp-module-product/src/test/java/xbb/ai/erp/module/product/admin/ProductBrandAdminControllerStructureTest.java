package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductBrandAdminControllerStructureTest {

    @Test
    void should_depend_on_brand_admin_app_service() throws Exception {
        Field field = ProductBrandAdminController.class.getDeclaredField("productBrandAdminAppService");
        assertEquals(
            Class.forName("xbb.ai.erp.module.product.application.service.ProductBrandAdminAppService"),
            field.getType()
        );
    }

    @Test
    void should_declare_post_request_body_endpoints() throws Exception {
        Method list = ProductBrandAdminController.class.getMethod("list", ProductBrandListDTO.class);
        Method addItem = ProductBrandAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = ProductBrandAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method save = ProductBrandAdminController.class.getMethod("save", ProductBrandSaveDTO.class);
        Method detail = ProductBrandAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method delete = ProductBrandAdminController.class.getMethod("delete", BatchBaseDTO.class);

        assertNotNull(list.getAnnotation(PostMapping.class));
        assertNotNull(addItem.getAnnotation(PostMapping.class));
        assertNotNull(updateItem.getAnnotation(PostMapping.class));
        assertNotNull(save.getAnnotation(PostMapping.class));
        assertNotNull(detail.getAnnotation(PostMapping.class));
        assertNotNull(delete.getAnnotation(PostMapping.class));
        assertNotNull(list.getParameters()[0].getAnnotation(RequestBody.class));
        assertNotNull(addItem.getParameters()[0].getAnnotation(RequestBody.class));
        assertNotNull(updateItem.getParameters()[0].getAnnotation(RequestBody.class));
        assertNotNull(save.getParameters()[0].getAnnotation(RequestBody.class));
        assertNotNull(detail.getParameters()[0].getAnnotation(RequestBody.class));
        assertNotNull(delete.getParameters()[0].getAnnotation(RequestBody.class));
    }

    @Test
    void should_make_brand_controller_contract_match_crud_facade() throws Exception {
        Method list = ProductBrandAdminController.class.getMethod("list", ProductBrandListDTO.class);
        Method addItem = ProductBrandAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = ProductBrandAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method detail = ProductBrandAdminController.class.getMethod("detail", IdBaseDTO.class);

        ParameterizedType listResultType = (ParameterizedType) list.getGenericReturnType();
        ParameterizedType listDataType = (ParameterizedType) listResultType.getActualTypeArguments()[0];
        ParameterizedType addItemResultType = (ParameterizedType) addItem.getGenericReturnType();
        ParameterizedType addItemDataType = (ParameterizedType) addItemResultType.getActualTypeArguments()[0];
        ParameterizedType updateItemResultType = (ParameterizedType) updateItem.getGenericReturnType();
        ParameterizedType updateItemDataType = (ParameterizedType) updateItemResultType.getActualTypeArguments()[0];
        ParameterizedType detailResultType = (ParameterizedType) detail.getGenericReturnType();

        assertEquals(ListBaseVO.class, listDataType.getRawType());
        assertEquals(ProductBrandVO.class, listDataType.getActualTypeArguments()[0]);
        assertEquals(SaveItemVO.class, addItemDataType.getRawType());
        assertEquals(ProductBrandSaveItemVO.class, addItemDataType.getActualTypeArguments()[0]);
        assertEquals(SaveItemVO.class, updateItemDataType.getRawType());
        assertEquals(ProductBrandSaveItemVO.class, updateItemDataType.getActualTypeArguments()[0]);
        assertEquals(ProductBrandDetailVO.class, detailResultType.getActualTypeArguments()[0]);
    }
}
