package xbb.ai.erp.module.approval.application.service.impl;

import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowDetailDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowCatalogDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowIdDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowListDTO;
import xbb.ai.erp.module.approval.admin.dto.ApprovalFlowSaveDTO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowApproverVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowCatalogVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowNodeVO;
import xbb.ai.erp.module.approval.admin.vo.ApprovalFlowVO;
import xbb.ai.erp.module.approval.domain.model.ApprovalApproverType;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowDefinition;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowNode;
import xbb.ai.erp.module.approval.domain.model.ApprovalFlowStatus;
import xbb.ai.erp.module.approval.domain.model.ApprovalNodeApprover;
import xbb.ai.erp.module.approval.domain.repository.ApprovalFlowRepository;
import xbb.ai.erp.module.approval.application.service.ApprovalFlowService;
import xbb.ai.erp.module.approval.application.service.ApprovalInstanceService;
import xbb.ai.erp.module.approval.application.catalog.ApprovalSubjectCatalog;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApprovalFlowServiceImpl implements ApprovalFlowService {

    private final ApprovalFlowRepository repository;
    private final ApprovalInstanceService approvalInstanceService;
    private final ApprovalSubjectCatalog approvalSubjectCatalog;
    private final ObjectMapper objectMapper;

    @Override
    public List<ApprovalFlowCatalogVO> catalog(ApprovalFlowCatalogDTO dto) {
        return approvalSubjectCatalog.list();
    }

    @Override
    public List<ApprovalFlowVO> list(ApprovalFlowListDTO dto) {
        return repository.findByCondition(dto.getCorpid(), dto.getBusinessCode(), dto.getApprovalScene()).stream()
            .map(this::toVO).toList();
    }

    @Override
    public ApprovalFlowVO detail(ApprovalFlowDetailDTO dto) {
        return toVO(require(dto.getCorpid(), dto.getId()));
    }

    @Override
    @Transactional
    public ApprovalFlowVO saveDraft(ApprovalFlowSaveDTO dto) {
        boolean creating = dto.getId() == null;
        ApprovalFlowDefinition definition = creating ? new ApprovalFlowDefinition() : require(dto.getCorpid(), dto.getId());
        ApprovalFlowStatus originalStatus = definition.getStatus();
        fill(definition, dto);
        if (creating) {
            definition.setVersion(repository.findMaxVersion(dto.getCorpid(), dto.getBusinessCode(), dto.getApprovalScene(),
                dto.getFlowCode()) + 1);
            definition.setStatus(ApprovalFlowStatus.DRAFT);
        } else {
            definition.setStatus(originalStatus);
        }
        repository.save(definition);
        return toVO(require(dto.getCorpid(), definition.getId()));
    }

    @Override
    @Transactional
    public ApprovalFlowVO publish(ApprovalFlowIdDTO dto) {
        ApprovalFlowDefinition definition = require(dto.getCorpid(), dto.getId());
        if (definition.getStatus() != ApprovalFlowStatus.DRAFT && definition.getStatus() != ApprovalFlowStatus.DISABLED) {
            throw new BizException("仅草稿或已停用流程可以启用");
        }
        requireBusinessCode(definition.getBusinessCode());
        validatePublish(definition);
        definition.setStatus(ApprovalFlowStatus.PUBLISHED);
        definition.setModifyId(dto.getUserId());
        repository.save(definition);
        return toVO(require(dto.getCorpid(), definition.getId()));
    }

    @Override
    @Transactional
    public ApprovalFlowVO createNextVersion(ApprovalFlowIdDTO dto) {
        ApprovalFlowDefinition source = require(dto.getCorpid(), dto.getId());
        if (source.getStatus() == ApprovalFlowStatus.DRAFT) {
            throw new BizException("草稿流程无需创建新版本");
        }
        requireBusinessCode(source.getBusinessCode());
        ApprovalFlowDefinition next = new ApprovalFlowDefinition();
        next.setCorpid(source.getCorpid());
        next.setBusinessCode(source.getBusinessCode());
        next.setApprovalScene(source.getApprovalScene());
        next.setFlowCode(source.getFlowCode());
        next.setFlowName(source.getFlowName());
        next.setVersion(source.getVersion() + 1);
        next.setPriority(source.getPriority());
        next.setStatus(ApprovalFlowStatus.DRAFT);
        next.setScopeJson(source.getScopeJson());
        next.setCreatorId(dto.getUserId());
        next.setModifyId(dto.getUserId());
        next.setNodes(source.getNodes());
        repository.save(next);
        return toVO(require(dto.getCorpid(), next.getId()));
    }

    @Override
    @Transactional
    public ApprovalFlowVO disable(ApprovalFlowIdDTO dto) {
        ApprovalFlowDefinition definition = require(dto.getCorpid(), dto.getId());
        if (definition.getStatus() != ApprovalFlowStatus.PUBLISHED) {
            throw new BizException("仅已生效流程可以停用");
        }
        definition.setStatus(ApprovalFlowStatus.DISABLED);
        definition.setModifyId(dto.getUserId());
        repository.save(definition);
        return toVO(require(dto.getCorpid(), definition.getId()));
    }

    @Override
    @Transactional
    public void remove(ApprovalFlowIdDTO dto) {
        ApprovalFlowDefinition definition = require(dto.getCorpid(), dto.getId());
        if (definition.getStatus() != ApprovalFlowStatus.DRAFT && definition.getStatus() != ApprovalFlowStatus.DISABLED) {
            throw new BizException("仅未启用或已停用流程可以删除");
        }
        if (approvalInstanceService.hasActiveInstancesByFlowDefinitionId(dto.getCorpid(), definition.getId())) {
            throw new BizException("流程存在待审批或审批中的实例，不允许删除");
        }
        repository.remove(dto.getCorpid(), definition.getId(), dto.getUserId());
    }

    private ApprovalFlowDefinition require(String corpid, Long id) {
        if (id == null) {
            throw new BizException("流程标识不能为空");
        }
        ApprovalFlowDefinition definition = repository.findById(corpid, id);
        if (definition == null) {
            throw new BizException("审批流程不存在");
        }
        return definition;
    }

    private void fill(ApprovalFlowDefinition definition, ApprovalFlowSaveDTO dto) {
        if (dto.getBusinessCode() == null || dto.getBusinessCode().isBlank() || dto.getApprovalScene() == null
            || dto.getFlowCode() == null || dto.getFlowCode().isBlank() || dto.getFlowName() == null || dto.getFlowName().isBlank()) {
            throw new BizException("流程基本信息不完整");
        }
        requireBusinessCode(dto.getBusinessCode());
        definition.setCorpid(dto.getCorpid());
        definition.setBusinessCode(dto.getBusinessCode());
        definition.setApprovalScene(dto.getApprovalScene());
        definition.setFlowCode(dto.getFlowCode());
        definition.setFlowName(dto.getFlowName());
        definition.setVersion(definition.getVersion() == null ? 1 : definition.getVersion());
        definition.setPriority(dto.getPriority() == null ? 100 : dto.getPriority());
        definition.setScopeJson(normalizeScopeJson(dto.getScopeJson()));
        definition.setCreatorId(definition.getCreatorId() == null ? dto.getUserId() : definition.getCreatorId());
        definition.setModifyId(dto.getUserId());
        definition.setNodes(dto.getNodes().stream().map(nodeDTO -> {
            ApprovalFlowNode node = new ApprovalFlowNode();
            node.setNodeNo(nodeDTO.getNodeNo());
            node.setNodeName(nodeDTO.getNodeName());
            node.setApprovalMode(nodeDTO.getApprovalMode());
            node.setApprovers(nodeDTO.getApprovers().stream().map(approverDTO -> {
                ApprovalNodeApprover approver = new ApprovalNodeApprover();
                approver.setApproverType(approverDTO.getApproverType());
                approver.setApproverValue(approverDTO.getApproverValue());
                approver.setSuperiorLevel(approverDTO.getSuperiorLevel());
                return approver;
            }).toList());
            return node;
        }).toList());
    }

    private void validatePublish(ApprovalFlowDefinition definition) {
        if (definition.getNodes() == null || definition.getNodes().isEmpty()) {
            throw new BizException("审批流程至少需要一个节点");
        }
        List<Integer> expectedNumbers = java.util.stream.IntStream.rangeClosed(1, definition.getNodes().size()).boxed().toList();
        List<Integer> actualNumbers = definition.getNodes().stream().map(ApprovalFlowNode::getNodeNo).sorted().toList();
        if (!expectedNumbers.equals(actualNumbers)) {
            throw new BizException("审批节点必须从 1 开始连续排序");
        }
        for (ApprovalFlowNode node : definition.getNodes()) {
            if (node.getNodeName() == null || node.getNodeName().isBlank() || node.getApprovalMode() == null
                || node.getApprovers() == null || node.getApprovers().isEmpty()) {
                throw new BizException("审批节点信息不完整");
            }
            for (ApprovalNodeApprover approver : node.getApprovers()) {
                if (approver.getApproverType() == null) {
                    throw new BizException("审批人类型不能为空");
                }
                if (approver.getApproverType() == ApprovalApproverType.SUPERIOR) {
                    if (approver.getSuperiorLevel() == null || approver.getSuperiorLevel() < 1) {
                        throw new BizException("主管审批人必须指定主管层级");
                    }
                } else if (approver.getApproverValue() == null || approver.getApproverValue().isBlank()) {
                    throw new BizException("指定人员或角色不能为空");
                }
            }
        }
    }

    private void requireBusinessCode(String businessCode) {
        if (!BusinessCodeEnum.containsCode(businessCode)) {
            throw new BizException("审批业务编码未在 BusinessCodeEnum 中定义: " + businessCode);
        }
    }

    private String normalizeScopeJson(String scopeJson) {
        String normalizedScopeJson = scopeJson == null || scopeJson.isBlank() ? "{}" : scopeJson;
        try {
            objectMapper.readTree(normalizedScopeJson);
        } catch (JsonProcessingException exception) {
            throw new BizException("适用范围配置不是合法 JSON");
        }
        return normalizedScopeJson;
    }

    private ApprovalFlowVO toVO(ApprovalFlowDefinition definition) {
        ApprovalFlowVO vo = new ApprovalFlowVO();
        vo.setId(definition.getId());
        vo.setBusinessCode(definition.getBusinessCode());
        vo.setApprovalScene(definition.getApprovalScene());
        vo.setFlowCode(definition.getFlowCode());
        vo.setFlowName(definition.getFlowName());
        vo.setVersion(definition.getVersion());
        vo.setPriority(definition.getPriority());
        vo.setStatus(definition.getStatus());
        vo.setScopeJson(definition.getScopeJson());
        vo.setNodes(definition.getNodes().stream().sorted(Comparator.comparing(ApprovalFlowNode::getNodeNo)).map(node -> {
            ApprovalFlowNodeVO nodeVO = new ApprovalFlowNodeVO();
            nodeVO.setNodeNo(node.getNodeNo());
            nodeVO.setNodeName(node.getNodeName());
            nodeVO.setApprovalMode(node.getApprovalMode());
            nodeVO.setApprovers(node.getApprovers().stream().filter(Objects::nonNull).map(approver -> {
                ApprovalFlowApproverVO approverVO = new ApprovalFlowApproverVO();
                approverVO.setApproverType(approver.getApproverType());
                approverVO.setApproverValue(approver.getApproverValue());
                approverVO.setSuperiorLevel(approver.getSuperiorLevel());
                return approverVO;
            }).toList());
            return nodeVO;
        }).toList());
        return vo;
    }
}
