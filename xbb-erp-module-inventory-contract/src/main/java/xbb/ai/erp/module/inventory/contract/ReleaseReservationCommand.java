package xbb.ai.erp.module.inventory.contract;

import java.time.LocalDateTime;

public record ReleaseReservationCommand(String corpid, Long sourceId, String sourceType, String operatorId,
                                        LocalDateTime occurredAt, String idempotencyKey) {
}
