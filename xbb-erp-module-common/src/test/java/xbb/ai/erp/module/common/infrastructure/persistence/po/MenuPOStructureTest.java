package xbb.ai.erp.module.common.infrastructure.persistence.po;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuPOStructureTest {

    @Test
    void should_not_keep_removed_remark_field_on_menu_po() {
        assertThrows(NoSuchFieldException.class, () -> MenuPO.class.getDeclaredField("remark"));
    }
}
