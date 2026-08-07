package xbb.ai.erp.module.demo.application.service.draft;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftSaveVO;

import java.util.List;

@Service
public class DemoDraftAppServiceImpl implements DemoDraftAppService {

    @Override
    public DemoDraftSaveVO saveDraft(DemoDraftSaveDTO dto) {
        throw new BizException("草稿存储需要由业务模块实现");
    }

    @Override
    public List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto) {
        throw new BizException("草稿存储需要由业务模块实现");
    }

    @Override
    public DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto) {
        throw new BizException("草稿存储需要由业务模块实现");
    }
}
