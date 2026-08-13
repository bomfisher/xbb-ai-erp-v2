package xbb.ai.erp.module.inventory.application.service;

final class InventoryConcurrentException extends RuntimeException {
    InventoryConcurrentException() {
        super("库存余额并发变更");
    }
}
