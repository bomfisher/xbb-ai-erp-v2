package xbb.ai.erp.module.settlement.application.service.query;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
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
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptListItemVO;
import xbb.ai.erp.module.settlement.application.assembler.ReceiptAdminAssembler;
import xbb.ai.erp.module.settlement.application.field.ReceiptFieldFactory;
import xbb.ai.erp.module.settlement.application.field.ReceiptFormSectionFactory;
import xbb.ai.erp.module.settlement.application.schema.ReceiptListSchemaProvider;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptWriteOffRepository;
import xbb.ai.erp.module.masterdata.contract.FundAccountReferenceQueryApi;

@Service
public class ReceiptQueryAppServiceImpl {
    private final ReceiptRepository receiptRepository;
    private final ReceiptFieldFactory fieldFactory;
    private final ReceiptListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;
    private final FundAccountReferenceQueryApi fundAccountReferenceQueryApi;
    private final ReceiptWriteOffRepository receiptWriteOffRepository;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public ReceiptQueryAppServiceImpl(ReceiptRepository receiptRepository, ReceiptFieldFactory fieldFactory,
            ReceiptListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator) {
        this(receiptRepository, fieldFactory, schemaProvider, listValueRenderer, bizNoGenerator, null);
    }

    public ReceiptQueryAppServiceImpl(ReceiptRepository receiptRepository, ReceiptFieldFactory fieldFactory,
            ReceiptListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator,
            FundAccountReferenceQueryApi fundAccountReferenceQueryApi) {
        this(receiptRepository, fieldFactory, schemaProvider, listValueRenderer, bizNoGenerator,
            fundAccountReferenceQueryApi, null);
    }

    @Autowired
    public ReceiptQueryAppServiceImpl(ReceiptRepository receiptRepository, ReceiptFieldFactory fieldFactory,
            ReceiptListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator,
            FundAccountReferenceQueryApi fundAccountReferenceQueryApi,
            ReceiptWriteOffRepository receiptWriteOffRepository) {
        this.receiptRepository = receiptRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
        this.fundAccountReferenceQueryApi = fundAccountReferenceQueryApi;
        this.receiptWriteOffRepository = receiptWriteOffRepository;
    }

    public ListBaseVO<ReceiptListItemVO> list(ListBaseDTO dto) {
        return listByReceiptType(dto, "CUSTOMER_PAYMENT", BusinessCodeEnum.RECEIPT);
    }

    public ListBaseVO<ReceiptListItemVO> listAdvanceReceipt(ListBaseDTO dto) {
        return listByReceiptType(dto, "ADVANCE_PAYMENT", BusinessCodeEnum.ADVANCE_RECEIPT);
    }

    private ListBaseVO<ReceiptListItemVO> listByReceiptType(ListBaseDTO dto, String receiptType, BusinessCodeEnum businessCode) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        conditionMap.put("receiptType", receiptType);
        List<Receipt> list = receiptRepository.findByCondition(conditionMap);
        Long total = receiptRepository.count(conditionMap);
        ListBaseVO<ReceiptListItemVO> vo = new ListBaseVO<>();
        List<ReceiptListItemVO> items = list.stream().map(ReceiptAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), businessCode.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> addItem(BaseDTO dto) {
        return addItemByReceiptType(dto, "CUSTOMER_PAYMENT", BusinessCodeEnum.RECEIPT);
    }

    public SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> addAdvanceReceipt(BaseDTO dto) {
        return addItemByReceiptType(dto, "ADVANCE_PAYMENT", BusinessCodeEnum.ADVANCE_RECEIPT);
    }

    private SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> addItemByReceiptType(BaseDTO dto,
            String receiptType, BusinessCodeEnum businessCode) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setFormSections(ReceiptFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO data = ReceiptAdminAssembler.buildEmptySaveItemVO();
        xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO main = new xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO();
        main.setReceiptNo(bizNoGenerator.next(dto.getCorpid(), businessCode.getCode()));
        main.setReceiptType(receiptType);
        data.setMain(main);
        data.setPayments(List.of(buildDefaultPayment(dto.getCorpid())));
        data.setWriteOffs(List.of());
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> updateItem(IdBaseDTO dto) {
        return updateItemByReceiptType(dto, "CUSTOMER_PAYMENT");
    }

    public SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> updateAdvanceReceipt(IdBaseDTO dto) {
        return updateItemByReceiptType(dto, "ADVANCE_PAYMENT");
    }

    private SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> updateItemByReceiptType(IdBaseDTO dto,
            String receiptType) {
        AdminParamValidator.validateIdQuery(dto);
        Receipt entity = receiptRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null || !receiptType.equals(entity.getReceiptType())) {
            throw new xbb.ai.erp.base.common.exception.BizException("收款单不存在或不属于当前业务");
        }
        SaveItemVO<xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setFormSections(ReceiptFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        entity.setPayments(receiptRepository.findPaymentsByReceiptId(dto.getCorpid(), entity.getId()));
        xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO data = ReceiptAdminAssembler.toSaveItemVO(entity);
        if (receiptWriteOffRepository != null) {
            data.setWriteOffs(ReceiptAdminAssembler.toWriteOffItemDTOs(
                receiptWriteOffRepository.findActiveByReceiptId(dto.getCorpid(), entity.getId())));
        }
        vo.setData(data);
        return vo;
    }

    public ReceiptDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Receipt entity = receiptRepository.findById(dto.getCorpid(), dto.getId());
        entity.setPayments(receiptRepository.findPaymentsByReceiptId(dto.getCorpid(), entity.getId()));
        return ReceiptAdminAssembler.toDetailVO(ReceiptAdminAssembler.toSaveItemVO(entity));
    }

    private xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO buildDefaultPayment(String corpid) {
        xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO payment = new xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO();
        if (fundAccountReferenceQueryApi == null) {
            return payment;
        }
        FundAccountReferenceQueryApi.FundAccountReference account = fundAccountReferenceQueryApi.findDefaultEnabledAccount(corpid);
        if (account != null) {
            payment.setBankAccountId(account.id());
            payment.setPaymentMethod(defaultPaymentMethod(account.accountType()));
        }
        return payment;
    }

    private String defaultPaymentMethod(String accountType) {
        if ("CASH".equals(accountType)) {
            return "CASH";
        }
        if ("BANK".equals(accountType)) {
            return "BANK_TRANSFER";
        }
        return null;
    }
}
