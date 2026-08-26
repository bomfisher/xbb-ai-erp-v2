package xbb.ai.erp.module.settlement.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.settlement.admin.dto.PayableMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableSaveItemVO;
import xbb.ai.erp.module.settlement.domain.model.Payable;

public final class PayableAdminAssembler {

    private PayableAdminAssembler() {
    }

    public static PayableSaveItemVO buildEmptySaveItemVO() {
        return new PayableSaveItemVO();
    }

    public static Payable toPayable(PayableSaveDTO dto) {
        Payable payable = new Payable();
        PayableMainDTO main = dto.getMain();
        if (main != null) {
            payable.setId(main.getId());
            payable.setCorpid(main.getCorpid());
            payable.setPayableNo(main.getPayableNo());
            payable.setSupplierId(main.getSupplierId());
            payable.setSourceType(main.getSourceType());
            payable.setSourceInvoiceId(main.getSourceInvoiceId());
            payable.setPayableDate(main.getPayableDate());
            payable.setDueDate(main.getDueDate());
            payable.setAmount(main.getAmount());
            payable.setWrittenOffAmount(main.getWrittenOffAmount());
            payable.setRemainingAmount(main.getRemainingAmount());
            payable.setStatus(main.getStatus());
            payable.setAuditStatus(main.getAuditStatus());
            payable.setRemark(main.getRemark());
            payable.setCreatorId(main.getCreatorId());
            payable.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                payable.setCreatorId(dto.getUserId());
            }
            payable.setModifyId(dto.getUserId());
        }
        payable.setCorpid(dto.getCorpid());
        return payable;
    }

    public static PayableListItemVO toListItemVO(Payable payable) {
        PayableListItemVO vo = new PayableListItemVO();
        vo.setId(Objects.isNull(payable.getId()) ? "" : Objects.toString(payable.getId()));
        vo.setPayableNo(payable.getPayableNo());
        vo.setSupplierId(Objects.isNull(payable.getSupplierId()) ? "" : Objects.toString(payable.getSupplierId()));
        vo.setSourceType(payable.getSourceType());
        vo.setPayableDate(Objects.isNull(payable.getPayableDate()) ? "" : Objects.toString(payable.getPayableDate()));
        vo.setDueDate(Objects.isNull(payable.getDueDate()) ? "" : Objects.toString(payable.getDueDate()));
        vo.setAmount(Objects.isNull(payable.getAmount()) ? "" : Objects.toString(payable.getAmount()));
        vo.setWrittenOffAmount(Objects.isNull(payable.getWrittenOffAmount()) ? "" : Objects.toString(payable.getWrittenOffAmount()));
        vo.setRemainingAmount(Objects.isNull(payable.getRemainingAmount()) ? "" : Objects.toString(payable.getRemainingAmount()));
        vo.setStatus(Objects.isNull(payable.getStatus()) ? "" : Objects.toString(payable.getStatus()));
        vo.setRemark(payable.getRemark());
        return vo;
    }

    public static PayableSaveItemVO toSaveItemVO(Payable payable) {
        PayableSaveItemVO vo = new PayableSaveItemVO();
        if (payable == null) {
            return vo;
        }
        PayableMainDTO main = new PayableMainDTO();
        main.setId(payable.getId());
        main.setCorpid(payable.getCorpid());
        main.setPayableNo(payable.getPayableNo());
        main.setSupplierId(payable.getSupplierId());
        main.setSourceType(payable.getSourceType());
        main.setSourceInvoiceId(payable.getSourceInvoiceId());
        main.setPayableDate(payable.getPayableDate());
        main.setDueDate(payable.getDueDate());
        main.setAmount(payable.getAmount());
        main.setWrittenOffAmount(payable.getWrittenOffAmount());
        main.setRemainingAmount(payable.getRemainingAmount());
        main.setStatus(payable.getStatus());
        main.setAuditStatus(payable.getAuditStatus());
        main.setRemark(payable.getRemark());
        main.setCreatorId(payable.getCreatorId());
        main.setModifyId(payable.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PayableDetailVO toDetailVO(PayableSaveItemVO saveItemVO) {
        PayableDetailVO detailVO = new PayableDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
