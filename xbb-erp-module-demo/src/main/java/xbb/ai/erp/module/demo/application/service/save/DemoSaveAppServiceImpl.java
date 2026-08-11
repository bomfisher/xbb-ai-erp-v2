package xbb.ai.erp.module.demo.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.application.assembler.DemoAdminAssembler;
import xbb.ai.erp.module.demo.application.validator.DemoValidator;
import xbb.ai.erp.module.demo.application.port.DemoDraftRepository;
import xbb.ai.erp.module.demo.application.validator.DemoSaveProtocolValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveCommonValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveBusinessValidator;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import xbb.ai.erp.module.demo.domain.model.DemoItem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DemoSaveAppServiceImpl {

    private final DemoRepository demoRepository;
    private final DemoItemRepository demoItemRepository;

    private final DemoDraftRepository draftRepository;
    private final DemoSaveProtocolValidator protocolValidator;
    private final DemoSaveCommonValidator commonValidator;
    private final DemoSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(DemoSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        Long dataId = save(dto);
        syncItems(dto, dataId);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(DemoSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        DemoValidator.validateSave(dto);
        Demo entity = DemoAdminAssembler.toDemo(dto);
        long now = System.currentTimeMillis();
        entity.setCreatorId(dto.getUserId());
        entity.setModifyId(dto.getUserId());
        entity.setAddTime(now);
        entity.setUpdateTime(now);
        entity.setDel(0);
        if (entity.getId() == null) {
            demoRepository.insert(entity);
        } else {
            demoRepository.update(entity);
        }
        return entity.getId();
    }

    private void syncItems(DemoSaveDTO dto, Long dataId) {
        long now = System.currentTimeMillis();
        List<DemoItem> submittedItems = java.util.stream.Stream.concat(
                validItems(dto.getItems()).stream().map(item -> DemoAdminAssembler.toDemoItem(item,
                    false, dto.getCorpid(), dataId, dto.getUserId(), now)),
                validItems(dto.getItems2()).stream().map(item -> DemoAdminAssembler.toDemoItem(item,
                    true, dto.getCorpid(), dataId, dto.getUserId(), now)))
            .toList();

        Map<Long, DemoItem> existingItems = new HashMap<>();
        for (DemoItem item : demoItemRepository.findByDataId(dto.getCorpid(), dataId)) {
            existingItems.put(item.getId(), item);
        }

        Set<Long> submittedIds = new HashSet<>();
        List<DemoItem> itemsToInsert = new java.util.ArrayList<>();
        List<DemoItem> itemsToUpdate = new java.util.ArrayList<>();
        for (DemoItem item : submittedItems) {
            if (item.getId() == null) {
                itemsToInsert.add(item);
                continue;
            }
            if (!submittedIds.add(item.getId())) {
                throw new BizException("子档ID重复");
            }
            if (!existingItems.containsKey(item.getId())) {
                throw new BizException("子档不存在或不属于当前DEMO");
            }
            itemsToUpdate.add(item);
        }

        List<Long> itemsToRemove = existingItems.keySet().stream()
            .filter(id -> !submittedIds.contains(id))
            .toList();
        if (!itemsToRemove.isEmpty()) {
            demoItemRepository.removeBatchByIds(dto.getCorpid(), itemsToRemove);
        }
        for (DemoItem item : itemsToUpdate) {
            demoItemRepository.update(item);
        }
        if (!itemsToInsert.isEmpty()) {
            demoItemRepository.insertBatch(itemsToInsert);
        }
    }

    private static List<DemoItemDTO> validItems(List<DemoItemDTO> items) {
        if (items == null) return List.of();
        return items.stream().filter(item -> item != null && item.getName() != null && !item.getName().isBlank()).toList();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            demoRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
