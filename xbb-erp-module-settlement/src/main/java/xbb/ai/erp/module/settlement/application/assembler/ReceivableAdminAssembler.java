package xbb.ai.erp.module.settlement.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableSaveItemVO;
import xbb.ai.erp.module.settlement.domain.model.Receivable;

public final class ReceivableAdminAssembler {

    private ReceivableAdminAssembler() {
    }

    public static ReceivableSaveItemVO buildEmptySaveItemVO() {
        return new ReceivableSaveItemVO();
    }

    public static Receivable toReceivable(ReceivableSaveDTO dto) {
        Receivable receivable = new Receivable();
        ReceivableMainDTO main = dto.getMain();
        if (main != null) {
            receivable.setId(main.getId());
            receivable.setCorpid(main.getCorpid());
            receivable.setReceivableNo(main.getReceivableNo());
            receivable.setCustomerId(main.getCustomerId());
            receivable.setSourceType(main.getSourceType());
            receivable.setSourceInvoiceId(main.getSourceInvoiceId());
            receivable.setOpeningBatchId(main.getOpeningBatchId());
            receivable.setReceivableDate(main.getReceivableDate());
            receivable.setDueDate(main.getDueDate());
            receivable.setAmount(main.getAmount());
            receivable.setWrittenOffAmount(main.getWrittenOffAmount());
            receivable.setRemainingAmount(main.getRemainingAmount());
            receivable.setStatus(main.getStatus());
            receivable.setRemark(main.getRemark());
            receivable.setCreatorId(main.getCreatorId());
            receivable.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                receivable.setCreatorId(dto.getUserId());
            }
            receivable.setModifyId(dto.getUserId());
        }
        receivable.setCorpid(dto.getCorpid());
        return receivable;
    }

    public static ReceivableListItemVO toListItemVO(Receivable receivable) {
        ReceivableListItemVO vo = new ReceivableListItemVO();
        vo.setId(Objects.isNull(receivable.getId()) ? "" : Objects.toString(receivable.getId()));
        vo.setReceivableNo(receivable.getReceivableNo());
        vo.setCustomerId(Objects.isNull(receivable.getCustomerId()) ? "" : Objects.toString(receivable.getCustomerId()));
        vo.setSourceType(Objects.isNull(receivable.getSourceType()) ? "" : Objects.toString(receivable.getSourceType()));
        vo.setSourceInvoiceId(Objects.isNull(receivable.getSourceInvoiceId()) ? "" : Objects.toString(receivable.getSourceInvoiceId()));
        vo.setOpeningBatchId(Objects.isNull(receivable.getOpeningBatchId()) ? "" : Objects.toString(receivable.getOpeningBatchId()));
        vo.setReceivableDate(Objects.isNull(receivable.getReceivableDate()) ? "" : java.time.Instant.ofEpochMilli(receivable.getReceivableDate()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setDueDate(Objects.isNull(receivable.getDueDate()) ? "" : java.time.Instant.ofEpochMilli(receivable.getDueDate()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setAmount(Objects.isNull(receivable.getAmount()) ? "" : Objects.toString(receivable.getAmount()));
        vo.setWrittenOffAmount(Objects.isNull(receivable.getWrittenOffAmount()) ? "" : Objects.toString(receivable.getWrittenOffAmount()));
        vo.setRemainingAmount(Objects.isNull(receivable.getRemainingAmount()) ? "" : Objects.toString(receivable.getRemainingAmount()));
        vo.setStatus(Objects.isNull(receivable.getStatus()) ? "" : Objects.toString(receivable.getStatus()));
        vo.setRemark(receivable.getRemark());
        vo.setCreatorId(receivable.getCreatorId());
        vo.setModifyId(receivable.getModifyId());
        return vo;
    }

    public static ReceivableSaveItemVO toSaveItemVO(Receivable receivable) {
        ReceivableSaveItemVO vo = new ReceivableSaveItemVO();
        if (receivable == null) {
            return vo;
        }
        ReceivableMainDTO main = new ReceivableMainDTO();
        main.setId(receivable.getId());
        main.setCorpid(receivable.getCorpid());
        main.setReceivableNo(receivable.getReceivableNo());
        main.setCustomerId(receivable.getCustomerId());
        main.setSourceType(receivable.getSourceType());
        main.setSourceInvoiceId(receivable.getSourceInvoiceId());
        main.setOpeningBatchId(receivable.getOpeningBatchId());
        main.setReceivableDate(receivable.getReceivableDate());
        main.setDueDate(receivable.getDueDate());
        main.setAmount(receivable.getAmount());
        main.setWrittenOffAmount(receivable.getWrittenOffAmount());
        main.setRemainingAmount(receivable.getRemainingAmount());
        main.setStatus(receivable.getStatus());
        main.setRemark(receivable.getRemark());
        main.setCreatorId(receivable.getCreatorId());
        main.setModifyId(receivable.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static ReceivableDetailVO toDetailVO(ReceivableSaveItemVO saveItemVO) {
        ReceivableDetailVO detailVO = new ReceivableDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
