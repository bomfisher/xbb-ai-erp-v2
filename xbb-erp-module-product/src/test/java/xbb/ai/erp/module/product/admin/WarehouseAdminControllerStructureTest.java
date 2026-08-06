package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.WarehouseListDTO;
import xbb.ai.erp.module.product.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.product.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WarehouseAdminControllerStructureTest {

    @Test
    void should_depend_on_warehouse_admin_app_service() throws Exception {
        Field field = WarehouseAdminController.class.getDeclaredField("warehouseAdminAppService");
        assertEquals(
            Class.forName("xbb.ai.erp.module.product.application.service.WarehouseAdminAppService"),
            field.getType()
        );
    }

    @Test
    void should_declare_post_request_body_endpoints() throws Exception {
        Method list = WarehouseAdminController.class.getMethod("list", WarehouseListDTO.class);
        Method addItem = WarehouseAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = WarehouseAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method save = WarehouseAdminController.class.getMethod("save", WarehouseSaveDTO.class);
        Method detail = WarehouseAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method delete = WarehouseAdminController.class.getMethod("delete", BatchBaseDTO.class);

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
    void should_make_warehouse_controller_contract_match_task6() throws Exception {
        Method list = WarehouseAdminController.class.getMethod("list", WarehouseListDTO.class);
        Method addItem = WarehouseAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = WarehouseAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method detail = WarehouseAdminController.class.getMethod("detail", IdBaseDTO.class);

        ParameterizedType listResultType = (ParameterizedType) list.getGenericReturnType();
        ParameterizedType listDataType = (ParameterizedType) listResultType.getActualTypeArguments()[0];
        ParameterizedType addItemResultType = (ParameterizedType) addItem.getGenericReturnType();
        ParameterizedType addItemDataType = (ParameterizedType) addItemResultType.getActualTypeArguments()[0];
        ParameterizedType updateItemResultType = (ParameterizedType) updateItem.getGenericReturnType();
        ParameterizedType updateItemDataType = (ParameterizedType) updateItemResultType.getActualTypeArguments()[0];
        ParameterizedType detailResultType = (ParameterizedType) detail.getGenericReturnType();

        assertEquals(ListBaseVO.class, listDataType.getRawType());
        assertEquals(WarehouseListItemVO.class, listDataType.getActualTypeArguments()[0]);
        assertEquals(SaveItemVO.class, addItemDataType.getRawType());
        assertEquals(WarehouseSaveItemVO.class, addItemDataType.getActualTypeArguments()[0]);
        assertEquals(SaveItemVO.class, updateItemDataType.getRawType());
        assertEquals(WarehouseSaveItemVO.class, updateItemDataType.getActualTypeArguments()[0]);
        assertEquals(WarehouseDetailVO.class, detailResultType.getActualTypeArguments()[0]);
    }
}
