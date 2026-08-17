package xbb.ai.erp.module.inventory.contract;

public interface InventoryCommandApi {
    PostingResult reserve(ReservationCommand command);

    PostingResult postReservedOutbound(OutboundCommand command);

    void releaseReservation(ReleaseReservationCommand command);

    /**
     * 按库存模块当前生效的成本策略执行入库记账。
     */
    PostingResult postInbound(InboundCommand command);

    /**
     * 按库存模块当前生效的成本策略执行出库记账，并返回实际结转成本。
     */
    PostingResult postOutbound(OutboundCommand command);
}
