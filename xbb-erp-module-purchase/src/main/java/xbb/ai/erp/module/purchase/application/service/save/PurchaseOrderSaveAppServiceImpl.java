package xbb.ai.erp.module.purchase.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveContextPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.support.PurchaseInsertDefaults;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveProtocolValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PurchaseOrderSaveAppServiceImpl implements PurchaseOrderSaveAppService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderDraftRepository purchaseOrderDraftRepository;
    private final PurchaseOrderSaveProtocolValidator protocolValidator;
    private final PurchaseOrderSaveCommonValidator commonValidator;

    public PurchaseOrderSaveAppServiceImpl(
        PurchaseOrderRepository purchaseOrderRepository,
        PurchaseOrderItemRepository purchaseOrderItemRepository,
        PurchaseOrderDraftRepository purchaseOrderDraftRepository
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.purchaseOrderDraftRepository = purchaseOrderDraftRepository;
        this.protocolValidator = new PurchaseOrderSaveProtocolValidator();
        this.commonValidator = new PurchaseOrderSaveCommonValidator();
    }

    @Override
    public Long save(PurchaseOrderSaveDTO dto) {
        PurchaseOrder purchaseOrder = PurchaseOrderAdminAssembler.toPurchaseOrder(dto);
        if (purchaseOrder.getId() == null) {
            PurchaseInsertDefaults.apply(purchaseOrder, dto.getUserId(), System.currentTimeMillis());
            purchaseOrderRepository.insert(purchaseOrder);
        } else {
            purchaseOrderRepository.update(purchaseOrder);
        }
        syncItems(dto.getCorpid(), purchaseOrder.getId(), dto.getUserId(), dto.getItems());
        return purchaseOrder.getId();
    }

    @Override
    public BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto) {
        PurchaseOrderSaveContextPojo context = PurchaseOrderAdminAssembler.toSubmitContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForSubmit(context);

        PurchaseOrderSaveDTO saveDTO = new PurchaseOrderSaveDTO();
        saveDTO.setCorpid(dto.getCorpid());
        saveDTO.setUserId(dto.getUserId());
        saveDTO.setMain(dto.getMain());
        saveDTO.setItems(dto.getItems());
        save(saveDTO);

        if (purchaseOrderDraftRepository != null && dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
            purchaseOrderDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    private void syncItems(String corpid, Long orderId, String userId, List<PurchaseOrderItemMainDTO> items) {
        if (purchaseOrderItemRepository == null || items == null) {
            return;
        }
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", corpid);
        conditionMap.put("orderId", orderId);
        List<PurchaseOrderItem> existingItems = purchaseOrderItemRepository.findByCondition(conditionMap);
        Set<Long> incomingIds = items.stream().map(PurchaseOrderItemMainDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        for (PurchaseOrderItem existingItem : existingItems) {
            if (existingItem.getId() != null && !incomingIds.contains(existingItem.getId())) {
                purchaseOrderItemRepository.removeById(corpid, existingItem.getId());
            }
        }
        for (int index = 0; index < items.size(); index++) {
            PurchaseOrderItemMainDTO item = items.get(index);
            PurchaseOrderItem domain = PurchaseOrderAdminAssembler.toPurchaseOrderItem(corpid, orderId, item);
            if (domain.getId() == null) {
                PurchaseInsertDefaults.apply(domain, userId, System.currentTimeMillis(), index + 1);
                purchaseOrderItemRepository.insert(domain);
            } else {
                purchaseOrderItemRepository.update(domain);
            }
        }
    }
}
