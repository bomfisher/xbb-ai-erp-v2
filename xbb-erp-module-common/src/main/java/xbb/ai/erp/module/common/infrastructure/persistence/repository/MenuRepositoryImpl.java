package xbb.ai.erp.module.common.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.common.infrastructure.persistence.mapper.MenuMapper;
import xbb.ai.erp.module.common.infrastructure.persistence.po.MenuPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuMapper menuMapper;

    @Override
    public List<MenuPO> findByCondition(Map<String, Object> conditionMap) {
        return menuMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MenuPO>()
            .eq(conditionMap.get("corpid") != null, MenuPO::getCorpid, (String) conditionMap.get("corpid"))
            .eq(conditionMap.get("surface") != null, MenuPO::getSurface, (String) conditionMap.get("surface"))
            .eq(conditionMap.get("enableStatus") != null, MenuPO::getEnableStatus, (Integer) conditionMap.get("enableStatus"))
            .orderByAsc(MenuPO::getParentMenuId, MenuPO::getId));
    }
}
