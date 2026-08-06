package xbb.ai.erp.module.org.application.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.RoleListDTO;
import xbb.ai.erp.module.org.admin.vo.RoleListItemVO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
import xbb.ai.erp.module.org.application.pojo.RolePermissionSavePojo;
import xbb.ai.erp.module.org.application.service.RoleAdminAppService;
import xbb.ai.erp.module.org.domain.enums.EnableStatusEnum;
import xbb.ai.erp.module.org.domain.enums.PermissionTypeEnum;
import xbb.ai.erp.module.org.domain.model.Permission;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.domain.model.RolePermissionDataScope;
import xbb.ai.erp.module.org.domain.model.RolePermissionRelation;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.PermissionRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.RoleRepositoryImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleAdminAppServiceImpl implements RoleAdminAppService {

    private final RoleRepositoryImpl roleRepository;
    private final PermissionRepositoryImpl permissionRepository;

    public RoleAdminAppServiceImpl(
        RoleRepositoryImpl roleRepository,
        PermissionRepositoryImpl permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public ListBaseVO<RoleListItemVO> list(BaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (!(dto instanceof RoleListDTO listDTO)) {
            throw new BizException("角色列表参数错误");
        }
        Integer pageNum = dto instanceof ListBaseDTO listBaseDTO ? listBaseDTO.getPageNum() : null;
        Integer pageSize = dto instanceof ListBaseDTO listBaseDTO ? listBaseDTO.getPageSize() : null;
        return OrgAdminAssembler.toRoleListResultVO(
            roleRepository.list(dto.getCorpid(), listDTO.getKeyword(), listDTO.getRoleStatus()),
            pageNum,
            pageSize
        );
    }

    @Override
    public BaseVO detail(IdBaseDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO save(BaseDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO enable(IdBaseDTO dto) {
        validateRoleStatusCommand(dto);
        roleRepository.updateRoleStatus(dto.getCorpid(), dto.getId(), EnableStatusEnum.ENABLE.getCode());
        return new BaseVO();
    }

    @Override
    public BaseVO disable(IdBaseDTO dto) {
        validateRoleStatusCommand(dto);
        roleRepository.updateRoleStatus(dto.getCorpid(), dto.getId(), EnableStatusEnum.DISABLE.getCode());
        return new BaseVO();
    }

    @Override
    public OrgAdminAssembler.RolePermissionDetailResultVO permissionDetail(IdBaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (dto.getId() == null) {
            throw new BizException("角色id不能为空");
        }
        Role role = roleRepository.findById(dto.getCorpid(), dto.getId());
        if (role == null) {
            throw new BizException("角色不存在");
        }
        Set<Long> selectedPermissionIdSet = roleRepository.listPermissionRelations(dto.getCorpid(), dto.getId()).stream()
            .map(RolePermissionRelation::getPermissionId)
            .collect(Collectors.toSet());
        Map<Long, String> dataScopeMap = roleRepository.listPermissionDataScopes(dto.getCorpid(), dto.getId()).stream()
            .collect(Collectors.toMap(RolePermissionDataScope::getPermissionId, RolePermissionDataScope::getDataScopeType, (left, right) -> right));
        return OrgAdminAssembler.toRolePermissionDetailResultVO(
            role,
            permissionRepository.list(),
            selectedPermissionIdSet,
            dataScopeMap
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseVO savePermission(BaseDTO dto) {
        if (!(dto instanceof RolePermissionSavePojo savePojo)) {
            throw new BizException("角色权限保存参数错误");
        }
        if (savePojo.getCorpid() == null || savePojo.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (savePojo.getRoleId() == null) {
            throw new BizException("角色id不能为空");
        }
        if (roleRepository.findById(savePojo.getCorpid(), savePojo.getRoleId()) == null) {
            throw new BizException("角色不存在");
        }
        List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> menuPermissionList = normalizeMenuPermissionList(savePojo.getPermissionList());
        Map<Long, Permission> permissionMap = permissionRepository.list().stream()
            .collect(Collectors.toMap(Permission::getId, item -> item, (left, right) -> left, LinkedHashMap::new));
        validateMenuPermissionList(menuPermissionList, permissionMap);
        List<Long> permissionIdList = expandPermissionIds(menuPermissionList);
        roleRepository.replacePermissionRelations(savePojo.getCorpid(), savePojo.getRoleId(), buildPermissionRelations(savePojo, permissionIdList));
        roleRepository.replacePermissionDataScopes(savePojo.getCorpid(), savePojo.getRoleId(), buildPermissionScopes(savePojo, menuPermissionList, permissionMap));
        return OrgAdminAssembler.toRolePermissionSaveResultVO(savePojo.getRoleId(), menuPermissionList);
    }

    public static RoleAdminAppServiceImpl forTesting(
        RoleRepositoryImpl roleRepository,
        PermissionRepositoryImpl permissionRepository
    ) {
        return new RoleAdminAppServiceImpl(roleRepository, permissionRepository);
    }

    private void validateRoleStatusCommand(IdBaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (dto.getId() == null) {
            throw new BizException("角色id不能为空");
        }
        if (roleRepository.findById(dto.getCorpid(), dto.getId()) == null) {
            throw new BizException("角色不存在");
        }
    }

    private void validateMenuPermissionList(
        List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> menuPermissionList,
        Map<Long, Permission> permissionMap
    ) {
        for (RolePermissionSavePojo.MenuPermissionSaveItemPojo item : menuPermissionList) {
            if (item.getMenuPermissionId() == null) {
                throw new BizException("菜单权限id不能为空");
            }
            Permission menuPermission = permissionMap.get(item.getMenuPermissionId());
            if (menuPermission == null) {
                throw new BizException("权限不存在");
            }
            if (!Objects.equals(PermissionTypeEnum.MENU.getCode(), menuPermission.getPermissionType())) {
                throw new BizException("菜单权限配置错误");
            }
            if (item.getDataScopeType() != null && !Objects.equals(item.getSelected(), 1)) {
                throw new BizException("未授权菜单不能设置数据权限");
            }
            if (item.getDataScopeType() != null && !Objects.equals(menuPermission.getDataScopeFlag(), 1)) {
                throw new BizException("当前菜单不支持数据权限");
            }
            for (Long actionPermissionId : normalizeActionPermissionIds(item.getActionPermissionIdList())) {
                Permission actionPermission = permissionMap.get(actionPermissionId);
                if (actionPermission == null) {
                    throw new BizException("权限不存在");
                }
                if (!Objects.equals(PermissionTypeEnum.ACTION.getCode(), actionPermission.getPermissionType())) {
                    throw new BizException("操作权限配置错误");
                }
                if (!Objects.equals(actionPermission.getParentId(), item.getMenuPermissionId())) {
                    throw new BizException("操作权限不属于当前菜单");
                }
            }
        }
    }

    private List<RolePermissionRelation> buildPermissionRelations(RolePermissionSavePojo savePojo, List<Long> permissionIdList) {
        List<RolePermissionRelation> relations = new ArrayList<>();
        for (Long permissionId : permissionIdList) {
            relations.add(OrgAdminAssembler.toRolePermissionRelation(savePojo.getCorpid(), savePojo.getRoleId(), permissionId));
        }
        return relations;
    }

    private List<RolePermissionDataScope> buildPermissionScopes(
        RolePermissionSavePojo savePojo,
        List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> menuPermissionList,
        Map<Long, Permission> permissionMap
    ) {
        List<RolePermissionDataScope> scopes = new ArrayList<>();
        for (RolePermissionSavePojo.MenuPermissionSaveItemPojo item : menuPermissionList) {
            Permission menuPermission = permissionMap.get(item.getMenuPermissionId());
            if (!Objects.equals(item.getSelected(), 1)) {
                continue;
            }
            if (!Objects.equals(menuPermission.getDataScopeFlag(), 1)) {
                continue;
            }
            scopes.add(OrgAdminAssembler.toRolePermissionDataScope(
                savePojo.getCorpid(),
                savePojo.getRoleId(),
                item.getMenuPermissionId(),
                item.getDataScopeType()
            ));
        }
        return scopes;
    }

    private List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> normalizeMenuPermissionList(
        List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> permissionList
    ) {
        if (permissionList == null) {
            return List.of();
        }
        Map<Long, RolePermissionSavePojo.MenuPermissionSaveItemPojo> itemMap = new LinkedHashMap<>();
        for (RolePermissionSavePojo.MenuPermissionSaveItemPojo item : permissionList) {
            if (item == null || item.getMenuPermissionId() == null) {
                continue;
            }
            RolePermissionSavePojo.MenuPermissionSaveItemPojo normalized = new RolePermissionSavePojo.MenuPermissionSaveItemPojo();
            normalized.setMenuPermissionId(item.getMenuPermissionId());
            normalized.setSelected(Objects.equals(item.getSelected(), 1) ? 1 : 0);
            normalized.setDataScopeType(item.getDataScopeType());
            normalized.setActionPermissionIdList(normalizeActionPermissionIds(item.getActionPermissionIdList()));
            itemMap.put(normalized.getMenuPermissionId(), normalized);
        }
        return new ArrayList<>(itemMap.values());
    }

    private List<Long> normalizeActionPermissionIds(List<Long> actionPermissionIdList) {
        if (actionPermissionIdList == null) {
            return List.of();
        }
        return new ArrayList<>(new LinkedHashSet<>(actionPermissionIdList));
    }

    private List<Long> expandPermissionIds(List<RolePermissionSavePojo.MenuPermissionSaveItemPojo> menuPermissionList) {
        LinkedHashSet<Long> permissionIdSet = new LinkedHashSet<>();
        for (RolePermissionSavePojo.MenuPermissionSaveItemPojo item : menuPermissionList) {
            if (!Objects.equals(item.getSelected(), 1)) {
                continue;
            }
            permissionIdSet.add(item.getMenuPermissionId());
            permissionIdSet.addAll(normalizeActionPermissionIds(item.getActionPermissionIdList()));
        }
        return new ArrayList<>(permissionIdSet);
    }
}
