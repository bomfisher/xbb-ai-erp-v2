package xbb.ai.erp.module.inventory.contract;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationCommand(String corpid, String businessCode, Long sourceId, String sourceType,
                                 List<ReservationLine> lines, String operatorId, LocalDateTime occurredAt,
                                 String idempotencyKey) {
}
