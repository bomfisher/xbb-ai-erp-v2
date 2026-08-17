package xbb.ai.erp.module.system.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.admin.dto.BizNoNextDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BizNoNextVO;
import xbb.ai.erp.module.system.admin.vo.BizNoRuleVO;
import xbb.ai.erp.module.system.application.service.BizNoService;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;

@Service
@RequiredArgsConstructor
public class BizNoServiceImpl implements BizNoService {

    private final BizNoRuleRepository ruleRepository;
    private final BizNoGenerator bizNoGenerator;

    @Override
    public BizNoRuleVO getRule(String corpid, String businessCode) {
        BizNoRule rule = ruleRepository.findRequired(corpid, businessCode);
        BizNoRuleVO vo = new BizNoRuleVO();
        vo.setBusinessCode(rule.businessCode());
        vo.setPrefix(rule.prefix());
        vo.setRuleType(rule.ruleType().name());
        return vo;
    }

    @Override
    public void saveRule(BizNoRuleSaveDTO dto) {
        if (dto.getBusinessCode() == null || dto.getBusinessCode().isBlank() || dto.getPrefix() == null || dto.getPrefix().isBlank()) {
            throw new BizException("业务编码和业务前缀不能为空");
        }
        BizNoRuleTypeEnum type;
        try {
            type = BizNoRuleTypeEnum.valueOf(dto.getRuleType());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new BizException("编号规则类型无效");
        }
        ruleRepository.save(new BizNoRule(dto.getCorpid(), dto.getBusinessCode(), dto.getPrefix(), type));
    }

    @Override
    public BizNoNextVO next(BizNoNextDTO dto) {
        BizNoNextVO vo = new BizNoNextVO();
        vo.setCode(bizNoGenerator.next(dto.getCorpid(), dto.getBusinessCode()));
        return vo;
    }
}
