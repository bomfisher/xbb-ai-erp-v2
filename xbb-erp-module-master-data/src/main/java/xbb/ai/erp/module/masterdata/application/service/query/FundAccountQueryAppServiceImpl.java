package xbb.ai.erp.module.masterdata.application.service.query;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountListItemVO;
import xbb.ai.erp.module.masterdata.application.assembler.FundAccountAdminAssembler;
import xbb.ai.erp.module.masterdata.application.field.FundAccountFieldFactory;
import xbb.ai.erp.module.masterdata.application.schema.FundAccountListSchemaProvider;
import xbb.ai.erp.module.masterdata.domain.model.FundAccount;
import xbb.ai.erp.module.masterdata.domain.repository.FundAccountRepository;

@Service
public class FundAccountQueryAppServiceImpl {
    private static final int ENABLED = 1;
    private static final int QUICK_SEARCH_LIMIT = 5;
    private static final String CASH_ACCOUNT_TYPE = "CASH";
    private static final String BANK_ACCOUNT_TYPE = "BANK";

    private final FundAccountRepository fundAccountRepository;
    private final FundAccountFieldFactory fieldFactory;
    private final FundAccountListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public FundAccountQueryAppServiceImpl(FundAccountRepository fundAccountRepository, FundAccountFieldFactory fieldFactory, FundAccountListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.fundAccountRepository = fundAccountRepository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider; this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<FundAccountListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<FundAccount> list = fundAccountRepository.findByCondition(conditionMap);
        Long total = fundAccountRepository.count(conditionMap);
        ListBaseVO<FundAccountListItemVO> vo = new ListBaseVO<>();
        List<FundAccountListItemVO> items = list.stream().map(FundAccountAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.FUND_ACCOUNT.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(FundAccountAdminAssembler.buildEmptySaveItemVO());
        vo.setData(FundAccountAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        FundAccount entity = fundAccountRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(FundAccountAdminAssembler.toSaveItemVO(entity));
        vo.setData(FundAccountAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public FundAccountDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        FundAccount entity = fundAccountRepository.findById(dto.getCorpid(), dto.getId());
        return FundAccountAdminAssembler.toDetailVO(FundAccountAdminAssembler.toSaveItemVO(entity));
    }

    public List<FundAccountBusinessSelectOptionVO> businessSelectQuickSearch(
        FundAccountBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, QUICK_SEARCH_LIMIT);
    }

    public ListBaseVO<FundAccountBusinessSelectOptionVO> businessSelectDialogSearch(
        FundAccountBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        List<FundAccountBusinessSelectOptionVO> options = findBusinessSelectOptions(dto, (pageNum - 1) * pageSize, pageSize);
        Long total = fundAccountRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<FundAccountBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public FundAccountBusinessSelectOptionVO businessSelectGetById(FundAccountBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        FundAccount fundAccount = fundAccountRepository.findById(dto.getCorpid(), dto.getId());
        if (fundAccount == null || !Integer.valueOf(ENABLED).equals(fundAccount.getEnabled())) {
            return null;
        }
        return toBusinessSelectOption(fundAccount);
    }

    private List<FundAccountBusinessSelectOptionVO> findBusinessSelectOptions(FundAccountBusinessSelectQueryDTO dto,
                                                                                 Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("enabled", ENABLED);
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return fundAccountRepository.findByCondition(conditions).stream()
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private static Map<String, Object> businessSelectConditions(FundAccountBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("enabled", ENABLED);
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
    }

    private FundAccountBusinessSelectOptionVO toBusinessSelectOption(FundAccount fundAccount) {
        FundAccountBusinessSelectOptionVO option = new FundAccountBusinessSelectOptionVO();
        option.setId(fundAccount.getId());
        option.setCode(fundAccount.getAccountCode());
        option.setName(fundAccount.getAccountName());
        option.setLabel(fundAccount.getAccountCode() == null || fundAccount.getAccountCode().isBlank()
            ? fundAccount.getAccountName()
            : fundAccount.getAccountCode() + " " + fundAccount.getAccountName());
        option.setLinePatch(paymentMethodPatch(fundAccount.getAccountType()));
        return option;
    }

    private Map<String, Object> paymentMethodPatch(String accountType) {
        if (CASH_ACCOUNT_TYPE.equals(accountType)) {
            return Map.of("paymentMethod", "CASH");
        }
        if (BANK_ACCOUNT_TYPE.equals(accountType)) {
            return Map.of("paymentMethod", "BANK_TRANSFER");
        }
        return Map.of();
    }
}
