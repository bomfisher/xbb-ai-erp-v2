package xbb.ai.erp.module.settlement.application.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptPaymentDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffItemDTO;

class ReceiptSaveBusinessValidatorTest {

    private final ReceiptSaveBusinessValidator validator = new ReceiptSaveBusinessValidator();

    @Test
    void shouldRequireWriteOffsForCustomerPayment() {
        ReceiptSaveDTO dto = createSaveDTO("CUSTOMER_PAYMENT", List.of());

        assertThrows(BizException.class, () -> validator.validateForSubmit(dto));
    }

    @Test
    void shouldAllowCustomerPaymentWithWriteOffs() {
        ReceiptWriteOffItemDTO writeOff = new ReceiptWriteOffItemDTO();
        writeOff.setReceivableId(1L);
        writeOff.setAmount(BigDecimal.TEN);
        ReceiptSaveDTO dto = createSaveDTO("CUSTOMER_PAYMENT", List.of(writeOff));

        assertDoesNotThrow(() -> validator.validateForSubmit(dto));
    }

    @Test
    void shouldRejectWriteOffsForAdvancePayment() {
        ReceiptWriteOffItemDTO writeOff = new ReceiptWriteOffItemDTO();
        writeOff.setReceivableId(1L);
        writeOff.setAmount(BigDecimal.TEN);
        ReceiptSaveDTO dto = createSaveDTO("ADVANCE_PAYMENT", List.of(writeOff));

        assertThrows(BizException.class, () -> validator.validateForSubmit(dto));
    }

    private ReceiptSaveDTO createSaveDTO(String receiptType, List<ReceiptWriteOffItemDTO> writeOffs) {
        ReceiptPaymentDTO payment = new ReceiptPaymentDTO();
        payment.setAmount(BigDecimal.TEN);
        ReceiptMainDTO main = new ReceiptMainDTO();
        main.setReceiptType(receiptType);
        ReceiptSaveDTO dto = new ReceiptSaveDTO();
        dto.setMain(main);
        dto.setPayments(List.of(payment));
        dto.setWriteOffs(writeOffs);
        return dto;
    }
}
