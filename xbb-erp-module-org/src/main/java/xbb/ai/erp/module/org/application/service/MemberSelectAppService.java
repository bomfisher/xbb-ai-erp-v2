package xbb.ai.erp.module.org.application.service;

import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.org.admin.dto.MemberSelectQueryDTO;
import xbb.ai.erp.module.org.admin.vo.MemberSelectOptionVO;

import java.util.List;

public interface MemberSelectAppService {

    List<MemberSelectOptionVO> quickSearch(MemberSelectQueryDTO dto);

    ListBaseVO<MemberSelectOptionVO> dialogSearch(MemberSelectQueryDTO dto);

    MemberSelectOptionVO getById(MemberSelectQueryDTO dto);
}
