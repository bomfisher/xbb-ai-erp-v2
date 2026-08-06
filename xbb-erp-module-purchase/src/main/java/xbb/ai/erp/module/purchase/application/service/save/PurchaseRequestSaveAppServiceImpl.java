package xbb.ai.erp.module.purchase.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseRequestAdminAssembler;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveContextPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseRequestDraftRepository;
import xbb.ai.erp.module.purchase.application.support.PurchaseInsertDefaults;
import xbb.ai.erp.module.purchase.application.validator.PurchaseRequestSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseRequestSaveProtocolValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PurchaseRequestSaveAppServiceImpl implements PurchaseRequestSaveAppService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestItemRepository purchaseRequestItemRepository;
    private final PurchaseRequestDraftRepository purchaseRequestDraftRepository;
    private final PurchaseRequestSaveProtocolValidator protocolValidator;
    private final PurchaseRequestSaveCommonValidator commonValidator;

    public PurchaseRequestSaveAppServiceImpl(
        PurchaseRequestRepository purchaseRequestRepository,
        PurchaseRequestItemRepository purchaseRequestItemRepository,
        PurchaseRequestDraftRepository purchaseRequestDraftRepository
    ) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestItemRepository = purchaseRequestItemRepository;
        this.purchaseRequestDraftRepository = purchaseRequestDraftRepository;
        this.protocolValidator = new PurchaseRequestSaveProtocolValidator();
        this.commonValidator = new PurchaseRequestSaveCommonValidator();
    }

    @Override
    public Long save(PurchaseRequestSaveDTO dto) {
        PurchaseRequest purchaseRequest = PurchaseRequestAdminAssembler.toPurchaseRequest(dto);
        if (purchaseRequest.getId() == null) {
            PurchaseInsertDefaults.apply(purchaseRequest, dto.getUserId(), System.currentTimeMillis());
            purchaseRequestRepository.insert(purchaseRequest);
        } else {
            purchaseRequestRepository.update(purchaseRequest);
        }
        syncItems(dto.getCorpid(), purchaseRequest.getId(), dto.getUserId(), dto.getItems());
        return purchaseRequest.getId();
    }

    @Override
    public BaseVO saveAndSubmit(PurchaseRequestSubmitSaveDTO dto) {
        PurchaseRequestSaveContextPojo context = PurchaseRequestAdminAssembler.toSubmitContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForSubmit(context);

        PurchaseRequestSaveDTO saveDTO = new PurchaseRequestSaveDTO();
        saveDTO.setCorpid(dto.getCorpid());
        saveDTO.setUserId(dto.getUserId());
        saveDTO.setMain(dto.getMain());
        saveDTO.setItems(dto.getItems());
        save(saveDTO);

        if (purchaseRequestDraftRepository != null && dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
            purchaseRequestDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    private void syncItems(String corpid, Long requestId, String userId, List<PurchaseRequestItemMainDTO> items) {
        if (purchaseRequestItemRepository == null || items == null) {
            return;
        }
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", corpid);
        conditionMap.put("requestId", requestId);
        List<PurchaseRequestItem> existingItems = purchaseRequestItemRepository.findByCondition(conditionMap);
        Set<Long> incomingIds = items.stream().map(PurchaseRequestItemMainDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        for (PurchaseRequestItem existingItem : existingItems) {
            if (existingItem.getId() != null && !incomingIds.contains(existingItem.getId())) {
                purchaseRequestItemRepository.removeById(corpid, existingItem.getId());
            }
        }
        for (int index = 0; index < items.size(); index++) {
            PurchaseRequestItemMainDTO item = items.get(index);
            PurchaseRequestItem domain = PurchaseRequestAdminAssembler.toPurchaseRequestItem(corpid, requestId, item);
            if (domain.getId() == null) {
                PurchaseInsertDefaults.apply(domain, userId, System.currentTimeMillis(), index + 1);
                purchaseRequestItemRepository.insert(domain);
            } else {
                purchaseRequestItemRepository.update(domain);
            }
        }
    }
}
