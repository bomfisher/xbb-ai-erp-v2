package xbb.ai.erp.module.org.application.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.EmployeeBatchDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeIdDTO;
import xbb.ai.erp.module.org.admin.dto.EmployeeListDTO;
import xbb.ai.erp.module.org.admin.dto.ResignedEmployeeListDTO;
import xbb.ai.erp.module.org.admin.vo.EmployeeDetailVO;
import xbb.ai.erp.module.org.admin.vo.EmployeeListItemVO;
import xbb.ai.erp.module.org.application.assembler.OrgAdminAssembler;
import xbb.ai.erp.module.org.application.pojo.EmployeeSavePojo;
import xbb.ai.erp.module.org.application.service.EmployeeAdminAppService;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.domain.model.EmployeeDepartmentRelation;
import xbb.ai.erp.module.org.domain.model.EmployeeRoleRelation;
import xbb.ai.erp.module.org.domain.model.Role;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.RoleRepositoryImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeAdminAppServiceImpl implements EmployeeAdminAppService {

    private static final String SYSTEM_ROLE_TYPE = "SYSTEM";

    private final EmployeeRepositoryImpl employeeRepository;
    private final DepartmentRepositoryImpl departmentRepository;
    private final RoleRepositoryImpl roleRepository;

    public EmployeeAdminAppServiceImpl(
        EmployeeRepositoryImpl employeeRepository,
        DepartmentRepositoryImpl departmentRepository,
        RoleRepositoryImpl roleRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ListBaseVO<EmployeeListItemVO> list(BaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        String keyword;
        Long departmentId = null;
        String employmentStatus = null;
        Integer userStatus = null;
        if (dto instanceof EmployeeListDTO listDTO) {
            keyword = listDTO.getKeyword();
            departmentId = listDTO.getDepartmentId();
            employmentStatus = listDTO.getEmploymentStatus();
            userStatus = listDTO.getUserStatus();
        } else if (dto instanceof ResignedEmployeeListDTO resignedDTO) {
            keyword = resignedDTO.getKeyword();
            employmentStatus = EmploymentStatusEnum.RESIGNED.getCode();
        } else {
            throw new BizException("员工列表参数错误");
        }
        List<Employee> employeeList = employeeRepository.list(dto.getCorpid(), keyword, employmentStatus, userStatus, departmentId);
        Map<Long, String> departmentNameMap = buildDepartmentNameMap(dto.getCorpid(), employeeList);
        Map<String, List<Role>> employeeRoleMap = buildEmployeeRoleMap(dto.getCorpid(), employeeList);
        return OrgAdminAssembler.toEmployeeListResultVO(
            employeeList,
            departmentNameMap,
            buildRoleIdMap(employeeRoleMap),
            buildRoleNameMap(employeeRoleMap),
            dto instanceof xbb.ai.erp.base.common.dto.ListBaseDTO listBaseDTO ? listBaseDTO.getPageNum() : null,
            dto instanceof xbb.ai.erp.base.common.dto.ListBaseDTO listBaseDTO ? listBaseDTO.getPageSize() : null
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseVO save(BaseDTO dto) {
        if (!(dto instanceof EmployeeSavePojo savePojo)) {
            throw new BizException("员工保存参数错误");
        }
        String targetUserId = resolveTargetUserId(savePojo);
        Employee existedEmployee = targetUserId == null || targetUserId.isBlank()
            ? null
            : employeeRepository.findByUserId(savePojo.getCorpid(), targetUserId);
        if (savePojo.getId() != null && !savePojo.getId().isBlank() && existedEmployee == null) {
            throw new BizException("员工不存在");
        }
        validateEmployeeSave(savePojo, existedEmployee);
        Employee employee = OrgAdminAssembler.toEmployee(savePojo);
        List<Long> roleIdList = normalizedRoleIds(savePojo);
        validateSystemRoleProtection(savePojo, existedEmployee, roleIdList);
        if (existedEmployee == null) {
            employeeRepository.insert(employee);
        } else {
            mergeExistingEmployeeData(employee, existedEmployee);
            employee.setId(existedEmployee.getId());
            employeeRepository.update(employee);
        }
        List<Long> departmentIdList = normalizedDepartmentIds(savePojo);
        employeeRepository.replaceDepartmentRelations(savePojo.getCorpid(), employee.getUserId(), buildDepartmentRelations(savePojo, employee.getId(), departmentIdList));
        employeeRepository.replaceRoleRelations(savePojo.getCorpid(), employee.getUserId(), buildRoleRelations(savePojo, employee.getId(), roleIdList));
        return OrgAdminAssembler.toEmployeeSaveResultVO(employee.getUserId(), departmentIdList, roleIdList);
    }

    @Override
    public BaseVO detail(EmployeeIdDTO dto) {
        if (dto == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new BizException("员工id不能为空");
        }
        Employee employee = employeeRepository.findByUserId(dto.getCorpid(), dto.getId());
        if (employee == null) {
            throw new BizException("员工不存在");
        }
        Map<Long, String> departmentNameMap = buildDepartmentNameMap(dto.getCorpid(), List.of(employee));
        List<Long> departmentIdList = employeeRepository.listDepartmentRelationsByUserId(dto.getCorpid(), dto.getId()).stream()
            .map(EmployeeDepartmentRelation::getDepartmentId)
            .distinct()
            .toList();
        List<EmployeeRoleRelation> relationList = employeeRepository.listRoleRelationsByUserId(dto.getCorpid(), dto.getId());
        List<Long> roleIdList = relationList.stream().map(EmployeeRoleRelation::getRoleId).distinct().toList();
        List<Role> roleList = roleRepository.listByIds(dto.getCorpid(), roleIdList);
        EmployeeDetailVO vo = OrgAdminAssembler.toEmployeeDetailVO(employee, departmentNameMap, departmentIdList, roleList);
        return vo;
    }

    @Override
    public BaseVO enable(EmployeeIdDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO disable(EmployeeIdDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO resign(EmployeeIdDTO dto) {
        return new BaseVO();
    }

    @Override
    public BaseVO delete(EmployeeBatchDTO dto) {
        return new BaseVO();
    }

    public static EmployeeAdminAppServiceImpl forTesting(
        EmployeeRepositoryImpl employeeRepository,
        DepartmentRepositoryImpl departmentRepository,
        RoleRepositoryImpl roleRepository
    ) {
        return new EmployeeAdminAppServiceImpl(employeeRepository, departmentRepository, roleRepository);
    }

    private void validateEmployeeSave(EmployeeSavePojo dto, Employee existedEmployee) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司id不能为空");
        }
        if (dto.getMainDepartmentId() == null) {
            throw new BizException("主部门不能为空");
        }
        List<Long> departmentIds = normalizedDepartmentIds(dto);
        List<Long> roleIds = normalizedRoleIds(dto);
        if (EmploymentStatusEnum.RESIGNED.getCode().equals(dto.getEmploymentStatus())
            && (departmentIds.size() > 1 || !roleIds.isEmpty())) {
            throw new BizException("离职员工不能分配部门和角色");
        }
        Set<Long> existingDepartmentIds = departmentRepository.listByIds(dto.getCorpid(), departmentIds).stream()
            .map(Department::getId)
            .collect(Collectors.toSet());
        if (!existingDepartmentIds.contains(dto.getMainDepartmentId())) {
            throw new BizException("主部门不存在");
        }
        if (existingDepartmentIds.size() != departmentIds.size()) {
            throw new BizException("部门不存在");
        }
        Map<Long, Role> roleMap = roleRepository.listByIds(dto.getCorpid(), roleIds).stream()
            .collect(Collectors.toMap(Role::getId, item -> item));
        if (roleMap.size() != roleIds.size()) {
            throw new BizException("角色不存在");
        }
        Set<Long> originalRoleIdSet = existedEmployee == null
            ? Set.of()
            : employeeRepository.listRoleRelationsByUserId(dto.getCorpid(), existedEmployee.getUserId()).stream()
                .map(EmployeeRoleRelation::getRoleId)
                .collect(Collectors.toSet());
        boolean hasNewDisabledRole = roleIds.stream()
            .filter(roleId -> !originalRoleIdSet.contains(roleId))
            .map(roleMap::get)
            .anyMatch(role -> role != null && role.getRoleStatus() != null && role.getRoleStatus() != 1);
        if (hasNewDisabledRole) {
            throw new BizException("角色已停用，不能分配");
        }
    }

    private void validateSystemRoleProtection(EmployeeSavePojo dto, Employee existedEmployee, List<Long> targetRoleIdList) {
        if (existedEmployee == null || existedEmployee.getUserId() == null || existedEmployee.getUserId().isBlank()) {
            return;
        }
        List<Role> systemRoleList = roleRepository.listByType(dto.getCorpid(), SYSTEM_ROLE_TYPE);
        Set<Long> systemRoleIdSet = systemRoleList.stream().map(Role::getId).collect(Collectors.toSet());
        if (systemRoleIdSet.isEmpty()) {
            return;
        }
        String targetUserId = resolveTargetUserId(dto);
        Set<Long> originalRoleIdSet = employeeRepository.listRoleRelationsByUserId(dto.getCorpid(), existedEmployee.getUserId()).stream()
            .map(EmployeeRoleRelation::getRoleId)
            .collect(Collectors.toSet());
        boolean hadSystemRole = originalRoleIdSet.stream().anyMatch(systemRoleIdSet::contains);
        boolean hasSystemRoleAfterSave = targetRoleIdList.stream().anyMatch(systemRoleIdSet::contains);
        if (!hadSystemRole || hasSystemRoleAfterSave) {
            return;
        }
        if (targetUserId != null && targetUserId.equals(dto.getUserId())) {
            throw new BizException("不能删除自己的超级管理员角色");
        }
        List<Employee> systemRoleEmployeeList = employeeRepository.listByRoleIds(dto.getCorpid(), new ArrayList<>(systemRoleIdSet));
        boolean hasOtherSystemEmployee = systemRoleEmployeeList.stream()
            .filter(item -> !item.getUserId().equals(existedEmployee.getUserId()))
            .map(Employee::getUserId)
            .distinct()
            .findAny()
            .isPresent();
        if (!hasOtherSystemEmployee) {
            throw new BizException("一个公司至少需要保留一个超级管理员");
        }
    }

    private Map<Long, String> buildDepartmentNameMap(String corpid, List<Employee> employeeList) {
        List<Long> mainDepartmentIdList = employeeList.stream()
            .map(Employee::getMainDepartmentId)
            .filter(item -> item != null)
            .distinct()
            .toList();
        return departmentRepository.listByIds(corpid, mainDepartmentIdList).stream()
            .collect(Collectors.toMap(Department::getId, Department::getDepartmentName));
    }

    private Map<String, List<Role>> buildEmployeeRoleMap(String corpid, List<Employee> employeeList) {
        if (employeeList == null || employeeList.isEmpty()) {
            return Map.of();
        }
        List<Long> employeeIdList = employeeList.stream()
            .map(Employee::getId)
            .filter(item -> item != null)
            .distinct()
            .toList();
        if (employeeIdList.isEmpty()) {
            return Map.of();
        }
        Map<Long, String> employeeUserIdMap = employeeList.stream()
            .filter(item -> item.getId() != null && item.getUserId() != null)
            .collect(Collectors.toMap(Employee::getId, Employee::getUserId, (left, right) -> left, LinkedHashMap::new));
        List<EmployeeRoleRelation> relationList = employeeRepository.listRoleRelationsByUserIds(corpid, employeeIdList);
        if (relationList.isEmpty()) {
            return Map.of();
        }
        List<Long> roleIdList = relationList.stream().map(EmployeeRoleRelation::getRoleId).distinct().toList();
        Map<Long, Role> roleMap = roleRepository.listByIds(corpid, roleIdList).stream()
            .collect(Collectors.toMap(Role::getId, item -> item));
        Map<String, List<Role>> result = new LinkedHashMap<>();
        for (EmployeeRoleRelation relation : relationList) {
            String userId = employeeUserIdMap.get(relation.getUserId());
            Role role = roleMap.get(relation.getRoleId());
            if (userId == null || role == null) {
                continue;
            }
            result.computeIfAbsent(userId, key -> new ArrayList<>()).add(role);
        }
        return result;
    }

    private Map<String, List<Long>> buildRoleIdMap(Map<String, List<Role>> employeeRoleMap) {
        Map<String, List<Long>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Role>> entry : employeeRoleMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().stream().map(Role::getId).toList());
        }
        return result;
    }

    private Map<String, List<String>> buildRoleNameMap(Map<String, List<Role>> employeeRoleMap) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Role>> entry : employeeRoleMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().stream().map(Role::getRoleName).toList());
        }
        return result;
    }

    private List<EmployeeDepartmentRelation> buildDepartmentRelations(EmployeeSavePojo dto, Long userId, List<Long> departmentIdList) {
        List<EmployeeDepartmentRelation> relations = new ArrayList<>();
        for (Long departmentId : departmentIdList) {
            Integer mainFlag = departmentId.equals(dto.getMainDepartmentId()) ? 1 : 0;
            relations.add(OrgAdminAssembler.toEmployeeDepartmentRelation(dto.getCorpid(), userId, departmentId, mainFlag));
        }
        return relations;
    }

    private List<EmployeeRoleRelation> buildRoleRelations(EmployeeSavePojo dto, Long userId, List<Long> roleIdList) {
        List<EmployeeRoleRelation> relations = new ArrayList<>();
        for (Long roleId : roleIdList) {
            relations.add(OrgAdminAssembler.toEmployeeRoleRelation(dto.getCorpid(), userId, roleId));
        }
        return relations;
    }

    private List<Long> normalizedDepartmentIds(EmployeeSavePojo dto) {
        LinkedHashSet<Long> idSet = new LinkedHashSet<>();
        idSet.add(dto.getMainDepartmentId());
        if (dto.getDepartmentIdList() != null) {
            idSet.addAll(dto.getDepartmentIdList());
        }
        return new ArrayList<>(idSet);
    }

    private List<Long> normalizedRoleIds(EmployeeSavePojo dto) {
        if (dto.getRoleIdList() == null) {
            return List.of();
        }
        return new ArrayList<>(new LinkedHashSet<>(dto.getRoleIdList()));
    }

    private String resolveTargetUserId(EmployeeSavePojo dto) {
        if (dto.getId() != null && !dto.getId().isBlank()) {
            return dto.getId();
        }
        return dto.getUserId();
    }

    private void mergeExistingEmployeeData(Employee employee, Employee existedEmployee) {
        employee.setUserId(existedEmployee.getUserId());
        if (employee.getAccountId() == null) {
            employee.setAccountId(existedEmployee.getAccountId());
        }
        if (employee.getUserCode() == null || employee.getUserCode().isBlank()) {
            employee.setUserCode(existedEmployee.getUserCode());
        }
        if (employee.getUserName() == null || employee.getUserName().isBlank()) {
            employee.setUserName(existedEmployee.getUserName());
        }
        if (employee.getEmail() == null || employee.getEmail().isBlank()) {
            employee.setEmail(existedEmployee.getEmail());
        }
        if (employee.getJobNo() == null || employee.getJobNo().isBlank()) {
            employee.setJobNo(existedEmployee.getJobNo());
        }
        if (employee.getMainDepartmentId() == null) {
            employee.setMainDepartmentId(existedEmployee.getMainDepartmentId());
        }
        if (employee.getUserStatus() == null) {
            employee.setUserStatus(existedEmployee.getUserStatus());
        }
        if (employee.getEmploymentStatus() == null || employee.getEmploymentStatus().isBlank()) {
            employee.setEmploymentStatus(existedEmployee.getEmploymentStatus());
        }
        if (employee.getEntryTime() == null) {
            employee.setEntryTime(existedEmployee.getEntryTime());
        }
        if (employee.getResignedTime() == null) {
            employee.setResignedTime(existedEmployee.getResignedTime());
        }
        if (employee.getRemark() == null || employee.getRemark().isBlank()) {
            employee.setRemark(existedEmployee.getRemark());
        }
    }
}
