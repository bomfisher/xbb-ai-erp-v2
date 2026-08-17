package xbb.ai.erp.module.inventory.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import xbb.ai.erp.module.inventory.contract.OutboundCommand;
import xbb.ai.erp.module.inventory.contract.OutboundLine;
import xbb.ai.erp.module.inventory.contract.ReleaseReservationCommand;
import xbb.ai.erp.module.inventory.contract.ReservationCommand;
import xbb.ai.erp.module.inventory.contract.ReservationLine;
import xbb.ai.erp.module.inventory.domain.enums.StockReservationStatusEnum;
import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.domain.model.StockReservation;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockCostTransactionRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockReservationRepository;
import xbb.ai.erp.module.inventory.domain.repository.StockTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InventoryPostingServiceReservationTest {

    @Test
    void reserveShouldMoveAvailableQuantityToLockedQuantity() {
        StockBalanceRepository balanceRepository = mock(StockBalanceRepository.class);
        StockReservationRepository reservationRepository = mock(StockReservationRepository.class);
        StockBalance balance = balance("10", "0", "10");
        when(balanceRepository.findByWarehouseAndSkuPairsForUpdate(anyString(), anyList())).thenReturn(List.of(balance));
        when(reservationRepository.findBySourceForUpdate(anyString(), anyString(), anyLong())).thenReturn(List.of());

        service(balanceRepository, reservationRepository).reserve(new ReservationCommand("c1", "SALE_OUTBOUND", 1L,
            "SALE_OUTBOUND", List.of(new ReservationLine(1L, 1L, new BigDecimal("5"), 11L)), "u1",
            LocalDateTime.now(), "reserve-1"));

        assertEquals(new BigDecimal("5"), balance.getLockedQty());
        assertEquals(new BigDecimal("5"), balance.getAvailableQty());
        verify(reservationRepository).insertBatch(anyList());
    }

    @Test
    void approvedReservationShouldConsumeLockedQuantityWithoutChangingAvailableQuantity() {
        StockBalanceRepository balanceRepository = mock(StockBalanceRepository.class);
        StockReservationRepository reservationRepository = mock(StockReservationRepository.class);
        StockCostTransactionRepository costTransactionRepository = mock(StockCostTransactionRepository.class);
        StockBalance balance = balance("10", "5", "5");
        StockReservation reservation = reservation("5", StockReservationStatusEnum.RESERVED);
        when(balanceRepository.findByWarehouseAndSkuPairsForUpdate(anyString(), anyList())).thenReturn(List.of(balance));
        when(reservationRepository.findBySourceForUpdate(anyString(), anyString(), anyLong())).thenReturn(List.of(reservation));
        when(costTransactionRepository.findByIdempotencyKeys(anyString(), anyList())).thenReturn(List.of());

        service(balanceRepository, reservationRepository, costTransactionRepository).postReservedOutbound(new OutboundCommand("c1",
            "SALE_OUTBOUND", 1L, "SALE_OUTBOUND", List.of(new OutboundLine(1L, 1L, new BigDecimal("5"), 11L)),
            "u1", LocalDateTime.now(), "outbound-1"));

        assertEquals(new BigDecimal("5"), balance.getQty());
        assertEquals(BigDecimal.ZERO, balance.getLockedQty());
        assertEquals(new BigDecimal("5"), balance.getAvailableQty());
        assertEquals(StockReservationStatusEnum.FULLY_OUTBOUNDED.name(), reservation.getStatus());
        verify(reservationRepository).update(reservation);
    }

    @Test
    void rejectedReservationShouldReleaseLockedQuantity() {
        StockBalanceRepository balanceRepository = mock(StockBalanceRepository.class);
        StockReservationRepository reservationRepository = mock(StockReservationRepository.class);
        StockBalance balance = balance("10", "5", "5");
        StockReservation reservation = reservation("5", StockReservationStatusEnum.RESERVED);
        when(reservationRepository.findBySource(anyString(), anyString(), anyLong())).thenReturn(List.of(reservation));
        when(reservationRepository.findBySourceForUpdate(anyString(), anyString(), anyLong())).thenReturn(List.of(reservation));
        when(balanceRepository.findByWarehouseAndSkuPairsForUpdate(anyString(), anyList())).thenReturn(List.of(balance));

        service(balanceRepository, reservationRepository).releaseReservation(new ReleaseReservationCommand("c1", 1L,
            "SALE_OUTBOUND", "u1", LocalDateTime.now(), "release-1"));

        assertEquals(BigDecimal.ZERO, balance.getLockedQty());
        assertEquals(new BigDecimal("10"), balance.getAvailableQty());
        assertEquals(StockReservationStatusEnum.RELEASED.name(), reservation.getStatus());
        verify(reservationRepository).update(reservation);
    }

    private static InventoryPostingService service(StockBalanceRepository balanceRepository,
                                                   StockReservationRepository reservationRepository) {
        return service(balanceRepository, reservationRepository, mock(StockCostTransactionRepository.class));
    }

    private static InventoryPostingService service(StockBalanceRepository balanceRepository,
                                                   StockReservationRepository reservationRepository,
                                                   StockCostTransactionRepository costTransactionRepository) {
        return new InventoryPostingService(balanceRepository, mock(StockTransactionRepository.class), costTransactionRepository,
            reservationRepository, mock(PlatformTransactionManager.class));
    }

    private static StockBalance balance(String qty, String lockedQty, String availableQty) {
        StockBalance balance = new StockBalance();
        balance.setCorpid("c1");
        balance.setWarehouseId(1L);
        balance.setSkuId(1L);
        balance.setQty(new BigDecimal(qty));
        balance.setLockedQty(new BigDecimal(lockedQty));
        balance.setAvailableQty(new BigDecimal(availableQty));
        balance.setTotalCost(new BigDecimal("100"));
        balance.setUnitCost(new BigDecimal("10"));
        balance.setVersion(0);
        return balance;
    }

    private static StockReservation reservation(String remainingQty, StockReservationStatusEnum status) {
        StockReservation reservation = new StockReservation();
        reservation.setCorpid("c1");
        reservation.setWarehouseId(1L);
        reservation.setSkuId(1L);
        reservation.setSourceType("SALE_OUTBOUND");
        reservation.setSourceId(1L);
        reservation.setSourceLineId(11L);
        reservation.setReservedQty(new BigDecimal("5"));
        reservation.setOutboundQty(BigDecimal.ZERO);
        reservation.setReleasedQty(BigDecimal.ZERO);
        reservation.setRemainingQty(new BigDecimal(remainingQty));
        reservation.setStatus(status.name());
        reservation.setVersion(0);
        return reservation;
    }
}
