package xbb.ai.erp.module.inventory.contract;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 一次业务单据的出库记账命令。
 */
public record OutboundCommand(String corpid, String businessCode, Long sourceId, String sourceType,
                              List<OutboundLine> lines, String operatorId, LocalDateTime occurredAt,
                              String idempotencyKey) {
}
