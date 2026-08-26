package xbb.ai.erp.module.settlement.application.assembler;

import java.util.List;
import java.util.Objects;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.model.ReceiptPayment;

public final class ReceiptAdminAssembler {

    private ReceiptAdminAssembler() {
    }

    public static ReceiptSaveItemVO buildEmptySaveItemVO() {
        return new ReceiptSaveItemVO();
    }

    public static Receipt toReceipt(ReceiptSaveDTO dto) {
        Receipt receipt = new Receipt();
        ReceiptMainDTO main = dto.getMain();
        if (main != null) {
            receipt.setId(main.getId());
            receipt.setCorpid(main.getCorpid());
            receipt.setReceiptNo(main.getReceiptNo());
            receipt.setCustomerId(main.getCustomerId());
            receipt.setReceiptDate(main.getReceiptDate());
            receipt.setAmount(main.getAmount());
            receipt.setWrittenOffAmount(main.getWrittenOffAmount());
            receipt.setRemainingAmount(main.getRemainingAmount());
            receipt.setReceiptType(main.getReceiptType());
            receipt.setPaymentMethod(main.getPaymentMethod());
            receipt.setBankAccountId(main.getBankAccountId());
            receipt.setBankTransactionNo(main.getBankTransactionNo());
            receipt.setStatus(main.getStatus());
            receipt.setRemark(main.getRemark());
            receipt.setCreatorId(main.getCreatorId());
            receipt.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                receipt.setCreatorId(dto.getUserId());
            }
            receipt.setModifyId(dto.getUserId());
        }
        receipt.setCorpid(dto.getCorpid());
        receipt.setPayments(toReceiptPayments(dto));
        return receipt;
    }

    public static ReceiptListItemVO toListItemVO(Receipt receipt) {
        ReceiptListItemVO vo = new ReceiptListItemVO();
        vo.setId(Objects.isNull(receipt.getId()) ? "" : Objects.toString(receipt.getId()));
        vo.setReceiptNo(receipt.getReceiptNo());
        vo.setCustomerId(Objects.isNull(receipt.getCustomerId()) ? "" : Objects.toString(receipt.getCustomerId()));
        vo.setReceiptDate(Objects.isNull(receipt.getReceiptDate()) ? "" : java.time.Instant.ofEpochMilli(receipt.getReceiptDate()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setAmount(Objects.isNull(receipt.getAmount()) ? "" : Objects.toString(receipt.getAmount()));
        vo.setWrittenOffAmount(Objects.isNull(receipt.getWrittenOffAmount()) ? "" : Objects.toString(receipt.getWrittenOffAmount()));
        vo.setRemainingAmount(Objects.isNull(receipt.getRemainingAmount()) ? "" : Objects.toString(receipt.getRemainingAmount()));
        vo.setReceiptType(Objects.isNull(receipt.getReceiptType()) ? "" : Objects.toString(receipt.getReceiptType()));
        vo.setPaymentMethod(Objects.isNull(receipt.getPaymentMethod()) ? "" : Objects.toString(receipt.getPaymentMethod()));
        vo.setBankAccountId(Objects.isNull(receipt.getBankAccountId()) ? "" : Objects.toString(receipt.getBankAccountId()));
        vo.setBankTransactionNo(receipt.getBankTransactionNo());
        vo.setStatus(Objects.isNull(receipt.getStatus()) ? "" : Objects.toString(receipt.getStatus()));
        vo.setRemark(receipt.getRemark());
        vo.setCreatorId(receipt.getCreatorId());
        vo.setModifyId(receipt.getModifyId());
        return vo;
    }

    public static ReceiptSaveItemVO toSaveItemVO(Receipt receipt) {
        ReceiptSaveItemVO vo = new ReceiptSaveItemVO();
        if (receipt == null) {
            return vo;
        }
        ReceiptMainDTO main = new ReceiptMainDTO();
        main.setId(receipt.getId());
        main.setCorpid(receipt.getCorpid());
        main.setReceiptNo(receipt.getReceiptNo());
        main.setCustomerId(receipt.getCustomerId());
        main.setReceiptDate(receipt.getReceiptDate());
        main.setAmount(receipt.getAmount());
        main.setWrittenOffAmount(receipt.getWrittenOffAmount());
        main.setRemainingAmount(receipt.getRemainingAmount());
        main.setReceiptType(receipt.getReceiptType());
        main.setPaymentMethod(receipt.getPaymentMethod());
        main.setBankAccountId(receipt.getBankAccountId());
        main.setBankTransactionNo(receipt.getBankTransactionNo());
        main.setStatus(receipt.getStatus());
        main.setRemark(receipt.getRemark());
        main.setCreatorId(receipt.getCreatorId());
        main.setModifyId(receipt.getModifyId());
        vo.setMain(main);
        vo.setPayments(receipt.getPayments() == null ? List.of() : receipt.getPayments().stream()
            .map(ReceiptAdminAssembler::toPaymentDTO)
            .toList());
        vo.setWriteOffs(List.of());
        return vo;
    }

    public static List<ReceiptWriteOffItemDTO> toWriteOffItemDTOs(List<xbb.ai.erp.module.settlement.domain.model.ReceiptWriteOff> writeOffs) {
        return writeOffs.stream().map(writeOff -> {
            ReceiptWriteOffItemDTO item = new ReceiptWriteOffItemDTO();
            item.setReceivableId(writeOff.getReceivableId());
            item.setAmount(writeOff.getAmount());
            return item;
        }).toList();
    }

    public static ReceiptDetailVO toDetailVO(ReceiptSaveItemVO saveItemVO) {
        ReceiptDetailVO detailVO = new ReceiptDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    private static List<ReceiptPayment> toReceiptPayments(ReceiptSaveDTO dto) {
        if (dto.getPayments() == null) {
            return List.of();
        }
        return dto.getPayments().stream().map(paymentDTO -> {
            ReceiptPayment payment = new ReceiptPayment();
            payment.setId(paymentDTO.getId());
            payment.setBankAccountId(paymentDTO.getBankAccountId());
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
            payment.setAmount(paymentDTO.getAmount());
            payment.setHandlingFee(paymentDTO.getHandlingFee());
            payment.setTransactionNo(paymentDTO.getTransactionNo());
            payment.setRemark(paymentDTO.getRemark());
            payment.setCorpid(dto.getCorpid());
            payment.setCreatorId(dto.getUserId());
            payment.setModifyId(dto.getUserId());
            return payment;
        }).toList();
    }

    private static ReceiptPaymentDTO toPaymentDTO(ReceiptPayment payment) {
        ReceiptPaymentDTO dto = new ReceiptPaymentDTO();
        dto.setId(payment.getId());
        dto.setBankAccountId(payment.getBankAccountId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setAmount(payment.getAmount());
        dto.setHandlingFee(payment.getHandlingFee());
        dto.setTransactionNo(payment.getTransactionNo());
        dto.setRemark(payment.getRemark());
        return dto;
    }
}
