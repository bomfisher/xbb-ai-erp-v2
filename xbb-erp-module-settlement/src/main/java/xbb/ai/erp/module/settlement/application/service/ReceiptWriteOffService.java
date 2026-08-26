package xbb.ai.erp.module.settlement.application.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.settlement.admin.ReceivableWriteOffStatusEnum;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffAllocationDTO;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.model.ReceiptWriteOff;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptWriteOffRepository;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;

@Service
@RequiredArgsConstructor
public class ReceiptWriteOffService {
    private static final int ACTIVE = 1;
    private static final int REVERSED = 0;

    private final ReceiptRepository receiptRepository;
    private final ReceivableRepository receivableRepository;
    private final ReceiptWriteOffRepository receiptWriteOffRepository;
    private final BizNoGenerator bizNoGenerator;

    @Transactional
    public BaseVO writeOff(ReceiptWriteOffDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getReceiptId() == null || dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BizException("核销收款和应收明细不能为空");
        }
        writeOff(dto.getCorpid(), dto.getUserId(), dto.getReceiptId(), dto.getItems(), null, null);
        return new BaseVO();
    }

    public void writeOff(String corpid, String userId, Long receiptId, List<ReceiptWriteOffItemDTO> items) {
        writeOff(corpid, userId, receiptId, items, null, null);
    }

    public void writeOff(String corpid, String userId, Long receiptId, List<ReceiptWriteOffItemDTO> items,
            String writeoffNo, String remark) {
        Receipt receipt = requireReceipt(corpid, receiptId);
        if (!"ADVANCE_PAYMENT".equals(receipt.getReceiptType())) {
            throw new BizException("只有预收款可以通过核销明细核销");
        }
        String batchNo = writeoffNo == null || writeoffNo.isBlank()
            ? bizNoGenerator.next(corpid, BusinessCodeEnum.RECEIPT_WRITEOFF.getCode()) : writeoffNo;
        for (ReceiptWriteOffItemDTO item : items) {
            Receivable receivable = requireReceivable(corpid, item.getReceivableId());
            validate(item, receipt, receivable);
            apply(receipt, receivable, item.getAmount(), userId);
            ReceiptWriteOff writeOff = new ReceiptWriteOff();
            writeOff.setCorpid(corpid);
            writeOff.setWriteoffNo(batchNo);
            writeOff.setReceiptId(receipt.getId());
            writeOff.setReceivableId(receivable.getId());
            writeOff.setWriteoffDate(System.currentTimeMillis());
            writeOff.setAmount(item.getAmount());
            writeOff.setRemark(remark);
            writeOff.setCreatorId(userId);
            writeOff.setModifyId(userId);
            receiptWriteOffRepository.insert(writeOff);
        }
    }

    @Transactional
    public void writeOffAllocations(String corpid, String userId, Long customerId, String writeoffNo,
            Long writeoffDate, String remark, List<ReceiptWriteOffAllocationDTO> allocations) {
        if (customerId == null || writeoffNo == null || writeoffNo.isBlank() || allocations == null || allocations.isEmpty()) {
            throw new BizException("客户、核销批次号和核销明细不能为空");
        }
        for (ReceiptWriteOffAllocationDTO allocation : allocations) {
            Receipt receipt = requireReceipt(corpid, allocation.getReceiptId());
            Receivable receivable = requireReceivable(corpid, allocation.getReceivableId());
            if (!customerId.equals(receipt.getCustomerId()) || !customerId.equals(receivable.getCustomerId())) {
                throw new BizException("核销来源单据必须属于所选客户");
            }
            ReceiptWriteOffItemDTO item = new ReceiptWriteOffItemDTO();
            item.setReceivableId(allocation.getReceivableId());
            item.setAmount(allocation.getAmount());
            validate(item, receipt, receivable);
            apply(receipt, receivable, allocation.getAmount(), userId);
            ReceiptWriteOff writeOff = new ReceiptWriteOff();
            writeOff.setCorpid(corpid);
            writeOff.setWriteoffNo(writeoffNo);
            writeOff.setReceiptId(receipt.getId());
            writeOff.setReceivableId(receivable.getId());
            writeOff.setWriteoffDate(writeoffDate == null ? System.currentTimeMillis() : writeoffDate);
            writeOff.setAmount(allocation.getAmount());
            writeOff.setRemark(remark);
            writeOff.setCreatorId(userId);
            writeOff.setModifyId(userId);
            receiptWriteOffRepository.insert(writeOff);
        }
    }

    private void apply(Receipt receipt, Receivable receivable, BigDecimal amount, String userId) {
        receipt.setWrittenOffAmount(receipt.getWrittenOffAmount().add(amount));
        receipt.setRemainingAmount(receipt.getRemainingAmount().subtract(amount));
        receipt.setStatus(status(receipt.getRemainingAmount(), receipt.getAmount()));
        receipt.setModifyId(userId);
        receivable.setWrittenOffAmount(receivable.getWrittenOffAmount().add(amount));
        receivable.setRemainingAmount(receivable.getRemainingAmount().subtract(amount));
        receivable.setStatus(status(receivable.getRemainingAmount(), receivable.getAmount()));
        receivable.setModifyId(userId);
        receiptRepository.update(receipt);
        receivableRepository.update(receivable);
    }

    private void validate(ReceiptWriteOffItemDTO item, Receipt receipt, Receivable receivable) {
        if (item.getReceivableId() == null || item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("核销金额必须大于零");
        }
        if (!receipt.getCustomerId().equals(receivable.getCustomerId())) {
            throw new BizException("收款与应收客户不一致");
        }
        if (receipt.getRemainingAmount().compareTo(item.getAmount()) < 0 || receivable.getRemainingAmount().compareTo(item.getAmount()) < 0) {
            throw new BizException("收款或应收余额不足");
        }
    }

    private Receipt requireReceipt(String corpid, Long id) {
        Receipt receipt = receiptRepository.findById(corpid, id);
        if (receipt == null) throw new BizException("收款单不存在");
        return receipt;
    }

    private Receivable requireReceivable(String corpid, Long id) {
        Receivable receivable = receivableRepository.findById(corpid, id);
        if (receivable == null) throw new BizException("应收开放项不存在");
        return receivable;
    }

    private int status(BigDecimal remainingAmount, BigDecimal amount) {
        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) return ReceivableWriteOffStatusEnum.WRITTEN_OFF.getValue();
        if (remainingAmount.compareTo(amount) < 0) return ReceivableWriteOffStatusEnum.PARTIALLY_WRITTEN_OFF.getValue();
        return ReceivableWriteOffStatusEnum.UNWRITTEN_OFF.getValue();
    }
}
