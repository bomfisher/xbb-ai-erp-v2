package xbb.ai.erp.module.approval.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.domain.model.ApprovalApproverType;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowDefinition;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowNode;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowStatus;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeApprover;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeMode;
import xbb.ai.erp.module.approval.domain.repository.ApprovalFlowRepository;
import xbb.ai.erp.module.approval.infrastructure.persistence.mapper.ApprovalFlowDefinitionMapper;
import xbb.ai.erp.module.approval.infrastructure.persistence.mapper.ApprovalFlowNodeMapper;
import xbb.ai.erp.module.approval.infrastructure.persistence.mapper.ApprovalNodeApproverMapper;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalFlowDefinitionPO;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalFlowNodePO;
import xbb.ai.erp.module.approval.infrastructure.persistence.po.ApprovalNodeApproverPO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ApprovalFlowRepositoryImpl implements ApprovalFlowRepository {

    private final ApprovalFlowDefinitionMapper definitionMapper;
    private final ApprovalFlowNodeMapper nodeMapper;
    private final ApprovalNodeApproverMapper approverMapper;

    @Override
    public ApprovalFlowDefinition findById(String corpid, Long id) {
        ApprovalFlowDefinitionPO po = definitionMapper.findById(corpid, id);
        return po == null ? null : toDomain(po, true);
    }

    @Override
    public List<ApprovalFlowDefinition> findByCondition(String corpid, String businessCode, ApprovalScene approvalScene) {
        return definitionMapper.findByCondition(corpid, businessCode, approvalScene == null ? null : approvalScene.name()).stream()
            .map(po -> toDomain(po, false)).toList();
    }

    @Override
    public int findMaxVersion(String corpid, String businessCode, ApprovalScene approvalScene, String flowCode) {
        Integer maxVersion = definitionMapper.findMaxVersion(corpid, businessCode, approvalScene.name(), flowCode);
        return maxVersion == null ? 0 : maxVersion;
    }

    @Override
    public void save(ApprovalFlowDefinition definition) {
        long now = System.currentTimeMillis();
        ApprovalFlowDefinitionPO po = toPO(definition);
        po.setUpdateTime(now);
        if (po.getId() == null) {
            po.setDel(0);
            po.setAddTime(now);
            definitionMapper.insert(po);
            definition.setId(po.getId());
        } else {
            List<ApprovalFlowNodePO> oldNodes = nodeMapper.findByDefinitionId(po.getId());
            List<Long> oldNodeIds = oldNodes.stream().map(ApprovalFlowNodePO::getId).toList();
            if (!oldNodeIds.isEmpty()) {
                approverMapper.removeByNodeIds(oldNodeIds, now);
            }
            nodeMapper.removeByDefinitionId(po.getId(), now);
            definitionMapper.update(po);
        }
        for (ApprovalFlowNode node : definition.getNodes()) {
            ApprovalFlowNodePO nodePO = new ApprovalFlowNodePO();
            nodePO.setFlowDefinitionId(definition.getId());
            nodePO.setNodeNo(node.getNodeNo());
            nodePO.setNodeName(node.getNodeName());
            nodePO.setApprovalMode(node.getApprovalMode().name());
            nodePO.setDel(0);
            nodePO.setAddTime(now);
            nodePO.setUpdateTime(now);
            nodeMapper.insert(nodePO);
            for (ApprovalNodeApprover approver : node.getApprovers()) {
                ApprovalNodeApproverPO approverPO = new ApprovalNodeApproverPO();
                approverPO.setFlowNodeId(nodePO.getId());
                approverPO.setApproverType(approver.getApproverType().name());
                approverPO.setApproverValue(approver.getApproverValue());
                approverPO.setSuperiorLevel(approver.getSuperiorLevel());
                approverPO.setDel(0);
                approverPO.setAddTime(now);
                approverPO.setUpdateTime(now);
                approverMapper.insert(approverPO);
            }
        }
    }

    @Override
    public void remove(String corpid, Long id, String userId) {
        long now = System.currentTimeMillis();
        List<ApprovalFlowNodePO> nodes = nodeMapper.findByDefinitionId(id);
        List<Long> nodeIds = nodes.stream().map(ApprovalFlowNodePO::getId).toList();
        if (!nodeIds.isEmpty()) {
            approverMapper.removeByNodeIds(nodeIds, now);
        }
        nodeMapper.removeByDefinitionId(id, now);
        definitionMapper.remove(corpid, id, userId, now);
    }

    private ApprovalFlowDefinition toDomain(ApprovalFlowDefinitionPO po, boolean includeNodes) {
        ApprovalFlowDefinition definition = new ApprovalFlowDefinition();
        definition.setId(po.getId());
        definition.setCorpid(po.getCorpid());
        definition.setBusinessCode(po.getBusinessCode());
        definition.setApprovalScene(ApprovalScene.valueOf(po.getApprovalScene()));
        definition.setFlowCode(po.getFlowCode());
        definition.setFlowName(po.getFlowName());
        definition.setVersion(po.getVersion());
        definition.setPriority(po.getPriority());
        definition.setStatus(ApprovalFlowStatus.valueOf(po.getStatus()));
        definition.setScopeJson(po.getScopeJson());
        definition.setCreatorId(po.getCreatorId());
        definition.setModifyId(po.getModifyId());
        if (includeNodes) {
            List<ApprovalFlowNodePO> nodePOs = nodeMapper.findByDefinitionId(po.getId());
            Map<Long, List<ApprovalNodeApproverPO>> approvers = nodePOs.isEmpty() ? Map.of() : approverMapper
                .findByNodeIds(nodePOs.stream().map(ApprovalFlowNodePO::getId).toList()).stream()
                .collect(Collectors.groupingBy(ApprovalNodeApproverPO::getFlowNodeId));
            definition.setNodes(nodePOs.stream().map(nodePO -> toNode(nodePO, approvers.getOrDefault(nodePO.getId(), List.of()))).toList());
        } else {
            definition.setNodes(List.of());
        }
        return definition;
    }

    private ApprovalFlowNode toNode(ApprovalFlowNodePO po, List<ApprovalNodeApproverPO> approverPOs) {
        ApprovalFlowNode node = new ApprovalFlowNode();
        node.setId(po.getId());
        node.setFlowDefinitionId(po.getFlowDefinitionId());
        node.setNodeNo(po.getNodeNo());
        node.setNodeName(po.getNodeName());
        node.setApprovalMode(ApprovalNodeMode.valueOf(po.getApprovalMode()));
        node.setApprovers(approverPOs.stream().map(approverPO -> {
            ApprovalNodeApprover approver = new ApprovalNodeApprover();
            approver.setId(approverPO.getId());
            approver.setFlowNodeId(approverPO.getFlowNodeId());
            approver.setApproverType(ApprovalApproverType.valueOf(approverPO.getApproverType()));
            approver.setApproverValue(approverPO.getApproverValue());
            approver.setSuperiorLevel(approverPO.getSuperiorLevel());
            return approver;
        }).toList());
        return node;
    }

    private ApprovalFlowDefinitionPO toPO(ApprovalFlowDefinition definition) {
        ApprovalFlowDefinitionPO po = new ApprovalFlowDefinitionPO();
        po.setId(definition.getId());
        po.setCorpid(definition.getCorpid());
        po.setBusinessCode(definition.getBusinessCode());
        po.setApprovalScene(definition.getApprovalScene().name());
        po.setFlowCode(definition.getFlowCode());
        po.setFlowName(definition.getFlowName());
        po.setVersion(definition.getVersion());
        po.setPriority(definition.getPriority());
        po.setStatus(definition.getStatus().name());
        po.setScopeJson(definition.getScopeJson());
        po.setCreatorId(definition.getCreatorId());
        po.setModifyId(definition.getModifyId());
        return po;
    }
}
