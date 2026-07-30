package xbb.ai.erp.module.common.application.service;

import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
import xbb.ai.erp.module.common.admin.vo.ListHeaderVO;
import xbb.ai.erp.module.common.admin.vo.ListRowActionVO;
import xbb.ai.erp.module.common.admin.vo.ListSchemaVO;
import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;

public interface ListCommonService {

    ListFilterVO filter(ListCommonQueryDTO dto);

    ListHeaderVO header(ListCommonQueryDTO dto);

    ListTopButtonVO topButton(ListCommonQueryDTO dto);

    ListBottomButtonVO bottomButton(ListCommonQueryDTO dto);

    ListRowActionVO rowAction(ListCommonQueryDTO dto);

    ListSchemaVO schema(ListCommonQueryDTO dto);
}
