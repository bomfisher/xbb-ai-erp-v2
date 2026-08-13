package xbb.ai.erp.module.inventory.contract;

import java.math.BigDecimal;

public record PostingResult(String idempotencyKey, BigDecimal totalCost, int lineCount) {
}
