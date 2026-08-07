package xbb.ai.erp.module.demo.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftSaveVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.application.assembler.DemoAdminAssembler;
import xbb.ai.erp.module.demo.application.assembler.DemoFieldAssembler;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;
import xbb.ai.erp.module.demo.application.schema.DemoListQueryAdapter;
import xbb.ai.erp.module.demo.application.service.DemoAdminAppService;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.domain.model.DemoItem;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DemoAdminAppServiceImpl implements DemoAdminAppService {

    private final DemoRepository demoRepository;
    private final DemoItemRepository demoItemRepository;
    private final DemoFieldFactory demoFieldFactory;
    private final DemoListQueryAdapter demoListQueryAdapter;

    @Override
    public ListBaseVO<DemoListItemVO> list(ListBaseDTO dto) {
        Map<String, Object> conditionMap = demoListQueryAdapter.toConditionMap(dto);
        List<Demo> list = demoRepository.findByCondition(conditionMap);
        Long total = demoRepository.count(conditionMap);
        ListBaseVO<DemoListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(DemoAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<DemoSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<DemoSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(DemoFieldAssembler.buildHeadList(demoFieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(DemoAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<DemoSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<DemoSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(DemoFieldAssembler.buildHeadList(demoFieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public DemoDraftSaveVO saveDraft(DemoDraftSaveDTO dto) {
        throw new BizException("草稿存储需要由业务模块实现");
    }

    @Override
    public BaseVO saveAndSubmit(DemoSubmitSaveDTO dto) {
        save(dto);
        return new BaseVO();
    }

    @Override
    public List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto) {
        throw new BizException("草稿存储需要由业务模块实现");
    }

    @Override
    public DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto) {
        throw new BizException("草稿存储需要由业务模块实现");
    }

    @Override
    @Transactional
    public Long save(DemoSaveDTO dto) {
        Demo demo = DemoAdminAssembler.toDemo(dto);
        if (demo.getId() == null) {
            demoRepository.insert(demo);
        } else {
            demoRepository.update(demo);
        }
        syncItems(dto, demo);
        return demo.getId();
    }

    private void syncItems(DemoSaveDTO dto, Demo demo) {
        if (demo.getId() == null || dto.getItems() == null) {
            return;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", demo.getCorpid());
        condition.put("dataId", demo.getId());
        List<DemoItem> existing = demoItemRepository.findByCondition(condition);
        if (!existing.isEmpty()) {
            demoItemRepository.removeBatchByIds(demo.getCorpid(), existing.stream().map(DemoItem::getId).toList());
        }
        List<DemoItem> items = dto.getItems().stream()
            .map(item -> DemoAdminAssembler.toDemoItem(item, demo.getCorpid(), demo.getId()))
            .toList();
        if (!items.isEmpty()) {
            demoItemRepository.insertBatch(items);
        }
    }

    @Override
    public DemoDetailVO detail(IdBaseDTO dto) {
        return DemoAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        demoRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private DemoSaveItemVO toSaveItem(IdBaseDTO dto) {
        DemoSaveItemVO vo = DemoAdminAssembler.toSaveItemVO(demoRepository.findById(dto.getCorpid(), dto.getId()));
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpid", dto.getCorpid());
        condition.put("dataId", dto.getId());
        vo.setItems(demoItemRepository.findByCondition(condition).stream().map(DemoAdminAssembler::toDemoItemDTO).toList());
        return vo;
    }
}
