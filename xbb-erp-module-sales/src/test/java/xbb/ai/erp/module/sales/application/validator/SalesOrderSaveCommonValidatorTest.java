package xbb.ai.erp.module.sales.application.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;

class SalesOrderSaveCommonValidatorTest {
    private final SalesOrderSaveCommonValidator validator = new SalesOrderSaveCommonValidator();

    @Test
    void shouldRejectMissingOrderDateBeforePersistence() {
        SalesOrderSaveDTO dto = validSaveDTO();
        dto.getMain().setOrderDate(null);

        BizException exception = assertThrows(BizException.class, () -> validator.validateForSubmit(dto));

        assertEquals("下单日期不能为空", exception.getMessage());
    }

    @Test
    void shouldRejectMissingSalesOrderItemsBeforePersistence() {
        SalesOrderSaveDTO dto = validSaveDTO();
        dto.setItems(List.of());

        BizException exception = assertThrows(BizException.class, () -> validator.validateForSubmit(dto));

        assertEquals("销售订单行不能为空", exception.getMessage());
    }

    private static SalesOrderSaveDTO validSaveDTO() {
        SalesOrderMainDTO main = new SalesOrderMainDTO();
        main.setOrderNo("SO-001");
        main.setCustomerId(1L);
        main.setOrderDate(1L);
        SalesOrderItemDTO item = new SalesOrderItemDTO();
        item.setSkuId(1L);
        item.setUnitName("件");
        item.setQty(BigDecimal.ONE);
        item.setUnitPrice(BigDecimal.ONE);
        SalesOrderSaveDTO dto = new SalesOrderSaveDTO();
        dto.setMain(main);
        dto.setItems(List.of(item));
        return dto;
    }
}
