package xbb.ai.erp.module.common.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.common.admin.dto.MenuListDTO;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuControllerStructureTest {

    @Test
    void should_define_menu_endpoint_with_menu_list_dto() throws Exception {
        Method method = MenuController.class.getMethod("list", MenuListDTO.class);
        assertNotNull(method);
    }
}
