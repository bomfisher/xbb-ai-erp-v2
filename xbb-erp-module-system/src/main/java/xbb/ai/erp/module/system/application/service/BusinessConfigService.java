package xbb.ai.erp.module.system.application.service;

import xbb.ai.erp.module.system.admin.dto.BusinessConfigCategoryQueryDTO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigGlobalSaveDTO;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigCategoryVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigDetailVO;
import xbb.ai.erp.module.system.admin.vo.BusinessConfigDocumentVO;

import java.util.List;

public interface BusinessConfigService {

    List<BusinessConfigCategoryVO> catalog();

    BusinessConfigDetailVO detail(BusinessConfigCategoryQueryDTO dto);

    BusinessConfigDocumentVO save(BusinessConfigSaveDTO dto);

    BusinessConfigDetailVO saveGlobal(BusinessConfigGlobalSaveDTO dto);
}
