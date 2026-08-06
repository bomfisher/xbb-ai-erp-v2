package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.domain.model.RolePermissionDataScope;
import xbb.ai.erp.module.org.domain.model.RolePermissionRelation;
import xbb.ai.erp.module.org.infrastructure.persistence.convertor.OrgConvertor;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.RoleMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.RolePermissionDataScopeMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.RolePermissionRelationMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.po.RolePO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.RolePermissionDataScopePO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.RolePermissionRelationPO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl {

    private final RoleMapper roleMapper;
    private final RolePermissionRelationMapper rolePermissionRelationMapper;
    private final RolePermissionDataScopeMapper rolePermissionDataScopeMapper;

    public void insert(Role role) {
        RolePO po = OrgConvertor.toPO(role);
        roleMapper.insert(po);
        role.setId(po.getId());
    }

    public List<Role> list(String corpid, String keyword, Integer roleStatus) {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<RolePO>()
            .eq(RolePO::getCorpid, corpid)
            .eq(roleStatus != null, RolePO::getRoleStatus, roleStatus)
            .orderByAsc(RolePO::getId);
        if (keyword != null && !keyword.isBlank()) {
            queryWrapper.like(RolePO::getRoleName, keyword);
        }
        return roleMapper.selectList(queryWrapper).stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public void replacePermissionRelations(String corpid, Long roleId, List<RolePermissionRelation> relations) {
        rolePermissionRelationMapper.delete(new LambdaQueryWrapper<RolePermissionRelationPO>()
            .eq(RolePermissionRelationPO::getCorpid, corpid)
            .eq(RolePermissionRelationPO::getRoleId, roleId));
        for (RolePermissionRelation relation : relations) {
            rolePermissionRelationMapper.insert(OrgConvertor.toPO(relation));
        }
    }

    public void replacePermissionDataScopes(String corpid, Long roleId, List<RolePermissionDataScope> scopes) {
        rolePermissionDataScopeMapper.delete(new LambdaQueryWrapper<RolePermissionDataScopePO>()
            .eq(RolePermissionDataScopePO::getCorpid, corpid)
            .eq(RolePermissionDataScopePO::getRoleId, roleId));
        for (RolePermissionDataScope scope : scopes) {
            rolePermissionDataScopeMapper.insert(OrgConvertor.toPO(scope));
        }
    }

    public Role findById(String corpid, Long id) {
        return OrgConvertor.toDomain(roleMapper.selectOne(new LambdaQueryWrapper<RolePO>()
            .eq(RolePO::getCorpid, corpid)
            .eq(RolePO::getId, id)));
    }

    public List<Role> listByIds(String corpid, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return roleMapper.selectList(new LambdaQueryWrapper<RolePO>()
                .eq(RolePO::getCorpid, corpid)
                .in(RolePO::getId, ids))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public List<Role> listByType(String corpid, String roleType) {
        if (corpid == null || corpid.isBlank() || roleType == null || roleType.isBlank()) {
            return List.of();
        }
        return roleMapper.selectList(new LambdaQueryWrapper<RolePO>()
                .eq(RolePO::getCorpid, corpid)
                .eq(RolePO::getRoleType, roleType)
                .orderByAsc(RolePO::getId))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public void updateRoleStatus(String corpid, Long id, Integer roleStatus) {
        roleMapper.update(null, new LambdaUpdateWrapper<RolePO>()
            .eq(RolePO::getCorpid, corpid)
            .eq(RolePO::getId, id)
            .set(RolePO::getRoleStatus, roleStatus));
    }

    public List<RolePermissionRelation> listPermissionRelations(String corpid, Long roleId) {
        return rolePermissionRelationMapper.selectList(new LambdaQueryWrapper<RolePermissionRelationPO>()
                .eq(RolePermissionRelationPO::getCorpid, corpid)
                .eq(RolePermissionRelationPO::getRoleId, roleId))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public List<RolePermissionDataScope> listPermissionDataScopes(String corpid, Long roleId) {
        return rolePermissionDataScopeMapper.selectList(new LambdaQueryWrapper<RolePermissionDataScopePO>()
                .eq(RolePermissionDataScopePO::getCorpid, corpid)
                .eq(RolePermissionDataScopePO::getRoleId, roleId))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }
}
