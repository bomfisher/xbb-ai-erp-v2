package xbb.ai.erp.module.sales.application.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalPlatformApi;
import xbb.ai.erp.module.approval.contract.ApprovalSubmission;
import xbb.ai.erp.module.approval.contract.ApprovalSubmitCommand;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSubmitSaveDTO;

import java.time.Instant;
import java.time.ZoneId;

/**
 * 销售订单在落库后提交审批平台，并传递冻结快照。
 */
@Service
@RequiredArgsConstructor
public class SalesOrderApprovalSubmitService {

    private final ApprovalPlatformApi approvalPlatformApi;
    private final ObjectMapper objectMapper;

    public ApprovalSubmission submitCreate(Long orderId, SalesOrderSubmitSaveDTO dto) {
        return approvalPlatformApi.submit(new ApprovalSubmitCommand(
            dto.getCorpid(),
            BusinessCodeEnum.SALES_ORDER.getCode(),
            ApprovalScene.CREATE,
            orderId.toString(),
            "sales-order-create:" + dto.getMain().getOrderNo(),
            dto.getUserId(),
            "销售订单 " + dto.getMain().getOrderNo(),
            snapshotJson(dto)
        ));
    }

    private String snapshotJson(SalesOrderSubmitSaveDTO dto) {
        SalesOrderMainDTO main = dto.getMain();
        ObjectNode root = objectMapper.createObjectNode();
        ObjectNode mainNode = root.putObject("main");
        put(mainNode, "orderNo", main.getOrderNo());
        put(mainNode, "customerId", main.getCustomerId());
        put(mainNode, "warehouseId", main.getWarehouseId());
        putDate(mainNode, "orderDate", main.getOrderDate());
        putDate(mainNode, "deliveryDate", main.getDeliveryDate());
        if (main.getTotalAmount() != null) {
            mainNode.put("totalAmount", main.getTotalAmount());
        }
        put(mainNode, "remark", main.getRemark());
        root.set("items", objectMapper.valueToTree(dto.getItems()));
        try {
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException exception) {
            throw new BizException("销售订单审批快照生成失败");
        }
    }

    private void put(ObjectNode node, String fieldName, String value) {
        if (value != null) {
            node.put(fieldName, value);
        }
    }

    private void put(ObjectNode node, String fieldName, Long value) {
        if (value != null) {
            node.put(fieldName, value);
        }
    }

    private void putDate(ObjectNode node, String fieldName, Long timestamp) {
        if (timestamp != null) {
            node.put(fieldName, Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate().toString());
        }
    }
}
