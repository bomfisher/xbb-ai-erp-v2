package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategorySaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategorySaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductCategoryAdminControllerStructureTest {

    @Test
    void should_depend_on_category_admin_app_service() throws Exception {
        Field field = ProductCategoryAdminController.class.getDeclaredField("productCategoryAdminAppService");
        assertEquals(
            Class.forName("xbb.ai.erp.module.product.application.service.ProductCategoryAdminAppService"),
            field.getType()
        );
    }

    @Test
    void should_declare_post_request_body_endpoints() throws Exception {
        Method list = ProductCategoryAdminController.class.getMethod("list", ProductCategoryListDTO.class);
        Method addItem = ProductCategoryAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = ProductCategoryAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method save = ProductCategoryAdminController.class.getMethod("save", ProductCategorySaveDTO.class);
        Method detail = ProductCategoryAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method delete = ProductCategoryAdminController.class.getMethod("delete", BatchBaseDTO.class);

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
    void should_make_category_controller_contract_match_crud_facade() throws Exception {
        Method list = ProductCategoryAdminController.class.getMethod("list", ProductCategoryListDTO.class);
        Method addItem = ProductCategoryAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = ProductCategoryAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method detail = ProductCategoryAdminController.class.getMethod("detail", IdBaseDTO.class);

        ParameterizedType listResultType = (ParameterizedType) list.getGenericReturnType();
        ParameterizedType listDataType = (ParameterizedType) listResultType.getActualTypeArguments()[0];
        ParameterizedType addItemResultType = (ParameterizedType) addItem.getGenericReturnType();
        ParameterizedType addItemDataType = (ParameterizedType) addItemResultType.getActualTypeArguments()[0];
        ParameterizedType updateItemResultType = (ParameterizedType) updateItem.getGenericReturnType();
        ParameterizedType updateItemDataType = (ParameterizedType) updateItemResultType.getActualTypeArguments()[0];
        ParameterizedType detailResultType = (ParameterizedType) detail.getGenericReturnType();

        assertEquals(ListBaseVO.class, listDataType.getRawType());
        assertEquals(ProductCategoryVO.class, listDataType.getActualTypeArguments()[0]);
        assertEquals(SaveItemVO.class, addItemDataType.getRawType());
        assertEquals(ProductCategorySaveItemVO.class, addItemDataType.getActualTypeArguments()[0]);
        assertEquals(SaveItemVO.class, updateItemDataType.getRawType());
        assertEquals(ProductCategorySaveItemVO.class, updateItemDataType.getActualTypeArguments()[0]);
        assertEquals(ProductCategoryDetailVO.class, detailResultType.getActualTypeArguments()[0]);
    }
}
