package xbb.ai.erp.module.approval.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalInstanceListDTO;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstance;
import xbb.ai.erp.module.approval.domain.model.ApprovalInstanceActionLog;
import xbb.ai.erp.module.approval.domain.repository.ApprovalInstanceRepository;
import xbb.ai.erp.module.org.application.service.OrgListReferenceQueryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApprovalInstanceServiceImplTest {

    private final ApprovalInstanceRepository instanceRepository = Mockito.mock(ApprovalInstanceRepository.class);
    private final OrgListReferenceQueryService orgListReferenceQueryService = Mockito.mock(OrgListReferenceQueryService.class);
    private final ApprovalInstanceServiceImpl service = new ApprovalInstanceServiceImpl(instanceRepository, new ObjectMapper(),
        orgListReferenceQueryService);

    @Test
    void 待办列表映射运行时状态和冻结节点名称() {
        ApprovalInstance instance = buildInstance();
        ApprovalInstanceListDTO dto = new ApprovalInstanceListDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-1");
        dto.setView("TODO");
        dto.setStatus("IN_PROGRESS");
        Mockito.when(instanceRepository.findByApprovalCenter("demo-corp", "user-1", "TODO", "IN_APPROVAL", null, null))
            .thenReturn(List.of(instance));
        Mockito.when(orgListReferenceQueryService.findActiveMemberNames("demo-corp", java.util.Set.of("user-1")))
            .thenReturn(java.util.Map.of("user-1", "张三"));

        var result = service.list(dto);

        assertEquals(1, result.size());
        assertEquals("IN_PROGRESS", result.getFirst().getStatus());
        assertEquals("部门负责人审批", result.getFirst().getCurrentNodeName());
        assertEquals("销售订单", result.getFirst().getBusinessName());
        assertEquals("张三", result.getFirst().getSubmitterName());
    }

    @Test
    void 详情查询缺少实例标识时返回业务错误() {
        ApprovalInstanceDetailDTO dto = new ApprovalInstanceDetailDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-1");

        BizException exception = assertThrows(BizException.class, () -> service.detail(dto));

        assertEquals("审批实例标识不能为空", exception.getMessage());
    }

    @Test
    void 详情使用冻结快照并返回操作轨迹() {
        ApprovalInstance instance = buildInstance();
        ApprovalInstanceActionLog actionLog = new ApprovalInstanceActionLog();
        actionLog.setNodeNo(1);
        actionLog.setOperatorId("user-1");
        actionLog.setAction("APPROVE");
        actionLog.setOperatedAt(1_700_000_000_000L);
        ApprovalInstanceDetailDTO dto = new ApprovalInstanceDetailDTO();
        dto.setCorpid("demo-corp");
        dto.setUserId("user-1");
        dto.setInstanceId("instance-1");
        Mockito.when(instanceRepository.findById("demo-corp", "instance-1")).thenReturn(instance);
        Mockito.when(instanceRepository.canAccessApprovalCenter("demo-corp", "user-1", "instance-1")).thenReturn(true);
        Mockito.when(instanceRepository.findActionLogs("instance-1")).thenReturn(List.of(actionLog));
        Mockito.when(orgListReferenceQueryService.findActiveMemberNames("demo-corp", java.util.Set.of("user-1")))
            .thenReturn(java.util.Map.of("user-1", "张三"));

        var result = service.detail(dto);

        assertEquals("销售订单审批", result.getFlowName());
        assertEquals("部门负责人审批", result.getTimeline().getFirst().getNodeName());
        assertEquals("同意", result.getTimeline().getFirst().getAction());
    }

    @Test
    void 存在活动实例时流程不可删除() {
        Mockito.when(instanceRepository.hasActiveByFlowDefinitionId("demo-corp", 1L)).thenReturn(true);

        assertEquals(true, service.hasActiveInstancesByFlowDefinitionId("demo-corp", 1L));
    }

    @Test
    void 不存在活动实例时流程可以删除() {
        assertEquals(false, service.hasActiveInstancesByFlowDefinitionId("demo-corp", 1L));
    }

    private ApprovalInstance buildInstance() {
        ApprovalInstance instance = new ApprovalInstance();
        instance.setInstanceId("instance-1");
        instance.setCorpid("demo-corp");
        instance.setFlowCode("SALES_ORDER_CREATE_DEFAULT");
        instance.setFlowVersion(1);
        instance.setBusinessCode("SALES_ORDER");
        instance.setApprovalScene(ApprovalScene.CREATE);
        instance.setSubjectId("order-1");
        instance.setSubmitterId("user-1");
        instance.setSubjectSummary("SO-001");
        instance.setStatus(ApprovalStatus.IN_APPROVAL);
        instance.setCurrentNodeNo(1);
        instance.setSubmittedAt(1_700_000_000_000L);
        instance.setFlowSnapshotJson("""
            {"flowName":"销售订单审批","nodes":[{"nodeNo":1,"nodeName":"部门负责人审批"}]}
            """);
        instance.setSubjectSnapshotJson("{\"main\":{\"orderNo\":\"SO-001\"}}");
        return instance;
    }
}
