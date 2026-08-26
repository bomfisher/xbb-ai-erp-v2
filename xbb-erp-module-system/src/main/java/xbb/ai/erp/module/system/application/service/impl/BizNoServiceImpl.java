package xbb.ai.erp.module.system.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.system.admin.dto.BizNoNextDTO;
import xbb.ai.erp.module.system.admin.dto.BizNoRuleSaveDTO;
import xbb.ai.erp.module.system.admin.vo.BizNoNextVO;
import xbb.ai.erp.module.system.admin.vo.BizNoBusinessTreeVO;
import xbb.ai.erp.module.system.admin.vo.BizNoRuleVO;
import xbb.ai.erp.module.system.application.service.BizNoService;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.model.BizNoSerialModeEnum;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BizNoServiceImpl implements BizNoService {

    private final BizNoRuleRepository ruleRepository;
    private final BizNoGenerator bizNoGenerator;

    @Override
    public List<BizNoBusinessTreeVO> businessTree(String corpid) {
        Map<BizNoRuleTypeEnum, List<BizNoRule>> ruleGroups = ruleRepository.findAvailable(corpid).stream()
            .collect(Collectors.groupingBy(BizNoRule::ruleType, LinkedHashMap::new, Collectors.toList()));
        return ruleGroups.entrySet().stream()
            .map(entry -> toTreeGroup(corpid, entry.getKey(), entry.getValue()))
            .toList();
    }

    @Override
    public BizNoRuleVO getRule(String corpid, String businessCode) {
        BizNoRule rule = ruleRepository.findRequired(corpid, businessCode);
        BizNoRuleVO vo = new BizNoRuleVO();
        vo.setBusinessCode(rule.businessCode());
        vo.setPrefix(rule.prefix());
        vo.setIncludeDate(rule.includeDate());
        vo.setSuffixLength(rule.suffixLength());
        vo.setSerialMode(rule.serialMode().name());
        vo.setRuleType(rule.ruleType().name());
        vo.setOverridden(rule.corpid().equals(corpid) ? 1 : 0);
        return vo;
    }

    @Override
    public void saveRule(BizNoRuleSaveDTO dto) {
        if (dto.getBusinessCode() == null || dto.getBusinessCode().isBlank() || dto.getPrefix() == null || dto.getPrefix().isBlank()) {
            throw new BizException("业务编码和业务前缀不能为空");
        }
        BizNoSerialModeEnum serialMode;
        try {
            serialMode = dto.getSerialMode() == null || dto.getSerialMode().isBlank()
                ? serialModeFromRuleType(dto.getRuleType())
                : BizNoSerialModeEnum.valueOf(dto.getSerialMode());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new BizException("编号自增方式无效");
        }
        Integer includeDate = dto.getIncludeDate() == null
            ? serialMode == BizNoSerialModeEnum.DAILY ? 1 : 0
            : dto.getIncludeDate();
        if (includeDate != 0 && includeDate != 1) {
            throw new BizException("是否包含时间编码仅支持 0 或 1");
        }
        int suffixLength = dto.getSuffixLength() == null ? 5 : dto.getSuffixLength();
        if (suffixLength < 1 || suffixLength > 18) {
            throw new BizException("编号后缀位数必须在 1 到 18 之间");
        }
        BizNoRuleTypeEnum ruleType = serialMode == BizNoSerialModeEnum.DAILY
            ? BizNoRuleTypeEnum.DOCUMENT
            : BizNoRuleTypeEnum.MASTER_DATA;
        ruleRepository.save(new BizNoRule(dto.getCorpid(), dto.getBusinessCode(), dto.getPrefix(), includeDate,
            suffixLength, serialMode, ruleType));
    }

    @Override
    public BizNoNextVO next(BizNoNextDTO dto) {
        BizNoNextVO vo = new BizNoNextVO();
        vo.setCode(bizNoGenerator.next(dto.getCorpid(), dto.getBusinessCode()));
        return vo;
    }

    private BizNoBusinessTreeVO toTreeItem(String corpid, BizNoRule rule) {
        BizNoBusinessTreeVO vo = new BizNoBusinessTreeVO();
        vo.setBusinessCode(rule.businessCode());
        vo.setBusinessName(BusinessCodeEnum.chineseNameOf(rule.businessCode()));
        vo.setOverridden(rule.corpid().equals(corpid) ? 1 : 0);
        vo.setChildren(List.of());
        return vo;
    }

    private BizNoBusinessTreeVO toTreeGroup(String corpid, BizNoRuleTypeEnum ruleType, List<BizNoRule> rules) {
        BizNoBusinessTreeVO vo = new BizNoBusinessTreeVO();
        vo.setBusinessCode(ruleType.name());
        vo.setBusinessName(ruleType == BizNoRuleTypeEnum.MASTER_DATA ? "基础资料" : "单据");
        vo.setChildren(rules.stream().map(rule -> toTreeItem(corpid, rule)).toList());
        return vo;
    }

    private BizNoSerialModeEnum serialModeFromRuleType(String ruleType) {
        BizNoRuleTypeEnum type = BizNoRuleTypeEnum.valueOf(ruleType);
        return type == BizNoRuleTypeEnum.DOCUMENT ? BizNoSerialModeEnum.DAILY : BizNoSerialModeEnum.CONTINUOUS;
    }
}
