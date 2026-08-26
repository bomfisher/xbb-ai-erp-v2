package xbb.ai.erp.module.settlement.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.settlement.admin.dto.PaymentMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentInfoDTO;
import xbb.ai.erp.module.settlement.admin.dto.PaymentSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PaymentSaveItemVO;
import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.domain.model.PaymentDetail;
import java.util.List;

public final class PaymentAdminAssembler {

    private PaymentAdminAssembler() {
    }

    public static PaymentSaveItemVO buildEmptySaveItemVO() {
        PaymentSaveItemVO vo = new PaymentSaveItemVO();
        vo.setMain(new PaymentMainDTO());
        vo.setPaymentInfos(java.util.List.of());
        return vo;
    }

    public static Payment toPayment(PaymentSaveDTO dto) {
        Payment payment = new Payment();
        PaymentMainDTO main = dto.getMain();
        if (main != null) {
            payment.setId(main.getId());
            payment.setCorpid(main.getCorpid());
            payment.setPaymentNo(main.getPaymentNo());
            payment.setSupplierId(main.getSupplierId());
            payment.setPaymentDate(main.getPaymentDate());
            payment.setAmount(main.getAmount());
            payment.setWrittenOffAmount(main.getWrittenOffAmount());
            payment.setRemainingAmount(main.getRemainingAmount());
            payment.setPaymentType(main.getPaymentType());
            payment.setPaymentMethod(main.getPaymentMethod());
            payment.setBankAccountId(main.getBankAccountId());
            payment.setBankTransactionNo(main.getBankTransactionNo());
            payment.setStatus(main.getStatus());
            payment.setRemark(main.getRemark());
            payment.setCreatorId(main.getCreatorId());
            payment.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                payment.setCreatorId(dto.getUserId());
            }
            payment.setModifyId(dto.getUserId());
        }
        payment.setCorpid(dto.getCorpid());
        payment.setDetails(toPaymentDetails(dto));
        return payment;
    }

    public static PaymentListItemVO toListItemVO(Payment payment) {
        PaymentListItemVO vo = new PaymentListItemVO();
        vo.setId(Objects.isNull(payment.getId()) ? "" : Objects.toString(payment.getId()));
        vo.setPaymentNo(payment.getPaymentNo());
        vo.setSupplierId(Objects.isNull(payment.getSupplierId()) ? "" : Objects.toString(payment.getSupplierId()));
        vo.setPaymentDate(Objects.isNull(payment.getPaymentDate()) ? "" : Objects.toString(payment.getPaymentDate()));
        vo.setAmount(Objects.isNull(payment.getAmount()) ? "" : Objects.toString(payment.getAmount()));
        vo.setWrittenOffAmount(Objects.isNull(payment.getWrittenOffAmount()) ? "" : Objects.toString(payment.getWrittenOffAmount()));
        vo.setRemainingAmount(Objects.isNull(payment.getRemainingAmount()) ? "" : Objects.toString(payment.getRemainingAmount()));
        vo.setPaymentType(payment.getPaymentType());
        vo.setPaymentMethod(payment.getPaymentMethod());
        vo.setStatus(Objects.isNull(payment.getStatus()) ? "" : Objects.toString(payment.getStatus()));
        vo.setRemark(payment.getRemark());
        return vo;
    }

    public static PaymentSaveItemVO toSaveItemVO(Payment payment, List<PaymentDetail> details) {
        PaymentSaveItemVO vo = new PaymentSaveItemVO();
        if (payment == null) {
            return vo;
        }
        PaymentMainDTO main = new PaymentMainDTO();
        main.setId(payment.getId());
        main.setCorpid(payment.getCorpid());
        main.setPaymentNo(payment.getPaymentNo());
        main.setSupplierId(payment.getSupplierId());
        main.setPaymentDate(payment.getPaymentDate());
        main.setAmount(payment.getAmount());
        main.setWrittenOffAmount(payment.getWrittenOffAmount());
        main.setRemainingAmount(payment.getRemainingAmount());
        main.setPaymentType(payment.getPaymentType());
        main.setPaymentMethod(payment.getPaymentMethod());
        main.setBankAccountId(payment.getBankAccountId());
        main.setBankTransactionNo(payment.getBankTransactionNo());
        main.setStatus(payment.getStatus());
        main.setRemark(payment.getRemark());
        main.setCreatorId(payment.getCreatorId());
        main.setModifyId(payment.getModifyId());
        vo.setMain(main);
        vo.setPaymentInfos(details == null ? List.of() : details.stream().map(PaymentAdminAssembler::toPaymentInfoDTO).toList());
        return vo;
    }

    public static PaymentSaveItemVO toSaveItemVO(Payment payment) {
        return toSaveItemVO(payment, List.of());
    }

    public static PaymentDetailVO toDetailVO(PaymentSaveItemVO saveItemVO) {
        PaymentDetailVO detailVO = new PaymentDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    private static List<PaymentDetail> toPaymentDetails(PaymentSaveDTO dto) {
        if (dto.getPaymentInfos() == null) {
            return List.of();
        }
        return dto.getPaymentInfos().stream().map(info -> {
            PaymentDetail detail = new PaymentDetail();
            detail.setId(info.getId());
            detail.setCorpid(dto.getCorpid());
            detail.setBankAccountId(info.getBankAccountId());
            detail.setPaymentMethod(info.getPaymentMethod());
            detail.setAmount(info.getAmount());
            detail.setTransactionNo(info.getTransactionNo());
            detail.setRemark(info.getRemark());
            detail.setCreatorId(dto.getUserId());
            detail.setModifyId(dto.getUserId());
            return detail;
        }).toList();
    }

    private static PaymentInfoDTO toPaymentInfoDTO(PaymentDetail detail) {
        PaymentInfoDTO dto = new PaymentInfoDTO();
        dto.setId(detail.getId());
        dto.setBankAccountId(detail.getBankAccountId());
        dto.setPaymentMethod(detail.getPaymentMethod());
        dto.setAmount(detail.getAmount());
        dto.setTransactionNo(detail.getTransactionNo());
        dto.setRemark(detail.getRemark());
        return dto;
    }
}
