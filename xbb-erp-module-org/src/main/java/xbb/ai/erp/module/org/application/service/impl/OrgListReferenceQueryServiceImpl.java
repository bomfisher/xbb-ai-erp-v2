package xbb.ai.erp.module.org.application.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.org.application.service.OrgListReferenceQueryService;
import xbb.ai.erp.module.org.domain.enums.EnableStatusEnum;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import xbb.ai.erp.module.org.infrastructure.persistence.repository.EmployeeRepositoryImpl;

@Service
public class OrgListReferenceQueryServiceImpl implements OrgListReferenceQueryService {
  private final EmployeeRepositoryImpl employeeRepository;
  private final DepartmentRepositoryImpl departmentRepository;

  public OrgListReferenceQueryServiceImpl(
      EmployeeRepositoryImpl employeeRepository, DepartmentRepositoryImpl departmentRepository) {
    this.employeeRepository = employeeRepository;
    this.departmentRepository = departmentRepository;
  }

  @Override
  public Map<String, String> findActiveMemberNames(String corpid, Set<String> userIds) {
    if (userIds == null || userIds.isEmpty()) {
      return Map.of();
    }
    Map<String, String> result = new LinkedHashMap<>();
    employeeRepository.listActiveByUserIds(corpid, List.copyOf(userIds)).forEach(employee ->
        result.put(employee.getUserId(), employee.getUserName()));
    return result;
  }

  @Override
  public Map<String, String> findEnabledDepartmentNames(String corpid, Set<String> departmentIds) {
    if (departmentIds == null || departmentIds.isEmpty()) {
      return Map.of();
    }
    List<Long> ids = departmentIds.stream().flatMap(this::toLongStream).toList();
    if (ids.isEmpty()) {
      return Map.of();
    }
    Map<String, String> result = new LinkedHashMap<>();
    departmentRepository.listByIds(corpid, ids).stream()
        .filter(item -> EnableStatusEnum.ENABLE.getCode().equals(item.getDepartmentStatus()))
        .forEach(item -> result.put(String.valueOf(item.getId()), item.getDepartmentName()));
    return result;
  }

  private java.util.stream.Stream<Long> toLongStream(String value) {
    try {
      return java.util.stream.Stream.of(Long.valueOf(value));
    } catch (NumberFormatException exception) {
      return java.util.stream.Stream.empty();
    }
  }
}
