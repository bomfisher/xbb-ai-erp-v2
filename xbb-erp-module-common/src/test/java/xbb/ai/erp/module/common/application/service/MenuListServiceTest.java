package xbb.ai.erp.module.common.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.common.admin.dto.MenuListDTO;
import xbb.ai.erp.module.common.admin.vo.MenuListVO;
import xbb.ai.erp.module.common.application.service.impl.MenuServiceImpl;
import xbb.ai.erp.module.common.infrastructure.persistence.po.MenuPO;
import xbb.ai.erp.module.common.infrastructure.persistence.repository.MenuRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MenuListServiceTest {

    @Test
    void should_trim_menu_tree_by_route_path_rules() {
        MenuRepository repository = new InMemoryMenuRepository(List.of(
            menu(1L, null, "ROOT", "根节点", null, null),
            menu(2L, 1L, "CHILD_GROUP", "分组", null, null),
            menu(3L, 2L, "CHILD_LEAF", "叶子", "/leaf", "leaf/create"),
            menu(4L, null, "ORPHAN_LEAF", "孤儿叶子", "/orphan", "orphan/create"),
            menu(5L, 1L, "EMPTY_GROUP", "空分组", null, null)
        ));
        MenuServiceImpl service = new MenuServiceImpl(repository);
        MenuListDTO dto = new MenuListDTO();
        dto.setCorpid("corp-001");
        dto.setSurface("PC_ADMIN");

        MenuListVO vo = service.list(dto);

        assertEquals(1, vo.getList().size());
        assertEquals("ROOT", vo.getList().get(0).getMenuCode());
        assertEquals(1, vo.getList().get(0).getChildren().size());
        assertEquals("CHILD_GROUP", vo.getList().get(0).getChildren().get(0).getMenuCode());
        assertEquals("CHILD_LEAF", vo.getList().get(0).getChildren().get(0).getChildren().get(0).getMenuCode());
        assertEquals(List.of(), vo.getList().get(0).getChildren().get(0).getChildren().get(0).getChildren());
    }

    private static MenuPO menu(Long id, Long parentMenuId, String menuCode, String menuName, String routePath, String componentPath) {
        MenuPO menuPO = new MenuPO();
        menuPO.setId(id);
        menuPO.setCorpid("corp-001");
        menuPO.setMenuCode(menuCode);
        menuPO.setMenuName(menuName);
        menuPO.setSurface("PC_ADMIN");
        menuPO.setParentMenuId(parentMenuId);
        menuPO.setRoutePath(routePath);
        menuPO.setComponentPath(componentPath);
        menuPO.setEnableStatus(1);
        return menuPO;
    }

    private static final class InMemoryMenuRepository implements MenuRepository {
        private final List<MenuPO> menus;

        private InMemoryMenuRepository(List<MenuPO> menus) {
            this.menus = menus;
        }

        @Override
        public List<MenuPO> findByCondition(java.util.Map<String, Object> conditionMap) {
            String corpid = (String) conditionMap.get("corpid");
            String surface = (String) conditionMap.get("surface");
            Integer enableStatus = (Integer) conditionMap.get("enableStatus");
            return menus.stream()
                .filter(menu -> corpid == null || corpid.equals(menu.getCorpid()))
                .filter(menu -> surface == null || surface.equals(menu.getSurface()))
                .filter(menu -> enableStatus == null || enableStatus.equals(menu.getEnableStatus()))
                .toList();
        }
    }
}
