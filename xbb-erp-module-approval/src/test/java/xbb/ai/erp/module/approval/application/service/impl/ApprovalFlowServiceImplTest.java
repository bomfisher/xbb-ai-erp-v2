package xbb.ai.erp.module.approval.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowIdDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowSaveDTO;
import xbb.ai.erp.module.approval.application.catalog.ApprovalSubjectCatalog;
import xbb.ai.erp.module.approval.application.service.ApprovalInstanceService;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowDefinition;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowStatus;
import xbb.ai.erp.module.approval.domain.repository.ApprovalFlowRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApprovalFlowServiceImplTest {

    private final ApprovalFlowRepository repository = Mockito.mock(ApprovalFlowRepository.class);
    private final ApprovalInstanceService approvalInstanceService = Mockito.mock(ApprovalInstanceService.class);
    private final ApprovalFlowServiceImpl service = new ApprovalFlowServiceImpl(repository, approvalInstanceService,
        new ApprovalSubjectCatalog(List.of()), new ObjectMapper());

    @Test
    void 已启用流程不允许删除() {
        ApprovalFlowIdDTO dto = deleteDTO();
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(flow(ApprovalFlowStatus.PUBLISHED));

        BizException exception = assertThrows(BizException.class, () -> service.remove(dto));

        assertEquals("仅未启用或已停用流程可以删除", exception.getMessage());
        verify(approvalInstanceService, never()).hasActiveInstancesByFlowDefinitionId(Mockito.anyString(), Mockito.anyLong());
        verify(repository, never()).remove(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    void 存在待审批或审批中实例不允许删除() {
        ApprovalFlowIdDTO dto = deleteDTO();
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(flow(ApprovalFlowStatus.DISABLED));
        when(approvalInstanceService.hasActiveInstancesByFlowDefinitionId(dto.getCorpid(), dto.getId())).thenReturn(true);

        BizException exception = assertThrows(BizException.class, () -> service.remove(dto));

        assertEquals("流程存在待审批或审批中的实例，不允许删除", exception.getMessage());
        verify(repository, never()).remove(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    void 停用且无活动实例时逻辑删除流程() {
        ApprovalFlowIdDTO dto = deleteDTO();
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(flow(ApprovalFlowStatus.DISABLED));
        when(approvalInstanceService.hasActiveInstancesByFlowDefinitionId(dto.getCorpid(), dto.getId())).thenReturn(false);

        service.remove(dto);

        verify(repository).remove(dto.getCorpid(), dto.getId(), dto.getUserId());
    }

    @Test
    void 未启用草稿且无活动实例时逻辑删除流程() {
        ApprovalFlowIdDTO dto = deleteDTO();
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(flow(ApprovalFlowStatus.DRAFT));
        when(approvalInstanceService.hasActiveInstancesByFlowDefinitionId(dto.getCorpid(), dto.getId())).thenReturn(false);

        service.remove(dto);

        verify(repository).remove(dto.getCorpid(), dto.getId(), dto.getUserId());
    }

    @Test
    void 逻辑删除后重新创建沿用下一个历史版本号() {
        ApprovalFlowSaveDTO dto = new ApprovalFlowSaveDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-001");
        dto.setBusinessCode("SALES_CONTRACT");
        dto.setApprovalScene(ApprovalScene.CREATE);
        dto.setFlowCode("SALES_CONTRACT_CREATE_DEFAULT");
        dto.setFlowName("销售合同新建审批");
        AtomicReference<ApprovalFlowDefinition> savedDefinition = new AtomicReference<>();
        when(repository.findMaxVersion(dto.getCorpid(), dto.getBusinessCode(), dto.getApprovalScene(), dto.getFlowCode()))
            .thenReturn(6);
        Mockito.doAnswer(invocation -> {
            ApprovalFlowDefinition definition = invocation.getArgument(0);
            definition.setId(99L);
            savedDefinition.set(definition);
            return null;
        }).when(repository).save(Mockito.any(ApprovalFlowDefinition.class));
        when(repository.findById(dto.getCorpid(), 99L)).thenAnswer(invocation -> savedDefinition.get());

        service.saveDraft(dto);

        assertEquals(7, savedDefinition.get().getVersion());
        assertEquals(ApprovalFlowStatus.DRAFT, savedDefinition.get().getStatus());
    }

    @Test
    void 编辑已启用流程保持原标识和启用状态() {
        ApprovalFlowSaveDTO dto = saveDTO(1L);
        ApprovalFlowDefinition existing = flow(ApprovalFlowStatus.PUBLISHED);
        existing.setVersion(3);
        AtomicReference<ApprovalFlowDefinition> savedDefinition = new AtomicReference<>();
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(existing);
        Mockito.doAnswer(invocation -> {
            savedDefinition.set(invocation.getArgument(0));
            return null;
        }).when(repository).save(Mockito.any(ApprovalFlowDefinition.class));

        service.saveDraft(dto);

        assertEquals(1L, savedDefinition.get().getId());
        assertEquals(3, savedDefinition.get().getVersion());
        assertEquals(ApprovalFlowStatus.PUBLISHED, savedDefinition.get().getStatus());
        verify(repository, never()).findMaxVersion(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }

    @Test
    void 编辑已停用流程保持停用状态() {
        ApprovalFlowSaveDTO dto = saveDTO(1L);
        ApprovalFlowDefinition existing = flow(ApprovalFlowStatus.DISABLED);
        AtomicReference<ApprovalFlowDefinition> savedDefinition = new AtomicReference<>();
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(existing);
        Mockito.doAnswer(invocation -> {
            savedDefinition.set(invocation.getArgument(0));
            return null;
        }).when(repository).save(Mockito.any(ApprovalFlowDefinition.class));

        service.saveDraft(dto);

        assertEquals(1L, savedDefinition.get().getId());
        assertEquals(ApprovalFlowStatus.DISABLED, savedDefinition.get().getStatus());
    }

    @Test
    void 保存时拒绝不在业务编码枚举中的审批流程() {
        ApprovalFlowSaveDTO dto = new ApprovalFlowSaveDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-001");
        dto.setBusinessCode("SALES_ORDER_CREATE_DEFAULT");
        dto.setApprovalScene(ApprovalScene.CREATE);
        dto.setFlowCode("SALES_ORDER_CREATE_DEFAULT");
        dto.setFlowName("销售订单新建审批");

        BizException exception = assertThrows(BizException.class, () -> service.saveDraft(dto));

        assertEquals("审批业务编码未在 BusinessCodeEnum 中定义: SALES_ORDER_CREATE_DEFAULT", exception.getMessage());
        verify(repository, never()).save(Mockito.any(ApprovalFlowDefinition.class));
    }

    @Test
    void 不为旧的非法业务编码创建新流程版本() {
        ApprovalFlowIdDTO dto = deleteDTO();
        ApprovalFlowDefinition source = flow(ApprovalFlowStatus.DISABLED);
        source.setBusinessCode("SALES_ORDER_CREATE_DEFAULT");
        when(repository.findById(dto.getCorpid(), dto.getId())).thenReturn(source);

        BizException exception = assertThrows(BizException.class, () -> service.createNextVersion(dto));

        assertEquals("审批业务编码未在 BusinessCodeEnum 中定义: SALES_ORDER_CREATE_DEFAULT", exception.getMessage());
        verify(repository, never()).save(Mockito.any(ApprovalFlowDefinition.class));
    }

    private ApprovalFlowIdDTO deleteDTO() {
        ApprovalFlowIdDTO dto = new ApprovalFlowIdDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-001");
        dto.setId(1L);
        return dto;
    }

    private ApprovalFlowSaveDTO saveDTO(Long id) {
        ApprovalFlowSaveDTO dto = new ApprovalFlowSaveDTO();
        dto.setId(id);
        dto.setCorpid("demo-corp");
        dto.setUserId("user-001");
        dto.setBusinessCode("SALES_ORDER");
        dto.setApprovalScene(ApprovalScene.CREATE);
        dto.setFlowCode("SALES_ORDER_CREATE_DEFAULT");
        dto.setFlowName("销售订单新建审批");
        dto.setScopeJson("{}");
        dto.setNodes(List.of());
        return dto;
    }

    private ApprovalFlowDefinition flow(ApprovalFlowStatus status) {
        ApprovalFlowDefinition definition = new ApprovalFlowDefinition();
        definition.setId(1L);
        definition.setStatus(status);
        return definition;
    }
}
