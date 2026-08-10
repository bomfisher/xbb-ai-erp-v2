package xbb.ai.erp.module.org.application.render;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProvider;
import xbb.ai.erp.module.org.application.service.OrgListReferenceQueryService;

@Component
public class OrgListReferenceValueProvider implements ListReferenceValueProvider {
  private static final String MEMBER = "ORG_MEMBER";
  private static final String DEPARTMENT = "ORG_DEPARTMENT";

  private final OrgListReferenceQueryService referenceQueryService;

  public OrgListReferenceValueProvider(OrgListReferenceQueryService referenceQueryService) {
    this.referenceQueryService = referenceQueryService;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(String.valueOf(FieldTypeEnum.USER.getType()), MEMBER);
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return referenceQueryService.findActiveMemberNames(corpid, values);
  }

  @Component
  public static class DepartmentProvider implements ListReferenceValueProvider {
    private final OrgListReferenceQueryService referenceQueryService;

    public DepartmentProvider(OrgListReferenceQueryService referenceQueryService) {
      this.referenceQueryService = referenceQueryService;
    }

    @Override
    public ListReferenceKey key() {
      return new ListReferenceKey(String.valueOf(FieldTypeEnum.DEPT.getType()), DEPARTMENT);
    }

    @Override
    public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
      return referenceQueryService.findEnabledDepartmentNames(corpid, values);
    }
  }

}
