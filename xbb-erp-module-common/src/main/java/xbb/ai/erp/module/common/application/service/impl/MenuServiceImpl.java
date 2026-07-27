package xbb.ai.erp.module.common.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.common.admin.dto.MenuListDTO;
import xbb.ai.erp.module.common.admin.vo.MenuItemVO;
import xbb.ai.erp.module.common.admin.vo.MenuListVO;
import xbb.ai.erp.module.common.application.service.MenuService;
import xbb.ai.erp.module.common.infrastructure.persistence.po.MenuPO;
import xbb.ai.erp.module.common.infrastructure.persistence.repository.MenuRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public MenuListVO list(MenuListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("surface", dto.getSurface());
        conditionMap.put("enableStatus", 1);
        List<MenuPO> menus = menuRepository == null ? List.of() : menuRepository.findByCondition(conditionMap);
        Map<Long, MenuPO> menuMap = menus.stream()
            .filter(menu -> menu.getId() != null)
            .collect(Collectors.toMap(MenuPO::getId, menu -> menu, (left, right) -> left, LinkedHashMap::new));

        Set<Long> keepIds = menus.stream()
            .filter(this::isClickable)
            .filter(menu -> hasRootChain(menu, menuMap))
            .map(MenuPO::getId)
            .filter(Objects::nonNull)
            .flatMap(id -> ancestorIds(id, menuMap).stream())
            .collect(Collectors.toSet());

        MenuListVO vo = new MenuListVO();
        vo.setList(buildForest(menus, keepIds));
        return vo;
    }

    private List<MenuItemVO> buildForest(List<MenuPO> menus, Set<Long> keepIds) {
        Map<Long, List<MenuPO>> childrenMap = menus.stream()
            .filter(menu -> menu.getId() != null)
            .filter(menu -> keepIds.contains(menu.getId()))
            .collect(Collectors.groupingBy(menu -> menu.getParentMenuId() == null ? 0L : menu.getParentMenuId(), LinkedHashMap::new, Collectors.toList()));
        return childrenMap.getOrDefault(0L, List.of()).stream()
            .map(menu -> toNode(menu, childrenMap, keepIds))
            .filter(Objects::nonNull)
            .toList();
    }

    private MenuItemVO toNode(MenuPO menu, Map<Long, List<MenuPO>> childrenMap, Set<Long> keepIds) {
        if (menu.getId() == null || !keepIds.contains(menu.getId())) {
            return null;
        }
        List<MenuItemVO> children = childrenMap.getOrDefault(menu.getId(), List.of()).stream()
            .map(child -> toNode(child, childrenMap, keepIds))
            .filter(Objects::nonNull)
            .toList();

        MenuItemVO vo = new MenuItemVO();
        vo.setMenuCode(menu.getMenuCode());
        vo.setMenuName(menu.getMenuName());
        vo.setRoutePath(menu.getRoutePath());
        vo.setComponentPath(menu.getComponentPath());
        vo.setChildren(children);
        return vo;
    }

    private boolean isClickable(MenuPO menu) {
        return menu.getRoutePath() != null && !menu.getRoutePath().isBlank();
    }

    private boolean hasRootChain(MenuPO menu, Map<Long, MenuPO> menuMap) {
        if (menu.getParentMenuId() == null) {
            return false;
        }
        Long currentParentId = menu.getParentMenuId();
        while (currentParentId != null) {
            MenuPO current = menuMap.get(currentParentId);
            if (current == null) {
                return false;
            }
            if (current.getParentMenuId() == null) {
                return true;
            }
            currentParentId = current.getParentMenuId();
        }
        return false;
    }

    private List<Long> ancestorIds(Long menuId, Map<Long, MenuPO> menuMap) {
        List<Long> ids = new ArrayList<>();
        MenuPO current = menuMap.get(menuId);
        while (current != null && current.getId() != null) {
            ids.add(current.getId());
            Long parentMenuId = current.getParentMenuId();
            if (parentMenuId == null) {
                break;
            }
            current = menuMap.get(parentMenuId);
        }
        return ids;
    }
}
