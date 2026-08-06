package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.domain.model.EmployeeDepartmentRelation;
import xbb.ai.erp.module.org.domain.model.EmployeeRoleRelation;
import xbb.ai.erp.module.org.infrastructure.persistence.convertor.OrgConvertor;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.EmployeeDepartmentRelationMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.EmployeeMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.EmployeeRoleRelationMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeeDepartmentRelationPO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeePO;
import xbb.ai.erp.module.org.infrastructure.persistence.po.EmployeeRoleRelationPO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl {

    private final EmployeeMapper employeeMapper;
    private final EmployeeDepartmentRelationMapper employeeDepartmentRelationMapper;
    private final EmployeeRoleRelationMapper employeeRoleRelationMapper;

    public void insert(Employee employee) {
        EmployeePO po = OrgConvertor.toPO(employee);
        employeeMapper.insert(po);
        employee.setId(po.getId());
    }

    public void update(Employee employee) {
        Employee existed = findByUserId(employee.getCorpid(), employee.getUserId());
        if (existed == null) {
            return;
        }
        EmployeePO po = OrgConvertor.toPO(employee);
        po.setId(existed.getId());
        employeeMapper.updateById(po);
        employee.setId(existed.getId());
    }

    public List<Employee> list(
        String corpid,
        String keyword,
        String employmentStatus,
        Integer userStatus,
        Long departmentId
    ) {
        LambdaQueryWrapper<EmployeePO> queryWrapper = new LambdaQueryWrapper<EmployeePO>()
            .eq(EmployeePO::getCorpid, corpid)
            .eq(employmentStatus != null, EmployeePO::getEmploymentStatus, employmentStatus)
            .eq(userStatus != null, EmployeePO::getUserStatus, userStatus)
            .orderByAsc(EmployeePO::getId);
        if (keyword != null && !keyword.isBlank()) {
            queryWrapper.and(wrapper -> wrapper
                .like(EmployeePO::getUserId, keyword)
                .or()
                .like(EmployeePO::getUserCode, keyword)
                .or()
                .like(EmployeePO::getUserName, keyword)
                .or()
                .like(EmployeePO::getEmail, keyword));
        }
        if (departmentId != null) {
            List<Long> userIdList = employeeDepartmentRelationMapper.selectList(new LambdaQueryWrapper<EmployeeDepartmentRelationPO>()
                    .eq(EmployeeDepartmentRelationPO::getCorpid, corpid)
                    .eq(EmployeeDepartmentRelationPO::getDepartmentId, departmentId))
                .stream()
                .map(EmployeeDepartmentRelationPO::getUserId)
                .distinct()
                .toList();
            if (userIdList.isEmpty()) {
                return List.of();
            }
            queryWrapper.in(EmployeePO::getId, userIdList);
        }
        return employeeMapper.selectList(queryWrapper).stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public void replaceDepartmentRelations(String corpid, String userId, List<EmployeeDepartmentRelation> relations) {
        Employee employee = findByUserId(corpid, userId);
        if (employee == null) {
            return;
        }
        employeeDepartmentRelationMapper.delete(new LambdaQueryWrapper<EmployeeDepartmentRelationPO>()
            .eq(EmployeeDepartmentRelationPO::getCorpid, corpid)
            .eq(EmployeeDepartmentRelationPO::getUserId, employee.getId()));
        for (EmployeeDepartmentRelation relation : relations) {
            employeeDepartmentRelationMapper.insert(OrgConvertor.toPO(relation));
        }
    }

    public void replaceRoleRelations(String corpid, String userId, List<EmployeeRoleRelation> relations) {
        Employee employee = findByUserId(corpid, userId);
        if (employee == null) {
            return;
        }
        employeeRoleRelationMapper.delete(new LambdaQueryWrapper<EmployeeRoleRelationPO>()
            .eq(EmployeeRoleRelationPO::getCorpid, corpid)
            .eq(EmployeeRoleRelationPO::getUserId, employee.getId()));
        for (EmployeeRoleRelation relation : relations) {
            employeeRoleRelationMapper.insert(OrgConvertor.toPO(relation));
        }
    }

    public Employee findByUserId(String corpid, String userId) {
        return OrgConvertor.toDomain(employeeMapper.selectOne(new LambdaQueryWrapper<EmployeePO>()
            .eq(EmployeePO::getCorpid, corpid)
            .eq(EmployeePO::getUserId, userId)));
    }

    public List<EmployeeRoleRelation> listRoleRelationsByUserIds(String corpid, List<Long> userIdList) {
        if (corpid == null || corpid.isBlank() || userIdList == null || userIdList.isEmpty()) {
            return List.of();
        }
        return employeeRoleRelationMapper.selectList(new LambdaQueryWrapper<EmployeeRoleRelationPO>()
                .eq(EmployeeRoleRelationPO::getCorpid, corpid)
                .in(EmployeeRoleRelationPO::getUserId, userIdList))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public List<EmployeeRoleRelation> listRoleRelationsByUserId(String corpid, String userId) {
        Employee employee = findByUserId(corpid, userId);
        if (employee == null || employee.getId() == null) {
            return List.of();
        }
        return listRoleRelationsByUserIds(corpid, List.of(employee.getId()));
    }

    public List<EmployeeDepartmentRelation> listDepartmentRelationsByUserId(String corpid, String userId) {
        Employee employee = findByUserId(corpid, userId);
        if (employee == null || employee.getId() == null) {
            return List.of();
        }
        return employeeDepartmentRelationMapper.selectList(new LambdaQueryWrapper<EmployeeDepartmentRelationPO>()
                .eq(EmployeeDepartmentRelationPO::getCorpid, corpid)
                .eq(EmployeeDepartmentRelationPO::getUserId, employee.getId()))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public List<Employee> listByRoleIds(String corpid, List<Long> roleIdList) {
        if (corpid == null || corpid.isBlank() || roleIdList == null || roleIdList.isEmpty()) {
            return List.of();
        }
        List<Long> employeeIdList = employeeRoleRelationMapper.selectList(new LambdaQueryWrapper<EmployeeRoleRelationPO>()
                .eq(EmployeeRoleRelationPO::getCorpid, corpid)
                .in(EmployeeRoleRelationPO::getRoleId, roleIdList))
            .stream()
            .map(EmployeeRoleRelationPO::getUserId)
            .distinct()
            .toList();
        if (employeeIdList.isEmpty()) {
            return List.of();
        }
        return employeeMapper.selectList(new LambdaQueryWrapper<EmployeePO>()
                .eq(EmployeePO::getCorpid, corpid)
                .in(EmployeePO::getId, employeeIdList))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public long countActiveByMainDepartment(String corpid, Long departmentId) {
        if (corpid == null || corpid.isBlank() || departmentId == null) {
            return 0L;
        }
        return employeeMapper.selectCount(new LambdaQueryWrapper<EmployeePO>()
            .eq(EmployeePO::getCorpid, corpid)
            .eq(EmployeePO::getMainDepartmentId, departmentId)
            .eq(EmployeePO::getEmploymentStatus, EmploymentStatusEnum.ACTIVE.getCode()));
    }
}
