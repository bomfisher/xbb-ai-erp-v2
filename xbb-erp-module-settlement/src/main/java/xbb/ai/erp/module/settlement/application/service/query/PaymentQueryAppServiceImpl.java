package xbb.ai.erp.module.settlement.application.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.settlement.admin.PaymentTypeEnum;
import xbb.ai.erp.module.settlement.admin.PaymentFieldEnum;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentSaveItemVO;
import xbb.ai.erp.module.settlement.application.assembler.PaymentAdminAssembler;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.domain.model.PaymentDetail;
import xbb.ai.erp.module.settlement.domain.repository.PaymentRepository;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.settlement.application.field.PaymentFormSectionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentQueryAppServiceImpl {

    private final PaymentRepository paymentRepository;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;

    public PaymentQueryAppServiceImpl(PaymentRepository paymentRepository, ListValueRenderer listValueRenderer,
        BizNoGenerator bizNoGenerator) {
        this.paymentRepository = paymentRepository;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
    }

    public ListBaseVO<PaymentListItemVO> list(ListBaseDTO dto) {
        return listByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT, BusinessCodeEnum.PAYMENT);
    }

    public ListBaseVO<PaymentListItemVO> listAdvancePayment(ListBaseDTO dto) {
        return listByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT, BusinessCodeEnum.ADVANCE_PAYMENT);
    }

    private ListBaseVO<PaymentListItemVO> listByPaymentType(ListBaseDTO dto, PaymentTypeEnum paymentType,
        BusinessCodeEnum businessCode) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("paymentType", paymentType.getCode());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("conditions", dto.getConditions());
        List<Payment> list = paymentRepository.findByCondition(conditionMap);
        Long total = paymentRepository.count(conditionMap);
        ListBaseVO<PaymentListItemVO> vo = new ListBaseVO<>();
        List<PaymentListItemVO> items = list.stream().map(PaymentAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), businessCode.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<PaymentSaveItemVO> addItem(BaseDTO dto) {
        return addItemByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT, BusinessCodeEnum.PAYMENT);
    }

    public SaveItemVO<PaymentSaveItemVO> addAdvancePayment(BaseDTO dto) {
        return addItemByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT, BusinessCodeEnum.ADVANCE_PAYMENT);
    }

    private SaveItemVO<PaymentSaveItemVO> addItemByPaymentType(BaseDTO dto, PaymentTypeEnum paymentType,
        BusinessCodeEnum businessCode) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<PaymentSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(java.util.Arrays.stream(PaymentFieldEnum.values())
            .filter(field -> field.supports(SceneTypeEnum.CREATE))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta()))
            .toList());
        vo.setFormSections(PaymentFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        PaymentSaveItemVO data = PaymentAdminAssembler.buildEmptySaveItemVO();
        data.getMain().setPaymentNo(bizNoGenerator.next(dto.getCorpid(), businessCode.getCode()));
        data.getMain().setPaymentType(paymentType.getCode());
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<PaymentSaveItemVO> updateItem(IdBaseDTO dto) {
        return updateItemByPaymentType(dto, PaymentTypeEnum.SUPPLIER_PAYMENT);
    }

    public SaveItemVO<PaymentSaveItemVO> updateAdvancePayment(IdBaseDTO dto) {
        return updateItemByPaymentType(dto, PaymentTypeEnum.ADVANCE_PAYMENT);
    }

    private SaveItemVO<PaymentSaveItemVO> updateItemByPaymentType(IdBaseDTO dto, PaymentTypeEnum paymentType) {
        AdminParamValidator.validateIdQuery(dto);
        Payment entity = paymentRepository.findById(dto.getCorpid(), dto.getId());
        if (entity == null || !paymentType.getCode().equals(entity.getPaymentType())) {
            throw new xbb.ai.erp.base.common.exception.BizException("付款单不存在或不属于当前业务");
        }
        SaveItemVO<PaymentSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(java.util.Arrays.stream(PaymentFieldEnum.values())
            .filter(field -> field.supports(SceneTypeEnum.UPDATE))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta()))
            .toList());
        vo.setFormSections(PaymentFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        List<PaymentDetail> details = paymentRepository.findDetailsByPaymentId(dto.getCorpid(), dto.getId());
        vo.setData(PaymentAdminAssembler.toSaveItemVO(entity, details));
        return vo;
    }

    public PaymentDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        Payment entity = paymentRepository.findById(dto.getCorpid(), dto.getId());
        return PaymentAdminAssembler.toDetailVO(PaymentAdminAssembler.toSaveItemVO(entity));
    }
}
