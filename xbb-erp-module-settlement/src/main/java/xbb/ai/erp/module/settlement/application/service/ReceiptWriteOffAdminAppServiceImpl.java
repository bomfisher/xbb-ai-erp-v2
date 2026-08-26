package xbb.ai.erp.module.settlement.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffSourceQueryDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffSourceVO;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.module.settlement.domain.model.ReceiptWriteOff;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptWriteOffRepository;
import xbb.ai.erp.module.settlement.admin.ReceiptWriteOffFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;

@Service
@RequiredArgsConstructor
public class ReceiptWriteOffAdminAppServiceImpl implements ReceiptWriteOffAdminAppService {
    private final ReceiptWriteOffRepository repository;
    private final ReceiptWriteOffService writeOffService;
    private final BizNoGenerator bizNoGenerator;
    private final ReceiptRepository receiptRepository;
    private final ReceivableRepository receivableRepository;

    @Override
    public ListBaseVO<ReceiptWriteOffListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> condition = new java.util.HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("offset", Math.max(0, (dto.getPageNum() - 1) * dto.getPageSize()));
        condition.put("limit", dto.getPageSize());
        List<ReceiptWriteOffListItemVO> items = repository.findByCondition(condition).stream().map(this::toList).toList();
        ListBaseVO<ReceiptWriteOffListItemVO> result = new ListBaseVO<>();
        result.setList(items);
        result.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), repository.count(condition).intValue()));
        return result;
    }

    @Override
    public SaveItemVO<ReceiptWriteOffSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<ReceiptWriteOffSaveItemVO> result = new SaveItemVO<>();
        result.setHeadList(java.util.Arrays.stream(ReceiptWriteOffFieldEnum.values())
            .map(ReceiptWriteOffFieldEnum::toSceneFieldMeta)
            .map(SceneFieldAssembler::build)
            .toList());
        ReceiptWriteOffSaveItemVO data = new ReceiptWriteOffSaveItemVO();
        data.setWriteoffNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.RECEIPT_WRITEOFF.getCode()));
        data.setWriteoffDate(System.currentTimeMillis());
        result.setData(data);
        return result;
    }

    @Override
    @Transactional
    public BaseVO saveAndSubmit(ReceiptWriteOffSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getCustomerId() == null || dto.getAllocations() == null || dto.getAllocations().isEmpty()) {
            throw new BizException("客户和核销明细不能为空");
        }
        if (dto.getAllocations().stream().map(item -> item.getAmount() == null ? BigDecimal.ZERO : item.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("核销金额必须大于零");
        }
        String writeoffNo = dto.getWriteoffNo() == null || dto.getWriteoffNo().isBlank()
            ? bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.RECEIPT_WRITEOFF.getCode()) : dto.getWriteoffNo();
        writeOffService.writeOffAllocations(dto.getCorpid(), dto.getUserId(), dto.getCustomerId(), writeoffNo,
            dto.getWriteoffDate(), dto.getRemark(), dto.getAllocations());
        return new BaseVO();
    }

    @Override
    public List<ReceiptWriteOffSourceVO> findAdvanceSources(ReceiptWriteOffSourceQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        requireCustomer(dto.getCustomerId());
        return receiptRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getCustomerId(), "receiptType", "ADVANCE_PAYMENT"))
            .stream().filter(item -> item.getRemainingAmount() != null && item.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0)
            .filter(item -> dto.getKeyword() == null || dto.getKeyword().isBlank() || item.getReceiptNo().contains(dto.getKeyword()))
            .map(this::toAdvanceSource).toList();
    }

    @Override
    public List<ReceiptWriteOffSourceVO> findReceivableSources(ReceiptWriteOffSourceQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        requireCustomer(dto.getCustomerId());
        return receivableRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getCustomerId()))
            .stream().filter(item -> item.getRemainingAmount() != null && item.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0)
            .filter(item -> dto.getKeyword() == null || dto.getKeyword().isBlank() || item.getReceivableNo().contains(dto.getKeyword()))
            .map(this::toReceivableSource).toList();
    }

    @Override
    @Transactional
    public BaseVO reverse(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        ReceiptWriteOff writeOff = repository.findById(dto.getCorpid(), dto.getId());
        if (writeOff == null) {
            throw new BizException("核销记录不存在");
        }
        if (!Integer.valueOf(1).equals(writeOff.getStatus())) {
            throw new BizException("核销记录已冲销");
        }
        Receipt receipt = receiptRepository.findById(dto.getCorpid(), writeOff.getReceiptId());
        Receivable receivable = receivableRepository.findById(dto.getCorpid(), writeOff.getReceivableId());
        if (receipt == null || receivable == null) {
            throw new BizException("核销关联的收款单或应收款不存在");
        }
        repository.reverse(dto.getCorpid(), dto.getId(), System.currentTimeMillis(), dto.getUserId());
        restore(receipt, writeOff.getAmount(), dto.getUserId());
        restore(receivable, writeOff.getAmount(), dto.getUserId());
        receiptRepository.update(receipt);
        receivableRepository.update(receivable);
        return new BaseVO();
    }

    private void restore(Receipt receipt, BigDecimal amount, String userId) {
        receipt.setWrittenOffAmount(receipt.getWrittenOffAmount().subtract(amount));
        receipt.setRemainingAmount(receipt.getRemainingAmount().add(amount));
        receipt.setStatus(status(receipt.getRemainingAmount(), receipt.getAmount()));
        receipt.setModifyId(userId);
    }

    private void restore(Receivable receivable, BigDecimal amount, String userId) {
        receivable.setWrittenOffAmount(receivable.getWrittenOffAmount().subtract(amount));
        receivable.setRemainingAmount(receivable.getRemainingAmount().add(amount));
        receivable.setStatus(status(receivable.getRemainingAmount(), receivable.getAmount()));
        receivable.setModifyId(userId);
    }

    private int status(BigDecimal remainingAmount, BigDecimal amount) {
        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 2;
        }
        if (remainingAmount.compareTo(amount) < 0) {
            return 1;
        }
        return 0;
    }

    private void requireCustomer(Long customerId) { if (customerId == null) throw new BizException("请先选择客户"); }
    private ReceiptWriteOffSourceVO toAdvanceSource(Receipt source) { ReceiptWriteOffSourceVO target = new ReceiptWriteOffSourceVO(); target.setId(source.getId()); target.setCode(source.getReceiptNo()); target.setAmount(source.getAmount()); target.setWrittenOffAmount(source.getWrittenOffAmount()); target.setRemainingAmount(source.getRemainingAmount()); return target; }
    private ReceiptWriteOffSourceVO toReceivableSource(Receivable source) { ReceiptWriteOffSourceVO target = new ReceiptWriteOffSourceVO(); target.setId(source.getId()); target.setCode(source.getReceivableNo()); target.setAmount(source.getAmount()); target.setWrittenOffAmount(source.getWrittenOffAmount()); target.setRemainingAmount(source.getRemainingAmount()); return target; }

    private ReceiptWriteOffListItemVO toList(ReceiptWriteOff source) {
        ReceiptWriteOffListItemVO target = new ReceiptWriteOffListItemVO();
        target.setId(source.getId()); target.setWriteoffNo(source.getWriteoffNo()); target.setReceiptId(source.getReceiptId());
        target.setCustomerId(source.getCustomerId());
        target.setReceivableId(source.getReceivableId()); target.setWriteoffDate(source.getWriteoffDate());
        target.setAmount(source.getAmount() == null ? null : source.getAmount().toPlainString());
        target.setRemark(source.getRemark());
        return target;
    }
}
