package xbb.ai.erp.module.common.admin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ListCommonControllerStructureTest {

    @Test
    void should_use_list_common_query_dto_for_all_four_metadata_endpoints() throws Exception {
        Class<?> dtoClass = Class.forName("xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO");

        Method filter = ListCommonController.class.getMethod("filter", dtoClass);
        Method header = ListCommonController.class.getMethod("header", dtoClass);
        Method topButton = ListCommonController.class.getMethod("topButton", dtoClass);
        Method bottomButton = ListCommonController.class.getMethod("bottomButton", dtoClass);

        assertNotNull(filter);
        assertNotNull(header);
        assertNotNull(topButton);
        assertNotNull(bottomButton);
    }

    @Test
    void should_define_unified_list_protocol_types() throws Exception {
        assertNotNull(Class.forName("xbb.ai.erp.module.common.admin.dto.ListQueryDTO"));
        assertNotNull(Class.forName("xbb.ai.erp.module.common.admin.vo.ListPageVO"));
        assertNotNull(Class.forName("xbb.ai.erp.module.common.admin.vo.ListSchemaVO"));
    }

    @Test
    void should_define_button_vo_list_field() throws Exception {
        Class<?> topButtonVoClass = Class.forName("xbb.ai.erp.module.common.admin.vo.ListTopButtonVO");
        Class<?> bottomButtonVoClass = Class.forName("xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO");
        Class<?> buttonItemClass = Class.forName("xbb.ai.erp.module.common.admin.pojo.ListButtonItemPojo");

        Field topList = topButtonVoClass.getDeclaredField("list");
        Field bottomList = bottomButtonVoClass.getDeclaredField("list");

        assertEquals(List.class, topList.getType());
        assertEquals(List.class, bottomList.getType());
        assertNotNull(buttonItemClass);
    }

    @Test
    void should_define_row_action_endpoint_on_common_controller() throws Exception {
        Method rowAction = ListCommonController.class.getMethod("rowAction", Class.forName("xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO"));
        assertNotNull(rowAction);
    }
}
