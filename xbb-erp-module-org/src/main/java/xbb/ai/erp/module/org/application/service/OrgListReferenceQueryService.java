package xbb.ai.erp.module.org.application.service;

import java.util.Map;
import java.util.Set;

public interface OrgListReferenceQueryService {
  Map<String, String> findActiveMemberNames(String corpid, Set<String> userIds);

  Map<String, String> findEnabledDepartmentNames(String corpid, Set<String> departmentIds);
}
