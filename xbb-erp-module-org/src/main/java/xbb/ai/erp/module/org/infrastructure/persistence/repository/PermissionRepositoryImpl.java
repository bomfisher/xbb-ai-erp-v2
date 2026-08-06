package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.org.domain.model.Permission;
import xbb.ai.erp.module.org.infrastructure.persistence.convertor.OrgConvertor;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.PermissionMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.po.PermissionPO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl {

    private final PermissionMapper permissionMapper;

    public void insert(Permission permission) {
        PermissionPO po = OrgConvertor.toPO(permission);
        permissionMapper.insert(po);
        permission.setId(po.getId());
    }

    public Permission findById(Long id) {
        return OrgConvertor.toDomain(permissionMapper.selectById(id));
    }

    public List<Permission> listByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return permissionMapper.selectList(new LambdaQueryWrapper<PermissionPO>()
                .in(PermissionPO::getId, ids))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public List<Permission> list() {
        return list(null, null, null);
    }

    public List<Permission> list(String keyword, String permissionType, Integer permissionStatus) {
        LambdaQueryWrapper<PermissionPO> queryWrapper = new LambdaQueryWrapper<PermissionPO>()
            .eq(permissionType != null && !permissionType.isBlank(), PermissionPO::getPermissionType, permissionType)
            .eq(permissionStatus != null, PermissionPO::getPermissionStatus, permissionStatus)
            .orderByAsc(PermissionPO::getId);
        if (keyword != null && !keyword.isBlank()) {
            queryWrapper.and(wrapper -> wrapper
                .like(PermissionPO::getPermissionCode, keyword)
                .or()
                .like(PermissionPO::getPermissionName, keyword));
        }
        return permissionMapper.selectList(queryWrapper).stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }
}
