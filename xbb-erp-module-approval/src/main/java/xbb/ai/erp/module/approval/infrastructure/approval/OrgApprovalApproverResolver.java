package xbb.ai.erp.module.approval.infrastructure.approval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.approval.application.service.ApprovalApproverResolver;
import xbb.ai.erp.module.approval.domain.model.ApprovalApproverType;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeApprover;
import xbb.ai.erp.module.org.domain.enums.EmploymentStatusEnum;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.domain.model.Employee;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 使用当前组织数据计算审批人，并将结果交给实例任务持久化冻结。
 */
@Component
@RequiredArgsConstructor
public class OrgApprovalApproverResolver implements ApprovalApproverResolver {

    private final EmployeeRepositoryImpl employeeRepository;

    private final DepartmentRepositoryImpl departmentRepository;

    @Override
    public List<String> resolve(String corpid, String submitterId, ApprovalNodeApprover rule) {
        if (rule.getApproverType() == ApprovalApproverType.USER) {
            return employeeRepository.listActiveByUserIds(corpid, splitUserIds(rule.getApproverValue())).stream()
                .map(Employee::getUserId)
                .toList();
        }
        if (rule.getApproverType() == ApprovalApproverType.ROLE) {
            return employeeRepository.listByRoleIds(corpid, splitRoleIds(rule.getApproverValue())).stream()
                .filter(this::isActive)
                .map(Employee::getUserId)
                .distinct()
                .toList();
        }
        return resolveSuperiors(corpid, submitterId, rule.getSuperiorLevel());
    }

    private List<String> resolveSuperiors(String corpid, String submitterId, Integer superiorLevel) {
        Employee submitter = employeeRepository.findByUserId(corpid, submitterId);
        if (submitter == null || submitter.getMainDepartmentId() == null || superiorLevel == null || superiorLevel < 1) {
            return List.of();
        }
        List<String> supervisorIds = new ArrayList<>();
        Long departmentId = submitter.getMainDepartmentId();
        while (departmentId != null && supervisorIds.size() < superiorLevel) {
            Department department = departmentRepository.findById(corpid, departmentId);
            if (department == null) {
                break;
            }
            if (department.getLeaderUserId() != null) {
                String leaderUserId = String.valueOf(department.getLeaderUserId());
                Employee leader = employeeRepository.findByUserId(corpid, leaderUserId);
                if (leader != null && isActive(leader) && !leaderUserId.equals(submitterId)) {
                    supervisorIds.add(leaderUserId);
                }
            }
            departmentId = department.getParentId();
        }
        return supervisorIds.stream().distinct().toList();
    }

    private boolean isActive(Employee employee) {
        return Integer.valueOf(1).equals(employee.getUserStatus())
            && EmploymentStatusEnum.ACTIVE.getCode().equals(employee.getEmploymentStatus());
    }

    private List<String> splitUserIds(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(value.split(","))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .toList();
    }

    private List<Long> splitRoleIds(String value) {
        return splitUserIds(value).stream()
            .map(item -> {
                try {
                    return Long.valueOf(item);
                } catch (NumberFormatException exception) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();
    }
}
