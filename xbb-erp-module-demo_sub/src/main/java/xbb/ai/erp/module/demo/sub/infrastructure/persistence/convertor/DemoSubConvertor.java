package xbb.ai.erp.module.demo.sub.infrastructure.persistence.convertor;

import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;
import xbb.ai.erp.module.demo.sub.infrastructure.persistence.po.DemoSubPO;

public final class DemoSubConvertor {

  private DemoSubConvertor() {}

  public static DemoSubPO toPO(DemoSub demoSub) {
    if (demoSub == null) {
      return null;
    }
    DemoSubPO po = new DemoSubPO();
    po.setId(demoSub.getId());
    po.setCorpid(demoSub.getCorpid());
    po.setDataId(demoSub.getDataId());
    po.setName(demoSub.getName());
    po.setUserId(demoSub.getUserId());
    po.setDepartmentId(demoSub.getDepartmentId());
    po.setDeleted(demoSub.getDeleted());
    po.setAddTime(demoSub.getAddTime());
    po.setUpdateTime(demoSub.getUpdateTime());
    po.setCreatorId(demoSub.getCreatorId());
    po.setModifyId(demoSub.getModifyId());
    return po;
  }

  public static DemoSub toDomain(DemoSubPO po) {
    if (po == null) {
      return null;
    }
    DemoSub demoSub = new DemoSub();
    demoSub.setId(po.getId());
    demoSub.setCorpid(po.getCorpid());
    demoSub.setDataId(po.getDataId());
    demoSub.setName(po.getName());
    demoSub.setUserId(po.getUserId());
    demoSub.setDepartmentId(po.getDepartmentId());
    demoSub.setDeleted(po.getDeleted());
    demoSub.setAddTime(po.getAddTime());
    demoSub.setUpdateTime(po.getUpdateTime());
    demoSub.setCreatorId(po.getCreatorId());
    demoSub.setModifyId(po.getModifyId());
    return demoSub;
  }
}
