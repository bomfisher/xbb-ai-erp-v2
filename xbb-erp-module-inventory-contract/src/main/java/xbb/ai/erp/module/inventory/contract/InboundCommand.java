package xbb.ai.erp.module.inventory.contract;

import java.time.LocalDateTime;
import java.util.List;

public record InboundCommand(String corpid, String businessCode, Long sourceId, String sourceType,
                             List<InboundLine> lines, String operatorId, LocalDateTime occurredAt,
                             String idempotencyKey) {
}
