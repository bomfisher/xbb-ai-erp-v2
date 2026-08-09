package xbb.ai.erp.module.demo.sub.application.assembler;

import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubMainDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSaveDTO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubDetailVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubListItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubSaveItemVO;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;

public final class DemoSubAdminAssembler {

  private DemoSubAdminAssembler() {}

  public static DemoSubSaveItemVO buildEmptySaveItemVO() {
    return new DemoSubSaveItemVO();
  }

  public static DemoSub toDemoSub(DemoSubSaveDTO dto) {
    DemoSub demoSub = new DemoSub();
    DemoSubMainDTO main = dto.getMain();
    if (main != null) {
      demoSub.setId(main.getId());
      demoSub.setCorpid(main.getCorpid());
      demoSub.setDataId(main.getDataId());
      demoSub.setName(main.getName());
      demoSub.setUserId(main.getUserId());
      demoSub.setDepartmentId(main.getDepartmentId());
      demoSub.setDeleted(main.getDeleted());
      demoSub.setAddTime(main.getAddTime());
      demoSub.setUpdateTime(main.getUpdateTime());
      demoSub.setCreatorId(main.getCreatorId());
      demoSub.setModifyId(main.getModifyId());
    }
    demoSub.setCorpid(dto.getCorpid());
    return demoSub;
  }

  public static DemoSubListItemVO toListItemVO(DemoSub demoSub) {
    DemoSubListItemVO vo = new DemoSubListItemVO();
    vo.setDataId(demoSub.getDataId());
    vo.setName(demoSub.getName());
    vo.setUserId(demoSub.getUserId());
    vo.setDepartmentId(demoSub.getDepartmentId());
    vo.setCreatorId(demoSub.getCreatorId());
    vo.setModifyId(demoSub.getModifyId());
    return vo;
  }

  public static DemoSubSaveItemVO toSaveItemVO(DemoSub demoSub) {
    DemoSubSaveItemVO vo = new DemoSubSaveItemVO();
    if (demoSub == null) {
      return vo;
    }
    DemoSubMainDTO main = new DemoSubMainDTO();
    main.setId(demoSub.getId());
    main.setCorpid(demoSub.getCorpid());
    main.setDataId(demoSub.getDataId());
    main.setName(demoSub.getName());
    main.setUserId(demoSub.getUserId());
    main.setDepartmentId(demoSub.getDepartmentId());
    main.setDeleted(demoSub.getDeleted());
    main.setAddTime(demoSub.getAddTime());
    main.setUpdateTime(demoSub.getUpdateTime());
    main.setCreatorId(demoSub.getCreatorId());
    main.setModifyId(demoSub.getModifyId());
    vo.setMain(main);
    return vo;
  }

  public static DemoSubDetailVO toDetailVO(DemoSubSaveItemVO saveItemVO) {
    DemoSubDetailVO detailVO = new DemoSubDetailVO();
    detailVO.setMainData(saveItemVO);
    return detailVO;
  }
}
