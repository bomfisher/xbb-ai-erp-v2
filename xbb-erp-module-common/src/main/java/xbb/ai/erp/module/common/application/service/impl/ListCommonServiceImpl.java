package xbb.ai.erp.module.common.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.admin.vo.ListBottomButtonVO;
import xbb.ai.erp.module.common.admin.vo.ListFilterVO;
import xbb.ai.erp.module.common.admin.vo.ListTopButtonVO;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;
import xbb.ai.erp.module.common.application.service.ListCommonService;

@Service
@RequiredArgsConstructor
public class ListCommonServiceImpl implements ListCommonService {

    private final ListMetaRegistry listMetaRegistry;

    @Override
    public ListFilterVO filter(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListFilterVO vo = new ListFilterVO();
        vo.setList(provider.buildFilterMeta(dto));
        return vo;
    }

    @Override
    public ListTopButtonVO topButton(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListMetaBundlePojo bundle = provider.buildTopButtonMeta(dto);
        ListTopButtonVO vo = new ListTopButtonVO();
        vo.setList(bundle.getTopButtonList());
        return vo;
    }

    @Override
    public ListBottomButtonVO bottomButton(ListCommonQueryDTO dto) {
        ListMetaProvider provider = listMetaRegistry.getRequiredProvider(dto.getBusinessCode());
        ListMetaBundlePojo bundle = provider.buildBottomButtonMeta(dto);
        ListBottomButtonVO vo = new ListBottomButtonVO();
        vo.setList(bundle.getBottomButtonList());
        return vo;
    }
}
